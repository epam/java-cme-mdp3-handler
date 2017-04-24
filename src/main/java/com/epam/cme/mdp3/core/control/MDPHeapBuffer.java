/*
 * Copyright 2004-2016 EPAM Systems
 * This file is part of Java Market Data Handler for CME Market Data (MDP 3.0).
 * Java Market Data Handler for CME Market Data (MDP 3.0) is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Java Market Data Handler for CME Market Data (MDP 3.0) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Java Market Data Handler for CME Market Data (MDP 3.0).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.MdpPacket;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This is dirty implementation. It has been created as quick implementation of Buffer.
 */
public class MDPHeapBuffer implements Buffer<MdpPacket> {
    private PriorityQueue<MdpPacket> queue = new PriorityQueue<>(new MdpPacketComparator());
    private final int capacity;

    public MDPHeapBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void add(MdpPacket entity) {
        if(queue.size() == capacity){
            queue.poll();
        }
        queue.add(entity.copy());
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
