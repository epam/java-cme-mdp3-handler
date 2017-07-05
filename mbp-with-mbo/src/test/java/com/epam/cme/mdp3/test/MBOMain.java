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
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.epam.cme.mdp3.MdConstants.SECURITY_ID;
import static com.epam.cme.mdp3.mktdata.MdConstants.SECURITY_ID;

public class MBOMain {
    private static final Logger logger = LoggerFactory.getLogger(MBOMain.class);
    private static Map<String, MBOBook> mboBook = new HashMap<>();

    private static class ChannelListenerImpl implements ChannelListener {
        private final MdpGroup mdpGroup = SbeGroup.instance();
        private final SbeDouble sbeDouble = SbeDouble.instance();
        private final boolean traceEnabled = logger.isTraceEnabled();

        @Override
        public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is started", channelId, feedType, feed);
//            if(feedType.equals(SMBO)){
//                mboBook.forEach((s, book) -> {
//                    book.clear();
//                });
//            }
        }

        @Override
        public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is stopped", channelId, feedType, feed);
        }

        @Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
//            if (feedType != FeedType.S) {
//                logger.info("{} - {} {}: {}", channelId, feedType, feed, mdpPacket.getMsgSeqNum());
//            }
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
            logger.info("Channel '{}' state is changed from '{}' to '{}'", channelId, prevState, newState);
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
//            logger.info("Received SecurityDefinition(d). ChannelId: {}, Schema Id: {}", channelId, mdpMessage.getSchemaId());
            return MdEventFlags.NOTHING;
        }

        @Override
        public void onIncrementalMBORefresh(final String channelId, final short matchEventIndicator, final int securityId,
                                     final String secDesc, final long msgSeqNum, final FieldSet orderIDEntry, final FieldSet mdEntry){
            long orderId;
            long mdOrderPriority;
            double mdEntryPx;
            long mdDisplayQty;
            int mdUpdateAction;
            char mdEntryType;
            if(mdEntry == null){//MBO only
                orderId = orderIDEntry.getUInt64(37);
                mdOrderPriority = orderIDEntry.getUInt64(37707);
                orderIDEntry.getDouble(270, sbeDouble);
                mdEntryPx = sbeDouble.asDouble();
                mdDisplayQty = orderIDEntry.getInt32(37706);
                mdUpdateAction = orderIDEntry.getUInt8(279);
                mdEntryType = orderIDEntry.getChar(269);
                if(traceEnabled){
                    String result = String.format("MBO only     onIncrementalMBORefresh : ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s', mdUpdateAction - '%s', MatchEventIndicator: %s (byte representation: '%s')",
                            channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty,  mdEntryType, mdUpdateAction, matchEventIndicator, String.format("%08d", Integer.parseInt(Integer.toBinaryString(0xFFFF & matchEventIndicator))));
                    logger.trace(result);
                }
            } else {
                orderId = orderIDEntry.getUInt64(37);
                mdOrderPriority = orderIDEntry.getUInt64(37707);
                mdDisplayQty = orderIDEntry.getInt32(37706);
                mdUpdateAction = orderIDEntry.getUInt8(37708);

                mdEntry.getDouble(270, sbeDouble);
                mdEntryPx = sbeDouble.asDouble();
                mdEntryType = mdEntry.getChar(269);
                if(traceEnabled) {
                    String result = String.format("MBO with MBP onIncrementalMBORefresh : ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s', mdUpdateAction - '%s', MatchEventIndicator: %s (byte representation: '%s')",
                            channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdEntryType, mdUpdateAction, matchEventIndicator, String.format("%08d", Integer.parseInt(Integer.toBinaryString(0xFFFF & matchEventIndicator))));
                    logger.trace(result);
                }
            }
////            updateBook(securityId + "-" + secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdUpdateAction, mdEntryType);

        }

        @Override
        public void onSnapshotMBOFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage){
            int securityId = snptMessage.getInt32(SECURITY_ID);

            snptMessage.getGroup(268, mdpGroup);

            while (mdpGroup.hashNext()){
                mdpGroup.next();
                long orderId = mdpGroup.getUInt64(37);
                long mdOrderPriority = mdpGroup.getUInt64(37707);
                mdpGroup.getDouble(270, sbeDouble);
                double mdEntryPx = sbeDouble.asDouble();
                long mdDisplayQty = mdpGroup.getInt32(37706);
                char mdEntryType = mdpGroup.getChar(269);
                if(traceEnabled) {
                    String result = String.format("onSnapshotMBOFullRefresh : ChannelId: %s, SecurityId: %s-%s, orderId - '%s', mdOrderPriority - '%s', mdEntryPx - '%s', mdDisplayQty - '%s',  mdEntryType - '%s'",
                            channelId, securityId, secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, mdEntryType);
                    logger.trace(result);
                }
//                updateBook(securityId + "-" + secDesc, orderId, mdOrderPriority, mdEntryPx, mdDisplayQty, 0, mdEntryType);
            }
        }

        private void updateBook(String instrumentId, long orderId, long mdOrderPriority, double mdEntryPx, long mdDisplayQty, int mdUpdateAction, char mdEntryType){
            if(!mboBook.containsKey(instrumentId)){
                mboBook.put(instrumentId, new MBOBook());
            }
            MBOBook currentMBOBook = mboBook.get(instrumentId);
            MBOBook.Side side;
            if(mdEntryType == '0'){
                side = MBOBook.Side.BID;
            } else {
                side = MBOBook.Side.ASK;
            }
            MBOBook.BookEntity bookEntity = new MBOBook.BookEntity(orderId, mdOrderPriority, mdEntryPx, mdDisplayQty);
            switch (mdUpdateAction) {
                case 0:
                    currentMBOBook.add(side, bookEntity);
                    break;
                case 1:
                    boolean updated = currentMBOBook.update(side, bookEntity);
                    if(!updated) {
                        logger.error("Update failed");
                    }
                    break;
                case 2:
                    boolean removed = currentMBOBook.remove(side, bookEntity);
                    if(!removed) {
                        logger.error("Remove failed");
                    }
                    break;
            }
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

    private static MdpChannel openChannel(final String channelId, final Set<InstrumentInfo> instruments,
                                          String networkInterface, ScheduledExecutorService executorService) throws Exception {
        final MdpChannel mdpChannel = new MdpChannelBuilder(channelId,
                MBOMain.class.getResource("/config.xml").toURI(),
                MBOMain.class.getResource("/templates_FixBinary.xml").toURI())
                .usingListener(new ChannelListenerImpl())
                .setNetworkInterface(FeedType.SMBO, Feed.A, networkInterface).setNetworkInterface(FeedType.SMBO, Feed.B, networkInterface)
                .setNetworkInterface(FeedType.S, Feed.A, networkInterface).setNetworkInterface(FeedType.S, Feed.B, networkInterface)
                .setNetworkInterface(FeedType.I, Feed.A, networkInterface).setNetworkInterface(FeedType.I, Feed.B, networkInterface)
                .setNetworkInterface(FeedType.N, Feed.A, networkInterface).setNetworkInterface(FeedType.N, Feed.B, networkInterface)
                .mbpEnable(false)
                .mboEnable(true)
                .usingIncrQueueSize(15000)
                .usingScheduler(executorService)
                .build();
        instruments.forEach(instrumentInfo -> mdpChannel.subscribe(instrumentInfo.instrumentId, instrumentInfo.desc));
        mdpChannel.startIncrementalFeedA();
        mdpChannel.startIncrementalFeedB();
        mdpChannel.startSnapshotMBOFeedA();
        return mdpChannel;
    }

    public static void main(String args[]) throws Exception {
//        # 310 for ES
//        # 314 for 6A, 6B, 6J, 6S
//        # 318 for NQ
//        # 382 for CL
//        # 360 for HG, GC, SI
//        # 320 for 6C, 6E, 6M, 6N
//        # 342 for YM
//        # 344 for ZB, ZN, ZF
        final String networkInterface;
        if(args.length > 0) {
            networkInterface =args[0];
        } else {
            networkInterface = null;
        }


        final Map<String, List<String>> channelInfos = new HashMap<>();
//        defineChannel(channelInfos, "310", "ES");
        defineChannel(channelInfos, "314", "6A", "6B", "6J", "6S");
//        defineChannel(channelInfos, "318", "NQ");
        defineChannel(channelInfos, "382", "CL");
        defineChannel(channelInfos, "360", "HG", "GC", "SI");
//        defineChannel(channelInfos, "320", "6C", "6E", "6M", "6N");
//        defineChannel(channelInfos, "342", "YM");
        defineChannel(channelInfos, "344", "ZB", "ZN", "ZF");

        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(channelInfos.size());

        final Map<String, Set<InstrumentInfo>> resolvedInstruments = new HashMap<>();
        channelInfos.forEach((s, groups) -> resolvedInstruments.put(s, new ChannelHelper().resolveInstruments(s, groups, networkInterface)));

        final List<MdpChannel> openChannels = new ArrayList<>();
        try {
            resolvedInstruments.forEach((s, instrumentInfos) -> {
                if (!resolvedInstruments.isEmpty()) {
                    try {
                        openChannels.add(openChannel(s, instrumentInfos, networkInterface, executorService));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("Press enter to shutdown.");
            while (true) {
                int read = System.in.read();
                if(((char)read) == '1') {
                    mboBook.forEach((s, book) -> {
                        logger.info("MBO MBOBook for '{}'\n{}", s, book.toString());
                    });

                } else if(((char)read) == '0') {
                    mboBook.forEach((s, book) -> book.clear());
                } else if(((char)read) == '3') {
                    openChannels.forEach(MdpChannel::close);
                    executorService.shutdownNow();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
