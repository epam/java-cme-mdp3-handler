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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.MESSAGE_SEQ_NUM_OFFSET;


public class MBOIncrementalRefreshPerfTest {
    private static MdpChannelBuilder mdpHandlerBuilder;
    private static final int TEST_CHANNEL_ID = 311;
    private static final int TEST_PACKET_ARRAY_SIZE =  700000;
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
                            printEntity(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, orderIDEntry, mdEntry, true);
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
    @Measurement(iterations = 3, time = 3)
    public void test(MBOIncrementalRefreshTestState testState) {
        testState.handleNextTestPacket();
    }


    private static void printEntity(final String channelId, final short matchEventIndicator, final int securityId,
                                    final String secDesc, final long msgSeqNum, final FieldSet orderIDEntry, final FieldSet mdEntry, boolean print){
        double mdEntryPx;
        int mdUpdateAction;
        long orderId = orderIDEntry.getUInt64(37);
        long mdOrderPriority = orderIDEntry.getUInt64(37707);
        long mdDisplayQty = orderIDEntry.getInt32(37706);
        char mdEntryType = orderIDEntry.getChar(269);
        if(mdEntry == null) {//MBO only
            orderIDEntry.getDouble(270, doubleVal);
            mdEntryPx = doubleVal.asDouble();
            mdUpdateAction = orderIDEntry.getUInt8(279);
        } else {
            mdEntry.getDouble(270, doubleVal);
            mdEntryPx = doubleVal.asDouble();
            mdUpdateAction = orderIDEntry.getUInt8(37708);
        }
        if(print) {
            System.out.printf("MBO only     onIncrementalMBORefresh : ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s', mdUpdateAction - '%s', MatchEventIndicator: %s (byte representation: '%s')",
                    channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdEntryType, mdUpdateAction, matchEventIndicator, String.format("%08d", Integer.parseInt(Integer.toBinaryString(0xFFFF & matchEventIndicator))));
        }
    }

    @State(Scope.Benchmark)
    public static class MBOIncrementalRefreshTestState {
        private MdpChannel mdpHandler;
        final MdpFeedContext incrementContext = new MdpFeedContext(Feed.A, FeedType.I);
        final MdpFeedContext mboSnapshotContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
        List<byte[]> testPackets;
        int testPkctSeqNum = 0;
        final MdpPacket testPacket = MdpPacket.instance();
        final ByteBuffer testBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);

        public void handleNextTestPacket() {
            final byte[] testPcktBytes = testPackets.get(testPkctSeqNum++);
            testBuffer.clear();
            testBuffer.put(testPcktBytes);
            testBuffer.flip();
            testPacket.length(testPcktBytes.length);
            mdpHandler.handlePacket(this.incrementContext, testPacket);
        }

        @Setup(Level.Trial)
        public void doSetup() throws Exception {
            testPacket.wrapFromBuffer(testBuffer);
            testPackets = new ArrayList<>(TEST_PACKET_ARRAY_SIZE);
            ByteBuffer incrementBB = ModelUtils.getMBOOnlyIncrementWith12TestMessages(0);
            byte[] incrementBytes = new byte[incrementBB.remaining()];
            incrementBB.get(incrementBytes);
            for(int i = 0; i < TEST_PACKET_ARRAY_SIZE; i++){
                int nextSequence = i + 1;
                incrementBytes[MESSAGE_SEQ_NUM_OFFSET + 0] = (byte) ((nextSequence & 0xFF000000) >> 24);
                incrementBytes[MESSAGE_SEQ_NUM_OFFSET + 1] = (byte) ((nextSequence & 0x00FF0000) >> 16);
                incrementBytes[MESSAGE_SEQ_NUM_OFFSET + 2] = (byte) ((nextSequence & 0x0000FF00) >> 8);
                incrementBytes[MESSAGE_SEQ_NUM_OFFSET + 3] = (byte) ((nextSequence & 0x000000FF) >> 0);
                testPackets.add(incrementBytes.clone());
            }
            mdpHandler = mdpHandlerBuilder.build();
            ByteBuffer snapshotBB = ModelUtils.getMBOSnapshotTestMessage(1, 998350, 0, 1, 1, 1);
            byte[] snapshotBytes = new byte[snapshotBB.remaining()];
            snapshotBB.get(snapshotBytes);
            testBuffer.clear();
            testBuffer.put(snapshotBytes);
            testBuffer.flip();
            testPacket.length(snapshotBytes.length);
            mdpHandler.handlePacket(mboSnapshotContext, testPacket);
            System.out.println("Initialized");
        }

        @TearDown(Level.Trial)
        public void doShutdown() throws Exception {
            testPacket.release();
            mdpHandler.close();
        }
    }

    public static void main(String[] args) throws Exception {
        MBOIncrementalRefreshTestState mboIncrementalRefreshTestState = new MBOIncrementalRefreshTestState();
        mboIncrementalRefreshTestState.doSetup();
        mboIncrementalRefreshTestState.handleNextTestPacket();
    }


}
