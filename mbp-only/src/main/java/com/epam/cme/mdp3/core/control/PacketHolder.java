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
import net.openhft.chronicle.bytes.internal.NativeBytesStore;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Deprecated
public class PacketHolder {
    private static final Logger logger = LoggerFactory.getLogger(PacketHolder.class);
    private long seqNumHolder;
    private NativeBytesStore<Void> store;
    private int packetSize;

    public PacketHolder(final int incrQueueDefPacketSize) {
        store = NativeBytesStore.nativeStoreWithFixedCapacity(incrQueueDefPacketSize);
    }

    public boolean put(final MdpPacket mdpPacket, final long seqNum) {
        if (seqNumHolder < seqNum) {
            seqNumHolder = seqNum;
            packetSize = mdpPacket.getPacketSize();

            if (store.capacity() < packetSize) {
                store.releaseLast();
                store = NativeBytesStore.nativeStoreWithFixedCapacity(packetSize);
            }
            mdpPacket.buffer().copyTo(this.store);
            return true;
        } else {
            logger.trace("MDP Packet #{} data was not stored because too old", seqNum);
            return false;
        }
    }

    public boolean contains(final long seqNum) {
        return seqNumHolder == seqNum;
    }

    public int get(final long seqNum, final MdpPacket mdpPacket) {
        if (seqNumHolder == seqNum) {
            mdpPacket.buffer().copyFrom(this.store);
            mdpPacket.buffer().length(packetSize);
            return packetSize;
        } else {
            return 0;
        }
    }

    public void reset() {
        seqNumHolder = 0;
    }

    public void release() {
        seqNumHolder = 0;
        this.store.releaseLast();
    }
}
