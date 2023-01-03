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

import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.bytes.internal.NativeBytesStore;
import org.agrona.collections.Long2ObjectHashMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferUnderflowException;


public class OffHeapSnapshotCycleHandler implements SnapshotCycleHandler {
    private static final Logger logger = LoggerFactory.getLogger(OffHeapSnapshotCycleHandler.class);
    private final Long2ObjectHashMap<MutableLongToObjPair<LongArray>> dataCache = new Long2ObjectHashMap<>();
    private final Long2ObjectHashMap<MutableLongToObjPair<LongArray>> data = new Long2ObjectHashMap<>();
    private volatile int dataSize;

    @Override
    public void reset(){
        data.forEach((ignore, pair) -> {
            clearArray(pair.getValue());
            pair.setKey(0);
        });
    }

    @Override
    public void update(long totNumReports, long lastMsgSeqNumProcessed, int securityId, long noChunks, long currentChunk) {
        if(currentChunk > noChunks) {
            logger.error("Current chunk number '{}' is more than noChunks number '{}' for securityId '{}'", currentChunk, noChunks, securityId);
            return;
        }
        if(dataSize != totNumReports){
            dataSize = (int)totNumReports;
            dataCache.putAll(data);
            data.clear();
        }
        MutableLongToObjPair<LongArray> securityIdMetaData = data.computeIfAbsent(securityId, ignore -> {
            if (dataCache.containsKey(securityId)) {
                return dataCache.remove(securityId);
            } else {
                long arrayLength = noChunks > MAX_NO_CHUNK_VALUE ? noChunks : MAX_NO_CHUNK_VALUE;
                LongArray newArray = new LongArray(arrayLength);
                clearArray(newArray);
                return new MutableLongToObjPair<>(noChunks, newArray);
            }
        });

        LongArray currentArray = securityIdMetaData.getValue();
        if(securityIdMetaData.getKey() != noChunks) {
            if(currentArray.getLength() < noChunks) {
                currentArray.reInit(noChunks);
            }
            securityIdMetaData.setKey(noChunks);
            clearArray(currentArray);
        }
        currentArray.setValue(currentChunk - 1, lastMsgSeqNumProcessed);
    }

    @Override
    public long getSnapshotSequence(int securityId) {
        MutableLongToObjPair<LongArray> pair = data.get(securityId);
        return pair != null ? pair.getValue().getValue(0) : SNAPSHOT_SEQUENCE_UNDEFINED;
    }

    @Override
    public long getSmallestSnapshotSequence() {
        return getSnapshotSequence(false);
    }

    @Override
    public long getHighestSnapshotSequence() {
        return getSnapshotSequence(true);
    }

    private long getSnapshotSequence(boolean highest) {
        long result = SNAPSHOT_SEQUENCE_UNDEFINED;
        boolean existUndefined = false;
        if(data.size() == dataSize) {
            for (MutableLongToObjPair<LongArray> pair : data.values()) {
                for (int j = 0; j < pair.getKey(); j++) {
                    long seq = pair.getValue().getValue(j);
                    if(seq != SNAPSHOT_SEQUENCE_UNDEFINED){
                        if(result == SNAPSHOT_SEQUENCE_UNDEFINED) {
                            result = seq;
                        } else if(highest && seq > result) {
                            result = seq;
                        } else if(!highest && seq < result) {
                            result = seq;
                        }
                    } else {
                        existUndefined = true;
                        break;
                    }
                }
            }
        } else {
            existUndefined = true;
        }
        if(existUndefined){
            result = SNAPSHOT_SEQUENCE_UNDEFINED;
        }
        return result;
    }

    private void clearArray(LongArray array) {
        for (int i = 0; i < array.getLength(); i++) {
            array.setValue(i, SNAPSHOT_SEQUENCE_UNDEFINED);
        }
    }

    private class LongArray {
        private BytesStore<?, ?> bytes;
        private long length;

        public LongArray(long length) {
            this.bytes = NativeBytesStore.nativeStore(length * Long.BYTES);
            this.length = length;
        }

        public void reInit(long length){
            bytes.releaseLast();
            this.bytes = NativeBytesStore.nativeStore(length * Long.BYTES);
            this.length = length;
        }

        public long getValue(long index) throws BufferUnderflowException {
            return bytes.readLong(index * Long.BYTES);
        }

        public void setValue(long index, long value) {
            if(index < length){
                bytes.writeLong(index * Long.BYTES, value);
            } else {
                logger.error("It tries to set value at '{}' index in array, but the array has '{}' length", index, length);
            }
        }

        public long getLength() {
            return length;
        }
    }

    private class MutableLongToObjPair<T> {
        private long key;
        private T value;

        public MutableLongToObjPair(long key, T value) {
            this.key = key;
            this.value = value;
        }

        public long getKey() {
            return key;
        }

        public void setKey(long key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            MutableLongToObjPair<?> that = (MutableLongToObjPair<?>) o;

            return new EqualsBuilder()
                    .append(key, that.key)
                    .append(value, that.value)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(key)
                    .append(value)
                    .toHashCode();
        }
    }
}
