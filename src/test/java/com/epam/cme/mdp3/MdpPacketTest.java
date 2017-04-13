package com.epam.cme.mdp3;

import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Test;

import java.util.Iterator;

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

}