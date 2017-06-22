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

package com.epam.cme.mdp3.test.perf;

import com.epam.cme.mdp3.core.control.MBOSnapshotCycleHandler;
import com.epam.cme.mdp3.core.control.OffHeapMBOSnapshotCycleHandler;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class MBOSnapshotCycleHandlerTest {
    private MBOSnapshotCycleHandler cycleHandler = new OffHeapMBOSnapshotCycleHandler();
    private long totNumReports = 2;
    private int securityId1 = 10;
    private int securityId2 = 20;
    private long securityId1NoChunks = 2;
    private long securityId2NoChunks = 1;
    private long lastMsgSeqNumProcessed = 1001;


    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 3, time = 20)
    public void test(){
        cycleHandler.getSnapshotSequence(securityId1);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 1);
        cycleHandler.getSnapshotSequence(securityId1);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId1, securityId1NoChunks, 2);
        cycleHandler.getSnapshotSequence(securityId2);
        cycleHandler.update(totNumReports, lastMsgSeqNumProcessed, securityId2, securityId2NoChunks, 1);
        if(cycleHandler.getSnapshotSequence(securityId1) == MBOSnapshotCycleHandler.SNAPSHOT_SEQUENCE_UNDEFINED){
            throw new RuntimeException();
        }
        cycleHandler.reset();
    }
}
