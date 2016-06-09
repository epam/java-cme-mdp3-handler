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

package com.epam.cme.mdp3.test.def;

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;
import com.epam.cme.mdp3.test.SbeDataDumpHelper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

class MdpFeedSimHelper {
    private final List<FeedSimulator> openFeedSims = new ArrayList<>();

    public MdpFeedSimHelper() throws IOException {

    }

    private class FeedSimulator implements Runnable {
        private final Path dataFilePath;
        private volatile boolean stop = false;
        private Lock lock = new ReentrantLock();
        private Condition startedCond = lock.newCondition();
        private Condition stoppedCond = lock.newCondition();
        private String channelId;
        private String feed;
        private String ip;
        private int port;

        FeedSimulator(final Path dataFilePath) {
            this.dataFilePath = dataFilePath;
            extractFeedPropsFromFilename();
        }

        private void extractFeedPropsFromFilename() {
            final String[] dataFileNameComp = this.dataFilePath.getName(dataFilePath.getNameCount() - 1).toString().split("_");
            this.channelId = dataFileNameComp[0];
            this.feed = dataFileNameComp[1];
            this.ip = dataFileNameComp[2];
            this.port = Integer.parseInt(dataFileNameComp[3].replace(".bin", ""));
        }

        public void run() {
            System.out.println(String.format("Channel %1$s Feed %2$s simulator has been started (data from %3$s)\n", channelId, feed, dataFilePath));
            try {
                final byte[] testData = SbeDataDumpHelper.loadTestPackets(dataFilePath);
                lock.lock();
                try {
                    startedCond.signal();
                } finally {
                    lock.unlock();
                }
                Thread.currentThread().sleep(1000);
                playSbePackets(testData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("Channel %1$s Feed %2$s simulator has been stopped (data from %3$s)\n", channelId, feed, dataFilePath));
            lock.lock();
            try {
                stoppedCond.signal();
            } finally {
                lock.unlock();
            }
        }

        private void playSbePackets(final byte[] testData) throws Exception {
            final DatagramSocket datagramSocket = new DatagramSocket();
            final InetAddress addr = InetAddress.getByName(this.ip);

            int offset = SbeDataDumpHelper.INIT_OFFSET;
            while (!stop && offset + SbeDataDumpHelper.HEADER_OFFSET <= testData.length) {
                offset += SbePrimitiveType.UInt64.getSize();
                final int blockSize = SbeDataDumpHelper.getUInt16BigEndian(testData, offset);
                offset += SbePrimitiveType.UInt16.getSize();

                if (blockSize == 0) {
                    continue;
                }
                DatagramPacket msgPacket = new DatagramPacket(testData, offset, blockSize, addr, port);
                datagramSocket.send(msgPacket);

                //System.out.println(String.format("%1$d, %2$d", offset, blockSize));
                offset += blockSize;
                LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(500));
            }
            datagramSocket.close();
        }

        void stop() {
            stop = true;
            lock.lock();
            try {
                stoppedCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    void release() {
        openFeedSims.forEach(FeedSimulator::stop);
    }

    private void registerOpenFeedSim(final FeedSimulator feedSimulator) {
        openFeedSims.add(feedSimulator);
    }

    private void runFeed(final int channelId, final String feed) throws IOException {
        final Path mdpFeedData = SbeDataDumpHelper.lookupDataFile(channelId, feed);
        final FeedSimulator feedSimulator = new FeedSimulator(mdpFeedData);
        registerOpenFeedSim(feedSimulator);
        new Thread(feedSimulator).start();
        feedSimulator.lock.lock();
        try {
            feedSimulator.startedCond.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            feedSimulator.lock.unlock();
        }

    }

    void runInstrumentFeedA(final int channelId) throws IOException {
        runFeed(channelId, "AD");
    }

    void runSnapshotFeedA(final int channelId) throws IOException {
        runFeed(channelId, "AW");
    }

    void runIncrementRefreshFeedA(final int channelId) throws IOException {
        runFeed(channelId, "AX");
    }
}
