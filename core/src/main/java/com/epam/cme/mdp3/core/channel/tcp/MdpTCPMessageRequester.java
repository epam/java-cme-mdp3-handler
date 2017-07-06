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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.CoreChannelListener;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeString;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class MdpTCPMessageRequester<T extends CoreChannelListener> implements TCPMessageRequester {
    private static final Logger logger = LoggerFactory.getLogger(MdpTCPChannel.class);
    public static final String DEFAULT_USERNAME = "CME";
    public static final String DEFAULT_PASSWORD = "CME";
    public final static int NUMBER_OF_SIZE_BYTES = 2;
    private List<T> coreChannelListeners;
    private final MdpMessageTypes mdpMessageTypes;
    private final TCPChannel tcpChannel;
    private final static int REQUEST_TIMEOUT_IN_MSEC = 4000;
    private final static int LOGON_TEMPLATE_ID = 15;
    private final static int LOGOUT_TEMPLATE_ID = 16;
    private final static int LOGOUT_TEXT_FIELD_ID = 58;
    private final ByteBuffer logon;
    private final ByteBuffer logout;
    private final MdpPacket mdpPacket = MdpPacket.instance();
    private final ByteBuffer workBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
    private final SbeString sbeString = SbeString.allocate(500);
    private volatile int requestCounter;
    private final String channelId;

    public MdpTCPMessageRequester(String channelId, List<T> coreChannelListeners, MdpMessageTypes mdpMessageTypes, TCPChannel tcpChannel, String username, String password) {
        this.channelId = channelId;
        this.coreChannelListeners = coreChannelListeners;
        this.mdpMessageTypes = mdpMessageTypes;
        this.tcpChannel = tcpChannel;
        logon = prepareLogonMessages(username, password);
        logout = prepareLogooutMessages();
        mdpPacket.wrapFromBuffer(workBuffer);
    }

    @Override
    public synchronized boolean askForLostMessages(long beginSeqNo, long endSeqNo, TCPPacketListener tcpPacketListener) {
        ByteBuffer request = prepareRequestMessages(channelId, beginSeqNo, endSeqNo, ++requestCounter);
        boolean connected = tcpChannel.connect();
        if(connected) {
            try {
                onFeedStarted();
                sendPacket(logon);
                MdpPacket receivedPacket = receivePacket();
                if (isPacketWithLogon(receivedPacket)) {
                    tcpChannel.send(request);
                    long startRequestTime = System.currentTimeMillis();
                    long packetSeqNum;
                    do {
                        if((System.currentTimeMillis() - startRequestTime) > REQUEST_TIMEOUT_IN_MSEC){
                            logger.warn("Request has not been processed for {} msec", REQUEST_TIMEOUT_IN_MSEC);
                            return false;
                        }
                        receivedPacket = receivePacket();
                        packetSeqNum = receivedPacket.getMsgSeqNum();
                        if (packetSeqNum >= beginSeqNo && packetSeqNum <= endSeqNo) {
                            tcpPacketListener.onPacket(tcpChannel.getFeedContext(), receivedPacket);
                        }
                    } while (packetSeqNum < endSeqNo);
                    receivedPacket = receivePacket();
                    return isPacketWithLogout(receivedPacket);
                } else {
                    MdpMessage firstMessage = receivedPacket.iterator().next();
                    int schemaId = firstMessage.getSchemaId();
                    firstMessage.setMessageType(mdpMessageTypes.getMessageType(schemaId));
                    logger.warn("The message {} has been received instead of logon", firstMessage);
                    if(schemaId == LOGOUT_TEMPLATE_ID){
                        firstMessage.getString(LOGOUT_TEXT_FIELD_ID, sbeString);
                        logger.warn("Logout has been received with reason '{}'", sbeString.getString());
                    }
                }
            } catch (Exception e) {
                logger.error("Last packet [{}] - {}", mdpPacket, e.getMessage(), e);
            } finally {
                try {
                    sendPacket(logout);
                } catch (IOException e) {
                    logger.error("Error has occurred during logout", e);
                }
                tcpChannel.disconnect();
                onFeedStopped();
            }
        }
        return false;
    }

    private void onFeedStopped() {
        FeedType feedType = tcpChannel.getFeedContext().getFeedType();
        Feed feed = tcpChannel.getFeedContext().getFeed();
        coreChannelListeners.forEach(coreChannelListener -> coreChannelListener.onFeedStopped(channelId, feedType, feed));
    }

    private void onFeedStarted() {
        FeedType feedType = tcpChannel.getFeedContext().getFeedType();
        Feed feed = tcpChannel.getFeedContext().getFeed();
        coreChannelListeners.forEach(coreChannelListener -> coreChannelListener.onFeedStarted(channelId, feedType, feed));
    }

    private void sendPacket(ByteBuffer bb) throws IOException {
        tcpChannel.send(bb);
        bb.flip();
    }

    private MdpPacket receivePacket() throws IOException {
        read(NUMBER_OF_SIZE_BYTES);
        int nextPacketSize = workBuffer.getShort() & 0xFFFF;
        read(nextPacketSize);
        mdpPacket.length(nextPacketSize);
        logger.trace("Packet [{}] has been read ", mdpPacket);
        return mdpPacket;
    }

    private void read(int size) throws IOException {
        workBuffer.clear();
        workBuffer.limit(size);
        tcpChannel.receive(workBuffer);
        workBuffer.flip();
    }

    private boolean isPacketWithLogon(MdpPacket mdpPacket) {
        if(mdpPacket.getMsgSeqNum() == 1) {
            MdpMessage firstMessage = mdpPacket.iterator().next();
            return firstMessage.getSchemaId() == LOGON_TEMPLATE_ID;
        }
        return false;
    }

    private boolean isPacketWithLogout(MdpPacket mdpPacket){
        MdpMessage firstMessage = mdpPacket.iterator().next();
        return firstMessage.getSchemaId() == LOGOUT_TEMPLATE_ID;
    }

    private ByteBuffer prepareLogonMessages(String username, String password) {
        String logonMessage = "35=A\u0001553=" + username + "\u0001554=" + password + "\u0001";
        logonMessage = "9=" + logonMessage.length() + "\u0001" + logonMessage;
        String logonChecksum = calculateChecksum(logonMessage);
        return ByteBuffer.wrap((logonMessage + "10=" + logonChecksum + "\u0001").getBytes());
    }

    private ByteBuffer prepareLogooutMessages() {
        String logoutMessage = "35=5\u0001";
        logoutMessage = "9=" + logoutMessage.length() + "\u0001" + logoutMessage;
        String logonChecksum = calculateChecksum(logoutMessage);
        return ByteBuffer.wrap((logoutMessage + "10=" + logonChecksum + "\u0001").getBytes());
    }

    private ByteBuffer prepareRequestMessages(String channelId, long beginSeqNo, long endSeqNo, int mdReqId) {
        String logonMessage = "35=V\u0001" + "1180=" + channelId + "\u0001262=" + channelId + "-" + mdReqId +
                "\u00011182=" + beginSeqNo + "\u00011183=" + endSeqNo + "\u0001";
        logonMessage = "9=" + logonMessage.length() + "\u0001" + logonMessage;
        String logonChecksum = calculateChecksum(logonMessage);
        return ByteBuffer.wrap((logonMessage + "10=" + logonChecksum + "\u0001").getBytes());//todo optimize it
    }

    private String calculateChecksum(String message) {
        int checksum = 0;
        for (int i = 0; i < message.length(); ++i) {
            checksum += message.charAt(i);
        }
        checksum = checksum & 255;
        String validChecksum = String.valueOf(checksum);
        while(validChecksum.length() < 3) {
            validChecksum = "0" + validChecksum;
        }
        return validChecksum;
    }
}
