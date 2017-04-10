package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class GapMBOChannelController implements MBOChannelController {
    private Lock lock = new ReentrantLock();
    private long lastProcessedSeqNum;
    private int gapThreshold;
    private CircularBuffer<MdpPacket> buffer;
    private ChannelContext channelContext;
    private MBOChannelController target;

    public GapMBOChannelController(MBOChannelController target, ChannelContext channelContext, CircularBuffer<MdpPacket> buffer, int gapThreshold) {
        this.buffer = buffer;
        this.channelContext = channelContext;
        this.target = target;
    }

    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {

    }

    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        final long pkgSequence = mdpPacket.getMsgSeqNum();
//        log.trace("Feed {}:{} | handleIncrementalPacket: processed sequence '{}', package's sequence '{}'",
//                feedContext.getFeedType(), feedContext.getFeed(), lastProcessedSeqNum, pkgSequence);
        try {
            lock.lock();
            long expectedSequence = lastProcessedSeqNum + 1;
            if(pkgSequence == expectedSequence){
                target.handleIncrementalPacket(feedContext, mdpPacket);
            } else if(pkgSequence > expectedSequence){
                buffer.add(mdpPacket);
                if(pkgSequence > (expectedSequence + gapThreshold)){
                    channelContext.startSnapshotMBOFeeds();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
