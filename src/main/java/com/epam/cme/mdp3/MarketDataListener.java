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

package com.epam.cme.mdp3;

import com.epam.cme.mdp3.mktdata.*;
import com.epam.cme.mdp3.mktdata.enums.*;
import com.epam.cme.mdp3.sbe.message.SbeString;

/**
 * High-level Market Data listener
 */
public interface MarketDataListener {
    /**
     * Called when top of the Implied Book is refreshed.
     *
     * @param channelId  ID of MDP Channel
     * @param securityId Security ID
     * @param bid        Bid part of top level
     * @param offer      Ask part of top level
     */
    void onTopOfImpliedBookRefresh(final String channelId, final int securityId, final ImpliedBookPriceLevel bid, final ImpliedBookPriceLevel offer);

    /**
     * Called when top of the Regular Book is refreshed.
     *
     * @param channelId  ID of MDP Channel
     * @param securityId Security ID
     * @param bid        Bid part of top level
     * @param offer      Ask part of top level
     */
    void onTopOfBookRefresh(final String channelId, final int securityId, final OrderBookPriceLevel bid, final OrderBookPriceLevel offer);

    /**
     * Called when a Implied Book is refreshed.
     *
     * @param channelId   ID of MDP Channel
     * @param securityId  Security ID
     * @param impliedBook
     */
    void onImpliedBookRefresh(final String channelId, final int securityId, final ImpliedBook impliedBook);

    /**
     * Called when an entire Implied Book is refreshed from Snapshot Full Refresh Message.
     *
     * @param channelId   ID of MDP Channel
     * @param securityId  Security ID
     * @param impliedBook
     */
    void onImpliedBookFullRefresh(final String channelId, final int securityId, final ImpliedBook impliedBook);

    /**
     * Called when a Regular Book is refreshed.
     *
     * @param channelId  ID of MDP Channel
     * @param securityId Security ID
     * @param orderBook
     */
    void onOrderBookRefresh(final String channelId, final int securityId, final OrderBook orderBook);

    /**
     * Called when an entire Regular Book is refreshed from Snapshot Full Refresh Message.
     *
     * @param channelId  ID of MDP Channel
     * @param securityId Security ID
     * @param orderBook
     */
    void onOrderBookFullRefresh(final String channelId, final int securityId, final OrderBook orderBook);

    /**
     * Called when Security Statistics is refreshed.
     *
     * @param channelId  ID of MDP Channel
     * @param securityId Security ID
     * @param statistics Security Statistics
     */
    void onStatisticsRefresh(final String channelId, final int securityId, final SecurityStatistics statistics);

    /**
     * Called when an RFQ is received.
     *
     * @param channelId  ID of MDP Channel
     * @param quoReqId   Quote Reqiest ID
     * @param entryIdx   Entry Index (in RFQ group)
     * @param entryNum   Number of entries in RFQ group
     * @param securityId Security ID
     * @param quoteType  Quote Type
     * @param orderQty   Order quantity
     * @param side       Order side
     */
    void onRequestForQuote(final String channelId, final SbeString quoReqId, final int entryIdx, final int entryNum,
                           final int securityId, final QuoteType quoteType, final int orderQty, final Side side);

    /**
     * Called when Security Status update is received.
     *
     * @param channelId           ID of MDP Channel
     * @param secGroup            Security Group
     * @param secAsset            Asset
     * @param securityId          Security ID
     * @param tradeDate           Trade Date
     * @param matchEventIndicator MDP Event indicator (bitset)
     * @param secTrdStatus        Security Trading Status
     * @param haltRsn             Halt Reason
     * @param secTrdEvnt          Security Trading Event
     */
    void onSecurityStatus(final String channelId, final SbeString secGroup, final SbeString secAsset, final int securityId, final int tradeDate,
                          final short matchEventIndicator, final SecurityTradingStatus secTrdStatus, final HaltReason haltRsn, final SecurityTradingEvent secTrdEvnt);
}
