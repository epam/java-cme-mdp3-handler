package com.epam.cme.mdp3.test.control;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.control.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.Constants;
import com.epam.cme.mdp3.test.ModelUtils;
import com.epam.cme.mdp3.test.TestChannelListener;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import static com.epam.cme.mdp3.MdConstants.REFERENCE_ID;
import static com.epam.cme.mdp3.MdConstants.SECURITY_ID;
import static org.junit.Assert.*;


public class MBOChannelControllerRouterTest {
    private TestChannelListener testListener = new TestChannelListener();
    private String channelId = "648";
    private int testSecurityId = 99;
    private ChannelController channelController;
    private String secDesc = "for test";
    private InstrumentManager instrumentManager;
    private TestInstrumentObserver instrumentObserver = new TestInstrumentObserver(channelId);

    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(Constants.TEMPLATE_NAME).toURI());
        List<ChannelListener> listeners = Collections.singletonList(testListener);
        instrumentManager = new MdpInstrumentManager(channelId, listeners);
        instrumentManager.registerSecurity(testSecurityId, secDesc);
        channelController = new ChannelControllerRouter(channelId, instrumentManager, mdpMessageTypes,
                Collections.singletonList(testListener), instrumentObserver, Collections.emptyList(), null, null);
    }

    @Test
    public void controllerMustProcessMBOSnapshotMessageAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOSnapshotTestMessage(1, testSecurityId);
        mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
        channelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        assertNotNull(testListener.nextMBOSnapshotMessage());
    }

    @Test
    public void controllerMustProcessMBPSnapshotMessageAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBPSnapshotTestMessage(1, testSecurityId);
        mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
        channelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        assertNotNull(testListener.nextMBPSnapshotMessage());
    }

    @Test
    public void controllerMustProcessMBOIncrementAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        long orderID = 1;
        long mDOrderPriority = 2;
        short mDUpdateAction = 0;
        byte mDEntryType = (byte)48;
        int mDDisplayQty = 5;
        int mDEntryPx = 10;
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBOIncrementTestMessage(1, testSecurityId, orderID, mDOrderPriority, mDUpdateAction, mDEntryType, mDDisplayQty, mDEntryPx);
        mdpPacketWithIncrement.wrapFromBuffer(mboIncrementTestMessage);
        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);
        TestChannelListener.IncrementalRefreshEntity incrementalRefreshEntity = testListener.nextMBOIncrementMessage();
        assertNotNull(incrementalRefreshEntity);
        Assert.assertEquals(channelId, incrementalRefreshEntity.getChannelId());
        Assert.assertEquals(secDesc, incrementalRefreshEntity.getSecDesc());
        Assert.assertEquals(1, incrementalRefreshEntity.getMDPMEssage().getUInt8(SbeConstants.MATCHEVENTINDICATOR_TAG));
        Assert.assertEquals(1, incrementalRefreshEntity.getMsgSeqNum());
        FieldSet orderIDEntry = incrementalRefreshEntity.getOrderIDEntry();
        assertNotNull(orderIDEntry);
        assertEquals(orderID, orderIDEntry.getUInt64(37));
        assertEquals(mDOrderPriority, orderIDEntry.getUInt64(37707));
        assertEquals(mDUpdateAction, orderIDEntry.getInt8(279));
        assertEquals(mDEntryType, orderIDEntry.getUInt8(269));
        assertEquals(mDDisplayQty, orderIDEntry.getInt32(37706));
        assertEquals(mDEntryPx, orderIDEntry.getInt64(270));
    }

    @Test
    public void controllerMustProcessSecurityDefinitionAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithInstrumentDefinition = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        ByteBuffer instrumentDefinitionTestMessage = ModelUtils.getMDInstrumentDefinitionFuture27(1, testSecurityId);
        mdpPacketWithInstrumentDefinition.wrapFromBuffer(instrumentDefinitionTestMessage);
        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithInstrumentDefinition);
        Pair<String,MdpMessage> securityMessagePair = instrumentObserver.nextSecurityMessage();
        assertNotNull(securityMessagePair);
        assertEquals(channelId, securityMessagePair.getLeft());
        assertEquals(SemanticMsgType.SecurityDefinition, securityMessagePair.getRight().getSemanticMsgType());
    }

    @Test
    public void controllerMustProcessMBO2and3IncrementInMBPTemplate() throws Exception {
        int secId1 = 1, secId2 = 4, secId3 = 8;
        short ref1 = 2, ref2 = 3;
        instrumentManager.registerSecurity(secId1, secDesc);
        instrumentManager.registerSecurity(secId2, secDesc);
        instrumentManager.registerSecurity(secId3, secDesc);
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(1, new int[]{secId1, secId2, secId3}, new short[]{ref1, ref2});
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        mdpPacketWithIncrement.wrapFromBuffer(mboIncrementTestMessage);

        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);

        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId1);

        checkMBOIncrementalRefreshEntity(testListener.nextMBOIncrementMessage(), ref1);
        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId2);

        checkMBOIncrementalRefreshEntity(testListener.nextMBOIncrementMessage(), ref2);
        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId3);

        assertNull(testListener.nextMBOIncrementMessage());
        assertNull(testListener.nextMBPIncrementMessage());
    }

    @Test
    public void controllerMustProcessMBO1and3IncrementInMBPTemplate() throws Exception {
        int secId1 = 1, secId2 = 4, secId3 = 8;
        short ref1 = 1, ref2 = 3;
        instrumentManager.registerSecurity(secId1, secDesc);
        instrumentManager.registerSecurity(secId2, secDesc);
        instrumentManager.registerSecurity(secId3, secDesc);
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(1, new int[]{secId1, secId2, secId3}, new short[]{ref1, ref2});
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        mdpPacketWithIncrement.wrapFromBuffer(mboIncrementTestMessage);

        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);

        checkMBOIncrementalRefreshEntity(testListener.nextMBOIncrementMessage(), ref1);
        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId1);

        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId2);

        checkMBOIncrementalRefreshEntity(testListener.nextMBOIncrementMessage(), ref2);
        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId3);

        assertNull(testListener.nextMBOIncrementMessage());
        assertNull(testListener.nextMBPIncrementMessage());
    }

    @Test
    public void controllerMustProcessMBPWhenMBOEntitiesAreLess() throws Exception {
        int secId1 = 1, secId2 = 4, secId3 = 8;
        short ref1 = 1;
        instrumentManager.registerSecurity(secId1, secDesc);
        instrumentManager.registerSecurity(secId2, secDesc);
        instrumentManager.registerSecurity(secId3, secDesc);
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(1, new int[]{secId1, secId2, secId3}, new short[]{ref1});
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        mdpPacketWithIncrement.wrapFromBuffer(mboIncrementTestMessage);

        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);

        checkMBOIncrementalRefreshEntity(testListener.nextMBOIncrementMessage(), ref1);
        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId1);

        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId2);

        checkMBPIncrementalRefreshEntity(testListener.nextMBPIncrementMessage(), secId3);

        assertNull(testListener.nextMBOIncrementMessage());
        assertNull(testListener.nextMBPIncrementMessage());
    }

    private void checkMBOIncrementalRefreshEntity(TestChannelListener.IncrementalRefreshEntity incrementalMBORefreshEntity, short ref){
        assertNotNull(incrementalMBORefreshEntity);
        FieldSet orderIDEntry = incrementalMBORefreshEntity.getOrderIDEntry();
        assertNotNull(orderIDEntry);
        assertEquals(ref, orderIDEntry.getUInt8(REFERENCE_ID));
    }

    private void checkMBPIncrementalRefreshEntity(TestChannelListener.IncrementalRefreshEntity incrementalMBPRefreshEntity, int secId){
        assertNotNull(incrementalMBPRefreshEntity);
        FieldSet mdEntry = incrementalMBPRefreshEntity.getMdEntry();
        assertNotNull(mdEntry);
        assertEquals(secId, mdEntry.getInt32(SECURITY_ID));
    }
}