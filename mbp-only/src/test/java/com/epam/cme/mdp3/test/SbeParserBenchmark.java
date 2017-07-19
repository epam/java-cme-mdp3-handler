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

package com.epam.cme.mdp3.test;

import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

public class SbeParserBenchmark {
    private static final Logger logger = LoggerFactory.getLogger(SbeParserBenchmark.class);
    private static final byte INIT_OFFSET = 10;
    private static final byte HEADER_OFFSET = 10;

    private static MdpMessageTypes mdpMessageTypes;

    static {
        try {
            mdpMessageTypes = new MdpMessageTypes(SbeParserBenchmark.class.getResource("/templates_FixBinary.xml").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
    private static final MdpPacket mdpPacket = MdpPacket.instance();
    private final static MdpGroup incrGroup = SbeGroup.instance();
    private final static SbeDouble doubleVal = SbeDouble.instance();

    private static void handleMdIncrementalRefreshBook(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getDouble(270, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            if (!incrGroup.isNull(271)) {
                incrGroup.getInt32(271);
            }
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            if (!incrGroup.isNull(346)) {
                incrGroup.getInt32(346);
            }
            incrGroup.getUInt8(1023);
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handleMdIncrementalRefreshDailyStatistics(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getDouble(270, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            if (!incrGroup.isNull(271)) {
                incrGroup.getInt32(271);
            }
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            if (!incrGroup.isNull(5796)) {
                incrGroup.getUInt16(5796);
            }
            boolean a = incrGroup.getUInt8(731) == 7;
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handleMdIncrementalRefreshLimitsBanding(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getDouble(1149, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            incrGroup.getDouble(1148, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            incrGroup.getDouble(1143, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handleMdIncrementalRefreshSessionStatistics(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getDouble(270, doubleVal);
            doubleVal.getExponent();
            doubleVal.getMantissa();
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            if (!incrGroup.isNull(286)) {
                incrGroup.getUInt8(286);
            }
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handleMdIncrementalRefreshTrade(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getDouble(270, doubleVal);
            if (!doubleVal.isNull()) {
                doubleVal.getExponent();
                doubleVal.getMantissa();
            }
            incrGroup.getInt32(271);
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            if (!incrGroup.isNull(346)) {
                incrGroup.getInt32(346);
            }
            incrGroup.getInt32(1003);
            if (!incrGroup.isNull(5797)) {
                incrGroup.getUInt8(5797);
            }
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handleMdIncrementalRefreshVolume(final MdpMessage mdpMessage) {
        mdpMessage.getUInt64(60);
        mdpMessage.getUInt8(5799);
        mdpMessage.getGroup(268, incrGroup);
        while (incrGroup.hashNext()) {
            incrGroup.next();
            incrGroup.getInt32(271);
            incrGroup.getInt32(48);
            incrGroup.getUInt32(83);
            incrGroup.getInt8(279);
            incrGroup.getChar(269);
        }
    }

    private static void handlePacketByAntenna(final MdpMessageTypes mdpMessageTypes, final MdpPacket mdpPacket) throws Exception {
        final Iterator<MdpMessage> mdpMessageIterator = mdpPacket.iterator();
        while (mdpMessageIterator.hasNext()) {
            final MdpMessage sbeMessage = mdpMessageIterator.next();
            final MdpMessageType messageType = mdpMessageTypes.getMessageType(sbeMessage.getSchemaId());
            sbeMessage.setMessageType(messageType);
            switch (sbeMessage.getSchemaId()) {
                case 32:
                    handleMdIncrementalRefreshBook(sbeMessage);
                    break;
                case 33:
                    handleMdIncrementalRefreshDailyStatistics(sbeMessage);
                    break;
                case 34:
                    handleMdIncrementalRefreshLimitsBanding(sbeMessage);
                    break;
                case 35:
                    handleMdIncrementalRefreshSessionStatistics(sbeMessage);
                    break;
                case 36:
                    handleMdIncrementalRefreshTrade(sbeMessage);
                    break;
                case 37:
                    handleMdIncrementalRefreshVolume(sbeMessage);
                    break;
            }
        }
    }

    public static void main(String args[]) {
        try {
            final byte[] testData = loadTestPackets("dist/pcap/311_AX.bin");
            testSbeParser(testData);
        } catch (Exception e) {
            logger.error("Exception in MDP3.0/SBE Parser Benchmark", e);
        }
    }

    private static int testSbePackets(final byte[] testData, boolean isWarmUp) throws Exception {
        return testSbePackets(testData, isWarmUp, null);
    }

    private static int getUInt16BigEndian(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xff) << 8) + (buffer[offset + 1] & 0xff);
    }

    private static int testSbePackets(final byte[] testData, boolean isWarmUp, final long[] delays) throws Exception {
        int offset = INIT_OFFSET;
        int testPacketNum = 0;
        long startTime = 0;
        while (offset + HEADER_OFFSET <= testData.length) {
            offset += SbePrimitiveType.UInt64.getSize();
            final int blockSize = getUInt16BigEndian(testData, offset);
            offset += SbePrimitiveType.UInt16.getSize();

            if (blockSize == 0) {
                continue;
            }
            byteBuffer.clear();
            byteBuffer.put(testData, offset, blockSize);
            if (!isWarmUp) startTime = System.nanoTime();
            mdpPacket.wrapFromBuffer(byteBuffer);
            handlePacketByAntenna(mdpMessageTypes, mdpPacket);
            if (!isWarmUp) delays[testPacketNum] = System.nanoTime() - startTime;
            testPacketNum++;
            offset += blockSize;
        }
        return testPacketNum;
    }

    private static void testSbeParser(final byte[] testData) throws Exception {
        logger.info("SBE parser benchmark: warm up ...");
        int testPacketNum = testSbePackets(testData, true);
        logger.info("SBE parser benchmark: warm up ... Done. Pcap file has {} packets", testPacketNum);
        final long[] delays = new long[testPacketNum];
        testSbePackets(testData, false, delays);
        logger.info("SBE parser benchmark results:");
        printBenchmarks(delays);
    }

    private static byte[] loadTestPackets(final String path) throws Exception {
        final File file = new File(path);
        final int size = (int) file.length();
        final byte[] data = new byte[size];
        final FileInputStream in = new FileInputStream(file);
        in.read(data);
        in.close();
        return data;
    }

    private static void printBenchmarks(final long[] delays) {
        java.util.Arrays.sort(delays);
        System.out.printf("\nMIN = %.3f\n25%% = %.3f\n50%% = %.3f\n70%% = %.3f\n80%% = %.3f\n90%% = %.3f\n99%% = %.3f\n99.9%% = %.3f\n99.99%% = %.3f\nMAX = %.3f uSec\n\n",
                delays[0] / 1e3,
                delays[delays.length / 4] / 1e3,
                delays[delays.length / 2] / 1e3,
                delays[delays.length * 7 / 10] / 1e3,
                delays[delays.length * 8 / 10] / 1e3,
                delays[delays.length * 9 / 10] / 1e3,
                delays[delays.length - delays.length / 100 - 1] / 1e3,
                delays[delays.length - delays.length / 1000 - 1] / 1e3,
                delays[delays.length - delays.length / 10000 - 1] / 1e3,
                delays[delays.length - 1] / 1e3
        );
    }
}
