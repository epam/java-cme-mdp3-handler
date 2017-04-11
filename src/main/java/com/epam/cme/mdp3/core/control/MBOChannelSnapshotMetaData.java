package com.epam.cme.mdp3.core.control;


import java.util.ArrayList;
import java.util.List;

/**
 * Now it is dirty implementation for unit tests and debug. It must be implemented to not allocate data in future, use one off-heap structure.
 */
public class MBOChannelSnapshotMetaData {
    private List<List<Long>> metaData;

    public void reset(){
        metaData = null;
    }

    public void update(long totNumReports, long lastMsgSeqNumProcessed, int securityId, long noChunks, long currentChunk){
        if(metaData == null){
            metaData = new ArrayList<>((int)totNumReports);
        }
    }

    public boolean isWholeSnapshotReceived(){
        return false;
    }

    public long getSnapshotSequence(){
        return 0;
    }
}
