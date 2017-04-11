package com.epam.cme.mdp3.core.control;

public class MBOChannelSnapshotMetaData {
    public void reset(){

    }

    public void update(long lastMsgSeqNumProcessed, int securityId, long noChunks, long currentChunk){

    }

    public boolean isWholeSnapshotReceived(){
        return false;
    }

    public long getSnapshotSequence(){
        return 0;
    }
}
