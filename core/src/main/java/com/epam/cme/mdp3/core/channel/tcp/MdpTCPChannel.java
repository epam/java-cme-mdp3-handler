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

import com.epam.cme.mdp3.core.cfg.ConnectionCfg;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class MdpTCPChannel implements TCPChannel {
    private static final Logger logger = LoggerFactory.getLogger(MdpTCPChannel.class);
    private final ConnectionCfg cfg;
    private volatile SocketChannel socketChannel;
    private MdpFeedContext feedContext;

    public MdpTCPChannel(final ConnectionCfg cfg) {
        this.cfg = cfg;
        this.feedContext = new MdpFeedContext(cfg);
    }

    @Override
    public boolean connect() {
        List<String> hostIPs = cfg.getHostIPs();
        for (String hostIP : hostIPs) {
            try {
                final InetSocketAddress inetSocketAddress = new InetSocketAddress(hostIP, cfg.getPort());
                socketChannel = SocketChannel.open(inetSocketAddress);
                logger.debug("Connected to {}:{}", hostIP, cfg.getPort());
                return true;
            } catch (Exception e) {
                logger.error("Failed to connect to {}:{}. Exception: {}", hostIP, cfg.getPort(), e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void send(ByteBuffer bb) throws IOException {
        socketChannel.write(bb);
    }

    @Override
    public int receive(ByteBuffer bb) throws IOException {
        int readBytes = socketChannel.read(bb);
        if(readBytes < 0) {
            throw new EOFException("Length of last received bytes is less than zero '" + readBytes + "'");
        } else {
            return readBytes;
        }
    }

    @Override
    public MdpFeedContext getFeedContext() {
        return feedContext;
    }
}
