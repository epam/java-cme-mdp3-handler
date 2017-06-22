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
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import net.openhft.chronicle.bytes.NativeBytesStore;

import java.util.Arrays;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.MESSAGE_SEQ_NUM_OFFSET;

public class MDPOffHeapBuffer implements Buffer<MdpPacket> {
    private final static long UNDEFINED_VALUE = Integer.MAX_VALUE;
    private final MdpPacket[] data;
    private MdpPacket resultPacket = MdpPacket.allocate();
    private final MdpPacket emptyPacket = MdpPacket.allocate();
    private boolean full;

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

    @Override
    public synchronized void add(MdpPacket entity) {
        int firstEmptyPosition = calculateFirstEmptyPosition();
        MdpPacket mdpPacket = data[firstEmptyPosition];
        copy(entity, mdpPacket);
        sort();
    }

    /**
     * It returns the entities in sorted order and removes them after.
     * Entry was returned in previous call will be filled the data of next entry. Make sure you took data from it and don't use it anymore.
     * @return T or null if buffer is empty.
     */
    @Override
    public synchronized MdpPacket remove() {
        MdpPacket nextPackage = data[0];
        if(isPacketEmpty(nextPackage)){
            return null;
        }
        copy(nextPackage, resultPacket);
        copy(emptyPacket, nextPackage);
        System.arraycopy(data, 1, data, 0, data.length - 1);
        data[data.length - 1] = nextPackage;
        full = false;
        return resultPacket;
    }

    @Override
    public boolean isEmpty() {
        return !full && isPacketEmpty(data[0]);
    }

    private int calculateFirstEmptyPosition(){
        if(full){
            return 0;
        }
        for (int i = 0; i < data.length; i++) {
            MdpPacket nextPackage = data[i];
            if(isPacketEmpty(nextPackage)){
                return i;
            }
        }
        full = true;
        return 0;
    }

    private void sort(){
        //Arrays.sort is not the best variant here because it allocates TimSort class and array into it every time.
        Arrays.sort(data, (o1, o2) -> {
            long sequence1 = o1.getMsgSeqNum();
            long sequence2 = o2.getMsgSeqNum();
            return Long.compare(sequence1, sequence2);
        });
    }

    private void copy(MdpPacket from, MdpPacket to){
        to.buffer().copyFrom(from.buffer());
        to.length(from.getPacketSize());
    }

    private boolean isPacketEmpty(MdpPacket mdpPacket){
        return mdpPacket.getMsgSeqNum() == UNDEFINED_VALUE;
    }
}
