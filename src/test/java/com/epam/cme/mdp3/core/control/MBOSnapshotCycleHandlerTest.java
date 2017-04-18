package com.epam.cme.mdp3.core.control;

import org.junit.Test;

import static org.junit.Assert.*;


public class MBOSnapshotCycleHandlerTest {

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceived(){
        MBOSnapshotCycleHandler channelSnapshotMetaData = new MBOSnapshotCycleHandler();
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long securityId1NoChunks = 2;
        long securityId2NoChunks = 1;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId2, securityId2NoChunks, 1);
        assertTrue(channelSnapshotMetaData.isWholeSnapshotReceived());
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceivedInOneMessage(){
        MBOSnapshotCycleHandler channelSnapshotMetaData = new MBOSnapshotCycleHandler();
        long totNumReports = 1;
        int securityId1 = 10;
        long securityId1NoChunks = 1;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertTrue(channelSnapshotMetaData.isWholeSnapshotReceived());
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceivedWithZeroSequence(){
        MBOSnapshotCycleHandler channelSnapshotMetaData = new MBOSnapshotCycleHandler();
        long totNumReports = 1;
        int securityId1 = 10;
        long securityId1NoChunks = 1;
        long lastMsgSeqNumProcessed = 0;
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertTrue(channelSnapshotMetaData.isWholeSnapshotReceived());
    }

    @Test
    public void metaDataMustBeRebuiltIfItContainsDifferentSequences(){
        MBOSnapshotCycleHandler channelSnapshotMetaData = new MBOSnapshotCycleHandler();
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long securityId1NoChunks = 2;
        long securityId2NoChunks = 1;
        long lastMsgSeqNumProcessed1 = 1001;
        long lastMsgSeqNumProcessed2 = 1002;
        long lastMsgSeqNumProcessed3 = 1003;
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed1, securityId1, securityId1NoChunks, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed1, securityId1, securityId1NoChunks, 2);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed2, securityId2, securityId2NoChunks, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());

        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed3, securityId1, securityId1NoChunks, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed3, securityId1, securityId1NoChunks, 2);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed3, securityId2, securityId2NoChunks, 1);
        assertTrue(channelSnapshotMetaData.isWholeSnapshotReceived());
    }

    @Test
    public void itMustUpdateMetadataCorrectlyIfThereChangedNumberOfChunks(){
        MBOSnapshotCycleHandler channelSnapshotMetaData = new MBOSnapshotCycleHandler();
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 1, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 2, 1);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 2, 2);
        assertFalse(channelSnapshotMetaData.isWholeSnapshotReceived());
        channelSnapshotMetaData.update(totNumReports, lastMsgSeqNumProcessed, securityId2, 1, 1);
        assertTrue(channelSnapshotMetaData.isWholeSnapshotReceived());
    }

}