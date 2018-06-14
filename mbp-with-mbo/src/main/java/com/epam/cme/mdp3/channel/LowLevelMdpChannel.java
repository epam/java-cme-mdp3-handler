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

package com.epam.cme.mdp3.channel;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.control.*;
import com.epam.cme.mdp3.core.cfg.ChannelCfg;
import com.epam.cme.mdp3.core.cfg.ConnectionCfg;
import com.epam.cme.mdp3.core.channel.*;
import com.epam.cme.mdp3.core.channel.tcp.MdpTCPChannel;
import com.epam.cme.mdp3.core.channel.tcp.MdpTCPMessageRequester;
import com.epam.cme.mdp3.core.channel.tcp.TCPChannel;
import com.epam.cme.mdp3.core.channel.tcp.TCPMessageRequester;
import com.epam.cme.mdp3.control.ChannelController;
import com.epam.cme.mdp3.sbe.message.SbeString;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class LowLevelMdpChannel implements MdpChannel {
    private static final Logger logger = LoggerFactory.getLogger(LowLevelMdpChannel.class);

    private static final int FEED_IDLE_CHECK_DELAY = 100;
    private static final TimeUnit FEED_IDLE_CHECK_DELAY_UNIT = TimeUnit.SECONDS;
    private final ChannelCfg channelCfg;
    private final ScheduledExecutorService scheduledExecutorService;
    private int rcvBufSize;
    private Map<FeedType, Pair<MdpFeedWorker, Thread>> feedsA = new ConcurrentHashMap<>();
    private Map<FeedType, Pair<MdpFeedWorker, Thread>> feedsB = new ConcurrentHashMap<>();
    private Map<FeedType, String> feedANetworkInterfaces;
    private Map<FeedType, String> feedBNetworkInterfaces;
    private volatile Feed snptFeedToUse = Feed.A;
    private final MdpFeedListener feedListener = new FeedListenerImpl();
    private final List<ChannelListener> listeners = new ArrayList<>();
    private long idleWindowInMillis = FEED_IDLE_CHECK_DELAY_UNIT.toMillis(FEED_IDLE_CHECK_DELAY);
    private final GapChannelController channelController;
    private volatile ScheduledFuture<?> checkFeedIdleStateFuture;
    private volatile long lastIncrPcktReceived = 0;
    private final InstrumentManager instrumentManager;
    private final MdpMessageTypes mdpMessageTypes;
    private final boolean mboEnabled;
    private final InstrumentObserver instrumentObserver = new InstrumentObserverImpl();
    private final GapChannelController.SnapshotRecoveryManager recoveryManager;

    LowLevelMdpChannel(final ScheduledExecutorService scheduledExecutorService,
                       final ChannelCfg channelCfg,
                       final MdpMessageTypes mdpMessageTypes,
                       final int incrQueueSize,
                       final int rcvBufSize,
                       final int gapThreshold,
                       final String tcpUsername,
                       final String tcpPassword,
                       final Map<FeedType, String> feedANetworkInterfaces,
                       final Map<FeedType, String> feedBNetworkInterfaces,
                       boolean mboEnabled,
                       final List<Integer> mboIncrementMessageTemplateIds, 
                       final List<Integer> mboSnapshotMessageTemplateIds) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.channelCfg = channelCfg;
        this.rcvBufSize = rcvBufSize;
        this.feedANetworkInterfaces = feedANetworkInterfaces;
        this.feedBNetworkInterfaces = feedBNetworkInterfaces;
        this.mdpMessageTypes = mdpMessageTypes;
        this.mboEnabled = mboEnabled;
        String channelId = channelCfg.getId();
        instrumentManager = new MdpInstrumentManager(channelId, listeners);
        Buffer<MdpPacket> buffer = new MDPOffHeapBuffer(incrQueueSize);
        List<Consumer<MdpMessage>> emptyBookConsumers = new ArrayList<>();
        ChannelController target = new ChannelControllerRouter(channelId, instrumentManager, mdpMessageTypes, listeners,
                instrumentObserver, emptyBookConsumers, mboIncrementMessageTemplateIds, mboSnapshotMessageTemplateIds);
        SnapshotCycleHandler mbpCycleHandler = new OffHeapSnapshotCycleHandler();
        SnapshotCycleHandler mboCycleHandler;
        FeedType recoveryFeedType;
        if(mboEnabled) {
            recoveryFeedType = FeedType.SMBO;
            mboCycleHandler = new OffHeapSnapshotCycleHandler();
        } else {
            recoveryFeedType = FeedType.S;
            mboCycleHandler = mbpCycleHandler;
        }
        recoveryManager = getRecoveryManager(recoveryFeedType);
        ChannelController targetForBuffered = new BufferedMessageRouter(channelId, instrumentManager, mdpMessageTypes,
                listeners, mboCycleHandler, instrumentObserver, emptyBookConsumers, mboIncrementMessageTemplateIds, mboSnapshotMessageTemplateIds);
        ConnectionCfg connectionCfg = channelCfg.getConnectionCfg(FeedType.H, Feed.A);
        TCPChannel tcpChannel = new MdpTCPChannel(connectionCfg);
        TCPMessageRequester tcpMessageRequester = new MdpTCPMessageRequester<>(channelId, listeners, mdpMessageTypes, tcpChannel, tcpUsername, tcpPassword);
        this.channelController = new GapChannelController(listeners, target, targetForBuffered, recoveryManager, buffer, gapThreshold,
                channelId, mdpMessageTypes, mboCycleHandler, mbpCycleHandler, scheduledExecutorService, tcpMessageRequester, mboIncrementMessageTemplateIds, mboSnapshotMessageTemplateIds);
        emptyBookConsumers.add(channelController);
        if (scheduledExecutorService != null) initChannelStateThread();
    }

    @Override
    public String getId() {
        return this.channelCfg.getId();
    }

    @Override
    public void close() {
        if(checkFeedIdleStateFuture != null){
            checkFeedIdleStateFuture.cancel(true);
        }
        channelController.preClose();
        stopAllFeeds();
        feedsA.values().forEach(this::closeFeed);
        feedsB.values().forEach(this::closeFeed);
        channelController.close();
    }

    @Override
    public ChannelState getState() {
        return channelController.getState();
    }

    private void initChannelStateThread() {
        checkFeedIdleStateFuture = scheduledExecutorService.scheduleWithFixedDelay(this::checkFeedIdleState, FEED_IDLE_CHECK_DELAY, FEED_IDLE_CHECK_DELAY, FEED_IDLE_CHECK_DELAY_UNIT);
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
    public List<ChannelListener> getListeners() {
        return listeners;
    }

    @Override
    public void stopAllFeeds() {
        stopFeed(FeedType.I, Feed.A);
        stopFeed(FeedType.I, Feed.B);
        stopFeed(mboEnabled ? FeedType.SMBO : FeedType.S, Feed.A);
        stopFeed(mboEnabled ? FeedType.SMBO : FeedType.S, Feed.B);
        stopFeed(FeedType.H, Feed.A);
        stopFeed(FeedType.H, Feed.B);
    }

    @Override
    public boolean subscribe(final int securityId, final String secDesc) {
        instrumentManager.registerSecurity(securityId, secDesc);
        return true;
    }

    @Override
    public void discontinueSecurity(final int securityId) {
        instrumentManager.discontinueSecurity(securityId);
    }

    public MdpFeedListener getFeedListener() {
        return feedListener;
    }

    private final class FeedListenerImpl implements MdpFeedListener {
        @Override
        public void onFeedStarted(FeedType feedType, Feed feed) {
            listeners.forEach(channelListener -> channelListener.onFeedStarted(getId(), feedType, feed));
        }

        @Override
        public void onFeedStopped(FeedType feedType, Feed feed) {
            listeners.forEach(channelListener -> channelListener.onFeedStopped(getId(), feedType, feed));
        }

        @Override
        public void onPacket(final MdpFeedContext feedContext, final MdpPacket mdpPacket) {
            final FeedType feedType = feedContext.getFeedType();
            final Feed feed = feedContext.getFeed();
            if (logger.isTraceEnabled()) {
                logger.trace("New MDP Packet: #{} from Feed {}:{}", mdpPacket.getMsgSeqNum(), feedType, feed);
            }
            for (ChannelListener mboChannelListener : listeners) {
                mboChannelListener.onPacket(getId(), feedType, feed, mdpPacket);
            }
            if (feedType == FeedType.N) {
                instrumentObserver.onPacket(feedContext, mdpPacket);
            } else if (feedType == FeedType.I) {
                lastIncrPcktReceived = System.currentTimeMillis();
                channelController.handleIncrementalPacket(feedContext, mdpPacket);
            } else if (feedType == FeedType.SMBO || feedType == FeedType.S) {
                channelController.handleSnapshotPacket(feedContext, mdpPacket);
            }
        }
    }

    @Override
    public void startFeed(FeedType feedType, Feed feed) throws MdpFeedException {
        Map<FeedType, Pair<MdpFeedWorker, Thread>> currentFeed;
        Map<FeedType, String> networkInterfaces;

        if(mboEnabled && FeedType.S.equals(feedType)) {
            throw new MdpFeedException("It is not allowed to use MBP snapshot feed when MBO is enabled");
        }

        if(Feed.A.equals(feed)){
            currentFeed = feedsA;
            networkInterfaces = feedANetworkInterfaces;
        } else if(Feed.B.equals(feed)){
            currentFeed = feedsB;
            networkInterfaces = feedBNetworkInterfaces;
        } else {
            throw new IllegalArgumentException(String.format("%s feed is not supported", feed));
        }

        if (!currentFeed.containsKey(feedType)) {
            synchronized (this) {
                if (!currentFeed.containsKey(feedType)) {
                    MdpFeedWorker mdpFeedWorker = new MdpFeedWorker(channelCfg.getConnectionCfg(feedType, feed), networkInterfaces.get(feedType), rcvBufSize);
                    mdpFeedWorker.addListener(feedListener);
                    currentFeed.put(feedType, MutablePair.of(mdpFeedWorker, null));
                }
            }
        }
        Pair<MdpFeedWorker, Thread> feedThread = currentFeed.get(feedType);
        MdpFeedWorker mdpFeedWorker = feedThread.getLeft();
        if (!mdpFeedWorker.cancelShutdownIfStarted()) {
            if (!mdpFeedWorker.isActive()) {
                Thread thread = new Thread(mdpFeedWorker);
                feedThread.setValue(thread);
                thread.start();
            }
        }
    }

    private void checkFeedIdleState() {
        try {
            synchronized (this) {
                MdpFeedWorker incrementalFeedA = feedsA.get(FeedType.I).getLeft();
                MdpFeedWorker incrementalFeedB = feedsB.get(FeedType.I).getLeft();
                final long allowedInactiveEndTime = lastIncrPcktReceived + idleWindowInMillis;
                if (allowedInactiveEndTime < System.currentTimeMillis() &&
                        (incrementalFeedA.isActiveAndNotShutdown() || incrementalFeedB.isActiveAndNotShutdown())) {
                    recoveryManager.startRecovery();
                }
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void stopFeed(FeedType feedType, Feed feed){
        Map<FeedType, Pair<MdpFeedWorker, Thread>> currentFeed;
        if(Feed.A.equals(feed)){
            currentFeed = feedsA;
        } else if(Feed.B.equals(feed)){
            currentFeed = feedsB;
        } else {
            throw new IllegalArgumentException(String.format("%s feed is not supported", feed));
        }
        if(currentFeed.containsKey(feedType)){
            Pair<MdpFeedWorker, Thread> feedThread = currentFeed.get(feedType);
            MdpFeedWorker mdpFeedWorker = feedThread.getLeft();
            if(mdpFeedWorker.isActive()){
                mdpFeedWorker.shutdown();
            }
        }
    }

    private void closeFeed(Pair<MdpFeedWorker, Thread> feedThread){
        try {
            Thread thread = feedThread.getRight();
            MdpFeedWorker feedWorker = feedThread.getLeft();
            if (thread.isAlive()) {
                thread.join();
                feedWorker.close();
            }
        } catch (Exception e) {
            logger.error("Failed to stop Feed Worker: " + e.getMessage(), e);
        }
    }

    private GapChannelController.SnapshotRecoveryManager getRecoveryManager(FeedType feedType){
        return new GapChannelController.SnapshotRecoveryManager() {
            @Override
            public void startRecovery() {
                try {
                    startFeed(feedType, snptFeedToUse);
                } catch (MdpFeedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            @Override
            public void stopRecovery() {
                stopFeed(feedType, snptFeedToUse);
            }
        };
    }

    private class InstrumentObserverImpl implements InstrumentObserver {
        private static final int PRCD_MSG_COUNT_NULL = Integer.MAX_VALUE;   // max value used as undefined (null)
        private static final int INSTRUMENT_CYCLES_MAX = 2; // do we need an option in configuration for this?
        private AtomicInteger msgCountDown = new AtomicInteger(PRCD_MSG_COUNT_NULL);
        private final SbeString strValObj = SbeString.allocate(256);

        @Override
        public void onPacket(final MdpFeedContext feedContext, final MdpPacket instrumentPacket) {
            final Iterator<MdpMessage> mdpMessageIterator = instrumentPacket.iterator();
            MdpMessage mdpMessage;
            while (mdpMessageIterator.hasNext()) {
                mdpMessage = mdpMessageIterator.next();
                final MdpMessageType messageType = mdpMessageTypes.getMessageType(mdpMessage.getSchemaId());
                final SemanticMsgType semanticMsgType = messageType.getSemanticMsgType();
                if (semanticMsgType == SemanticMsgType.SecurityDefinition) {
                    mdpMessage.setMessageType(messageType);
                }
                onMessage(feedContext, mdpMessage);
            }
        }

        @Override
        public void onMessage(final MdpFeedContext feedContext, final MdpMessage secDefMsg) {
            final int subscriptionFlags = notifySecurityDefinitionListeners(secDefMsg);
            final int securityId = secDefMsg.getInt32(MdConstants.SECURITY_ID);
            if(logger.isDebugEnabled()) {
                logger.debug("Subscription flags for channel '{}' and instrument '{}' are '{}'", getId(), securityId, subscriptionFlags);
            }
            String secDesc = null;
            if(secDefMsg.getString(MdConstants.SEC_DESC_TAG, strValObj)) {
                secDesc = strValObj.getString();
            }
            if (MdEventFlags.hasMessage(subscriptionFlags)) {
                instrumentManager.registerSecurity(securityId, secDesc);
            } else {
                instrumentManager.updateSecDesc(securityId, secDesc);
            }
            if (msgCountDown.get() == PRCD_MSG_COUNT_NULL) {
                final int totalNumReports = getTotalReportNum(secDefMsg) * INSTRUMENT_CYCLES_MAX;
                this.msgCountDown.compareAndSet(PRCD_MSG_COUNT_NULL, totalNumReports);
            }
            final int msgLeft = msgCountDown.decrementAndGet();
            if (canStopInstrumentListening(msgLeft)) {
                stopFeed(FeedType.N, Feed.A);
                stopFeed(FeedType.N, Feed.B);
            }
        }
    }

    private int notifySecurityDefinitionListeners(final MdpMessage mdpMessage) {
        int flags = MdEventFlags.NOTHING;
        for (int i = 0; i < listeners.size(); i++) {
            final int clbFlags = listeners.get(i).onSecurityDefinition(getId(), mdpMessage);
            flags |= clbFlags;
        }
        return flags;
    }

    private int getTotalReportNum(final MdpMessage mdpMessage) {
        return (int) mdpMessage.getUInt32(MdConstants.TOT_NUM_REPORTS);
    }

    private boolean canStopInstrumentListening(final int cyclesLeft) {
        return cyclesLeft <= 0;
    }
}
