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
import com.epam.cme.mdp3.MutableMdpGroupEntry;
import com.epam.cme.mdp3.sbe.message.SbeBuffer;
import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;
import net.openhft.chronicle.bytes.internal.NativeBytesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.epam.cme.mdp3.core.control.IncrementalRefreshQueue.IncrementalRefreshQueueEntry;

public class IncrementalRefreshHolder {
    private static final Logger logger = LoggerFactory.getLogger(IncrementalRefreshHolder.class);
    private long rptSeqNumHolder;
    private NativeBytesStore<Void> store;
    private int entrySize;
    private SbeGroupType sbeGroupType;
    private short matchEventIndicator;
    private long incrPcktSeqNum;

    public IncrementalRefreshHolder(final int incrQueueEntryDefSize) {
        store = NativeBytesStore.nativeStoreWithFixedCapacity(incrQueueEntryDefSize);
    }

    public boolean put(final IncrementalRefreshQueueEntry queueEntry, final long rptSeqNum) {
        if (rptSeqNumHolder < rptSeqNum) {
            rptSeqNumHolder = rptSeqNum;
            final MdpGroupEntry incrEntry = queueEntry.groupEntry;
            this.entrySize = incrEntry.getBlockLength();
            this.matchEventIndicator = queueEntry.matchEventIndicator;
            this.incrPcktSeqNum = queueEntry.incrPcktSeqNum;

            if (store.capacity() < entrySize) {
                store.releaseLast();
                store = NativeBytesStore.nativeStoreWithFixedCapacity(entrySize);
            }
            incrEntry.buffer().copyTo(incrEntry.getAbsoluteEntryOffset(), this.store, entrySize);
            this.sbeGroupType = incrEntry.getSbeGroupType();
            return true;
        } else {
            if(logger.isTraceEnabled()) {
                logger.trace("Incremental Refresh Entry #{} data was not stored because too old", rptSeqNum);
            }
            return false;
        }
    }

    public boolean contains(final long rptSeqNum) {
        return rptSeqNumHolder == rptSeqNum;
    }

    public int get(final long rptSeqNum, final SbeBuffer sbeBuffer, final IncrementalRefreshQueueEntry queueEntry) {
        if (rptSeqNumHolder == rptSeqNum) {
            final MdpGroupEntry incrEntry = queueEntry.groupEntry;
            final MutableMdpGroupEntry entry = (MutableMdpGroupEntry) incrEntry;
            sbeBuffer.copyFrom(this.store);
            queueEntry.incrPcktSeqNum = this.incrPcktSeqNum;
            queueEntry.matchEventIndicator = this.matchEventIndicator;
            entry.reset(sbeBuffer, this.sbeGroupType, 0, this.entrySize);
            return entrySize;
        } else {
            return 0;
        }
    }

    public void reset() {
        rptSeqNumHolder = 0;
    }

    public void release() {
        rptSeqNumHolder = 0;
        this.store.releaseLast();
    }
}
