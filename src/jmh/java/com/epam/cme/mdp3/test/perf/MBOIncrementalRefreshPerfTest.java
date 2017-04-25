package com.epam.cme.mdp3.test.perf;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import org.openjdk.jmh.annotations.*;
import com.epam.cme.mdp3.test.ModelUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;



public class MBOIncrementalRefreshPerfTest {
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
                            printEntity(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, orderIDEntry, mdEntry, false);
                        }

                        @Override
                        public int onSecurityDefinition(final String channelId, final MdpMessage secDefMessage) {
                            return MdEventFlags.BOOK;
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
    public void onlyMBO(MBOIncrementalRefreshTestState testState) {
        testState.handleNextTestPacket();
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void mboWithMBP(MBOWithMBPIncrementalRefreshTestState testState) {
        testState.handleNextTestPacket();
    }


    private static void printEntity(final String channelId, final short matchEventIndicator, final int securityId,
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

    @State(Scope.Benchmark)
    public static class MBOIncrementalRefreshTestState {
        public static final int SECURITY_ID = 998350;
        private MdpChannel mdpHandler;
        private final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        private final MdpFeedContext instrumentContext = new MdpFeedContext(Feed.A, FeedType.N);
        private final MdpFeedContext mboSnapshotContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        private int testPkctSeqNum = 0;
        private final MdpPacket testPacket = MdpPacket.instance();
        private final ByteBuffer testBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        private byte[] incrementBytes;
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
            mdpHandler.handlePacket(context, testPacket);
        }

        private void handleNextTestPacketWithSequenceReplace(MdpFeedContext context, ByteBuffer bb, int sequence) {
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
            mdpHandler = mdpHandlerBuilder.build();
            ByteBuffer snapshotBB = ModelUtils.getMBOSnapshotTestMessage(1, SECURITY_ID, 0, 1, 1, 1);
            ByteBuffer instrumentBB = ModelUtils.getMDInstrumentDefinitionFuture27(1, SECURITY_ID);
            handleNextTestPacketWithSequenceReplace(instrumentContext, instrumentBB, 1);
            handleNextTestPacketWithSequenceReplace(mboSnapshotContext, snapshotBB, 1);
            System.out.println("Initialized");
        }

        @TearDown(Level.Trial)
        public void doShutdown() throws Exception {
            testPacket.release();
            mdpHandler.close();
        }

        protected ByteBuffer getTestPacket(){
            return ModelUtils.getMBOOnlyIncrementWith12TestEntries(0, SECURITY_ID);
        }
    }

    @State(Scope.Benchmark)
    public static class MBOWithMBPIncrementalRefreshTestState extends MBOIncrementalRefreshTestState {
        protected ByteBuffer getTestPacket(){
            return ModelUtils.getMBOWithMBPIncrementWith12TestEntries(0, SECURITY_ID);
        }
    }

    public static void main(String[] args) throws Exception {
        MBOIncrementalRefreshTestState mboIncrementalRefreshTestState = new MBOIncrementalRefreshTestState();
        mboIncrementalRefreshTestState.doSetup();
        mboIncrementalRefreshTestState.handleNextTestPacket();
        mboIncrementalRefreshTestState.doShutdown();

        MBOIncrementalRefreshTestState mboWithMbpIncrementalRefreshTestState = new MBOWithMBPIncrementalRefreshTestState();
        mboWithMbpIncrementalRefreshTestState.doSetup();
        mboWithMbpIncrementalRefreshTestState.handleNextTestPacket();
        mboWithMbpIncrementalRefreshTestState.doShutdown();
    }


}
