package com.epam.cme.mdp3.test.mbo;

import com.epam.cme.mdp3.Feed;
import com.epam.cme.mdp3.FeedType;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.control.ChannelController;
import com.epam.cme.mdp3.core.control.GapChannelController;
import com.epam.cme.mdp3.test.ModelUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.epam.cme.mdp3.test.Constants.WAITING_TIME_IN_MILLIS;
import static org.junit.Assert.*;

public class GapChannelControllerTest {
    private GapChannelController gapChannelController;
    private TestChannelController testChannelController;

    @Before
    public void init(){
        testChannelController = new TestChannelController();
        gapChannelController = new GapChannelController(testChannelController, null, null, 0, null, null, null);
    }

    @Test
    public void incrementalMessagesMustBeSentToClientsIfThereAreNoGaps() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        for(int i = 1; i < 10; i++) {
            ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOIncrementTestMessage(i);
            mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
            gapChannelController.handleIncrementalPacket(smboContext, mdpPacketWithSnapshot);
            Pair<MdpFeedContext, MdpPacket> mdpFeedContextMdpPacketPair = testChannelController.nextIncrementalMessage();
            assertNotNull(mdpFeedContextMdpPacketPair);
            MdpPacket mdpPacket = mdpFeedContextMdpPacketPair.getRight();
            assertEquals(i, mdpPacket.getMsgSeqNum());
        }
    }

    private class TestChannelController implements ChannelController {
        private BlockingQueue<Pair<MdpFeedContext, MdpPacket>> snapshotQueue = new LinkedBlockingQueue<>();
        private BlockingQueue<Pair<MdpFeedContext, MdpPacket>> incrementalQueue = new LinkedBlockingQueue<>();

        @Override
        public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
            snapshotQueue.add(new ImmutablePair<>(feedContext, mdpPacket));
        }

        @Override
        public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
            incrementalQueue.add(new ImmutablePair<>(feedContext, mdpPacket));
        }

        public Pair<MdpFeedContext, MdpPacket> nextSnapshotMessage() throws InterruptedException {
            return snapshotQueue.poll(WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
        }

        public Pair<MdpFeedContext, MdpPacket> nextIncrementalMessage() throws InterruptedException {
            return incrementalQueue.poll(WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
        }
    }
}