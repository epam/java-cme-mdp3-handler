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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.control.InstrumentController;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeString;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import net.openhft.koloboke.collect.map.IntObjCursor;
import net.openhft.koloboke.collect.map.IntObjMap;
import net.openhft.koloboke.collect.map.hash.HashIntObjMaps;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelInstruments implements MdpFeedListener {
    private static final int SEC_ID_TAG = 48;
    private static final int SEC_DESC_TAG = 55;
    private static final int SEC_MD_FEED_TYPES = 1141;
    private static final int SEC_MD_FEED_TYPE = 1022;
    private static final int SEC_MARKET_DEPTH = 264;
    private static final int SEC_DEFAULT_MD_DEPTH = 10;

    private static final int PRCD_MSG_COUNT_NULL = Integer.MAX_VALUE;   // max value used as undefined (null)
    private static final int INSTRUMENT_CYCLES_MAX = 2; // do we need an option in configuration for this?

    private final ChannelContext channelContext;
    private final IntObjMap<InstrumentController> instruments = HashIntObjMaps.newMutableMap();
    private AtomicInteger msgCountDown = new AtomicInteger(PRCD_MSG_COUNT_NULL);

    private final SbeString secDescString = SbeString.allocate(100);
    private final MdpGroup mdTypeGroup = SbeGroup.instance();
    private final SbeString secMdFeedType = SbeString.allocate(3);

    public ChannelInstruments(final ChannelContext channelContext) {
        this.channelContext = channelContext;
    }

    @Override
    public void onFeedStarted(FeedType feedType, Feed feed) {
        // nothing yet
    }

    @Override
    public void onFeedStopped(FeedType feedType, Feed feed) {
        // nothing yet
    }

    public void onMessage(final MdpFeedContext feedContext, final MdpMessage secDefMsg) {
        final int subscriptionFlags = this.channelContext.notifySecurityDefinitionListeners(secDefMsg);
        final byte depth = extractMaxDepthFromSecDef(secDefMsg);
        if (!MdEventFlags.isNothing(subscriptionFlags)) {
            registerSecurity(secDefMsg, subscriptionFlags, depth);
        } else {
            final int securityId = secDefMsg.getInt32(SEC_ID_TAG);
            final SbeString strValObj = feedContext.getStrValObj();
            boolean hasValue = secDefMsg.getString(SEC_DESC_TAG, strValObj);
            if (hasValue) {
                updateSecDesc(securityId, secDescString.getString());
            }
        }
        if (this.msgCountDown.get() == PRCD_MSG_COUNT_NULL) {
            final int totalNumReports = getTotalReportNum(secDefMsg) * INSTRUMENT_CYCLES_MAX;
            this.msgCountDown.compareAndSet(PRCD_MSG_COUNT_NULL, totalNumReports);
        }
        final int msgLeft = this.msgCountDown.decrementAndGet();
        if (canStopInstrumentListening(msgLeft)) {
            channelContext.stopInstrumentFeeds();
        }
    }

    @Override
    public void onPacket(final MdpFeedContext feedContext, final MdpPacket instrumentPacket) {
        final Iterator<MdpMessage> mdpMessageIterator = instrumentPacket.iterator();
        MdpMessage mdpMessage;
        while (mdpMessageIterator.hasNext()) {
            mdpMessage = mdpMessageIterator.next();
            final MdpMessageType messageType = channelContext.getMdpMessageTypes().getMessageType(mdpMessage.getSchemaId());
            final SemanticMsgType semanticMsgType = messageType.getSemanticMsgType();
            if (semanticMsgType == SemanticMsgType.SecurityDefinition) {
                mdpMessage.setMessageType(messageType);
            }
            onMessage(feedContext, mdpMessage);
        }
    }

    private boolean canStopInstrumentListening(final int cyclesLeft) {
        return cyclesLeft <= 0;
    }

    public InstrumentController find(final int securityId) {
        return find(securityId, null, false, 0, (byte) 0);
    }

    public InstrumentController find(final int securityId, final String secDesc, boolean createIfAbsent, final int subscriptionFlags, final byte maxDepth) {
        InstrumentController controller = this.instruments.get(securityId);
        if (controller == null && createIfAbsent) {
            registerSecurity(securityId, secDesc, subscriptionFlags, maxDepth);
        }
        return controller;
    }

    private void registerController(final int securityId, final String secDesc, final int subscriptionFlags, final byte maxDepth, final int gapThreshold) {
        this.instruments.put(securityId, new InstrumentController(channelContext, securityId, secDesc, subscriptionFlags, maxDepth, gapThreshold));
    }

    public void registerSecurity(final MdpMessage secDef, final int subscriptionFlags, final byte maxDepth) {
        final int securityId = secDef.getInt32(SEC_ID_TAG);
        secDef.getString(SEC_DESC_TAG, secDescString);
        registerSecurity(securityId, secDescString.getString(), subscriptionFlags, maxDepth);
    }

    public void updateSecDesc(final int securityId, final String secDesc) {
        synchronized (this.instruments) {
            final InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                instrumentController.setSecDesc(secDesc);
            }
        }
    }

    public boolean registerSecurity(final int securityId, final String secDesc, final int subscriptionFlags, final byte maxDepth) {
        boolean updatedState;
        synchronized (this.instruments) {
            InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                updatedState = instrumentController.onResubscribe(subscriptionFlags);
            } else {
                registerController(securityId, secDesc, subscriptionFlags, maxDepth, this.channelContext.getGapThreshold());
                updatedState = true;
            }
        }
        return updatedState;
    }

    public void resetAll() {
        final IntObjCursor<InstrumentController> entries = this.instruments.cursor();

        while (entries.moveNext()) {
            entries.value().onChannelReset();
        }
    }

    private int getTotalReportNum(final MdpMessage mdpMessage) {
        return (int) mdpMessage.getUInt32(911);
    }

    public void resetCycleCounter() {
        this.msgCountDown.set(PRCD_MSG_COUNT_NULL);
    }

    public void discontinueSecurity(final int securityId) {
        synchronized (this.instruments) {
            InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                instrumentController.discontinue();
            }
        }
    }

    public int getSubscriptionFlags(final int securityId) {
        InstrumentController instrumentController = find(securityId);
        if (instrumentController != null) {
            return instrumentController.getSubscriptionFlags();
        }
        return MdEventFlags.NOTHING;
    }

    public void setSubscriptionFlags(final int securityId, final int flags, final byte maxDepth) {
        synchronized (this.instruments) {
            InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                instrumentController.setSubscriptionFlags(flags);
            } else {
                registerController(securityId, null, flags, maxDepth, this.channelContext.getGapThreshold());
            }
        }
    }

    public void addSubscriptionFlags(final int securityId, final int flags, final byte maxDepth) {
        synchronized (this.instruments) {
            InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                instrumentController.addSubscriptionFlag(flags);
            } else {
                registerController(securityId, null, flags, maxDepth, this.channelContext.getGapThreshold());
            }
        }
    }

    public void removeSubscriptionFlags(final int securityId, final int flags) {
        synchronized (this.instruments) {
            InstrumentController instrumentController = find(securityId);
            if (instrumentController != null) {
                instrumentController.removeSubscriptionFlag(flags);
            }
        }
    }

    private byte extractMaxDepthFromSecDef(final MdpMessage secDef) {
        secDef.getGroup(SEC_MD_FEED_TYPES, mdTypeGroup);
        while (mdTypeGroup.hashNext()) {
            mdTypeGroup.next();
            mdTypeGroup.getString(SEC_MD_FEED_TYPE, secMdFeedType);

            if (secMdFeedType.getCharAt(secMdFeedType.getLength() - 1) == 'X') {
                return mdTypeGroup.getInt8(SEC_MARKET_DEPTH);
            }
        }
        return SEC_DEFAULT_MD_DEPTH;
    }
}
