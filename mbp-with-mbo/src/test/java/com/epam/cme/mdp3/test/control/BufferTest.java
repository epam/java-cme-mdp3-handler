package com.epam.cme.mdp3.test.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.control.IMDPOffHeapBuffer;
import com.epam.cme.mdp3.control.MDPOffHeapBuffer;
import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Test;


import static org.junit.Assert.*;


public class BufferTest {

    @Test
    public void elementsMustBeInSequenceOrder(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(5);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        buffer.add(4, n4);
        buffer.add(1, n1);
        buffer.add(5, n5);
        buffer.add(2, n2);
        buffer.add(3, n3);
        assertEquals(5, buffer.getLastMsgSeqNum());
        for (int i = 1; i <= 5; i++) {
            MdpPacket nextPacket = buffer.remove(i);
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
    }

    @Test
    public void elementsMustNotDuplicateSequence(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(5);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        buffer.add(1, n1);
        buffer.add(1, n2);
        buffer.add(2, n3);
        assertEquals(2, buffer.getLastMsgSeqNum());
        for (int i = 1; i <= 2; i++) {
            MdpPacket nextPacket = buffer.remove(i);
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
        assertNull(buffer.remove(1));
    }

    @Test
    public void bufferMustCopyDataFromObject(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket packet = MdpPacket.instance();
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        buffer.add(1, packet);
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        buffer.add(2, packet);
        packet.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        buffer.add(3, packet);
        assertEquals(3, buffer.getLastMsgSeqNum());
        for (int i = 1; i <= 3; i++) {
            MdpPacket nextPacket = buffer.remove(i);
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
    }

    @Test
    public void lowElementsMustBeRemovedIfBufferIsFull(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        buffer.add(1, n1);
        buffer.add(2, n2);
        buffer.add(3, n3);
        buffer.add(5, n5);
        buffer.add(4, n4);
        assertEquals(5, buffer.getLastMsgSeqNum());
        for (int i = 3; i <= 5; i++) {            
            MdpPacket nextPacket = buffer.remove(i);
            assertEquals(i, nextPacket.getMsgSeqNum());
        }
    }
    
    @Test
    public void outOfOrderAdditionAndRemoval(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        buffer.add(3, n3);
        buffer.add(1, n1);
        buffer.add(5, n5);
        buffer.add(2, n2);
        buffer.add(4, n4);
        assertEquals(5, buffer.getLastMsgSeqNum());
        
        MdpPacket nextPacket = buffer.remove(3);
        assertEquals(3, nextPacket.getMsgSeqNum());
        nextPacket = buffer.remove(4);
        assertEquals(4, nextPacket.getMsgSeqNum());
        nextPacket = buffer.remove(2);
        assertEquals(2, nextPacket.getMsgSeqNum());
                
        MdpPacket n6 = MdpPacket.instance(); n6.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(6));
        MdpPacket n7 = MdpPacket.instance(); n7.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(7));
        MdpPacket n8 = MdpPacket.instance(); n8.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(8));
        MdpPacket n9 = MdpPacket.instance(); n9.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(9));
        buffer.add(7, n7);
        buffer.add(8, n8);
        buffer.add(6, n6);
        buffer.add(9, n9);
        assertEquals(9, buffer.getLastMsgSeqNum());
        
        nextPacket = buffer.remove(7);
        assertEquals(7, nextPacket.getMsgSeqNum());
        nextPacket = buffer.remove(8);
        assertEquals(8, nextPacket.getMsgSeqNum());
        nextPacket = buffer.remove(9);
        assertEquals(9, nextPacket.getMsgSeqNum());
    }
    
    @Test
    public void addAndRemoveHalfFull(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(5);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        MdpPacket n2 = MdpPacket.instance(); n2.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(2));
        MdpPacket n3 = MdpPacket.instance(); n3.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(3));
        MdpPacket n4 = MdpPacket.instance(); n4.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(4));
        MdpPacket n5 = MdpPacket.instance(); n5.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(5));
        MdpPacket n6 = MdpPacket.instance(); n6.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(6));
        MdpPacket n7 = MdpPacket.instance(); n7.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(7));
        MdpPacket n8 = MdpPacket.instance(); n8.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(8));
        buffer.add(1, n1);
        buffer.add(2, n2);
        buffer.add(3, n3);
        buffer.add(5, n5);
        buffer.add(4, n4);
        assertEquals(5, buffer.getLastMsgSeqNum());
        MdpPacket nextPacket = buffer.remove(1);
        assertEquals(1, nextPacket.getMsgSeqNum());

        nextPacket = buffer.remove(2);
        assertEquals(2, nextPacket.getMsgSeqNum());
        
        buffer.add(7, n7);
        buffer.add(6, n6);
        buffer.add(8, n8);
        assertEquals(8, buffer.getLastMsgSeqNum());
        for (int x = 4; x <= 8; x++) {
            nextPacket = buffer.remove(x);
            assertEquals(x, nextPacket.getMsgSeqNum());        	
        }
    }
    
    @Test
    public void methodRemoveShouldReturnNullIfBufferIsEmpty(){
        IMDPOffHeapBuffer buffer = new MDPOffHeapBuffer(3);
        MdpPacket n1 = MdpPacket.instance(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
        buffer.add(1, n1);
        assertEquals(1, buffer.getLastMsgSeqNum());
        MdpPacket nextPacket = buffer.remove(1);
        assertEquals(1, nextPacket.getMsgSeqNum());
        assertNull(buffer.remove(1));
    }
}