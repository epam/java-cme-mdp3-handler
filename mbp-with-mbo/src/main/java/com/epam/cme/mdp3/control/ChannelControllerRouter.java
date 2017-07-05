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

package com.epam.cme.mdp3.control;


import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ChannelControllerRouter implements MdpChannelController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelControllerRouter.class);
    private final InstrumentManager instrumentManager;
    private final MdpMessageTypes mdpMessageTypes;
    private final MdpGroupEntry mdEntry = SbeGroupEntry.instance();
    private final MdpGroup noMdEntriesGroup = SbeGroup.instance();
    private final List<ChannelListener> channelListeners;
    private final String channelId;

    public ChannelControllerRouter(String channelId, InstrumentManager instrumentManager, MdpMessageTypes mdpMessageTypes, List<ChannelListener> channelListeners){
        this.channelId = channelId;
        this.instrumentManager = instrumentManager;
        this.mdpMessageTypes = mdpMessageTypes;
        this.channelListeners = channelListeners;
    }

    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        for (MdpMessage mdpMessage : mdpPacket) {
            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
            if (isMessageSupported(mdpMessage)) {
                int securityId = getSecurityId(mdpMessage);
                InstrumentController instrumentController = instrumentManager.getMBOInstrumentController(securityId);
                if (instrumentController != null) {
                    instrumentController.handleSnapshotMDEntry(mdpMessage);
                }
            }
        }
    }

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        MdpGroup mdpGroup = feedContext.getMdpGroupObj();
        MdpGroupEntry mdpGroupEntry = feedContext.getMdpGroupEntryObj();
        long msgSeqNum = mdpPacket.getMsgSeqNum();
        for (MdpMessage mdpMessage : mdpPacket) {
            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
            SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
            switch (semanticMsgType) {
                case MarketDataIncrementalRefresh:
                    handleIncrementalMessage(mdpMessage, mdpGroup, mdpGroupEntry, msgSeqNum);
                    break;
                case QuoteRequest:
                    handleQuoteRequest(mdpMessage);
                    break;
                case SecurityStatus:
                    handleSecurityStatus(mdpMessage);
                    break;
                case SecurityDefinition:
                    handleSecurityDefinition(mdpMessage);
                    break;
                default:
                    logger.debug("Message has been ignored due to its SemanticMsgType '{}'", semanticMsgType);
            }

        }
    }

    @Override
    public void preClose() {

    }

    @Override
    public void close() {

    }

    protected void routeEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry orderIDEntry, MdpGroupEntry mdEntry, long msgSeqNum){
        InstrumentController instrumentController = instrumentManager.getMBOInstrumentController(securityId);
        if (instrumentController != null) {
            instrumentController.handleIncrementMDEntry(mdpMessage, orderIDEntry, mdEntry, msgSeqNum);
        }
    }

    private void handleIncrementalMessage(MdpMessage mdpMessage, MdpGroup mdpGroup, MdpGroupEntry mdpGroupEntry, long msgSeqNum){
        if (isMessageSupported(mdpMessage)) {
            if (isIncrementOnlyForMBO(mdpMessage)) {
                mdpMessage.getGroup(MdConstants.NO_MD_ENTRIES, mdpGroup);
                while (mdpGroup.hashNext()) {
                    mdpGroup.next();
                    mdpGroup.getEntry(mdpGroupEntry);
                    int securityId = getSecurityId(mdpGroupEntry);
                    routeEntry(securityId, mdpMessage, mdpGroupEntry, null, msgSeqNum);
                }
            } else {
                if (mdpMessage.getGroup(MdConstants.NO_ORDER_ID_ENTRIES, mdpGroup)) {
                    while (mdpGroup.hashNext()) {
                        mdpMessage.getGroup(MdConstants.NO_MD_ENTRIES, noMdEntriesGroup);
                        mdpGroup.next();
                        mdpGroup.getEntry(mdpGroupEntry);
                        short entryNum = mdpGroupEntry.getUInt8(MdConstants.REFERENCE_ID);
                        noMdEntriesGroup.getEntry(entryNum, mdEntry);
                        int securityId = mdEntry.getInt32(MdConstants.SECURITY_ID);
                        routeEntry(securityId, mdpMessage, mdpGroupEntry, mdEntry, msgSeqNum);
                    }
                }
            }
        }
    }

    private void handleQuoteRequest(MdpMessage mdpMessage){
        for (ChannelListener listener : channelListeners) {
            listener.onRequestForQuote(channelId, mdpMessage);
        }
    }

    private void handleSecurityStatus(MdpMessage mdpMessage){
        int securityId = getSecurityId(mdpMessage);
        for (ChannelListener listener : channelListeners) {
            listener.onSecurityStatus(channelId, securityId, mdpMessage);
        }
    }

    private void handleSecurityDefinition(MdpMessage mdpMessage){
        for (ChannelListener listener : channelListeners) {
            listener.onSecurityDefinition(channelId, mdpMessage);
        }
    }

    private int getSecurityId(MdpMessage mdpMessage){
        return mdpMessage.getInt32(MdConstants.SECURITY_ID);
    }

    private int getSecurityId(MdpGroupEntry mdpGroupEntry){
        return mdpGroupEntry.getInt32(MdConstants.SECURITY_ID);
    }
}
