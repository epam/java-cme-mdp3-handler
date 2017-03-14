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
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.mktdata.MdConstants;
import com.epam.cme.mdp3.mktdata.enums.MDEntryType;
import com.epam.cme.mdp3.mktdata.enums.MDUpdateAction;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.message.meta.SbeFieldType;
import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypeBuildException;
import com.epam.cme.mdp3.test.SbeDataDumpHelper;
import net.openhft.chronicle.bytes.NativeBytesStore;
import org.openjdk.jmh.annotations.*;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;
import static com.epam.cme.mdp3.sbe.message.SbeConstants.MESSAGE_SEQ_NUM_OFFSET;

public class IncrementalRefreshPerfTest {
    private static final int TEST_PACKET_ARRAY_SIZE =  7000000;
    private static final int TEST_CHANNEL_ID = 311;
    private static SbeDouble doubleVal = SbeDouble.instance();
    private static MdpChannelBuilder mdpHandlerBuilder;
    private final static MdpGroup incrGroup = SbeGroup.instance();

    static {
        try {
            mdpHandlerBuilder = new MdpChannelBuilder(String.valueOf(TEST_CHANNEL_ID),
                    IncrementalRefreshPerfTest.class.getResource("/config.xml").toURI(),
                    IncrementalRefreshPerfTest.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new ChannelListenerImpl());
        } catch (MdpMessageTypeBuildException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void doSomething(long msgSeqNum, int securityId, FieldSet incrRefreshEntry) {
        printEntry(msgSeqNum, securityId, incrRefreshEntry, false);
    }


    private static void printEntry(long msgSeqNum, int securityId, final FieldSet incrGroup) {
        printEntry(msgSeqNum, securityId, incrGroup, true);
    }

    private static void printEntry(long msgSeqNum, int securityId, final FieldSet incrFieldSet, final boolean console) {
        final MDEntryType mdEntryType = MDEntryType.fromFIX(incrFieldSet.getChar(INCR_RFRSH_MD_ENTRY_TYPE));
        final MDUpdateAction mdUpdateAction = MDUpdateAction.fromFIX(incrFieldSet.getUInt8(INCR_RFRSH_MD_ACTION));
        final long rptSeqNum = incrFieldSet.getUInt32(RPT_SEQ_NUM);

        incrFieldSet.getDouble(INCR_RFRSH_MD_ENTRY_PX, doubleVal);
        int entrySize = incrFieldSet.getInt32(INCR_RFRSH_MD_ENTRY_SIZE);
        int orderNum = incrFieldSet.getInt32(INCR_RFRSH_MD_ORDER_NUM);
        final short level = incrFieldSet.getUInt8(INCR_RFRSH_MD_PRICE_LEVEL);

        if (console) System.out.printf("\tmsgSeqNum=%1$d; secId=%2$d; rptSeqNum=%3$d; mdEntryType=%4$s; mdAction=%5$s; level=%6$d; entrySize=%7$d; orderNum=%8$d; priceMantissa=%9$d%n",
                msgSeqNum, securityId, rptSeqNum, mdEntryType, mdUpdateAction, level, entrySize, orderNum, doubleVal.getMantissa());
    }

    private static void printPacket(final MdpPacket mdpPacket) {
        final Iterator<MdpMessage> mdpMessageIterator = mdpPacket.iterator();
        while (mdpMessageIterator.hasNext()) {
            final MdpMessage mdpMessage = mdpMessageIterator.next();
            final MdpMessageType messageType = mdpHandlerBuilder.getMdpMessageTypes().getMessageType(mdpMessage.getSchemaId());
            mdpMessage.setMessageType(messageType);

            if (mdpMessage.getGroup(MdConstants.INCR_RFRSH_GRP_TAG, incrGroup)) {
                while (incrGroup.hashNext()) {
                    incrGroup.next();
                    final int secId = incrGroup.getInt32(MdConstants.SECURITY_ID);
                    printEntry(mdpPacket.getMsgSeqNum(), secId, incrGroup);

                }
            }
        }
    }

    private static void printTestPacket(final List<byte[]> testPackets) {
        final MdpPacket mdpPacket = MdpPacket.instance();
        try {
            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
            mdpPacket.wrapFromBuffer(byteBuffer);

            for (final byte[] testPacketBytes : testPackets) {
                byteBuffer.clear();
                byteBuffer.put(testPacketBytes);
                byteBuffer.flip();
                mdpPacket.length(testPacketBytes.length);

                System.out.println("Test MDP Packet #" + mdpPacket.getMsgSeqNum() + ". Size: " + mdpPacket.getPacketSize());
                printPacket(mdpPacket);
            }
        } finally {
            mdpPacket.release();
        }
    }


    private static class ChannelListenerImpl implements VoidChannelListener {
        /*@Override
        public void onChannelStateChanged(String channelId, ChannelState prevState, ChannelState newState) {
            System.out.printf("%nChannel %1$s's state has been changed from %2$s to %3$s%n", channelId, prevState, newState);
        }*/

        /*@Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
            System.out.printf("pcktSeqNum=%1$d%n", mdpPacket.getMsgSeqNum());
        }*/

        @Override
        public void onIncrementalRefresh(String channelId, short matchEventIndicator, int securityId, String secDesc, long msgSeqNum, FieldSet incrRefreshEntry) {
            doSomething(msgSeqNum, securityId, incrRefreshEntry);
        }
    }

    @State(Scope.Benchmark)
    public static class IncrementalRefreshTestState {
        private MdpChannel mdpHandler;
        final MdpFeedContext feedContext = new MdpFeedContext(Feed.A, FeedType.I);
        List<byte[]> testPackets;
        int testPkctSeqNum = 0;
        final MdpPacket testPacket = MdpPacket.instance();
        final ByteBuffer testBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);

        private MdpPacket findSample(final byte[] testData, final int incrGroupSizeMin, final int incrGroupSizeMax, final int secNumMin, final int secNumMax) {
            final MdpPacket mdpPacket = MdpPacket.instance();
            try {
                final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
                mdpPacket.wrapFromBuffer(byteBuffer);
                final HashSet<Integer> secIds = new HashSet<>();

                int offset = SbeDataDumpHelper.INIT_OFFSET;
                while (offset + SbeDataDumpHelper.HEADER_OFFSET <= testData.length) {
                    offset += SbePrimitiveType.UInt64.getSize();
                    final int blockSize = SbeDataDumpHelper.getUInt16BigEndian(testData, offset);
                    offset += SbePrimitiveType.UInt16.getSize();

                    if (blockSize == 0) {
                        continue;
                    }
                    secIds.clear();
                    byteBuffer.clear();
                    byteBuffer.put(testData, offset, blockSize);
                    byteBuffer.flip();
                    mdpPacket.length(blockSize);

                    int incrGroupSizeInPckt = 0;
                    boolean onlyBookEntries = true;
                    final Iterator<MdpMessage> mdpMessageIterator = mdpPacket.iterator();
                    while (mdpMessageIterator.hasNext()) {
                        final MdpMessage mdpMessage = mdpMessageIterator.next();
                        final MdpMessageType messageType = mdpHandlerBuilder.getMdpMessageTypes().getMessageType(mdpMessage.getSchemaId());
                        mdpMessage.setMessageType(messageType);

                        if (mdpMessage.getGroup(MdConstants.INCR_RFRSH_GRP_TAG, incrGroup)) {
                            while (incrGroup.hashNext()) {
                                incrGroup.next();
                                final MDEntryType mdEntryType = MDEntryType.fromFIX(incrGroup.getChar(INCR_RFRSH_MD_ENTRY_TYPE));

                                if (mdEntryType == MDEntryType.Offer || mdEntryType == MDEntryType.Bid) {
                                    incrGroupSizeInPckt++;
                                    final int secId = incrGroup.getInt32(MdConstants.SECURITY_ID);
                                    secIds.add(secId);
                                } else {
                                    onlyBookEntries = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (onlyBookEntries && incrGroupSizeInPckt >= incrGroupSizeMin && incrGroupSizeInPckt <= incrGroupSizeMax
                            && secIds.size() >= secNumMin && secIds.size() <= secNumMax)  {
                        return mdpPacket;
                    }
                    offset += blockSize;
                }
                return null;
            } finally {

            }
        }

        private Set<Integer> generateTestPackets(final MdpPacket samplePckt, final int testPacketsNum) {
            this.testPackets = new ArrayList<>(testPacketsNum);
            final Map<Integer, Integer> rptSeqNums = new HashMap<>();

            final NativeBytesStore<Void> store = NativeBytesStore.nativeStoreWithFixedCapacity(samplePckt.getPacketSize());
            samplePckt.buffer().copyTo(store);

            for (int i = 1; i <= testPacketsNum; i++) {
                store.writeUnsignedInt(MESSAGE_SEQ_NUM_OFFSET, i);

                final Iterator<MdpMessage> mdpMessageIterator = samplePckt.iterator();
                while (mdpMessageIterator.hasNext()) {
                    final MdpMessage mdpMessage = mdpMessageIterator.next();
                    final MdpMessageType messageType = mdpHandlerBuilder.getMdpMessageTypes().getMessageType(mdpMessage.getSchemaId());
                    mdpMessage.setMessageType(messageType);

                    if (mdpMessage.getGroup(MdConstants.INCR_RFRSH_GRP_TAG, incrGroup)) {
                        while (incrGroup.hashNext()) {
                            incrGroup.next();
                            final int secId = incrGroup.getInt32(MdConstants.SECURITY_ID);
                            if (!rptSeqNums.containsKey(secId)) {
                                rptSeqNums.put(secId, 0);
                            }
                            final int incrRptSeqNum = rptSeqNums.get(secId) + 1;
                            final SbeGroup sbeGroup = (SbeGroup) incrGroup;
                            final SbeFieldType fieldType = sbeGroup.getSbeGroupType().getMetadataContainer().findField(RPT_SEQ_NUM);
                            final int offset = ((SbeGroup)incrGroup).getAbsoluteEntryOffset() + fieldType.getFieldType().getOffset().intValue();
                            store.writeUnsignedInt(offset, incrRptSeqNum);
                            rptSeqNums.put(secId, incrRptSeqNum);
                        }
                    }
                }
                final byte[] bytes = new byte[samplePckt.getPacketSize()];
                store.copyTo(bytes);
                testPackets.add(bytes);
            }
            samplePckt.release();
            return rptSeqNums.keySet();
        }

        @Setup(Level.Trial)
        public void doSetup() throws Exception {
            testPacket.wrapFromBuffer(testBuffer);

            System.out.println("\nLoading test data dump...");
            final Path mdpFeedData = SbeDataDumpHelper.lookupDataFile(TEST_CHANNEL_ID, "AX");
            if (mdpFeedData == null) {
                System.err.println("Failed to load test data\n");
                System.exit(0);
            }
            final byte[] testData = SbeDataDumpHelper.loadTestPackets(mdpFeedData);
            System.out.println("Loading test data dump...Done");

            System.out.println("Generating test MDP packets...");
            final MdpPacket samplePckt = findSample(testData, 12, 14, 5, 6);
            System.out.println("The found MDP Packet sample:");
            printPacket(samplePckt);
            final Set<Integer> secIds = generateTestPackets(samplePckt, TEST_PACKET_ARRAY_SIZE);
            System.out.println("Generating test MDP packets...Done");

            System.out.println("Creating Data Handler instance...");
            mdpHandler = mdpHandlerBuilder.build();
            for (Integer secId : secIds) {
                mdpHandler.subscribe(secId, null);
            }
            mdpHandler.setStateForcibly(ChannelState.SYNC);
            System.out.println("Creating Data Handler instance...Done");
            //printTestPacket(this.testPackets);
        }

        void handleTestPacket(final int testPkctSeqIdx) {
            final byte[] testPcktBytes = testPackets.get(testPkctSeqNum);
            testBuffer.clear();
            testBuffer.put(testPcktBytes);
            testBuffer.flip();
            testPacket.length(testPcktBytes.length);

            this.mdpHandler.handlePacket(this.feedContext, testPacket);
        }

        int increaseTestSeqNum() {
            this.testPkctSeqNum++;
            return this.testPkctSeqNum;
        }

        @TearDown(Level.Trial)
        public void doShutdown() throws Exception {
            if (testPacket != null) testPacket.release();
            if (mdpHandler != null) mdpHandler.close();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
    public int testJitter(IncrementalRefreshTestState testState) {
        testState.handleTestPacket(testState.testPkctSeqNum);
        return testState.increaseTestSeqNum();
    }
}
