package com.epam.cme.mdp3.test;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.gen.MDEntryTypeBook;
import org.junit.Test;

import java.util.Iterator;

import static com.epam.cme.mdp3.test.Constants.TEMPLATE_NAME;
import static org.junit.Assert.*;


public class MdpPacketTest {

    @Test
    public void itMustBeCopyItselfCompletely(){
        MdpPacket packet = MdpPacket.allocate();
        int expectedSchemaId = 43;
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket copy = packet.copy();
        Iterator<MdpMessage> realIterator = packet.iterator();
        Iterator<MdpMessage> copyIterator = copy.iterator();
        while (realIterator.hasNext()){
            MdpMessage mdpMessage = realIterator.next();
            int schemaId = mdpMessage.getSchemaId();
            assertEquals(expectedSchemaId, schemaId);
        }
        while (copyIterator.hasNext()){
            MdpMessage mdpMessage = copyIterator.next();
            int schemaId = mdpMessage.getSchemaId();
            assertEquals(expectedSchemaId, schemaId);
        }
    }

    @Test
    public void fieldSetMustBeCopiedCompletely() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
        MdpPacket packet = MdpPacket.allocate();
        long sequence = 1;
        int securityId = 2;
        long orderID = 3;
        long mDOrderPriority = 4;
        short mDUpdateAction = 5;
        byte mDEntryType = MDEntryTypeBook.Bid.value();
        int mDDisplayQty = 7;
        int mDEntryPx = 8;
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(sequence, securityId, orderID, mDOrderPriority, mDUpdateAction, mDEntryType, mDDisplayQty, mDEntryPx));
        MdpMessage message = packet.iterator().next();
        message.setMessageType(mdpMessageTypes.getMessageType(message.getSchemaId()));
        MdpGroup mdpGroup = SbeGroup.instance();
        message.getGroup(MdConstants.NO_MD_ENTRIES, mdpGroup);
        MdpGroupEntry groupEntry = SbeGroupEntry.instance();
        mdpGroup.getEntry(1, groupEntry);
        MdpGroupEntry copied = groupEntry.copy();

        assertEquals(orderID, copied.getUInt64(37));
        assertEquals(mDOrderPriority, copied.getUInt64(37707));
        assertEquals(mDUpdateAction, copied.getInt8(279));
        assertEquals(mDEntryType, copied.getUInt8(269));
        assertEquals(mDDisplayQty, copied.getInt32(37706));
        assertEquals(mDEntryPx, copied.getInt64(270));
    }

}