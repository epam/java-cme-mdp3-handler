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
import com.epam.cme.mdp3.core.control.InstrumentState;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import static com.epam.cme.mdp3.mktdata.MdConstants.RPT_SEQ_NUM;
import static com.epam.cme.mdp3.mktdata.MdConstants.SECURITY_ID;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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
            logger.info("{} {}: {}", feedType, feed, mdpPacket);
        }

        @Override
        public void onBeforeChannelReset(String channelId, MdpMessage resetMessage) {
            logger.info("Channel '{}' is broken, all books should be restored", channelId);
        }

        @Override
        public void onFinishedChannelReset(String channelId, MdpMessage resetMessage) {
            logger.info("Channel '{}' has been reset and restored", channelId);
        }

        @Override
        public void onChannelStateChanged(String channelId, ChannelState prevState, ChannelState newState) {
            logger.info("Channel '{}' state is changed from '{}' to '{}'", channelId, prevState, newState);
        }

        @Override
        public void onInstrumentStateChanged(final String channelId, int securityId, InstrumentState prevState, InstrumentState newState) {
            logger.info("Channel '{}'s instrument {} state is changed from '{}' to '{}'", channelId, securityId, prevState, newState);
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            logger.info("Received SecurityDefinition(d). Schema Id: {}", mdpMessage.getSchemaId());
            return MdEventFlags.NOTHING;
        }

        @Override
        public void onIncrementalRefresh(final String channelId, final short matchEventIndicator, int securityId, String secDesc, long msgSeqNum, final FieldSet incrRefreshEntry) {
            logger.info("[{}] onIncrementalRefresh: SecurityId: {}-{}. RptSeqNum(83): {}", msgSeqNum, securityId, secDesc, incrRefreshEntry.getUInt32(RPT_SEQ_NUM));
        }

        @Override
        public void onSnapshotFullRefresh(final String channelId, String secDesc, final MdpMessage snptMessage) {
            logger.info("onFullRefresh: SecurityId: {}-{}. RptSeqNum(83): {}", snptMessage.getInt32(SECURITY_ID), secDesc, snptMessage.getUInt32(RPT_SEQ_NUM));
        }

        @Override
        public void onRequestForQuote(String channelId, MdpMessage rfqMessage) {
            logger.info("onRequestForQuote");
        }

        @Override
        public void onSecurityStatus(String channelId, int securityId, MdpMessage secStatusMessage) {
            logger.info("onSecurityStatus. SecurityId: {}, RptSeqNum(83): {}", securityId, secStatusMessage.getUInt32(RPT_SEQ_NUM));
        }
    }

    public static void main(String args[]) {
        try {
            final MdpChannel mdpChannel311 = new MdpChannelBuilder("311",
                    Main.class.getResource("/config.xml").toURI(),
                    Main.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new ChannelListenerImpl())
                    .usingGapThreshold(20)
                    .build();

            mdpChannel311.enableAllSecuritiesMode();
            mdpChannel311.startIncrementalFeedA();
            mdpChannel311.startIncrementalFeedB();
            mdpChannel311.startSnapshotFeedA();
            System.out.println("Press enter to shutdown.");
            System.in.read();
            mdpChannel311.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
