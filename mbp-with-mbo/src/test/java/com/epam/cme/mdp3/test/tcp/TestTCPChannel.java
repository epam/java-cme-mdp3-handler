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

package com.epam.cme.mdp3.test.tcp;

import com.epam.cme.mdp3.Feed;
import com.epam.cme.mdp3.FeedType;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.core.channel.tcp.MdpTCPMessageRequester;
import com.epam.cme.mdp3.core.channel.tcp.TCPChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestTCPChannel implements TCPChannel {
    public static final int WAITING_MESSAGE_TIMEOUT_IN_SEC = 5;
    private BlockingQueue<ByteBuffer> outQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<ByteBuffer> inQueue = new LinkedBlockingQueue<>();
    private MdpFeedContext mdpFeedContext = new MdpFeedContext(Feed.A, FeedType.H);

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void send(ByteBuffer bb) throws IOException {
        inQueue.add(bb);
    }

    @Override
    public int receive(ByteBuffer bb) throws IOException {
        try {
            ByteBuffer nextMessage = outQueue.poll(WAITING_MESSAGE_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
            if(bb.limit() == MdpTCPMessageRequester.NUMBER_OF_SIZE_BYTES) {
                bb.putChar((char)nextMessage.limit());
                outQueue.add(nextMessage);
                return MdpTCPMessageRequester.NUMBER_OF_SIZE_BYTES;
            } else {
                bb.put(nextMessage);
            }
            return nextMessage.limit();
        } catch (InterruptedException e) {
            return 0;
        }
    }

    @Override
    public MdpFeedContext getFeedContext() {
        return mdpFeedContext;
    }

    public void setNextMessageForReceiving(ByteBuffer bb){
        outQueue.add(bb);
    }

    public ByteBuffer nextMessage(){
        try {
            return inQueue.poll(WAITING_MESSAGE_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }
}
