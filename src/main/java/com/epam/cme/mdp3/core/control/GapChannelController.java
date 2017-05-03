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

package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.ChannelState;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;

public class GapChannelController implements MBOChannelController {
    private static final Logger log = LoggerFactory.getLogger(GapChannelController.class);
    private final Lock lock = new ReentrantLock();
    private final int gapThreshold;
    private final Buffer<MdpPacket> buffer;
    private final RecoveryManager recoveryManager;
    private final ChannelController target;
    private final ChannelController targetForBuffered;
    private final String channelId;
    private final MBOSnapshotCycleHandler cycleHandler;
    private long lastProcessedSeqNum;
    private long smallestSnapshotSequence;
    private long highestSnapshotSequence;
    private ChannelState currentState = ChannelState.INITIAL;
    private MdpMessageTypes mdpMessageTypes;
    private boolean receivingCycle = false;


    public GapChannelController(ChannelController target, ChannelController targetForBuffered, RecoveryManager recoveryManager, Buffer<MdpPacket> buffer,
                                int gapThreshold, String channelId, MdpMessageTypes mdpMessageTypes, MBOSnapshotCycleHandler cycleHandler) {
        this.buffer = buffer;
        this.recoveryManager = recoveryManager;
        this.target = target;
        this.gapThreshold = gapThreshold;
        this.channelId = channelId;
        this.mdpMessageTypes = mdpMessageTypes;
        this.cycleHandler = cycleHandler;
        this.targetForBuffered = targetForBuffered;
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
                    smallestSnapshotSequence = cycleHandler.getSmallestSnapshotSequence();
                    highestSnapshotSequence = cycleHandler.getHighestSnapshotSequence();
                    if (smallestSnapshotSequence != MBOSnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED
                            && highestSnapshotSequence != MBOSnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED) {
                        lastProcessedSeqNum = cycleHandler.getHighestSnapshotSequence();
                        processMessagesFromBuffer(feedContext);
                        recoveryManager.stopRecovery();
                        switchState(ChannelState.SYNC);
                        receivingCycle = false;
                    }
                } else {
                    cycleHandler.reset();
                    receivingCycle = true;
                }
            }
            switch (currentState) {
                case INITIAL:
                case OUTOFSYNC:
                    if(receivingCycle) {
                        mdpPacket.forEach(mdpMessage -> {
                            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
                            if (isMessageSupported(mdpMessage)) {
                                long lastMsgSeqNumProcessed = mdpMessage.getUInt32(LAST_MSG_SEQ_NUM_PROCESSED);
                                int securityId = mdpMessage.getInt32(SECURITY_ID);
                                long noChunks = mdpMessage.getUInt32(NO_CHUNKS);
                                long currentChunk = mdpMessage.getUInt32(CURRENT_CHUNK);
                                long totNumReports = mdpMessage.getUInt32(TOT_NUM_REPORTS);
                                cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId, noChunks, currentChunk);
                            }
                        });
                        target.handleSnapshotPacket(feedContext, mdpPacket);
                    }
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
            switch (currentState){
                case SYNC:
                    long expectedSequence = lastProcessedSeqNum + 1;
                    if(pkgSequence == expectedSequence){
                        target.handleIncrementalPacket(feedContext, mdpPacket);
                        lastProcessedSeqNum = pkgSequence;
                        processMessagesFromBuffer(feedContext);
                    } else if(pkgSequence > expectedSequence){
                        buffer.add(mdpPacket);
                        if(pkgSequence > (expectedSequence + gapThreshold)){
                            recoveryManager.startRecovery();
                            switchState(ChannelState.OUTOFSYNC);
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
                    buffer.add(mdpPacket);
                    if(log.isTraceEnabled()) {
                        log.trace("Feed {}:{} | handleIncrementalPacket: current state is '{}', so the packet with sequence '{}' has been put into buffer",
                                feedContext.getFeedType(), feedContext.getFeed(), currentState, pkgSequence);
                    }
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

    public interface RecoveryManager {
        void startRecovery();
        void stopRecovery();
    }

    private void switchState(ChannelState newState) {
        log.debug("Channel '{}' has changed its state from '{}' to '{}'", channelId, currentState, newState);
        currentState = newState;
    }

    private void processMessagesFromBuffer(MdpFeedContext feedContext){
        while (!buffer.isEmpty()) {
            MdpPacket mdpPacket = buffer.remove();
            long pkgSequence = mdpPacket.getMsgSeqNum();
            long expectedSequence = lastProcessedSeqNum + 1;
            if(pkgSequence == expectedSequence) {
                target.handleIncrementalPacket(feedContext, mdpPacket);
                lastProcessedSeqNum = pkgSequence;
            } else if(pkgSequence < expectedSequence && pkgSequence <= highestSnapshotSequence){
                long expectedSmallestSequence = smallestSnapshotSequence + 1;
                if(pkgSequence == expectedSmallestSequence){
                    targetForBuffered.handleIncrementalPacket(feedContext, mdpPacket);
                    smallestSnapshotSequence = expectedSmallestSequence;
                } if(pkgSequence > expectedSmallestSequence) {
                    buffer.add(mdpPacket);
                    break;
                }
            } else if(pkgSequence > expectedSequence){
                buffer.add(mdpPacket);
                break;
            }
        }
    }
}
