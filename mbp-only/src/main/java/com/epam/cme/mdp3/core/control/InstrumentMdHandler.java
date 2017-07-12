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

package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.FieldSet;
import com.epam.cme.mdp3.MdEventFlags;
import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.mktdata.ImpliedBookHandler;
import com.epam.cme.mdp3.mktdata.MultipleDepthBookHandler;
import com.epam.cme.mdp3.mktdata.StatisticsHandler;
import com.epam.cme.mdp3.mktdata.TradeHandler;
import com.epam.cme.mdp3.mktdata.enums.MDEntryType;

public class InstrumentMdHandler {
    private final ChannelContext channelContext;
    private final int securityId;
    private int subscriptionFlags;
    private byte maxDepth;
    private boolean enabled;

    private ImpliedBookHandler impliedBookHandler;
    private MultipleDepthBookHandler multipleDepthBookHandler;
    private TradeHandler tradeHandler;
    private StatisticsHandler statisticsHandler;

    public InstrumentMdHandler(final ChannelContext channelContext, final int securityId, final int subscriptionFlags, final byte maxDepth) {
        this.channelContext = channelContext;
        this.securityId = securityId;
        this.maxDepth = maxDepth;
        setSubscriptionFlags(subscriptionFlags);
    }

    public int getSubscriptionFlags() {
        return subscriptionFlags;
    }

    public byte getMaxDepth() {
        return maxDepth;
    }

    public void setSubscriptionFlags(final int subscriptionFlags) {
        this.enabled = false;
        this.subscriptionFlags = subscriptionFlags;
        if (MdEventFlags.hasImpliedBook(subscriptionFlags) || MdEventFlags.hasImpliedTop(subscriptionFlags)/* ||
                SubscriptionFlags.hasConsolidatedBook(subscriptionFlags) || SubscriptionFlags.hasConsolidatedTop(subscriptionFlags)*/) {
            if (this.impliedBookHandler == null) {
                this.impliedBookHandler = new ImpliedBookHandler(this.channelContext, securityId, subscriptionFlags);
            } else {
                this.impliedBookHandler.clear();
                this.impliedBookHandler.setSubscriptionFlags(subscriptionFlags);
            }
            this.enabled = true;
        } else {
            this.impliedBookHandler = null;
        }
        if (MdEventFlags.hasBook(subscriptionFlags) || MdEventFlags.hasTop(subscriptionFlags) /*||
                SubscriptionFlags.hasConsolidatedBook(subscriptionFlags) || SubscriptionFlags.hasConsolidatedTop(subscriptionFlags)*/) {
            if (this.multipleDepthBookHandler == null) {
                this.multipleDepthBookHandler = new MultipleDepthBookHandler(this.channelContext, securityId, subscriptionFlags, maxDepth);
            } else {
                this.multipleDepthBookHandler.clear();
                this.multipleDepthBookHandler.setSubscriptionFlags(subscriptionFlags);
            }
            this.enabled = true;
        } else {
            this.multipleDepthBookHandler = null;
        }
        if (MdEventFlags.hasTradeSummary(subscriptionFlags)) {
            if (this.tradeHandler == null) {
                this.tradeHandler = new TradeHandler(this.channelContext, securityId, subscriptionFlags);
            } else {
                this.tradeHandler.clear();
                this.tradeHandler.setSubscriptionFlags(subscriptionFlags);
            }
            this.enabled = true;
        } else {
            this.tradeHandler = null;
        }
        if (MdEventFlags.hasStatistics(subscriptionFlags)) {
            if (this.statisticsHandler == null) {
                this.statisticsHandler = new StatisticsHandler(this.channelContext, securityId, this.subscriptionFlags);
            } else {
                this.statisticsHandler.clear();
                this.statisticsHandler.setSubscriptionFlags(subscriptionFlags);
            }
            this.enabled = true;
        } else {
            this.statisticsHandler = null;
        }
    }

    public void addSubscriptionFlag(final int subscriptionFlags) {
        this.subscriptionFlags |= subscriptionFlags;
        setSubscriptionFlags(this.subscriptionFlags);
    }

    public void removeSubscriptionFlag(final int subscriptionFlags) {
        this.subscriptionFlags ^= subscriptionFlags;
        setSubscriptionFlags(this.subscriptionFlags);
    }

    public void reset() {
        if (this.impliedBookHandler != null) this.impliedBookHandler.clear();
        if (this.multipleDepthBookHandler != null) this.multipleDepthBookHandler.clear();
        if (this.tradeHandler != null) this.tradeHandler.clear();
        if (this.multipleDepthBookHandler != null) this.multipleDepthBookHandler.clear();
    }

    public void handleSnapshotFullRefreshEntries(final MdpFeedContext feedContext, final MdpMessage fullRefreshMsg) {
        if (!this.enabled) return;

        MdpGroup mdpGroupObj = feedContext.getMdpGroupObj();

        fullRefreshMsg.getGroup(268, mdpGroupObj);
        while (mdpGroupObj.hashNext()) {
            mdpGroupObj.next();
            final MDEntryType mdEntryType = MDEntryType.fromFIX(mdpGroupObj.getChar(269));
            switch (mdEntryType) {
                case Bid:
                    if (this.multipleDepthBookHandler != null)
                        this.multipleDepthBookHandler.handleSnapshotBidEntry(mdpGroupObj);
                    break;
                case Offer:
                    if (this.multipleDepthBookHandler != null)
                        this.multipleDepthBookHandler.handleSnapshotOfferEntry(mdpGroupObj);
                    break;
                case Trade:
                    if (this.tradeHandler != null) this.tradeHandler.updateTradeSummary(mdpGroupObj);
                    break;
                case OpeningPrice:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateOpeningPrice(mdpGroupObj);
                    break;
                case SettlementPrice:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateSettlementPrice(mdpGroupObj);
                    break;
                case TradingSessionHighPrice:
                    if (this.statisticsHandler != null)
                        this.statisticsHandler.updateTradingSessionHighPrice(mdpGroupObj);
                    break;
                case TradingSessionLowPrice:
                    if (this.statisticsHandler != null)
                        this.statisticsHandler.updateTradingSessionLowPrice(mdpGroupObj);
                    break;
                case TradeVolume:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateTradeVolume(mdpGroupObj);
                    break;
                case OpenInterest:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateOpenInterest(mdpGroupObj);
                    break;
                case ImpliedBid:
                    if (this.impliedBookHandler != null) this.impliedBookHandler.handleSnapshotBidEntry(mdpGroupObj);
                    break;
                case ImpliedOffer:
                    if (this.impliedBookHandler != null) this.impliedBookHandler.handleSnapshotOfferEntry(mdpGroupObj);
                    break;
                case SessionHighBid:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateSessionHighBid(mdpGroupObj);
                    break;
                case SessionLowOffer:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateSessionLowOffer(mdpGroupObj);
                    break;
                case FixingPrice:
                    if (this.statisticsHandler != null) this.statisticsHandler.updateFixingPrice(mdpGroupObj);
                    break;
                case ElectronicVolume:
                    if (this.tradeHandler != null) this.tradeHandler.updateElectronicVolume(mdpGroupObj);
                    break;
                case ThresholdLimitsandPriceBandVariation:
                    if (this.statisticsHandler != null)
                        this.statisticsHandler.updateThresholdLimitsAndPriceBandVariation(mdpGroupObj);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        if (this.multipleDepthBookHandler != null)
            this.channelContext.notifyBookFullRefresh(this.multipleDepthBookHandler);
        if (this.impliedBookHandler != null) this.channelContext.notifyImpliedBookFullRefresh(this.impliedBookHandler);
    }

    public void handleIncrementalRefreshEntry(final FieldSet incrementEntry) {
        if (!this.enabled) return;

        final MDEntryType mdEntryType = MDEntryType.fromFIX(incrementEntry.getChar(269));
        switch (mdEntryType) {
            case Bid:
                if (this.multipleDepthBookHandler != null)
                    this.multipleDepthBookHandler.handleIncrementBidEntry(incrementEntry);
                break;
            case Offer:
                if (this.multipleDepthBookHandler != null)
                    this.multipleDepthBookHandler.handleIncrementOfferEntry(incrementEntry);
                break;
            case Trade:
                if (this.tradeHandler != null) this.tradeHandler.updateTradeSummary(incrementEntry);
                break;
            case OpeningPrice:
                if (this.statisticsHandler != null) this.statisticsHandler.updateOpeningPrice(incrementEntry);
                break;
            case SettlementPrice:
                if (this.statisticsHandler != null) this.statisticsHandler.updateSettlementPrice(incrementEntry);
                break;
            case TradingSessionHighPrice:
                if (this.statisticsHandler != null)
                    this.statisticsHandler.updateTradingSessionHighPrice(incrementEntry);
                break;
            case TradingSessionLowPrice:
                if (this.statisticsHandler != null) this.statisticsHandler.updateTradingSessionLowPrice(incrementEntry);
                break;
            case TradeVolume:
                if (this.statisticsHandler != null) this.statisticsHandler.updateTradeVolume(incrementEntry);
                break;
            case OpenInterest:
                if (this.statisticsHandler != null) this.statisticsHandler.updateOpenInterest(incrementEntry);
                break;
            case ImpliedBid:
                if (this.impliedBookHandler != null) this.impliedBookHandler.handleIncrementBidEntry(incrementEntry);
                break;
            case ImpliedOffer:
                if (this.impliedBookHandler != null) this.impliedBookHandler.handleIncrementOfferEntry(incrementEntry);
                break;
            case EmptyBook:
                clearBooks();
                break;
            case SessionHighBid:
                if (this.statisticsHandler != null) this.statisticsHandler.updateSessionHighBid(incrementEntry);
                break;
            case SessionLowOffer:
                if (this.statisticsHandler != null) this.statisticsHandler.updateSessionLowOffer(incrementEntry);
                break;
            case FixingPrice:
                if (this.statisticsHandler != null) this.statisticsHandler.updateFixingPrice(incrementEntry);
                break;
            case ElectronicVolume:
                if (this.tradeHandler != null) this.tradeHandler.updateElectronicVolume(incrementEntry);
                break;
            case ThresholdLimitsandPriceBandVariation:
                if (this.statisticsHandler != null)
                    this.statisticsHandler.updateThresholdLimitsAndPriceBandVariation(incrementEntry);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void clearBooks() {
        if (this.multipleDepthBookHandler != null) this.multipleDepthBookHandler.clear();
        if (this.impliedBookHandler != null) this.impliedBookHandler.clear();
    }

    void commitEvent() {
        if (!this.enabled) return;

        if (this.multipleDepthBookHandler != null) this.multipleDepthBookHandler.commitEvent();
        if (this.impliedBookHandler != null) this.impliedBookHandler.commitEvent();
    }
}
