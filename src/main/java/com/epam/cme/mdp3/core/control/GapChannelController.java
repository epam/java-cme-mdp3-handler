package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.ChannelListener;
import com.epam.cme.mdp3.ChannelState;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;

public class GapChannelController implements ChannelController {
    private static final Logger log = LoggerFactory.getLogger(GapChannelController.class);
    private final Lock lock = new ReentrantLock();
    private final int gapThreshold;
    private final CircularBuffer<MdpPacket> buffer;
    private final RecoveryManager recoveryManager;
    private final ChannelController target;
    private final List<ChannelListener> listeners;
    private final String channelId;
    private final MBOChannelSnapshotMetaData snapshotMetaData;
    private long lastProcessedSeqNum;
    private ChannelState currentState = ChannelState.INITIAL;
    private MdpMessageTypes mdpMessageTypes;


    public GapChannelController(ChannelController target, RecoveryManager recoveryManager, CircularBuffer<MdpPacket> buffer,
                                int gapThreshold, List<ChannelListener> listeners, String channelId, MdpMessageTypes mdpMessageTypes) {
        this.buffer = buffer;
        this.recoveryManager = recoveryManager;
        this.target = target;
        this.gapThreshold = gapThreshold;
        this.listeners = listeners;
        this.channelId = channelId;
        this.mdpMessageTypes = mdpMessageTypes;
        snapshotMetaData = new MBOChannelSnapshotMetaData();
    }

    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        final long pkgSequence = mdpPacket.getMsgSeqNum();
        log.trace("Feed {}:{} | handleSnapshotPacket: previous processed sequence '{}', current package's sequence '{}'",
                feedContext.getFeedType(), feedContext.getFeed(), lastProcessedSeqNum, pkgSequence);
        try {
            lock.lock();
            switch (currentState) {
                case INITIAL:
                case OUTOFSYNC:
                    mdpPacket.forEach(mdpMessage -> {
                        updateSemanticMsgType(mdpMessageTypes, mdpMessage);
                        long lastMsgSeqNumProcessed = mdpMessage.getUInt32(LAST_MSG_SEQ_NUM_PROCESSED);
                        int securityId = mdpMessage.getInt32(SECURITY_ID);
                        long noChunks = mdpMessage.getUInt32(NO_CHUNKS);
                        long currentChunk = mdpMessage.getUInt32(CURRENT_CHUNK);
                        long totNumReports = mdpMessage.getUInt32(TOT_NUM_REPORTS);
                        snapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId, noChunks, currentChunk);
                        target.handleSnapshotPacket(feedContext, mdpPacket);
                    });
                    break;
            }
            if(snapshotMetaData.isWholeSnapshotReceived()){
                long snapshotSequence = snapshotMetaData.getSnapshotSequence();
                boolean success = handleStateAfterSnapshotRecovery(feedContext, snapshotSequence);
                if(success){
                    recoveryManager.stopRecovery();
                    switchState(ChannelState.SYNC);
                }
                snapshotMetaData.reset();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        final long pkgSequence = mdpPacket.getMsgSeqNum();
        log.trace("Feed {}:{} | handleIncrementalPacket: previous processed sequence '{}', current package's sequence '{}'",
                feedContext.getFeedType(), feedContext.getFeed(), lastProcessedSeqNum, pkgSequence);
        try {
            lock.lock();
            switch (currentState){
                case SYNC:
                    long expectedSequence = lastProcessedSeqNum + 1;
                    if(pkgSequence == expectedSequence){
                        target.handleIncrementalPacket(feedContext, mdpPacket);
                        lastProcessedSeqNum = pkgSequence;
                    } else if(pkgSequence > expectedSequence){
                        buffer.add(mdpPacket);
                        if(pkgSequence > (expectedSequence + gapThreshold)){
                            recoveryManager.startRecovery();
                            switchState(ChannelState.OUTOFSYNC);
                        }
                    } else {
                        log.trace("Feed {}:{} | handleIncrementalPacket: package that has sequence '{}' has been skipped. Expected sequence '{}'",
                                feedContext.getFeedType(), feedContext.getFeed(), pkgSequence, expectedSequence);
                    }
                    break;
                case INITIAL:
                case OUTOFSYNC:
                    buffer.add(mdpPacket);
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    public interface RecoveryManager {
        void startRecovery();
        void stopRecovery();
    }

    private void switchState(ChannelState newState) {
        log.debug("Channel '{}' has changed its state from '{}' to '{}'", channelId, currentState, newState);
        listeners.forEach(channelListener -> channelListener.onChannelStateChanged(channelId, currentState, newState));
        currentState = newState;
    }

    private boolean handleStateAfterSnapshotRecovery(MdpFeedContext feedContext, long snapshotSequence){
        lastProcessedSeqNum = snapshotSequence;
        for (MdpPacket mdpPacket : buffer) {
            long pkgSequence = mdpPacket.getMsgSeqNum();
            long expectedSequence = lastProcessedSeqNum + 1;
            if(pkgSequence == expectedSequence) {
                target.handleIncrementalPacket(feedContext, mdpPacket);
                lastProcessedSeqNum = pkgSequence;
            } else if(pkgSequence > expectedSequence){
                break;
            }
        }
        return buffer.isEmpty();
    }
}
