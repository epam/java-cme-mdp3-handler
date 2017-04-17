package com.epam.cme.mdp3.test.mbo;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.control.*;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.ModelUtils;
import com.epam.cme.mdp3.test.TestChannelListener;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import static com.epam.cme.mdp3.mktdata.MdConstants.REFERENCE_ID;
import static com.epam.cme.mdp3.mktdata.MdConstants.SECURITY_ID;
import static com.epam.cme.mdp3.test.Constants.*;
import static org.junit.Assert.*;


public class MBOChannelControllerRouterTest {
    private TestChannelListener testListener = new TestChannelListener();
    private String channelId = "648";
    private int testSecurityId = 99;
    private ChannelController channelController;
    private String secDesc = "for test";
    private InstrumentManager instrumentManager;

    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
        List<ChannelListener> listeners = Collections.singletonList(testListener);
        instrumentManager = new MBOInstrumentManager(channelId, listeners);
        instrumentManager.registerSecurity(testSecurityId, secDesc, 0, (byte)0);
        channelController = new MBOChannelControllerRouter(instrumentManager, mdpMessageTypes);
    }

    @Test
    public void controllerMustProcessSnapshotMessageAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOSnapshotTestMessage(1, testSecurityId);
        mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
        channelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        assertNotNull(testListener.nextMBOSnapshotMessage());
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
        TestChannelListener.IncrementalRefreshEntity incrementalRefreshEntity = testListener.nextIncrementMessage();
        assertNotNull(incrementalRefreshEntity);
        assertEquals(channelId, incrementalRefreshEntity.getChannelId());
        assertEquals(secDesc, incrementalRefreshEntity.getSecDesc());
        assertEquals(1, incrementalRefreshEntity.getMatchEventIndicator());
        assertEquals(1, incrementalRefreshEntity.getMsgSeqNum());
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
    public void controllerMustProcessMBOIncrementInMBPTemplate() throws Exception {
        int secId1 = 1, secId2 = 4, secId3 = 8;
        short ref1 = 2, ref2 = 3;
        instrumentManager.registerSecurity(secId1, secDesc, 0, (byte)0);
        instrumentManager.registerSecurity(secId2, secDesc, 0, (byte)0);
        instrumentManager.registerSecurity(secId3, secDesc, 0, (byte)0);
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(1, new int[]{secId1, secId2, secId3}, new short[]{ref1, ref2});
        final MdpPacket mdpPacketWithIncrement = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        mdpPacketWithIncrement.wrapFromBuffer(mboIncrementTestMessage);

        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithIncrement);

        TestChannelListener.IncrementalRefreshEntity incrementalRefreshEntity = testListener.nextIncrementMessage();
        assertNotNull(incrementalRefreshEntity);
        FieldSet orderIDEntry = incrementalRefreshEntity.getOrderIDEntry();
        assertEquals(ref1, orderIDEntry.getUInt8(REFERENCE_ID));
        FieldSet mdEntry = incrementalRefreshEntity.getMdEntry();
        assertEquals(secId2, mdEntry.getInt32(SECURITY_ID));

        incrementalRefreshEntity = testListener.nextIncrementMessage();
        assertNotNull(incrementalRefreshEntity);
        orderIDEntry = incrementalRefreshEntity.getOrderIDEntry();
        assertEquals(ref2, orderIDEntry.getUInt8(REFERENCE_ID));
        mdEntry = incrementalRefreshEntity.getMdEntry();
        assertEquals(secId3, mdEntry.getInt32(SECURITY_ID));

        incrementalRefreshEntity = testListener.nextIncrementMessage();
        assertNull(incrementalRefreshEntity);
    }


}