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

import java.util.HashMap;
import java.util.Map;

/**
 * Now it is dirty implementation for unit tests and debug. It must be implemented to not allocate data in future, use one off-heap structure.
 */
@Deprecated
public class HeapMBOSnapshotCycleHandler implements MBOSnapshotCycleHandler{
    private final static long SNAPSHOT_SEQUENCE_UNDEFINED = -1;
    private volatile Map<Integer, long[]> metaData;
    private volatile int metaDataSize;

    public void reset(){
        metaData = null;
    }

    public void update(long totNumReports, long lastMsgSeqNumProcessed, int securityId, long noChunks, long currentChunk){
        if(metaData == null || metaDataSize != totNumReports){
            metaDataSize = (int)totNumReports;
            metaData = new HashMap<>(metaDataSize);
        }
        long[] securityIdMetaData = metaData.computeIfAbsent(securityId, k -> getEmptyArray((int) noChunks));
        if(securityIdMetaData.length != noChunks){
            securityIdMetaData = getEmptyArray((int) noChunks);
            metaData.put(securityId, securityIdMetaData);
        }
        securityIdMetaData[(int) currentChunk - 1] = lastMsgSeqNumProcessed;
    }

    @Override
    public long getSnapshotSequence(int securityId) {
        if(metaData == null){
            return SNAPSHOT_SEQUENCE_UNDEFINED;
        }
        long[] array = metaData.get(securityId);
        return array != null ? array[0] : SNAPSHOT_SEQUENCE_UNDEFINED;
    }

    @Override
    public long getSmallestSnapshotSequence() {
        return getSnapshotSequence(false);
    }

    @Override
    public long getHighestSnapshotSequence() {
        return getSnapshotSequence(true);
    }

    public long getSnapshotSequence(boolean highest) {
        long sequence = SNAPSHOT_SEQUENCE_UNDEFINED;
        boolean result = true;
        if(metaData != null && metaData.size() == metaDataSize){
            for (long[] securityMetaData : metaData.values()) {
                for (int j = 0; j < securityMetaData.length; j++) {
                    long seq = securityMetaData[j];
                    if(seq != SNAPSHOT_SEQUENCE_UNDEFINED){
                        if(sequence == SNAPSHOT_SEQUENCE_UNDEFINED) {
                            sequence = seq;
                        } else if(highest && seq > sequence){
                            sequence = seq;
                        } else if(!highest && seq < sequence){
                            sequence = seq;
                        }
                    } else {
                        result = false;
                        break;
                    }
                }
            }
        } else {
            result = false;
        }
        if(!result){
            sequence = SNAPSHOT_SEQUENCE_UNDEFINED;
        }
        return sequence;
    }

    private long[] getEmptyArray(int length){
        long[] result = new long[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = SNAPSHOT_SEQUENCE_UNDEFINED;
        }
        return result;
    }
}
