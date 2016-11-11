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

import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.sbe.message.SbeBuffer;
import com.epam.cme.mdp3.sbe.message.SbeBufferImpl;
import com.epam.cme.mdp3.sbe.message.SbeConstants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IncrementalRefreshQueue {
    private IncrementalRefreshHolder[] slots;
    private int queueSize;
    private long lastRptSeqNum = 0;
    private final SbeBuffer sbeBuffer = new SbeBufferImpl();

    public IncrementalRefreshQueue(final int size, final int incrQueueEntryDefSize) {
        final ByteBuffer byteBuffer =
                ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        sbeBuffer.wrapForParse(byteBuffer);
        this.slots = new IncrementalRefreshHolder[size];
        this.queueSize = size;
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new IncrementalRefreshHolder(incrQueueEntryDefSize);
        }
    }

    public boolean exist(final long rptSeqNum) {
        final int pos = (int) rptSeqNum % this.queueSize;
        final IncrementalRefreshHolder packetHolder = this.slots[pos];
        return packetHolder.contains(rptSeqNum);
    }

    public int poll(final long rptSeqNum, final IncrementalRefreshQueueEntry incrEntry) {
        final int pos = (int) rptSeqNum % this.queueSize;
        final IncrementalRefreshHolder packetHolder = this.slots[pos];
        return packetHolder.get(rptSeqNum, this.sbeBuffer, incrEntry);
    }

    public boolean push(final long rptSeqNum, final IncrementalRefreshQueueEntry incrEntry) {
        final int pos = (int) rptSeqNum % this.queueSize;
        final IncrementalRefreshHolder packetHolder = this.slots[pos];
        final boolean res = packetHolder.put(incrEntry, rptSeqNum);
        if (res) {
            this.lastRptSeqNum = rptSeqNum;
        }
        return res;
    }

    public long getLastRptSeqNum() {
        return this.lastRptSeqNum;
    }

    public void clear() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].reset();
        }
        this.lastRptSeqNum = 0;
    }

    public void release() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].release();
        }
        this.lastRptSeqNum = 0;
    }

    final static class IncrementalRefreshQueueEntry {
        MdpGroupEntry groupEntry;
        short matchEventIndicator;
        long incrPcktSeqNum;

        public IncrementalRefreshQueueEntry(MdpGroupEntry groupEntry) {
            this.groupEntry = groupEntry;
        }
    }
}
