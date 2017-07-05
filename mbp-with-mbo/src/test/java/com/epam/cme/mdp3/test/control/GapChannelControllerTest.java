package com.epam.cme.mdp3.test.control;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.control.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.Constants;
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

import static org.junit.Assert.*;

public class GapChannelControllerTest {
    private GapChannelController gapChannelController;
    private TestChannelController testChannelController;
    private final int bufferCapacity = 10;
    private final String testChannelId = "1";
    private TestSnapshotRecoveryManager testRecoveryManager;
    private boolean recoveryStarted;
    private TestChannelListener testChannelListener;
    private InstrumentManager instrumentManager;

    @Before
    public void init() throws Exception {
        recoveryStarted = false;
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(Constants.TEMPLATE_NAME).toURI());
        testChannelController = new TestChannelController();
        Buffer<MdpPacket> buffer = new MDPOffHeapBuffer(bufferCapacity);
        testRecoveryManager = new TestSnapshotRecoveryManager();
        SnapshotCycleHandler mboSnapshotCycleHandler = new OffHeapSnapshotCycleHandler();
        testChannelListener = new TestChannelListener();
        List<ChannelListener> mboChannelListeners = Collections.singletonList(testChannelListener);
        instrumentManager = new MdpInstrumentManager("TEST", mboChannelListeners);
        ChannelController targetForBuffered = new BufferedMessageRouter(testChannelId, instrumentManager, mdpMessageTypes, mboChannelListeners, mboSnapshotCycleHandler);
        gapChannelController = new GapChannelController(mboChannelListeners, testChannelController, targetForBuffered, testRecoveryManager, buffer, 0, testChannelId, mdpMessageTypes, mboSnapshotCycleHandler, null, null);

    }

    @Test
    public void itMustChangeItsStateAndBeReadyToWorkAfterSnapshot() throws InterruptedException {
        assertNull(testChannelListener.getPrevSate());
        assertNull(testChannelListener.getCurrentSate());
        int lastMsgSeqNumProcessed = 1000;
        Pair<MdpFeedContext, MdpPacket> mdpFeedContextMdpPacketPair = sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        assertNotNull(mdpFeedContextMdpPacketPair);

        assertEquals(ChannelState.INITIAL, testChannelListener.getPrevSate());
        assertEquals(ChannelState.SYNC, testChannelListener.getCurrentSate());
    }

    @Test
    public void duplicateMessagesWhichWereTakenFromBufferAndHaveSeqNumGreaterThanHighSnapshotSeqShouldBeIgnored() throws Exception {
        int instrument = 1, instrumentLastMsgSeqNumProcessed = 1, incrementSequence = 2;
        byte ignored = 0;
        instrumentManager.registerSecurity(instrument, "", ignored, ignored);

        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(incrementSequence, new int[]{instrument}, new short[]{1}));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(incrementSequence, new int[]{instrument}, new short[]{1}));//duplicate

        sendSnapshotMessage(1, instrument, instrumentLastMsgSeqNumProcessed, 1, 1, 1);
        sendSnapshotMessage(1, instrument, instrumentLastMsgSeqNumProcessed, 1, 1, 1);//next cycle

        Pair<MdpFeedContext, MdpPacket> pair = testChannelController.nextIncrementalMessage();
        assertNotNull(pair);
        assertEquals(incrementSequence, pair.getRight().getMsgSeqNum());

        assertNull(testChannelListener.nextIncrementMessage());
        assertNull(testChannelController.nextIncrementalMessage());
    }

    @Test
    public void duplicateMessagesWhichWereTakenFromBufferAndHaveSeqNumLessThanHighSnapshotSeqShouldBeIgnored() throws Exception {
        int instrument = 1, instrumentLastMsgSeqNumProcessed = 1, incrementSequence = 2;
        byte ignored = 0;
        instrumentManager.registerSecurity(instrument, "", ignored, ignored);

        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(incrementSequence, new int[]{instrument}, new short[]{1}));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(incrementSequence, new int[]{instrument}, new short[]{1}));//duplicate

        sendSnapshotMessage(1, instrument, instrumentLastMsgSeqNumProcessed, 1, 1, 2);
        sendSnapshotMessage(2, 2, 2, 1, 1, 2);
        sendSnapshotMessage(1, instrument, instrumentLastMsgSeqNumProcessed, 1, 1, 1);//next cycle

        TestChannelListener.IncrementalRefreshEntity incrementMessage = testChannelListener.nextIncrementMessage();
        assertNotNull(incrementMessage);
        assertEquals(instrument, incrementMessage.getSecurityId());
        assertEquals(incrementSequence, incrementMessage.getMsgSeqNum());

        assertNull(testChannelListener.nextIncrementMessage());

        assertNull(testChannelController.nextIncrementalMessage());
    }

    @Test
    public void entriesFromIncrementShouldBeSentAccordingToSnapshotSequence() throws Exception {
        int instrument1 = 1, instrument1lastMsgSeqNumProcessed = 1, instrument1Sequence = 2;
        int instrument2 = 2, instrument2lastMsgSeqNumProcessed = 3, instrument2Sequence = 4;

        byte ignored = 0;
        instrumentManager.registerSecurity(instrument1, "", ignored, ignored);

        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(instrument1Sequence, new int[]{instrument1}, new short[]{1}));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(instrument2Sequence, new int[]{instrument2}, new short[]{1}));

        sendSnapshotMessage(1, instrument1, instrument1lastMsgSeqNumProcessed, 1, 1, 3);
        sendSnapshotMessage(2, 3, 2, 1, 1, 3);
        sendSnapshotMessage(3, instrument2, instrument2lastMsgSeqNumProcessed, 1, 1, 3);
        sendSnapshotMessage(1, instrument1, instrument1lastMsgSeqNumProcessed, 1, 1, 3);//next cycle

        TestChannelListener.IncrementalRefreshEntity incrementMessage = testChannelListener.nextIncrementMessage();
        assertNotNull(incrementMessage);
        assertEquals(instrument1, incrementMessage.getSecurityId());
        assertEquals(instrument1Sequence, incrementMessage.getMsgSeqNum());

        Pair<MdpFeedContext, MdpPacket> pair = testChannelController.nextIncrementalMessage();
        assertNotNull(pair);
        assertEquals(instrument2Sequence, pair.getRight().getMsgSeqNum());
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
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(Constants.TEMPLATE_NAME).toURI());
        testChannelController = new TestChannelController();
        Buffer<MdpPacket> buffer = new MDPOffHeapBuffer(bufferCapacity);
        testRecoveryManager = new TestSnapshotRecoveryManager();
        int gapThreshold = 3;
        gapChannelController = new GapChannelController(Collections.singletonList(testChannelListener), testChannelController, testChannelController, testRecoveryManager, buffer, gapThreshold, testChannelId, mdpMessageTypes, new OffHeapSnapshotCycleHandler(), null, null);


        int lastMsgSeqNumProcessed = 0;
        sendInitialMBOSnapshot(lastMsgSeqNumProcessed);
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(1));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(1));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(2));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(4));
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
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(1));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(2));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(4));
        gapChannelController.handleIncrementalPacket(incrementContext, createPacketWithIncrement(5));

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
        mdpPacketWithSnapshot.wrapFromBuffer(ModelUtils.getMBOSnapshotTestMessage(1, 100, lastMsgSeqNumProcessed, 1, 1, 1));
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);

        gapChannelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        gapChannelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);//next cycle
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
        sendSnapshotMessage(1, securityId, lastMsgSeqNumProcessed, 1, 1, 1);
        sendSnapshotMessage(1, securityId, lastMsgSeqNumProcessed, 1, 1, 1);//next cycle
        return testChannelController.nextSnapshotMessage();
    }

    private void sendSnapshotMessage(long sequence, int securityId, long lastMsgSeqNumProcessed, long noChunks, long currentChunk, long totNumReports) {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        mdpPacketWithSnapshot.wrapFromBuffer(ModelUtils.getMBOSnapshotTestMessage(sequence, securityId, lastMsgSeqNumProcessed, noChunks, currentChunk, totNumReports));
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        gapChannelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
    }

    private MdpPacket createPacketWithIncrement(long sequence){
        final MdpPacket mdpPacket = MdpPacket.instance();
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOIncrementTestMessage(sequence);
        mdpPacket.wrapFromBuffer(mboSnapshotTestMessage);
        return mdpPacket;
    }

    private MdpPacket createPacketWithIncrement(long sequence, int[] securityIds, short[] referenceIDs){
        final MdpPacket mdpPacket = MdpPacket.instance();
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(sequence, securityIds, referenceIDs);
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
            incrementalQueue.add(new ImmutablePair<>(feedContext, mdpPacket.copy()));
        }

        @Override
        public void preClose() {

        }

        @Override
        public void close() {

        }

        public Pair<MdpFeedContext, MdpPacket> nextSnapshotMessage() throws InterruptedException {
            return snapshotQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
        }

        public Pair<MdpFeedContext, MdpPacket> nextIncrementalMessage() throws InterruptedException {
            return incrementalQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
        }
    }

    private class TestSnapshotRecoveryManager implements GapChannelController.SnapshotRecoveryManager {

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