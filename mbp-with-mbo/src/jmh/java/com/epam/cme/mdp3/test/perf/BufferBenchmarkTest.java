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

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.control.MDPOffHeapBuffer;
import com.epam.cme.mdp3.test.ModelUtils;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class BufferBenchmarkTest {
    private MDPOffHeapBuffer buffer;
    private MdpPacket n1;

    @Setup
    public void init(){
        buffer = new MDPOffHeapBuffer(2);
        n1 = MdpPacket.allocate(); n1.wrapFromBuffer(ModelUtils.getMBOIncrementTestMessage(1));
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 3, time = 7)
    public void test(){
        buffer.add(n1);
        buffer.remove();
    }
}
