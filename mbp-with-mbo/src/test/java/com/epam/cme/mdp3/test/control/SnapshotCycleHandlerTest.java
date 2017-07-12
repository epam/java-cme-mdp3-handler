package com.epam.cme.mdp3.test.control;

import com.epam.cme.mdp3.control.OffHeapSnapshotCycleHandler;
import com.epam.cme.mdp3.control.SnapshotCycleHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


public class SnapshotCycleHandlerTest {
    private SnapshotCycleHandler cycleHandler;
    
    @Before
    public void init(){
        cycleHandler = new OffHeapSnapshotCycleHandler();
    }


    @Test
    public void snapshotSequenceMustBeUndefinedIfThereWereNoMessages(){
        assertEquals(SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED, cycleHandler.getSnapshotSequence(1));
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceived(){
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long securityId1NoChunks = 2;
        long securityId2NoChunks = 1;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId2, securityId2NoChunks, 1);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceivedInOneMessage(){
        long totNumReports = 1;
        int securityId1 = 10;
        long securityId1NoChunks = 1;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenSnapshotContainsNuChunkHigherThanMaxValue(){
        long totNumReports = 1;
        int securityId1 = 10;
        long securityId1NoChunks = SnapshotCycleHandler.MAX_NO_CHUNK_VALUE + 10;
        long lastMsgSeqNumProcessed = 1001;
        for(int i=1; i <= securityId1NoChunks; i++) {
            assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
            cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, i);
        }
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Test
    public void itMustUpdateMetadataAndGiveCorrectResultWhenWholeSnapshotIsReceivedWithZeroSequence(){
        long totNumReports = 1;
        int securityId1 = 10;
        long securityId1NoChunks = 1;
        long lastMsgSeqNumProcessed = 0;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Ignore("We don't need to do it")
    @Test
    public void metaDataMustBeRebuiltIfItContainsDifferentSequences(){
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long securityId1NoChunks = 2;
        long securityId2NoChunks = 1;
        long lastMsgSeqNumProcessed1 = 1001;
        long lastMsgSeqNumProcessed2 = 1002;
        long lastMsgSeqNumProcessed3 = 1003;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed1, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed1, securityId1, securityId1NoChunks, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed2, securityId2, securityId2NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);

        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed3, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed3, securityId1, securityId1NoChunks, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed3, securityId2, securityId2NoChunks, 1);
        assertEquals(lastMsgSeqNumProcessed3, cycleHandler.getSmallestSnapshotSequence());
    }

    @Test
    public void itMustUpdateMetadataCorrectlyIfThereChangedNumberOfChunks(){
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 1, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 2, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, 2, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId2, 1, 1);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Ignore("We don't need to do it")
    @Test
    public void itMustResetMetadataAndWorkCorrectlyInCaseWhenTotalChunkWasDecreased(){
        long totNumReports = 2;
        int securityId1 = 10;
        int securityId2 = 20;
        long securityId1NoChunks = 2;
        long securityId2NoChunks = 1;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId2, securityId2NoChunks, 1);
        assertTrue(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);

        cycleHandler.reset();
        totNumReports = 1;
        lastMsgSeqNumProcessed = 2000;

        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());
    }

    @Ignore("We don't need to do it")
    @Test
    public void itMustResetMetadataAndWorkCorrectlyInCaseWhenTotalChunkWasIncreased(){
        int securityId1 = 10;
        long totNumReports = 1;
        long securityId1NoChunks = 2;
        long lastMsgSeqNumProcessed = 1001;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertEquals(lastMsgSeqNumProcessed, cycleHandler.getSmallestSnapshotSequence());

        cycleHandler.reset();
        totNumReports = 2;

        int securityId2 = 20;
        long securityId2NoChunks = 1;
        lastMsgSeqNumProcessed = 2000;
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        assertFalse(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId2, securityId2NoChunks, 1);
        assertTrue(cycleHandler.getSmallestSnapshotSequence() != SnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED);
    }

}