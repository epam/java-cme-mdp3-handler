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

package com.epam.cme.mdp3.core.channel.tcp;

import com.epam.cme.mdp3.MBOChannelListener;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class TCPMessageRequesterTest {
    private TCPMessageRequester messageRequester;
    private TestTCPPacketListener testTCPPacketListener = new TestTCPPacketListener();
    private TestTCPChannel tcpChannel;


    @Before
    public void init(){
        tcpChannel = new TestTCPChannel();
        String channelId1 = "1";
        List<MBOChannelListener> mboListeners = Collections.emptyList();

        messageRequester = new MdpTCPMessageRequester(channelId1, mboListeners, null, tcpChannel, MdpTCPMessageRequester.DEFAULT_USERNAME,
                MdpTCPMessageRequester.DEFAULT_PASSWORD);
    }

    @Test
    public void lostMessagesShouldBeReceivedFromListener() throws InterruptedException {
        long beginSeqNo = 1;
        long endSeqNo = 3;
        new Thread(() -> messageRequester.askForLostMessages(beginSeqNo, endSeqNo, testTCPPacketListener)).start();
        tcpChannel.setNextMessageForReceiving(ModelUtils.getLogin(1));
        assertNotNull(nextPacket());
        for(long i = beginSeqNo; i <= endSeqNo; i++) {
            tcpChannel.setNextMessageForReceiving(ModelUtils.getMBOIncrementTestMessage(i));
            MdpPacket mdpPacket = testTCPPacketListener.nextPacket();
            assertNotNull(mdpPacket);
            assertEquals(i, mdpPacket.getMsgSeqNum());
        }
    }

    private MdpPacket nextPacket(){
        ByteBuffer nextMessage = tcpChannel.nextMessage();
        MdpPacket mdpPacket = MdpPacket.instance();
        mdpPacket.wrapFromBuffer(nextMessage);
        return mdpPacket;
    }

    private class TestTCPPacketListener implements TCPPacketListener {
        public static final int WAITING_TIMEOUT_IN_SEC = 5;
        private final BlockingQueue<MdpPacket> mdpPacketQueue = new LinkedBlockingQueue<>();

        @Override
        public void onPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
            mdpPacketQueue.add(mdpPacket);
        }

        public MdpPacket nextPacket() throws InterruptedException {
            return mdpPacketQueue.poll(WAITING_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        }
    }
}