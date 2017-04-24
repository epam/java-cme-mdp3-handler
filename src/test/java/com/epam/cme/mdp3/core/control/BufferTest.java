package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Test;


import static org.junit.Assert.*;


public class BufferTest {

    @Test
    public void elementsMustBeInSequenceOrder(){
        MDPOffHeapBuffer buffer = new MDPOffHeapBuffer(5);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        buffer.add(n4);
        buffer.add(n1);
        buffer.add(n5);
        buffer.add(n2);
        buffer.add(n3);
        for (int i = 1; i <= 5; i++) {
            MdpPacket nextPacket = buffer.remove();
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
    }

    @Test
    public void bufferMustCopyDataFromObject(){
        MDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket packet = MdpPacket.instance();
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        buffer.add(packet);
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        buffer.add(packet);
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        buffer.add(packet);
        for (int i = 1; i <= 3; i++) {
            MdpPacket nextPacket = buffer.remove();
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
    }

    @Test
    public void lowElementsMustBeRemovedIfBufferIsFull(){
        MDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        buffer.add(n1);
        buffer.add(n2);
        buffer.add(n3);
        buffer.add(n5);
        buffer.add(n4);
        for (int i = 3; i <= 5; i++) {
            assertFalse(buffer.isEmpty());
            MdpPacket nextPacket = buffer.remove();
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
        assertTrue(buffer.isEmpty());
    }

    @Test
    public void methodRemoveShouldReturnNullIfBufferIsEmpty(){
        MDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        buffer.add(n1);
        MdpPacket nextPacket = buffer.remove();
        assertEquals(1, nextPacket.getMsgSeqNum());
        assertNull(buffer.remove());
    }


}