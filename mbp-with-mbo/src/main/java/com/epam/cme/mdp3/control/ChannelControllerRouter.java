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
import com.epam.cme.mdp3.mktdata.enums.MDEntryType;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

import static com.epam.cme.mdp3.MdConstants.INCR_RFRSH_MD_ENTRY_TYPE;

public class ChannelControllerRouter implements MdpChannelController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelControllerRouter.class);
    private final InstrumentManager instrumentManager;
    private final MdpGroupEntry mdEntry = SbeGroupEntry.instance();
    private final MdpGroup noMdEntriesGroup = SbeGroup.instance();
    private MdpMessageTypes mdpMessageTypes;
    private final List<ChannelListener> channelListeners;
    private final InstrumentObserver instrumentObserver;
    private final List<Consumer<MdpMessage>> emptyBookConsumers;
    private final String channelId;
    private List<Integer> mboIncrementMessageTemplateIds;
    private List<Integer> mboSnapshotMessageTemplateIds;
    
    public ChannelControllerRouter(String channelId, InstrumentManager instrumentManager,
                                   MdpMessageTypes mdpMessageTypes, List<ChannelListener> channelListeners,
                                   InstrumentObserver instrumentObserver, List<Consumer<MdpMessage>> emptyBookConsumers,     
                                   List<Integer> mboIncrementMessageTemplateIds, List<Integer> mboSnapshotMessageTemplateIds){
        this.channelId = channelId;
        this.instrumentManager = instrumentManager;
        this.mdpMessageTypes = mdpMessageTypes;
        this.channelListeners = channelListeners;
        this.instrumentObserver = instrumentObserver;
        this.emptyBookConsumers = emptyBookConsumers;
        this.mboIncrementMessageTemplateIds = mboIncrementMessageTemplateIds;
        this.mboSnapshotMessageTemplateIds = mboSnapshotMessageTemplateIds;
    }

    @Override
	public List<Integer> getMBOIncrementMessageTemplateIds() {
    	return mboIncrementMessageTemplateIds == null ? MdpChannelController.super.getMBOIncrementMessageTemplateIds() : mboIncrementMessageTemplateIds;
    }
    
    @Override
    public List<Integer> getMBOSnapshotMessageTemplateIds() {
    	return mboSnapshotMessageTemplateIds == null ? MdpChannelController.super.getMBOSnapshotMessageTemplateIds() : mboSnapshotMessageTemplateIds;
    }
    
    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        for (MdpMessage mdpMessage : mdpPacket) {
            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
            int securityId = getSecurityId(mdpMessage);
            InstrumentController instrumentController = instrumentManager.getInstrumentController(securityId);
            if (instrumentController != null) {
                if (isMBOSnapshot(mdpMessage)) {
                    instrumentController.handleMBOSnapshotMDEntry(mdpMessage);
                } else {
                    instrumentController.handleMBPSnapshotMDEntry(mdpMessage);
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
                    instrumentObserver.onMessage(feedContext, mdpMessage);
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

    protected void routeMBOEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry orderIDEntry, MdpGroupEntry mdEntry, long msgSeqNum){
        InstrumentController instrumentController = instrumentManager.getInstrumentController(securityId);
        if (instrumentController != null) {
            instrumentController.handleMBOIncrementMDEntry(mdpMessage, orderIDEntry, mdEntry, msgSeqNum);
        }
    }

    protected void routeMBPEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry mdEntry, long msgSeqNum){
        InstrumentController instrumentController = instrumentManager.getInstrumentController(securityId);
        if (instrumentController != null) {
            instrumentController.handleMBPIncrementMDEntry(mdpMessage, mdEntry, msgSeqNum);
        }
    }

    private void handleIncrementalMessage(MdpMessage mdpMessage, MdpGroup mdpGroup, MdpGroupEntry mdpGroupEntry, long msgSeqNum){
        if (isIncrementalMessageSupported(mdpMessage)) {
            if (isIncrementOnlyForMBO(mdpMessage)) {
                mdpMessage.getGroup(MdConstants.NO_MD_ENTRIES, mdpGroup);
                while (mdpGroup.hashNext()) {
                    mdpGroup.next();
                    mdpGroup.getEntry(mdpGroupEntry);
                    int securityId = getSecurityId(mdpGroupEntry);
                    routeMBOEntry(securityId, mdpMessage, mdpGroupEntry, null, msgSeqNum);
                }
            } else {
                if (mdpMessage.getGroup(MdConstants.NO_MD_ENTRIES, noMdEntriesGroup)) {
                    while (noMdEntriesGroup.hashNext()) {
                        noMdEntriesGroup.next();
                        noMdEntriesGroup.getEntry(mdEntry);
                        final MDEntryType mdEntryType = MDEntryType.fromFIX(mdEntry.getChar(INCR_RFRSH_MD_ENTRY_TYPE));
                        if (mdEntryType == MDEntryType.EmptyBook) {
                            emptyBookConsumers.forEach(mdpMessageConsumer -> mdpMessageConsumer.accept(mdpMessage));
                        } else {
                            int securityId = mdEntry.getInt32(MdConstants.SECURITY_ID);
                            routeMBPEntry(securityId, mdpMessage, mdEntry, msgSeqNum);
                        }
                    }
                }
                if (mdpMessage.getGroup(MdConstants.NO_ORDER_ID_ENTRIES, mdpGroup) && isOrderEntityContainsReference(mdpGroup, mdpGroupEntry)) {
                    while (mdpGroup.hashNext()) {
                        mdpMessage.getGroup(MdConstants.NO_MD_ENTRIES, noMdEntriesGroup);
                        mdpGroup.next();
                        mdpGroup.getEntry(mdpGroupEntry);
                        short entryNum = mdpGroupEntry.getUInt8(MdConstants.REFERENCE_ID);
                        noMdEntriesGroup.getEntry(entryNum, mdEntry);
                        int securityId = mdEntry.getInt32(MdConstants.SECURITY_ID);
                        routeMBOEntry(securityId, mdpMessage, mdpGroupEntry, mdEntry, msgSeqNum);
                    }
                }
            }
        }
    }

    /**
     *
     * @return true if it is not TradeSummary order entity
     */
    private boolean isOrderEntityContainsReference(MdpGroup mdpGroup, MdpGroupEntry mdpGroupEntry){
        if(mdpGroup.hashNext()) {
            mdpGroup.getEntry(1, mdpGroupEntry);
            return mdpGroupEntry.hasField(MdConstants.REFERENCE_ID);
        } else {
            return false;
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

    private int getSecurityId(MdpMessage mdpMessage){
        return mdpMessage.getInt32(MdConstants.SECURITY_ID);
    }

    private int getSecurityId(MdpGroupEntry mdpGroupEntry){
        return mdpGroupEntry.getInt32(MdConstants.SECURITY_ID);
    }
}
