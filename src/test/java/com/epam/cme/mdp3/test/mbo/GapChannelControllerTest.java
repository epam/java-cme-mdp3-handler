package com.epam.cme.mdp3.test.mbo;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.control.ChannelController;
import com.epam.cme.mdp3.core.control.CircularBuffer;
import com.epam.cme.mdp3.core.control.GapChannelController;
import com.epam.cme.mdp3.core.control.MDPHeapCircularBuffer;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.ModelUtils;
import com.epam.cme.mdp3.test.TestChannelListener;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.epam.cme.mdp3.test.Constants.TEMPLATE_NAME;
import static com.epam.cme.mdp3.test.Constants.WAITING_TIME_IN_MILLIS;
import static org.junit.Assert.*;

public class GapChannelControllerTest {
    private GapChannelController gapChannelController;
    private TestChannelController testChannelController;
    private final int bufferCapacity = 10;
    private final String testChannelId = "1";
    private TestChannelListener testChannelListener;
    private TestRecoveryManager testRecoveryManager;
    private boolean recoveryStarted;

    @Before
    public void init() throws Exception {
        recoveryStarted = false;
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
        testChannelListener = new TestChannelListener();
        List<ChannelListener> listeners = Collections.singletonList(testChannelListener);
        testChannelController = new TestChannelController();
        CircularBuffer<MdpPacket> buffer = new MDPHeapCircularBuffer(bufferCapacity);
        testRecoveryManager = new TestRecoveryManager();
        gapChannelController = new GapChannelController(testChannelController, testRecoveryManager, buffer, 0, listeners, testChannelId, mdpMessageTypes);

    }

    @Test
    public void itMustChangeItsStateAndBeReadyToWorkAfterSnapshot() throws InterruptedException {
        int lastMsgSeqNumProcessed = 1000;
        Pair<MdpFeedContext, MdpPacket> mdpFeedContextMdpPacketPair = sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        assertNotNull(mdpFeedContextMdpPacketPair);

        Pair<ChannelState, ChannelState> channelStateChannelStatePair = testChannelListener.nextChannelState();
        assertNotNull(channelStateChannelStatePair);
        assertEquals(ChannelState.INITIAL, channelStateChannelStatePair.getLeft());
        assertEquals(ChannelState.SYNC, channelStateChannelStatePair.getRight());
    }

    @Test
    public void incrementalMessagesMustBeSentToClientsIfThereAreNoGaps() throws Exception {
        int lastMsgSeqNumProcessed = 1000;
        sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        for(int i = lastMsgSeqNumProcessed +1; i < lastMsgSeqNumProcessed + 10; i++) {
            ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOIncrementTestMessage(i);
            mdpPacketWithIncrement.wrapFromBuffer(mboSnapshotTestMessage);
            gapChannelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);
            Pair<MdpFeedContext, MdpPacket> incrementPair = testChannelController.nextIncrementalMessage();
            assertNotNull(incrementPair);
            MdpPacket mdpPacket = incrementPair.getRight();
            assertEquals(i, mdpPacket.getMsgSeqNum());
        }
    }

    @Test
    public void itMustHandleMessagesInSequenceOrder() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
        testChannelListener = new TestChannelListener();
        List<ChannelListener> listeners = Collections.singletonList(testChannelListener);
        testChannelController = new TestChannelController();
        CircularBuffer<MdpPacket> buffer = new MDPHeapCircularBuffer(bufferCapacity);
        testRecoveryManager = new TestRecoveryManager();
        int gapThreshold = 3;
        gapChannelController = new GapChannelController(testChannelController, testRecoveryManager, buffer, gapThreshold, listeners, testChannelId, mdpMessageTypes);


        int lastMsgSeqNumProcessed = 0;
        sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        assertNotNull(testChannelListener.nextChannelState());//changed from INITIAL to OUTOFSYNC
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(1));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(2));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(4));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(5));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(3));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(6));

        for(int i = 1; i <= 6; i++){
            Pair<MdpFeedContext, MdpPacket> nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
            assertNotNull(nextIncrementalMessagePair);
            assertEquals(i, nextIncrementalMessagePair.getRight().getMsgSeqNum());
        }

    }

    @Test
    public void itMustStartRecoveryIfThereIsGapAndResendMessagesAfter() throws Exception {
        int lastMsgSeqNumProcessed = 0;
        sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        assertNotNull(testChannelListener.nextChannelState());//changed from INITIAL to OUTOFSYNC
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(1));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(2));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(4));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(5));

        Pair<ChannelState, ChannelState> channelStateChannelStatePair = testChannelListener.nextChannelState();
        assertNotNull(channelStateChannelStatePair);
        assertEquals(ChannelState.SYNC, channelStateChannelStatePair.getLeft());
        assertEquals(ChannelState.OUTOFSYNC, channelStateChannelStatePair.getRight());

        assertTrue(recoveryStarted);

        Pair<MdpFeedContext, MdpPacket> nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();

        assertNotNull(nextIncrementalMessagePair);
        assertEquals(1, nextIncrementalMessagePair.getRight().getMsgSeqNum());

        nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
        assertNotNull(nextIncrementalMessagePair);
        assertEquals(2, nextIncrementalMessagePair.getRight().getMsgSeqNum());

        nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
        assertNull(nextIncrementalMessagePair);

        lastMsgSeqNumProcessed = 3;

        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        mdpPacketWithSnapshot.wrapFromBuffer(ModelUtils.getMBOSnapshotTestMessage(2, 100, lastMsgSeqNumProcessed, 1, 1, 1));
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);

        gapChannelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        assertNotNull(testChannelController.nextSnapshotMessage());

        assertFalse(recoveryStarted);

        nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
        assertNotNull(nextIncrementalMessagePair);
        assertEquals(4, nextIncrementalMessagePair.getRight().getMsgSeqNum());

        nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
        assertNotNull(nextIncrementalMessagePair);
        assertEquals(5, nextIncrementalMessagePair.getRight().getMsgSeqNum());

        nextIncrementalMessagePair = testChannelController.nextIncrementalMessage();
        assertNull(nextIncrementalMessagePair);
    }

    private Pair<MdpFeedContext, MdpPacket> sendInitialMBOSnapshot(int lastMsgSeqNumProcessed) throws InterruptedException {
        int securityId = 100;
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        mdpPacketWithSnapshot.wrapFromBuffer(ModelUtils.getMBOSnapshotTestMessage(1, securityId, lastMsgSeqNumProcessed, 1, 1, 1));
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);

        gapChannelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        return testChannelController.nextSnapshotMessage();
    }

    private MdpPacket createPacketWithIncrement(long sequence){
        final MdpPacket mdpPacket = MdpPacket.instance();
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOIncrementTestMessage(sequence);
        mdpPacket.wrapFromBuffer(mboSnapshotTestMessage);
        return mdpPacket;
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

    private class TestRecoveryManager implements GapChannelController.RecoveryManager{

        @Override
        public void startRecovery() {
            recoveryStarted = true;
        }

        @Override
        public void stopRecovery() {
            recoveryStarted = false;
        }
    }
}