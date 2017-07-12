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

@Deprecated
public class PacketQueue {
    private PacketHolder[] slots;
    private int queueSize;
    private long lastSeqNum = 0;

    public PacketQueue(final int size, final int incrQueueDefPacketSize) {
        this.slots = new PacketHolder[size];
        this.queueSize = size;
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new PacketHolder(incrQueueDefPacketSize);
        }
    }

    public boolean exist(final long seqNum) {
        final int pos = (int) seqNum % this.queueSize;
        final PacketHolder packetHolder = this.slots[pos];
        return packetHolder.contains(seqNum);
    }

    public int poll(final long seqNum, final MdpPacket mdpPacket) {
        final int pos = (int) seqNum % this.queueSize;
        final PacketHolder packetHolder = this.slots[pos];
        return packetHolder.get(seqNum, mdpPacket);
    }

    public boolean push(final long seqNum, final MdpPacket mdpPacket) {
        final int pos = (int) seqNum % this.queueSize;
        final PacketHolder packetHolder = this.slots[pos];
        final boolean res = packetHolder.put(mdpPacket, seqNum);
        if (res) {
            this.lastSeqNum = seqNum;
        }
        return res;
    }

    public long getLastSeqNum() {
        return lastSeqNum;
    }

    public void clear() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].reset();
        }
        this.lastSeqNum = 0;
    }

    public void release() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].release();
        }
        this.lastSeqNum = 0;
    }
}
