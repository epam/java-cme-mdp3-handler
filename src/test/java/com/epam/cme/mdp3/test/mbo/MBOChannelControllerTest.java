package com.epam.cme.mdp3.test.mbo;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.control.ChannelController;
import com.epam.cme.mdp3.core.control.MBOChannelController;
import com.epam.cme.mdp3.core.control.MBOInstrumentController;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.ModelUtils;
import com.epam.cme.mdp3.test.TestChannelListener;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import static com.epam.cme.mdp3.test.Constants.*;
import static org.junit.Assert.*;


public class MBOChannelControllerTest {
    private TestChannelListener testListener = new TestChannelListener();
    private String channelId = "648";
    private int testSecurityId = 99;
    private ChannelController channelController;
    private String secDesc = "for test";

    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
        List<ChannelListener> listeners = Collections.singletonList(testListener);
        MBOInstrumentController instrumentController = new MBOInstrumentController(listeners, channelId, testSecurityId, secDesc);
        channelController = new MBOChannelController(securityId -> securityId == testSecurityId ? instrumentController : null, mdpMessageTypes);
    }

    @Test
    public void controllerMustProcessSnapshotMessageAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOSnapshotTestMessage(1, testSecurityId);
        mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
        channelController.handleSnapshotPacket(smboContext, mdpPacketWithSnapshot);
        assertNotNull(testListener.nextSnapshotMessage());
    }

    @Test
    public void controllerMustProcessMBOIncrementAndSendToClient() throws InterruptedException {
        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        long orderID = 1;
        long mDOrderPriority = 2;
        short mDUpdateAction = 0;
        byte mDEntryType = (byte)48;
        int mDDisplayQty = 5;
        int mDEntryPx = 10;
        ByteBuffer mboIncrementTestMessage = ModelUtils.getMBOIncrementTestMessage(1, testSecurityId, orderID, mDOrderPriority, mDUpdateAction, mDEntryType, mDDisplayQty, mDEntryPx);
        mdpPacketWithSnapshot.wrapFromBuffer(mboIncrementTestMessage);
        channelController.handleIncrementalPacket(incrementContext, mdpPacketWithSnapshot);
        TestChannelListener.IncrementalRefreshEntity incrementalRefreshEntity = testListener.nextIncrementMessage();
        assertNotNull(incrementalRefreshEntity);
        assertEquals(channelId, incrementalRefreshEntity.getChannelId());
        assertEquals(secDesc, incrementalRefreshEntity.getSecDesc());
        assertEquals(1, incrementalRefreshEntity.getMatchEventIndicator());
        assertEquals(1, incrementalRefreshEntity.getMsgSeqNum());
        FieldSet incrRefreshEntry = incrementalRefreshEntity.getIncrRefreshEntry();
        assertNotNull(incrRefreshEntry);
        assertEquals(orderID, incrRefreshEntry.getUInt64(37));
        assertEquals(mDOrderPriority, incrRefreshEntry.getUInt64(37707));
        assertEquals(mDUpdateAction, incrRefreshEntry.getInt8(279));
        assertEquals(mDEntryType, incrRefreshEntry.getUInt8(269));
        assertEquals(mDDisplayQty, incrRefreshEntry.getInt32(37706));
        assertEquals(mDEntryPx, incrRefreshEntry.getInt64(270));
    }


}