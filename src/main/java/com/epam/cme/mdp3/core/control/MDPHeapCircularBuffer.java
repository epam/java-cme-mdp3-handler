package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.MdpPacket;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This is dirty implementation. It has been created as quick implementation of CircularBuffer.
 */
public class MDPHeapCircularBuffer implements CircularBuffer<MdpPacket> {
    private PriorityQueue<MdpPacket> queue = new PriorityQueue<>(new MdpPacketComparator());
    private final int capacity;

    public MDPHeapCircularBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void add(MdpPacket entity) {
        if(queue.size() == capacity){
            queue.poll();
        }
        queue.add(entity);
    }

    @Override
    public MdpPacket remove() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private class MdpPacketComparator implements Comparator<MdpPacket>{

        @Override
        public int compare(MdpPacket o1, MdpPacket o2) {
            long sequence1 = o1.getMsgSeqNum();
            long sequence2 = o2.getMsgSeqNum();
            return Long.compare(sequence1, sequence2);
        }
    }

}
