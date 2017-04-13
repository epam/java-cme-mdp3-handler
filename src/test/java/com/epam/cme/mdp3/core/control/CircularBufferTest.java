package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Test;


import static org.junit.Assert.*;


public class CircularBufferTest {

    @Test
    public void elementsMustBeInSequenceOrder(){
        MDPHeapCircularBuffer buffer = new MDPHeapCircularBuffer(5);
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
        MDPHeapCircularBuffer buffer = new MDPHeapCircularBuffer(3);
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
        MDPHeapCircularBuffer buffer = new MDPHeapCircularBuffer(3);
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
        for (int i = 3; i <= 5; i++) {
            MdpPacket nextPacket = buffer.remove();
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
        assertTrue(buffer.isEmpty());
    }
}