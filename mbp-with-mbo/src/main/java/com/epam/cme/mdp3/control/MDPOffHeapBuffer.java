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

package com.epam.cme.mdp3.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import net.openhft.chronicle.bytes.NativeBytesStore;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.MESSAGE_SEQ_NUM_OFFSET;

public class MDPOffHeapBuffer {
    private final static long UNDEFINED_VALUE = Integer.MAX_VALUE;
    private final MdpPacket[] data;
    private MdpPacket resultPacket = MdpPacket.allocate();
    private final MdpPacket emptyPacket = MdpPacket.allocate();
    private long lastMsgSeqNum = 0;

    public MDPOffHeapBuffer(int capacity) {
        NativeBytesStore<Void> emptyStore = NativeBytesStore.nativeStoreWithFixedCapacity(SbeConstants.MDP_PACKET_MAX_SIZE);
        emptyStore.writeUnsignedInt(MESSAGE_SEQ_NUM_OFFSET, UNDEFINED_VALUE);
        emptyPacket.buffer().copyFrom(emptyStore);
        data = new MdpPacket[capacity];
        for (int i = 0; i < capacity; i++) {
            MdpPacket mdpPacket = MdpPacket.allocate();
            mdpPacket.buffer().copyFrom(emptyStore);
            data[i] = mdpPacket;
        }
    }
    
    public boolean exist(final long msgSeqNum) {
        final int pos = (int) msgSeqNum % this.data.length;
        final MdpPacket packet = this.data[pos];
        return !isPacketEmpty(packet);
    }

    public MdpPacket remove(final long msgSeqNum) {
        final int pos = (int) msgSeqNum % this.data.length;
        MdpPacket nextPacket = this.data[pos];        
        if(isPacketEmpty(nextPacket)){
            return null;
        }
        copy(nextPacket, resultPacket);
        copy(emptyPacket, nextPacket);
        
        return resultPacket;
    }
    
    public void add(final long msgSeqNum, final MdpPacket packet) {
        final int pos = (int) msgSeqNum % this.data.length;
        MdpPacket emptyPacket = data[pos];
        copy(packet, emptyPacket);
        this.lastMsgSeqNum = msgSeqNum > this.lastMsgSeqNum ? msgSeqNum : this.lastMsgSeqNum;
    }
    
    public long getLastMsgSeqNum() {
        return this.lastMsgSeqNum;
    }
    
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            copy(emptyPacket, data[i]);
        }
        this.lastMsgSeqNum = 0;
    }
    
    private void copy(MdpPacket from, MdpPacket to){
        to.buffer().copyFrom(from.buffer());
        to.length(from.getPacketSize());
    }

    private boolean isPacketEmpty(MdpPacket mdpPacket){
        return mdpPacket.getMsgSeqNum() == UNDEFINED_VALUE;
    }
}
