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

package com.epam.cme.mdp3.control;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.channel.tcp.TCPMessageRequester;
import com.epam.cme.mdp3.core.channel.tcp.TCPPacketListener;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class GapChannelController implements MdpChannelController, Consumer<MdpMessage> {
    private static final Logger log = LoggerFactory.getLogger(GapChannelController.class);
    public static final int MAX_NUMBER_OF_TCP_ATTEMPTS = 3;
    private final Lock lock = new ReentrantLock();
    private final int gapThreshold;
    private final int maxNumberOfTCPAttempts;
    private final MDPOffHeapBuffer buffer;
    private final SnapshotRecoveryManager snapshotRecoveryManager;
    private final ChannelController target;
    private final String channelId;
    private final SnapshotCycleHandler mboCycleHandler;
    private final SnapshotCycleHandler mbpCycleHandler;
    private long lastProcessedSeqNum;
    private long smallestSnapshotSequence;
    private long highestSnapshotSequence;
    private boolean wasChannelResetInPrcdPacket;
    private ChannelState currentState = ChannelState.INITIAL;
    private MdpMessageTypes mdpMessageTypes;
    private boolean receivingCycle = false;
    private final List<ChannelListener> channelListeners;
    private final ScheduledExecutorService executor;
    private TCPRecoveryProcessor tcpRecoveryProcessor;
    private int numberOfTCPAttempts;
    private long packetsInBufferDuringInitialOrOutOfSync = 0;
    private List<Integer> mboIncrementMessageTemplateIds;
    private List<Integer> mboSnapshotMessageTemplateIds;
    
    public GapChannelController(List<ChannelListener> channelListeners, ChannelController target, SnapshotRecoveryManager snapshotRecoveryManager,
                                MDPOffHeapBuffer buffer, int gapThreshold, final int maxNumberOfTCPAttempts, String channelId, MdpMessageTypes mdpMessageTypes,
                                SnapshotCycleHandler mboCycleHandler, SnapshotCycleHandler mbpCycleHandler,
                                ScheduledExecutorService executor, TCPMessageRequester tcpMessageRequester,     
                                List<Integer> mboIncrementMessageTemplateIds, List<Integer> mboSnapshotMessageTemplateIds) {
        this.channelListeners = channelListeners;
        this.buffer = buffer;
        this.snapshotRecoveryManager = snapshotRecoveryManager;
        this.target = target;
        this.gapThreshold = gapThreshold;
        this.maxNumberOfTCPAttempts = maxNumberOfTCPAttempts;
        this.channelId = channelId;
        this.mdpMessageTypes = mdpMessageTypes;
        this.mboCycleHandler = mboCycleHandler;
        this.mbpCycleHandler = mbpCycleHandler;
        this.executor = executor;
        if(tcpMessageRequester != null) {
            TCPPacketListener tcpPacketListener = new TCPPacketListenerImpl();
            this.tcpRecoveryProcessor = new TCPRecoveryProcessor(tcpMessageRequester, tcpPacketListener);
        }
        this.mboIncrementMessageTemplateIds = mboIncrementMessageTemplateIds;
        this.mboSnapshotMessageTemplateIds = mboSnapshotMessageTemplateIds;
    }
    
    @Override
    public List<Integer> getMBOIncrementMessageTemplateIds() {
        return mboIncrementMessageTemplateIds == null ? MdpChannelController.super.getMBOIncrementMessageTemplateIds() : mboIncrementMessageTemplateIds;
    }
    
    @Override
    public List<Integer> getMBOSnapshotMessageTemplateIds() {
        return mboSnapshotMessageTemplateIds == null ? MdpChannelController.super.getMBOSnapshotMessageTemplateIds() : mboSnapshotMessageTemplateIds;
    }

    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        final long pkgSequence = mdpPacket.getMsgSeqNum();
        if(log.isTraceEnabled()) {
            log.trace("Feed {}:{} | handleSnapshotPacket: previous processed sequence '{}', current packet's sequence '{}'",
                    feedContext.getFeedType(), feedContext.getFeed(), lastProcessedSeqNum, pkgSequence);
        }
        try {
            lock.lock();
            if(mdpPacket.getMsgSeqNum() == 1) {
                if(receivingCycle) {
                    smallestSnapshotSequence = mboCycleHandler.getSmallestSnapshotSequence();
                    highestSnapshotSequence = mboCycleHandler.getHighestSnapshotSequence();
                    if (smallestSnapshotSequence != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED
                            && highestSnapshotSequence != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED
                            && mbpCycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED
                            && mbpCycleHandler.getHighestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED) {
                        if(mbpCycleHandler.getSmallestSnapshotSequence() != smallestSnapshotSequence
                                || mbpCycleHandler.getHighestSnapshotSequence() != highestSnapshotSequence) {
                            log.error("MBP(Highest '{}', Smallest '{}') and MBO(Highest '{}', Smallest '{}') snapshots are not synchronized",
                                    mbpCycleHandler.getHighestSnapshotSequence(), mbpCycleHandler.getSmallestSnapshotSequence(),
                                    mboCycleHandler.getHighestSnapshotSequence(), mboCycleHandler.getSmallestSnapshotSequence());
                        }
                        lastProcessedSeqNum = highestSnapshotSequence;
                        snapshotRecoveryManager.stopRecovery();
                        switchState(ChannelState.SYNC);
                        if (log.isInfoEnabled()) {
                            log.info("{} Packets added to buffer during initial or outofsync event", packetsInBufferDuringInitialOrOutOfSync);
                        }
                        packetsInBufferDuringInitialOrOutOfSync = 0;
                        processMessagesFromBuffer(feedContext);
                        receivingCycle = false;
                        numberOfTCPAttempts = 0;
                    }
                } else {
                    mboCycleHandler.reset();
                    mbpCycleHandler.reset();
                    receivingCycle = true;
                }
            }
            switch (currentState) {
                case INITIAL:
                case OUTOFSYNC:
                    if(receivingCycle) {
                        for (MdpMessage mdpMessage : mdpPacket) {
                            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
                            long lastMsgSeqNumProcessed = mdpMessage.getUInt32(MdConstants.LAST_MSG_SEQ_NUM_PROCESSED);
                            int securityId = mdpMessage.getInt32(MdConstants.SECURITY_ID);
                            long totNumReports = mdpMessage.getUInt32(MdConstants.TOT_NUM_REPORTS);
                            if (isMBOSnapshot(mdpMessage)) {
                                long noChunks = mdpMessage.getUInt32(MdConstants.NO_CHUNKS);
                                long currentChunk = mdpMessage.getUInt32(MdConstants.CURRENT_CHUNK);
                                mboCycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId, noChunks, currentChunk);
                            } else {
                                mbpCycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId, 1, 1);
                            }
                        }
                        target.handleSnapshotPacket(feedContext, mdpPacket);
                    }
                    break;
                default:
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        final long pkgSequence = mdpPacket.getMsgSeqNum();
        if(log.isTraceEnabled()) {
            log.trace("Feed {}:{} | handleIncrementalPacket: previous processed sequence '{}', current packet's sequence '{}'",
                    feedContext.getFeedType(), feedContext.getFeed(), lastProcessedSeqNum, pkgSequence);
        }
        try {
            lock.lock();
            switch (currentState) {
                case SYNC:
                    long expectedSequence = lastProcessedSeqNum + 1;
                    if(pkgSequence == expectedSequence) {
                        target.handleIncrementalPacket(feedContext, mdpPacket);
                        if (wasChannelResetInPrcdPacket) {
                            wasChannelResetInPrcdPacket = false;
                        } else {
                            lastProcessedSeqNum = pkgSequence;
                        }
                        processMessagesFromBuffer(feedContext);
                    } else if(pkgSequence > expectedSequence) {
                        buffer.add(pkgSequence, mdpPacket);
                        if(pkgSequence > (expectedSequence + gapThreshold)) {
                            if(log.isInfoEnabled()) {
                                log.info("Past gap of {} expected {} current {}, lost count {}", gapThreshold, expectedSequence, pkgSequence, (pkgSequence - 1) - expectedSequence);
                            }
                            switchState(ChannelState.OUTOFSYNC);
                            long amountOfLostMessages = (pkgSequence - 1) - expectedSequence;
                            if(numberOfTCPAttempts < maxNumberOfTCPAttempts && amountOfLostMessages < TCPMessageRequester.MAX_AVAILABLE_MESSAGES
                                    && tcpRecoveryProcessor != null && executor != null) {
                                if(log.isTraceEnabled()) {
                                    log.trace("TCP Replay request gap {}:{} TCP Attempts: {}", expectedSequence, (pkgSequence-1), numberOfTCPAttempts);
                                }
                                tcpRecoveryProcessor.setBeginSeqNo(expectedSequence);
                                tcpRecoveryProcessor.setEndSeqNo(pkgSequence - 1);
                                executor.execute(tcpRecoveryProcessor);
                                numberOfTCPAttempts++;
                            } else {
                                snapshotRecoveryManager.startRecovery();
                            }
                        }
                    } else {
                        if(log.isTraceEnabled()) {
                            log.trace("Feed {}:{} | handleIncrementalPacket: packet that has sequence '{}' has been skipped. Expected sequence '{}'",
                                    feedContext.getFeedType(), feedContext.getFeed(), pkgSequence, expectedSequence);
                        }
                    }
                    break;
                case INITIAL:
                case OUTOFSYNC:
                    buffer.add(pkgSequence, mdpPacket);
                    packetsInBufferDuringInitialOrOutOfSync++;
                    if(log.isTraceEnabled()) {
                        log.trace("Feed {}:{} | handleIncrementalPacket: current state is '{}', so the packet with sequence '{}' has been put into buffer",
                                feedContext.getFeedType(), feedContext.getFeed(), currentState, pkgSequence);
                    }
                    break;
                default:
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void preClose() {
        switchState(ChannelState.CLOSING);
    }

    @Override
    public void close() {
        switchState(ChannelState.CLOSED);
    }

    public ChannelState getState() {
        return currentState;
    }

    @Override
    public void accept(MdpMessage resetMessage) {
        channelListeners.forEach(channelListener -> channelListener.onBeforeChannelReset(channelId, resetMessage));
        lastProcessedSeqNum = 0;
        smallestSnapshotSequence = 0;
        highestSnapshotSequence = 0;
        buffer.clear();
        wasChannelResetInPrcdPacket = true;
        if(currentState != ChannelState.SYNC) {
            switchState(ChannelState.SYNC);
            snapshotRecoveryManager.stopRecovery();
        }
        channelListeners.forEach(channelListener -> channelListener.onFinishedChannelReset(channelId, resetMessage));
    }

    public interface SnapshotRecoveryManager {
        void startRecovery();
        void stopRecovery();
    }

    private void switchState(ChannelState newState) {
        log.debug("Channel '{}' has changed its state from '{}' to '{}'", channelId, currentState, newState);
        channelListeners.forEach(ChannelListener -> ChannelListener.onChannelStateChanged(channelId, currentState, newState));
        currentState = newState;
    }

    private void processMessagesFromBuffer(MdpFeedContext feedContext){
        for (long expectedSequence = lastProcessedSeqNum + 1; expectedSequence <= buffer.getLastMsgSeqNum(); expectedSequence++) {
            MdpPacket mdpPacket = buffer.remove(expectedSequence);
            if(mdpPacket != null) {
                target.handleIncrementalPacket(feedContext, mdpPacket);
                lastProcessedSeqNum = mdpPacket.getMsgSeqNum();
            }
        }
    }

    private class TCPRecoveryProcessor implements Runnable {
        private final TCPMessageRequester tcpMessageRequester;
        private final TCPPacketListener tcpPacketListener;
        private long beginSeqNo;
        private long endSeqNo;
        private final MdpFeedContext feedContext;

        private TCPRecoveryProcessor(TCPMessageRequester tcpMessageRequester, TCPPacketListener tcpPacketListener) {
            this.tcpMessageRequester = tcpMessageRequester;
            this.tcpPacketListener = tcpPacketListener;
            this.feedContext = new MdpFeedContext(Feed.A, FeedType.I);
        }

        @Override
        public void run() {
            try {
                boolean result = tcpMessageRequester.askForLostMessages(beginSeqNo, endSeqNo, tcpPacketListener);
                if (result) {
                    try {
                        lock.lock();
                        switchState(ChannelState.SYNC);
                        processMessagesFromBuffer(feedContext);
                        numberOfTCPAttempts = 0;
                    } finally {
                        lock.unlock();
                    }
                } else {
                    snapshotRecoveryManager.startRecovery();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        public void setBeginSeqNo(long beginSeqNo) {
            this.beginSeqNo = beginSeqNo;
        }

        public void setEndSeqNo(long endSeqNo) {
            this.endSeqNo = endSeqNo;
        }
    }

    private class TCPPacketListenerImpl implements TCPPacketListener {

        @Override
        public void onPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
            handleIncrementalPacket(feedContext, mdpPacket);
        }
    }
}
