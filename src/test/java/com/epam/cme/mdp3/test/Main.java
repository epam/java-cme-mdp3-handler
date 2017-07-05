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

import com.epam.cme.mdp3.MdpChannel;
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.core.control.InstrumentState;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static AtomicLong prcdSeqNum = new AtomicLong(0);

    private static class ChannelListenerImpl implements ChannelListener {
        @Override
        public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is started", channelId, feedType, feed);
        }

        @Override
        public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is stopped", channelId, feedType, feed);
        }

        @Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
            /*if (feedType != FeedType.S) {
                logger.info("{} {}: {}", feedType, feed, mdpPacket);
                /*final long seqNum = mdpPacket.getMsgSeqNum();
                if (!prcdSeqNum.compareAndSet(0, seqNum)) {
                    if (!prcdSeqNum.compareAndSet(seqNum-1, seqNum)) {
                        if (prcdSeqNum.get() + 3 < seqNum) {
                            final long prevPrcdSeqNum = prcdSeqNum.get();
                            prcdSeqNum.set(seqNum);
                            logger.warn("!!!! GAP {}:{}", prevPrcdSeqNum, seqNum);
                        }
                    }
                }
            }*/
        }

        @Override
        public void onBeforeChannelReset(String channelId, MdpMessage resetMessage) {
            //logger.info("Channel '{}' is broken, all books should be restored", channelId);
        }

        @Override
        public void onFinishedChannelReset(String channelId, MdpMessage resetMessage) {
            //logger.info("Channel '{}' has been reset and restored", channelId);
        }

        @Override
        public void onChannelStateChanged(String channelId, ChannelState prevState, ChannelState newState) {
            //logger.info("Channel '{}' state is changed from '{}' to '{}'", channelId, prevState, newState);
        }

        @Override
        public void onInstrumentStateChanged(final String channelId, int securityId, final String secDesc, InstrumentState prevState, InstrumentState newState) {
            if (newState != InstrumentState.INITIAL) {
                logger.info("Channel '{}'s instrument {}-{} state is changed from '{}' to '{}'", channelId, securityId, secDesc, prevState, newState);
            }
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            //logger.info("Received SecurityDefinition(d). ChannelId: {}, Schema Id: {}", mdpMessage.getSchemaId());
            return MdEventFlags.NOTHING;
        }

        @Override
        public void onIncrementalRefresh(final String channelId, final short matchEventIndicator, int securityId, String secDesc, long msgSeqNum, final FieldSet incrRefreshEntry) {
            /*logger.info("[{}] onIncrementalRefresh: ChannelId: {}, SecurityId: {}-{}. RptSeqNum(83): {}",
                    msgSeqNum, channelId, securityId, secDesc, incrRefreshEntry.getUInt32(RPT_SEQ_NUM));*/
        }

        @Override
        public void onSnapshotFullRefresh(final String channelId, String secDesc, final MdpMessage snptMessage) {
            /*logger.info("onFullRefresh: ChannelId: {}, SecurityId: {}-{}. RptSeqNum(83): {}",
                    channelId, snptMessage.getInt32(SECURITY_ID), secDesc, snptMessage.getUInt32(RPT_SEQ_NUM));*/
        }

        @Override
        public void onRequestForQuote(String channelId, MdpMessage rfqMessage) {
            //logger.info("onRequestForQuote");
        }

        @Override
        public void onSecurityStatus(String channelId, int securityId, MdpMessage secStatusMessage) {
            //logger.info("onSecurityStatus. ChannelId: {}, SecurityId: {}, SecurityTradingStatus(326): {}", channelId, securityId, SecurityTradingStatus.fromFIX(secStatusMessage.getUInt8(326)));
        }
    }

    private static void defineChannel(final Map<String, List<String>> channelInfos, final String channelId, String... groups) {
        channelInfos.put(channelId, Arrays.asList(groups));
    }

    private static MdpChannel openChannel(final String channelId, final Set<InstrumentInfo> instruments) throws Exception {
        final MdpChannel mdpChannel = new MdpChannelBuilder(channelId,
                Main.class.getResource("/config.xml").toURI(),
                Main.class.getResource("/templates_FixBinary.xml").toURI())
                .usingListener(new ChannelListenerImpl())
                //.usingGapThreshold(5)
                .build();

        instruments.forEach(instrumentInfo -> mdpChannel.subscribe(instrumentInfo.instrumentId, instrumentInfo.desc));
        mdpChannel.startIncrementalFeedA();
        mdpChannel.startIncrementalFeedB();
        mdpChannel.startSnapshotFeedA();
        mdpChannel.startSnapshotMBOFeedA();

        return mdpChannel;
    }

    public static void main(String args[]) {
//        # 310 for ES
//        # 314 for 6A, 6B, 6J, 6S
//        # 318 for NQ
//        # 382 for CL
//        # 360 for HG, GC, SI
//        # 320 for 6C, 6E, 6M, 6N
//        # 342 for YM
//        # 344 for ZB, ZN, ZF
        final Map<String, List<String>> channelInfos = new HashMap<>();
        defineChannel(channelInfos, "310", "ES");
        defineChannel(channelInfos, "314", "6A", "6B", "6J", "6S");
        defineChannel(channelInfos, "318", "NQ");
        defineChannel(channelInfos, "382", "CL");
        defineChannel(channelInfos, "360", "HG", "GC", "SI");
        defineChannel(channelInfos, "320", "6C", "6E", "6M", "6N");
        defineChannel(channelInfos, "342", "YM");
        defineChannel(channelInfos, "344", "ZB", "ZN", "ZF");

        final Map<String, Set<InstrumentInfo>> resolvedInstruments = new HashMap<>();
        channelInfos.forEach((s, groups) -> resolvedInstruments.put(s, new ChannelHelper().resolveInstruments(s, groups, null)));

        final List<MdpChannel> openChannels = new ArrayList<>();
        try {
            resolvedInstruments.forEach((s, instrumentInfos) -> {
                if (!resolvedInstruments.isEmpty()) {
                    try {
                        openChannels.add(openChannel(s, instrumentInfos));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("Press enter to shutdown.");
            System.in.read();
            openChannels.forEach(MdpChannel::close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
