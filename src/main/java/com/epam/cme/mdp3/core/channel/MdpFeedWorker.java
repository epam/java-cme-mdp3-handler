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

package com.epam.cme.mdp3.core.channel;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.cfg.ConnectionCfg;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static com.epam.cme.mdp3.core.channel.MdpFeedRtmState.*;

public class MdpFeedWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MdpFeedWorker.class);
    private static final int ACTIVE_MARK = 1;
    private static final int SHUTDOWN_MARK = 2;
    public static final int RCV_BUFFER_SIZE = 4*1024*1024;

    private final ConnectionCfg cfg;
    private String networkInterface = null;
    private int rcvBufSize = RCV_BUFFER_SIZE;
    private final List<MdpFeedListener> listeners = new ArrayList<>();
    private DatagramChannel multicastChannel;
    private MembershipKey membershipKey;
    private Selector selector;
    private NetworkInterface ni;
    private MdpFeedContext feedContext;
    private final AtomicReference<MdpFeedRtmState> feedState = new AtomicReference<>(STOPPED);

    public MdpFeedWorker(final ConnectionCfg cfg) throws MdpFeedException {
        this.cfg = cfg;
        init();
    }

    public MdpFeedWorker(final ConnectionCfg cfg, final String networkInterface, final int rcvBufSize) throws MdpFeedException {
        this.cfg = cfg;
        this.networkInterface = networkInterface;
        this.rcvBufSize = rcvBufSize;
        init();
    }

    void init() throws MdpFeedException {
        try {
            this.feedContext = new MdpFeedContext(this);
            this.ni = this.networkInterface != null ? NetworkInterface.getByName(this.networkInterface) : NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        } catch (IOException e) {
            logger.error("Failed open DatagramChannel", e);
            throw new MdpFeedException("Failed open DatagramChannel", e);
        }
    }

    public void open() throws Exception {
        multicastChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        multicastChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        multicastChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
        multicastChannel.setOption(StandardSocketOptions.SO_RCVBUF, this.rcvBufSize);
        multicastChannel.configureBlocking(false);
        connect(cfg.getIp(), cfg.getPort());
    }

    public void addListener(final MdpFeedListener mdpFeedListener) {
        listeners.add(mdpFeedListener);
    }

    private void connect(final String ip, final int port) throws MdpFeedException {
        try {
            final InetAddress group = InetAddress.getByName(ip);
            this.multicastChannel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            multicastChannel.register(selector, SelectionKey.OP_READ);
            logger.debug("Bound to {}", port);
            this.membershipKey = multicastChannel.join(group, ni);
            logger.debug("Joined to multicast group {}", group);
        } catch (Exception e) {
            logger.error("Failed to join to {}:{}. Exception: {}", ip, port, e);
            throw new MdpFeedException(String.format("Failed to connect to %1$s:%2$d", ip, port), e);
        }
    }

    public void close() throws IOException {
        if (selector.isOpen()) selector.close();
        if (this.membershipKey.isValid()) this.membershipKey.drop();
        if (this.multicastChannel.isOpen()) {
            this.multicastChannel.close();
        }
    }

    public boolean isRunnable() {
        return this.feedState.compareAndSet(STOPPED, ACTIVE);
    }

    @Override
    public void run() {
        /*
         * if any thread in result of concurrency created a new the same feed thread instance too early, and
         * previous feed thread shutdown still active, then wait until full shutdown
         */
        while (isShutdown()) {
            LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(1));
        }
        // if impossible to switch from stopped state to active, then we should not continue (concurrently illegal state)
        if (!isRunnable()) {
            return;
        }
        try {
            open();
        } catch (Exception e) {
            logger.error("Failed to open Feed", e);
            return;
        }
        notifyStarted();
        final MdpPacket mdpPacket = MdpPacket.instance();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        mdpPacket.wrapFromBuffer(byteBuffer);
        // work while any thread really started shutdown and did not cancel it in time
        while (!this.feedState.compareAndSet(PENDING_SHUTDOWN, SHUTDOWN)) {
            try {
                select(byteBuffer, mdpPacket);
            } catch (Exception e) {
                logger.error("Exception in message loop", e);
            }
        }
        try {
            close();
            mdpPacket.release();
            notifyStopped();
            // finally stop the feed thread. If exists another feed thread in wait state, then it will proceed from this moment
            this.feedState.compareAndSet(SHUTDOWN, STOPPED);
        } catch (IOException e) {
            logger.error("Failed to stop Feed", e);
        }
        logger.debug("Stop message loop in {}", cfg.toString());
    }

    private void select(final ByteBuffer byteBuffer, final MdpPacket mdpPacket) throws IOException {
        if (selector.select() > 0) {
            Iterator selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                final SelectionKey key = (SelectionKey) selectedKeys.next();
                selectedKeys.remove();

                if (key.isValid() && key.isReadable()) {
                    receiveMessageAndNotifySubscribers(byteBuffer, mdpPacket);
                }
            }
        }
    }

    private void receiveMessageAndNotifySubscribers(final ByteBuffer byteBuffer, final MdpPacket mdpPacket) throws IOException {
        byteBuffer.clear();
        multicastChannel.receive(byteBuffer);
        byteBuffer.flip();
        final int receivedPacketSize = byteBuffer.limit();
        if (receivedPacketSize > 0) {
            mdpPacket.length(receivedPacketSize);
            notifyListeners(mdpPacket);
        }
    }

    private void notifyListeners(final MdpPacket mdpPacket) {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onPacket(this.feedContext, mdpPacket);
        }
    }

    private void notifyStarted() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onFeedStarted(cfg.getFeedType(), cfg.getFeed());
        }
    }

    private void notifyStopped() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onFeedStopped(cfg.getFeedType(), cfg.getFeed());
        }
    }

    // review this later. Scheduler is not used now, so should be review entire context
    public boolean isActiveAndNotShutdown() {
        return this.isActive();
    }

    public boolean isActive() {
        return this.feedState.get() == ACTIVE;
    }

    public boolean isShutdown() {
        return this.feedState.get() == PENDING_SHUTDOWN || this.feedState.get() == SHUTDOWN;
    }

    public boolean cancelShutdownIfStarted() {
        return this.feedState.compareAndSet(PENDING_SHUTDOWN, ACTIVE);
    }

    public boolean shutdown() {
        final boolean res = this.feedState.compareAndSet(ACTIVE, PENDING_SHUTDOWN);
        selector.wakeup();
        return res;
    }

    public ConnectionCfg getCfg() {
        return cfg;
    }
}
