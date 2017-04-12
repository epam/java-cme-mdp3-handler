package com.epam.cme.mdp3.core.control;

import java.util.HashMap;
import java.util.Map;

/**
 * Now it is dirty implementation for unit tests and debug. It must be implemented to not allocate data in future, use one off-heap structure.
 */
public class MBOChannelSnapshotMetaData {
    private final static long SNAPSHOT_SEQUENCE_UNDEFINED = -1;
    private volatile Map<Integer, long[]> metaData;
    private volatile long snapshotSequence = SNAPSHOT_SEQUENCE_UNDEFINED;
    private volatile int metaDataSize;

    public void reset(){
        metaData = null;
        snapshotSequence = SNAPSHOT_SEQUENCE_UNDEFINED;
    }

    public void update(long totNumReports, long lastMsgSeqNumProcessed, int securityId, long noChunks, long currentChunk){
        if(metaData == null || metaDataSize != totNumReports){
            metaDataSize = (int)totNumReports;
            metaData = new HashMap<>(metaDataSize);
        }
        long[] securityIdMetaData = metaData.computeIfAbsent(securityId, k -> new long[(int) noChunks]);
        securityIdMetaData[(int)currentChunk -1] = lastMsgSeqNumProcessed;
    }

    public boolean isWholeSnapshotReceived(){
        boolean result = true;
        if(metaData != null && metaData.size() == metaDataSize){
            for (long[] securityMetaData : metaData.values()) {
                for (int j = 0; j < securityMetaData.length; j++) {
                    long seq = securityMetaData[j];
                    if(seq > 0){
                        if(snapshotSequence == SNAPSHOT_SEQUENCE_UNDEFINED){
                            snapshotSequence = seq;
                        }
                        if(seq != snapshotSequence){
                            result = false;
                            break;
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
            snapshotSequence = SNAPSHOT_SEQUENCE_UNDEFINED;
        }
        return result;
    }

    public long getSnapshotSequence() throws IllegalStateException {
        if(snapshotSequence != SNAPSHOT_SEQUENCE_UNDEFINED){
            return snapshotSequence;
        } else {
            throw new IllegalStateException("whole snapshot has not been received yet");
        }
    }
}
