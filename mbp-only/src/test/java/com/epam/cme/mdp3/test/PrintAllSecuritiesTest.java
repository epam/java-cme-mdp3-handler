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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeString;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class PrintAllSecuritiesTest {
    private static final Logger logger = LoggerFactory.getLogger(PrintAllSecuritiesTest.class);
    private static final MdpGroup group1141 = SbeGroup.instance();
    private static final SbeString tag1022value = SbeString.allocate(3);
    private static final SbeString tag55value = SbeString.allocate(20);
    private static final SbeString tag1151value = SbeString.allocate(6);
    private static final SbeString tag6937value = SbeString.allocate(6);
    private static final SbeString tag167value = SbeString.allocate(6);
    private static final AtomicInteger counter = new AtomicInteger();
    private static final Object resultIsReady = new Object();

    private static class ChannelListenerImpl implements VoidChannelListener {
        @Override
        public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is started", channelId, feedType, feed);
        }

        @Override
        public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is stopped", channelId, feedType, feed);
            synchronized (resultIsReady) {
                resultIsReady.notify();
            }
        }

        @Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
            //logger.info("Channel '{}': {} feed {} received MDP packet {}", channelId, feedType, feed, mdpPacket.toString());
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            final int securityId = mdpMessage.getInt32(48);
            counter.incrementAndGet();
            logger.info("Channel {}'s security: {}", channelId, securityId);
            mdpMessage.getString(55, tag55value);
            logger.info("   Symbol : {}", tag55value.getString());
            mdpMessage.getString(1151, tag1151value);
            logger.info("   SecurityGroup : {}", tag1151value.getString());
            mdpMessage.getString(6937, tag6937value);
            logger.info("   Asset : {}", tag6937value.getString());
            mdpMessage.getString(167, tag167value);
            logger.info("   SecurityType : {}", tag167value.getString());
            while (group1141.hashNext()) {
                group1141.next();
                group1141.getString(1022, tag1022value);
                final int depth = group1141.getInt8(264);
                logger.info("   {} depth : {}", tag1022value.getString(), depth);
            }
            return MdEventFlags.NOTHING;
        }
    }

    public static void main(String args[]) {
        try {
            final MdpChannel mdpChannel = new MdpChannelBuilder("344",
                    Main.class.getResource("/config.xml").toURI(),
                    Main.class.getResource("/templates_FixBinary.xml").toURI())
                    //.setNetworkInterface(FeedType.N, Feed.A, "172.17.94.44")
                    .usingListener(new ChannelListenerImpl())
                    .noFeedIdleControl()
                    .build();

            mdpChannel.startInstrumentFeedA();
            synchronized (resultIsReady) {
                resultIsReady.wait();
            }
            logger.info("Received packets in cycles: {}", counter.get());
            mdpChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
