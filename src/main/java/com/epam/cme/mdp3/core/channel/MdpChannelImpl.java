/*
 * Copyright 2004-2016 EPAM Systems
 * This file is part of Java Market Data Handler for CME Market Data (MDP 3.0).
 * Java Market Data Handler for CME Market Data (MDP 3.0) is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Java Market Data Handler for CME Market Data (MDP 3.0) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Java Market Data Handler for CME Market Data (MDP 3.0).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.epam.cme.mdp3.core.channel;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.cfg.ChannelCfg;
import com.epam.cme.mdp3.core.control.ChannelController;
import com.epam.cme.mdp3.core.control.InstrumentController;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MdpChannelImpl implements MdpChannel {
    private static final Logger logger = LoggerFactory.getLogger(MdpChannelImpl.class);

    private static final int FEED_IDLE_CHECK_DELAY = 100;
    private static final TimeUnit FEED_IDLE_CHECK_DELAY_UNIT = TimeUnit.SECONDS;
    private static final int PLATFORM_DEFAULT_BOOK_DEPTH = 10; // do we need an external option?

    final ChannelCfg channelCfg;
    private final ScheduledExecutorService scheduledExecutorService;
    private int rcvBufSize = MdpFeedWorker.RCV_BUFFER_SIZE;

    private MdpFeedWorker incrementalFeedA;
    private MdpFeedWorker incrementalFeedB;
    private MdpFeedWorker snapshotFeedA;
    private MdpFeedWorker snapshotFeedB;
    private MdpFeedWorker instrumentFeedA;
    private MdpFeedWorker instrumentFeedB;

    private Thread incrementalFeedAThread;
    private Thread incrementalFeedBThread;
    private Thread snapshotFeedAThread;
    private Thread snapshotFeedBThread;
    private Thread instrumentFeedAThread;
    private Thread instrumentFeedBThread;

    private String incrementalFeedAni;
    private String incrementalFeedBni;
    private String snapshotFeedAni;
    private String snapshotFeedBni;
    private String instrumentFeedAni;
    private String instrumentFeedBni;

    private volatile Feed snptFeedToUse = Feed.A;

    private final MdpFeedListener mdpFeedListener = new MdpFeelListenerImpl();
    final ChannelInstruments instruments;

    private final List<ChannelListener> listeners = new ArrayList<>();
    private final List<MarketDataListener> mdListeners = new ArrayList<>();
    private boolean hasMdListener = false;

    private final ChannelContext channelContext;
    private long idleWindowInMillis = FEED_IDLE_CHECK_DELAY_UNIT.toMillis(FEED_IDLE_CHECK_DELAY);

    private boolean allSecuritiesMode = false;
    private byte defMaxBookDepth = PLATFORM_DEFAULT_BOOK_DEPTH;
    private int defSubscriptionOptions = MdEventFlags.MESSAGE;

    private final ChannelController channelController;
    private int queueSlotInitBufferSize = InstrumentController.DEF_QUEUE_SLOT_INIT_BUFFER_SIZE;
    private int incrQueueSize = InstrumentController.DEF_INCR_QUEUE_SIZE;
    private int gapThreshold = InstrumentController.DEF_GAP_THRESHOLD;

    MdpChannelImpl(final ScheduledExecutorService scheduledExecutorService,
                   final ChannelCfg channelCfg,
                   final MdpMessageTypes mdpMessageTypes,
                   final int queueSlotInitBufferSize,
                   final int incrQueueSize,
                   final int gapThreshold) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.channelCfg = channelCfg;
        this.gapThreshold = gapThreshold;
        this.channelContext = new ChannelContext(this, mdpMessageTypes, this.gapThreshold);
        this.instruments = new ChannelInstruments(this.channelContext);
        this.queueSlotInitBufferSize = queueSlotInitBufferSize;
        this.incrQueueSize = incrQueueSize;
        this.channelController = new ChannelController(this.channelContext, this.incrQueueSize, this.queueSlotInitBufferSize);
        if (scheduledExecutorService != null) initChannelStateThread();
    }

    @Override
    public String getId() {
        return this.channelCfg.getId();
    }

    public int getGapThreshold() {
        return gapThreshold;
    }

    public void setGapThreshold(int gapThreshold) {
        this.gapThreshold = gapThreshold;
    }

    public void setIdleWindowInMillis(long idleWindowInMillis) {
        this.idleWindowInMillis = idleWindowInMillis;
    }

    public void setSnapshotFeedAni(String snapshotFeedAni) {
        this.snapshotFeedAni = snapshotFeedAni;
    }

    public void setSnapshotFeedBni(String snapshotFeedBni) {
        this.snapshotFeedBni = snapshotFeedBni;
    }

    public void setIncrementalFeedBni(String incrementalFeedBni) {
        this.incrementalFeedBni = incrementalFeedBni;
    }

    public void setIncrementalFeedAni(String incrementalFeedAni) {
        this.incrementalFeedAni = incrementalFeedAni;
    }

    public void setInstrumentFeedAni(String instrumentFeedAni) {
        this.instrumentFeedAni = instrumentFeedAni;
    }

    public void setInstrumentFeedBni(String instrumentFeedBni) {
        this.instrumentFeedBni = instrumentFeedBni;
    }

    private void closeFeed(final Thread thread, final MdpFeedWorker feedWorker) throws IOException, InterruptedException {
        if (thread != null && thread.isAlive()) {
            thread.join();
            feedWorker.close();
        }
    }

    @Override
    public void close() {
        this.channelController.lock();
        try {
            this.channelController.switchState(ChannelState.CLOSING);
        } finally {
            this.channelController.unlock();
        }
        stopAllFeeds();
        try {
            closeFeed(incrementalFeedAThread, incrementalFeedA);
            closeFeed(incrementalFeedBThread, incrementalFeedB);
            closeFeed(snapshotFeedAThread, snapshotFeedA);
            closeFeed(snapshotFeedBThread, snapshotFeedB);
            closeFeed(instrumentFeedAThread, instrumentFeedA);
            closeFeed(instrumentFeedBThread, instrumentFeedB);
        } catch (Exception e) {
            logger.error("Failed to stop Feed Worker: " + e.getMessage(), e);
        }
        this.channelController.lock();
        try {
            channelController.close();
            this.channelController.switchState(ChannelState.CLOSED);
        } finally {
            this.channelController.unlock();
        }
    }

    @Override
    public void enableAllSecuritiesMode() {
        this.allSecuritiesMode = true;
    }

    @Override
    public void disableAllSecuritiesMode() {
        this.allSecuritiesMode = false;
    }

    @Override
    public byte getDefMaxBookDepth() {
        return this.defMaxBookDepth;
    }

    @Override
    public void setDefMaxBookDepth(final byte defMaxBookDepth) {
        this.defMaxBookDepth = defMaxBookDepth;
    }

    @Override
    public int getDefSubscriptionOptions() {
        return defSubscriptionOptions;
    }

    @Override
    public void setDefSubscriptionOptions(final int defSubscriptionOptions) {
        this.defSubscriptionOptions = defSubscriptionOptions;
    }

    @Override
    public ChannelState getState() {
        return this.channelController.getState();
    }

    @Override
    public void setStateForcibly(ChannelState state) {
        this.channelController.switchState(state);
    }

    public ChannelController getController() {
        return channelController;
    }

    private void initChannelStateThread() {
        this.scheduledExecutorService.scheduleWithFixedDelay(() -> checkFeedIdleState(),
                FEED_IDLE_CHECK_DELAY, FEED_IDLE_CHECK_DELAY, FEED_IDLE_CHECK_DELAY_UNIT);
    }

    private void checkFeedIdleState() {
        synchronized (this) {
            final long allowedInactiveEndTime = this.channelController.getLastIncrPcktReceived() + idleWindowInMillis;
            if (allowedInactiveEndTime < System.currentTimeMillis() &&
                    (incrementalFeedA.isActiveAndNotShutdown() || incrementalFeedB.isActiveAndNotShutdown())) {
                this.channelController.lock();
                try {
                    if (channelController.getState() != ChannelState.CLOSING && channelController.getState() != ChannelState.CLOSED) {
                        startSnapshotFeeds();
                    }
                } finally {
                    this.channelController.unlock();
                }
            }
        }
    }

    @Override
    public void registerListener(final ChannelListener channelListener) {
        if (channelListener != null) {
            synchronized (listeners) {
                listeners.add(channelListener);
            }
        }
    }

    @Override
    public void removeListener(final ChannelListener channelListener) {
        if (channelListener != null) {
            synchronized (listeners) {
                listeners.remove(channelListener);
            }
        }
    }

    @Override
    public void registerMarketDataListener(final MarketDataListener mdListener) {
        synchronized (mdListeners) {
            mdListeners.add(mdListener);
            setMdEnabledFlag();
        }
    }

    @Override
    public void removeMarketDataListener(final MarketDataListener mdListener) {
        synchronized (mdListeners) {
            mdListeners.remove(mdListener);
            setMdEnabledFlag();
        }
    }

    private void setMdEnabledFlag() {
        this.hasMdListener = !this.mdListeners.isEmpty();
    }

    public boolean hasMdListener() {
        return hasMdListener;
    }

    @Override
    public List<ChannelListener> getListeners() {
        return listeners;
    }

    @Override
    public List<MarketDataListener> getMdListeners() {
        return mdListeners;
    }

    @Override
    public void startIncrementalFeedA() throws MdpFeedException {
        if (incrementalFeedA == null) {
            synchronized (this) {
                if (incrementalFeedA == null) {
                    incrementalFeedA = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.I, Feed.A), incrementalFeedAni, rcvBufSize);
                    incrementalFeedA.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!incrementalFeedA.cancelShutdownIfStarted()) {
            if (!incrementalFeedA.isActive()) {
                incrementalFeedAThread = new Thread(incrementalFeedA);
                incrementalFeedAThread.start();
            }
        }
    }

    @Override
    public void startIncrementalFeedB() throws MdpFeedException {
        if (incrementalFeedB == null) {
            synchronized (this) {
                if (incrementalFeedB == null) {
                    incrementalFeedB = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.I, Feed.B), incrementalFeedBni, rcvBufSize);
                    incrementalFeedB.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!incrementalFeedB.cancelShutdownIfStarted()) {
            if (!incrementalFeedB.isActive()) {
                incrementalFeedBThread = new Thread(incrementalFeedB);
                incrementalFeedBThread.start();
            }
        }
    }

    @Override
    public void startSnapshotFeedA() throws MdpFeedException {
        if (snapshotFeedA == null) {
            synchronized (this) {
                if (snapshotFeedA == null) {
                    snapshotFeedA = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.S, Feed.A), snapshotFeedAni, rcvBufSize);
                    snapshotFeedA.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!snapshotFeedA.cancelShutdownIfStarted()) {
            if (!snapshotFeedA.isActive()) {
                snapshotFeedAThread = new Thread(snapshotFeedA);
                snapshotFeedAThread.start();
            }
        }
    }

    @Override
    public void startSnapshotFeedB() throws MdpFeedException {
        if (snapshotFeedB == null) {
            synchronized (this) {
                if (snapshotFeedB == null) {
                    snapshotFeedB = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.S, Feed.B), snapshotFeedBni, rcvBufSize);
                    snapshotFeedB.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!snapshotFeedB.cancelShutdownIfStarted()) {
            if (!snapshotFeedB.isActive()) {
                snapshotFeedBThread = new Thread(snapshotFeedB);
                snapshotFeedBThread.start();
            }
        }
    }

    @Override
    public void startInstrumentFeedA() throws MdpFeedException {
        if (instrumentFeedA == null) {
            synchronized (this) {
                if (instrumentFeedA == null) {
                    instrumentFeedA = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.N, Feed.A), instrumentFeedAni, rcvBufSize);
                    instrumentFeedA.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!instrumentFeedA.cancelShutdownIfStarted()) {
            if (!instrumentFeedA.isActive()) {
                instrumentFeedAThread = new Thread(instrumentFeedA);
                instrumentFeedAThread.start();
            }
        }
    }

    @Override
    public void startInstrumentFeedB() throws MdpFeedException {
        if (instrumentFeedB == null) {
            synchronized (this) {
                if (instrumentFeedB == null) {
                    instrumentFeedB = new MdpFeedWorker(channelCfg.getConnectionCfg(FeedType.N, Feed.B), instrumentFeedBni, rcvBufSize);
                    instrumentFeedB.addListener(this.mdpFeedListener);
                }
            }
        }
        if (!instrumentFeedB.cancelShutdownIfStarted()) {
            if (!instrumentFeedB.isActive()) {
                instrumentFeedBThread = new Thread(instrumentFeedB);
                instrumentFeedBThread.start();
            }
        }
    }

    @Override
    public void stopIncrementalFeedA() {
        if (incrementalFeedA != null && incrementalFeedA.isActive()) {
            incrementalFeedA.shutdown();
        }
    }

    @Override
    public void stopIncrementalFeedB() {
        if (incrementalFeedB != null && incrementalFeedB.isActive()) {
            incrementalFeedB.shutdown();
        }
    }

    @Override
    public void stopSnapshotFeedA() {
        if (snapshotFeedA != null && snapshotFeedA.isActive()) {
            snapshotFeedA.shutdown();
        }
    }

    @Override
    public void stopSnapshotFeedB() {
        if (snapshotFeedB != null && snapshotFeedB.isActive()) {
            snapshotFeedB.shutdown();
        }
    }

    @Override
    public void stopInstrumentFeedA() {
        if (instrumentFeedA != null && instrumentFeedA.isActive()) {
            instrumentFeedA.shutdown();
        }
    }

    @Override
    public void stopInstrumentFeedB() {
        if (instrumentFeedB != null && instrumentFeedB.isActive()) {
            instrumentFeedB.shutdown();
        }
    }

    @Override
    public void stopAllFeeds() {
        stopIncrementalFeedA();
        stopIncrementalFeedB();
        stopSnapshotFeedA();
        stopSnapshotFeedB();
        stopInstrumentFeedA();
        stopInstrumentFeedB();
    }

    @Override
    public void startInstrumentFeeds() {
        try {
            this.instruments.resetCycleCounter();
            startInstrumentFeedA();
            startIncrementalFeedB();
        } catch (MdpFeedException e) {
            logger.error("Failed to start Instrument Feeds: " + e.getMessage(), e);
        }
    }

    boolean isSnapshotFeedsActive() {
        return (snapshotFeedA != null && snapshotFeedA.isActive()) || (snapshotFeedB != null && snapshotFeedB.isActive());
    }

    @Override
    public void startSnapshotFeeds() {
        try {
            channelController.resetSnapshotCycleCount();
            if (this.snptFeedToUse == Feed.A) {
                startSnapshotFeedA();
            } else {
                startSnapshotFeedB();
            }
        } catch (MdpFeedException e) {
            logger.error("Failed to start Snapshot Feeds: " + e.getMessage(), e);
        }
    }

    @Override
    public void stopSnapshotFeeds() {
        stopSnapshotFeedA();
        stopSnapshotFeedB();
    }

    void subscribeToSnapshotsForInstrument(final Integer securityId) {
        channelController.addOutOfSyncInstrument(securityId);
        startSnapshotFeeds();
    }

    void unsubscribeFromSnapshotsForInstrument(final Integer securityId) {
        if (channelController.removeOutOfSyncInstrument(securityId)) {
           if (isSnapshotFeedsActive()) {
               if (!channelController.hasOutOfSyncInstruments()) {
                   stopSnapshotFeeds();
               }
           }
        }
    }

    InstrumentController findController(final int securityId, final String secDesc) {
        return this.instruments.find(securityId, secDesc, allSecuritiesMode, defSubscriptionOptions, defMaxBookDepth);
    }

    @Override
    public boolean subscribe(final int securityId, final String secDesc, final int subscrFlags, final byte depth) {
        return instruments.registerSecurity(securityId, secDesc, subscrFlags, depth);
    }

    @Override
    public boolean subscribeWithDefDepth(final int securityId, final String secDesc, final int subscrFlags) {
        return subscribe(securityId, secDesc, subscrFlags, this.defMaxBookDepth);
    }

    @Override
    public boolean subscribe(final int securityId, final String secDesc, final byte depth) {
        return subscribe(securityId, secDesc, this.defSubscriptionOptions, depth);
    }

    @Override
    public boolean subscribe(final int securityId, final String secDesc) {
        return subscribeWithDefDepth(securityId, secDesc, this.defSubscriptionOptions);
    }

    @Override
    public void discontinueSecurity(final int securityId) {
        this.instruments.discontinueSecurity(securityId);
    }

    @Override
    public int getSubscriptionFlags(final int securityId) {
        return this.instruments.getSubscriptionFlags(securityId);
    }

    @Override
    public void setSubscriptionFlags(final int securityId, final int flags) {
        this.instruments.setSubscriptionFlags(securityId, flags, defMaxBookDepth);
    }

    @Override
    public void addSubscriptionFlags(final int securityId, final int flags) {
        this.instruments.addSubscriptionFlags(securityId, flags, defMaxBookDepth);
    }

    @Override
    public void removeSubscriptionFlags(final int securityId, final int flags) {
        this.instruments.removeSubscriptionFlags(securityId, flags);
    }

    @Override
    public void handlePacket(final MdpFeedContext feedContext, final MdpPacket mdpPacket) {
        final FeedType feedType = feedContext.getFeedType();
        final Feed feed = feedContext.getFeed();
        logger.trace("New MDP Packet: #{} from Feed {}{}:", mdpPacket.getMsgSeqNum(), feedType, feed);
        channelContext.notifyPacketReceived(feedType, feed, mdpPacket);
        if (feedType == FeedType.N) {
            instruments.onPacket(feedContext, mdpPacket);
        } else if (feedType == FeedType.I) {
            channelController.handleIncrementalPacket(feedContext, mdpPacket);
        } else if (feedType == FeedType.S) {
            channelController.handleSnapshotPacket(feedContext, mdpPacket);
        }
    }

    public int getQueueSlotInitBufferSize() {
        return queueSlotInitBufferSize;
    }

    public int getIncrQueueSize() {
        return incrQueueSize;
    }

    public void setRcvBufSize(int rcvBufSize) {
        this.rcvBufSize = rcvBufSize;
    }

    private final class MdpFeelListenerImpl implements MdpFeedListener {
        @Override
        public void onFeedStarted(FeedType feedType, Feed feed) {
            channelContext.notifyFeedStartedListeners(feedType, feed);
        }

        @Override
        public void onFeedStopped(FeedType feedType, Feed feed) {
            channelContext.notifyFeedStoppedListeners(feedType, feed);
        }

        @Override
        public void onPacket(final MdpFeedContext feedContext, final MdpPacket mdpPacket) {
            handlePacket(feedContext, mdpPacket);
        }
    }
}
