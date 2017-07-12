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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.channel.LowLevelMdpChannel;
import com.epam.cme.mdp3.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.channel.MdpFeedListener;
import com.epam.cme.mdp3.mktdata.enums.MDEntryType;
import com.epam.cme.mdp3.mktdata.enums.MDUpdateAction;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import org.openjdk.jmh.annotations.*;
import com.epam.cme.mdp3.test.ModelUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import static com.epam.cme.mdp3.MdConstants.*;


public class IncrementalRefreshPerfTest {
    private static MdpChannelBuilder mdpHandlerBuilder;
    private static final int TEST_CHANNEL_ID = 310;
    private static SbeDouble doubleVal = SbeDouble.instance();

    static {
        try {
            mdpHandlerBuilder = new MdpChannelBuilder(String.valueOf(TEST_CHANNEL_ID),
                    IncrementalRefreshPerfTest.class.getResource("/config.xml").toURI(),
                    IncrementalRefreshPerfTest.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new VoidChannelListener() {
                        @Override
                        public void onIncrementalMBORefresh(final String channelId, final short matchEventIndicator, final int securityId,
                                                            final String secDesc, final long msgSeqNum, final FieldSet orderIDEntry, final FieldSet mdEntry){
                            printMBOEntity(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, orderIDEntry, mdEntry, false);
                        }

                        @Override
                        public void onIncrementalMBPRefresh(final String channelId, final short matchEventIndicator, final int securityId,
                                                            final String secDesc, final long msgSeqNum, final FieldSet mdEntry){
                            printMBPEntity(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, mdEntry, false);
                        }

                        @Override
                        public int onSecurityDefinition(final String channelId, final MdpMessage secDefMessage) {
                            return MdEventFlags.MESSAGE;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void mboWithMBP(MBOIncrementalRefreshTestState testState) {
        testState.handleNextTestPacket();
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void MBPOnly(MBPOnlyIncrementalRefreshTestState testState) {
        testState.handleNextTestPacket();
    }


    private static void printMBOEntity(final String channelId, final short matchEventIndicator, final int securityId,
                                    final String secDesc, final long msgSeqNum, final FieldSet orderIDEntry, final FieldSet mdEntry, boolean print){
        double mdEntryPx;
        int mdUpdateAction;
        long orderId = orderIDEntry.getUInt64(37);
        long mdOrderPriority = orderIDEntry.getUInt64(37707);
        long mdDisplayQty = orderIDEntry.getInt32(37706);
        char mdEntryType;
        if(mdEntry == null) {//MBO only
            orderIDEntry.getDouble(270, doubleVal);
            mdEntryPx = doubleVal.asDouble();
            mdUpdateAction = orderIDEntry.getUInt8(279);
            mdEntryType = orderIDEntry.getChar(269);
            if(print) {
                System.out.printf("MBO only entry: ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s', mdUpdateAction - '%s', MatchEventIndicator: %s (byte representation: '%s')\n",
                        channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdEntryType, mdUpdateAction, matchEventIndicator, String.format("%08d", Integer.parseInt(Integer.toBinaryString(0xFFFF & matchEventIndicator))));
            }
        } else {
            mdEntry.getDouble(270, doubleVal);
            mdEntryPx = doubleVal.asDouble();
            mdUpdateAction = orderIDEntry.getUInt8(37708);
            mdEntryType = mdEntry.getChar(269);
            if(print) {
                System.out.printf("MBO with MBP entry: ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s', mdUpdateAction - '%s', MatchEventIndicator: %s (byte representation: '%s')\n",
                        channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdEntryType, mdUpdateAction, matchEventIndicator, String.format("%08d", Integer.parseInt(Integer.toBinaryString(0xFFFF & matchEventIndicator))));
            }
        }

    }

    private static void printMBPEntity(final String channelId, final short matchEventIndicator, final int securityId,
                                    final String secDesc, final long msgSeqNum, final FieldSet mdEntry, boolean print){
        final MDEntryType mdEntryType = MDEntryType.fromFIX(mdEntry.getChar(INCR_RFRSH_MD_ENTRY_TYPE));
        final MDUpdateAction mdUpdateAction = MDUpdateAction.fromFIX(mdEntry.getUInt8(INCR_RFRSH_MD_ACTION));
        final long rptSeqNum = mdEntry.getUInt32(RPT_SEQ_NUM);

        mdEntry.getDouble(INCR_RFRSH_MD_ENTRY_PX, doubleVal);
        int entrySize = mdEntry.getInt32(INCR_RFRSH_MD_ENTRY_SIZE);
        int orderNum = mdEntry.getInt32(INCR_RFRSH_MD_ORDER_NUM);
        final short level = mdEntry.getUInt8(INCR_RFRSH_MD_PRICE_LEVEL);

        if (print){
            System.out.printf("\tmsgSeqNum=%1$d; secId=%2$d; rptSeqNum=%3$d; mdEntryType=%4$s; mdAction=%5$s; level=%6$d; entrySize=%7$d; orderNum=%8$d; priceMantissa=%9$d%n",
                    msgSeqNum, securityId, rptSeqNum, mdEntryType, mdUpdateAction, level, entrySize, orderNum, doubleVal.getMantissa());
        }

    }

    @State(Scope.Benchmark)
    public static class MBOIncrementalRefreshTestState {
        public static final int SECURITY_ID = 998350;
        MdpChannel mdpChannel;
        MdpFeedListener mdpFeedListener;
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        final MdpFeedContext instrumentContext = new MdpFeedContext(Feed.A, FeedType.N);
        private final MdpFeedContext mboSnapshotContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        private int testPkctSeqNum = 0;
        final MdpPacket testPacket = MdpPacket.instance();
        final ByteBuffer testBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        byte[] incrementBytes;
        final int sequenceLength = 4;

        public void handleNextTestPacket() {
            handleNextTestPacket(incrementContext, incrementBytes, ++testPkctSeqNum);
        }

        private void handleNextTestPacket(MdpFeedContext context, byte[] bytes, int sequence) {
            testBuffer.clear();
            testBuffer.putInt(sequence);
            testBuffer.put(bytes);
            testBuffer.flip();
            testPacket.length(sequenceLength + bytes.length);
            mdpFeedListener.onPacket(context, testPacket);
        }

        void handleNextTestPacketWithSequenceReplace(MdpFeedContext context, ByteBuffer bb, int sequence) {
            byte[] bytes = new byte[bb.remaining() - sequenceLength];
            bb.position(sequenceLength);
            bb.get(bytes);
            handleNextTestPacket(context, bytes, sequence);
        }

        @Setup(Level.Trial)
        public void doSetup() throws Exception {
            testPacket.wrapFromBuffer(testBuffer);
            ByteBuffer incrementBB = getTestPacket();
            incrementBytes = new byte[incrementBB.remaining() - sequenceLength];
            incrementBB.position(sequenceLength);
            incrementBB.get(incrementBytes);
            LowLevelMdpChannel lowLevelMdpChannel = (LowLevelMdpChannel)mdpHandlerBuilder.build();
            mdpFeedListener = lowLevelMdpChannel.getFeedListener();
            mdpChannel = lowLevelMdpChannel;
            ByteBuffer snapshotMBOBBFirstCycle = ModelUtils.getMBOSnapshotTestMessage(1, SECURITY_ID, 0, 1, 1, 1);
            ByteBuffer snapshotMBPBBFirstCycle = ModelUtils.getMBPSnapshotTestMessage(1, SECURITY_ID, 0, 1);
            ByteBuffer snapshotMBOBBSecondCycle = ModelUtils.getMBOSnapshotTestMessage(1, SECURITY_ID, 0, 1, 1, 1);
            ByteBuffer snapshotMBPBBSecondCycle = ModelUtils.getMBPSnapshotTestMessage(1, SECURITY_ID, 0, 1);
            ByteBuffer instrumentBB = ModelUtils.getMDInstrumentDefinitionFuture27(1, SECURITY_ID);
            handleNextTestPacketWithSequenceReplace(instrumentContext, instrumentBB, 1);
            handleNextTestPacketWithSequenceReplace(mboSnapshotContext, snapshotMBOBBFirstCycle, 1);
            handleNextTestPacketWithSequenceReplace(mboSnapshotContext, snapshotMBPBBFirstCycle, 1);
            handleNextTestPacketWithSequenceReplace(mboSnapshotContext, snapshotMBOBBSecondCycle, 1);//next cycle
            handleNextTestPacketWithSequenceReplace(mboSnapshotContext, snapshotMBPBBSecondCycle, 1);//next cycle
            System.out.println("Initialized");
        }

        @TearDown(Level.Trial)
        public void doShutdown() throws Exception {
            testPacket.release();
            mdpChannel.close();
        }

        protected ByteBuffer getTestPacket(){
            return ModelUtils.getMBOWithMBPIncrementWith12TestEntries(0, SECURITY_ID);
        }
    }

    @State(Scope.Benchmark)
    public static class MBPOnlyIncrementalRefreshTestState extends MBOIncrementalRefreshTestState {
        private final MdpFeedContext mbpSnapshotContext = new MdpFeedContext(Feed.A, FeedType.S);

        protected ByteBuffer getTestPacket(){
            return ModelUtils.getMBPOnlyIncrementWith12TestEntries(0, SECURITY_ID);
        }

        @Setup(Level.Trial)
        public void doSetup() throws Exception {
            testPacket.wrapFromBuffer(testBuffer);
            ByteBuffer incrementBB = getTestPacket();
            incrementBytes = new byte[incrementBB.remaining() - sequenceLength];
            incrementBB.position(sequenceLength);
            incrementBB.get(incrementBytes);
            LowLevelMdpChannel lowLevelMdpChannel = (LowLevelMdpChannel)mdpHandlerBuilder.build();
            mdpFeedListener = lowLevelMdpChannel.getFeedListener();
            mdpChannel = lowLevelMdpChannel;
            ByteBuffer snapshotMBPBBFirstCycle = ModelUtils.getMBPSnapshotTestMessage(1, SECURITY_ID, 0, 1);
            ByteBuffer snapshotMBPBBSecondCycle = ModelUtils.getMBPSnapshotTestMessage(1, SECURITY_ID, 0, 1);
            ByteBuffer instrumentBB = ModelUtils.getMDInstrumentDefinitionFuture27(1, SECURITY_ID);
            handleNextTestPacketWithSequenceReplace(instrumentContext, instrumentBB, 1);
            handleNextTestPacketWithSequenceReplace(mbpSnapshotContext, snapshotMBPBBFirstCycle, 1);
            handleNextTestPacketWithSequenceReplace(mbpSnapshotContext, snapshotMBPBBSecondCycle, 1);//next cycle
            System.out.println("Initialized");
        }
    }

    public static void main(String[] args) throws Exception {
        MBOIncrementalRefreshTestState mboIncrementalRefreshTestState = new MBOIncrementalRefreshTestState();
        mboIncrementalRefreshTestState.doSetup();
        mboIncrementalRefreshTestState.handleNextTestPacket();
        mboIncrementalRefreshTestState.doShutdown();

        MBOIncrementalRefreshTestState mboWithMbpIncrementalRefreshTestState = new MBPOnlyIncrementalRefreshTestState();
        mboWithMbpIncrementalRefreshTestState.doSetup();
        mboWithMbpIncrementalRefreshTestState.handleNextTestPacket();
        mboWithMbpIncrementalRefreshTestState.doShutdown();
    }


}
