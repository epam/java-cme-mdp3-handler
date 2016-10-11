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
import com.epam.cme.mdp3.core.control.InstrumentState;
import com.epam.cme.mdp3.core.control.PacketQueue;
import com.epam.cme.mdp3.mktdata.ImpliedBook;
import com.epam.cme.mdp3.mktdata.MdConstants;
import com.epam.cme.mdp3.mktdata.OrderBook;
import com.epam.cme.mdp3.mktdata.enums.*;
import com.epam.cme.mdp3.sbe.message.SbeString;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import java.util.List;
import java.util.Set;

public class ChannelContext {
    private final MdpChannelImpl channel;
    private final MdpMessageTypes mdpMessageTypes;
    private int gapThreshold;

    public ChannelContext(final MdpChannelImpl channel, final MdpMessageTypes mdpMessageTypes, final int gapThreshold) {
        this.channel = channel;
        this.mdpMessageTypes = mdpMessageTypes;
        this.gapThreshold = gapThreshold;
    }

    public MdpChannelImpl getChannel() {
        return channel;
    }

    public InstrumentController findInstrumentController(final int securityId, final String secDesc) {
        return channel.findController(securityId, secDesc);
    }

    public ChannelInstruments getInstruments() {
        return channel.instruments;
    }

    public MdpMessageTypes getMdpMessageTypes() {
        return mdpMessageTypes;
    }

    public boolean hasMdListeners() {
        return this.getChannel().hasMdListener();
    }

    public boolean isSnapshotFeedsActive() {
        return channel.isSnapshotFeedsActive();
    }

    public int getGapThreshold() {
        return gapThreshold;
    }

    public void setGapThreshold(int gapThreshold) {
        this.gapThreshold = gapThreshold;
    }

    public int notifySecurityDefinitionListeners(final MdpMessage mdpMessage) {
        int flags = MdEventFlags.NOTHING;
        final List<ChannelListener> listeners = this.channel.getListeners();

        for (int i = 0; i < listeners.size(); i++) {
            final int clbFlags = listeners.get(i).onSecurityDefinition(this.channel.getId(), mdpMessage);
            flags |= clbFlags;
        }
        return flags;
    }

    public void notifyFeedStartedListeners(final FeedType feedType, final Feed feed) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onFeedStarted(this.channel.getId(), feedType, feed);
        }
    }

    public void notifyFeedStoppedListeners(final FeedType feedType, final Feed feed) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onFeedStopped(this.channel.getId(), feedType, feed);
        }
    }

    public void notifyImpliedBookRefresh(final ImpliedBook impliedBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onImpliedBookRefresh(this.channel.getId(), impliedBook.getSecurityId(), impliedBook);
        }
    }

    public void notifyImpliedBookFullRefresh(final ImpliedBook impliedBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onImpliedBookFullRefresh(this.channel.getId(), impliedBook.getSecurityId(), impliedBook);
        }
    }

    public void notifyImpliedTopOfBookRefresh(final ImpliedBook impliedBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onTopOfImpliedBookRefresh(this.channel.getId(), impliedBook.getSecurityId(),
                    impliedBook.getBid(MdConstants.TOP_OF_THE_BOOK_LEVEL), impliedBook.getOffer(MdConstants.TOP_OF_THE_BOOK_LEVEL));
        }
    }

    public void notifyBookRefresh(final OrderBook orderBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onOrderBookRefresh(this.channel.getId(), orderBook.getSecurityId(), orderBook);
        }
    }

    public void notifyBookFullRefresh(final OrderBook orderBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onOrderBookFullRefresh(this.channel.getId(), orderBook.getSecurityId(), orderBook);
        }
    }

    public void notifyTopOfBookRefresh(final OrderBook orderBook) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onTopOfBookRefresh(this.channel.getId(), orderBook.getSecurityId(),
                    orderBook.getBid(MdConstants.TOP_OF_THE_BOOK_LEVEL), orderBook.getOffer(MdConstants.TOP_OF_THE_BOOK_LEVEL));
        }
    }

    public void notifyIncrementalRefreshListeners(final short matchEventIndicator, final int securityId, final String secDesc, final long msgSeqNum, final FieldSet mdpGroupEntry) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onIncrementalRefresh(this.channel.getId(), matchEventIndicator, securityId, secDesc, msgSeqNum, mdpGroupEntry);
        }
    }

    public void notifySnapshotFullRefreshListeners(final String secDesc, final MdpMessage mdpMessage) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onSnapshotFullRefresh(this.channel.getId(), secDesc, mdpMessage);
        }
    }


    public void notifyChannelResetListeners(final MdpMessage mdpMessage) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onBeforeChannelReset(this.channel.getId(), mdpMessage);
        }
    }

    public void notifyChannelResetFinishedListeners(final MdpMessage mdpMessage) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onFinishedChannelReset(this.channel.getId(), mdpMessage);
        }
    }

    public void notifyChannelStateListeners(final ChannelState prevState, final ChannelState newState) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onChannelStateChanged(channel.getId(), prevState, newState);
        }
    }

    public void notifyInstrumentStateListeners(final int securityId, final InstrumentState prevState, final InstrumentState newState) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onInstrumentStateChanged(channel.getId(), securityId, prevState, newState);
        }
    }

    public void notifyRequestForQuote(final SbeString quoReqId, final int entryIdx, final int entryNum,
                                      final int securityId, final QuoteType quoteType, final int orderQty, final Side side) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onRequestForQuote(this.channel.getId(), quoReqId, entryIdx, entryNum, securityId, quoteType, orderQty, side);
        }
    }

    public void notifySecurityStatus(final int securityId, final MdpMessage secStatusMessage) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onSecurityStatus(this.channel.getId(), securityId, secStatusMessage);
        }
    }

    public void notifyPacketReceived(final FeedType feedType, final Feed feed, final MdpPacket mdpPacket) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onPacket(this.channel.getId(), feedType, feed, mdpPacket);
        }
    }

    public void notifyRequestForQuote(final MdpMessage rfqMessage) {
        final List<ChannelListener> listeners = this.channel.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onRequestForQuote(this.channel.getId(), rfqMessage);
        }
    }

    public void notifySecurityStatus(final SbeString secGroup, final SbeString secAsset, final int securityId,
                                     final int tradeDate, final short matchEventIndicator, final SecurityTradingStatus secTrdStatus,
                                     final HaltReason haltRsn, final SecurityTradingEvent secTrdEvnt) {
        final List<MarketDataListener> mdListeners = this.channel.getMdListeners();
        for (int i = 0; i < mdListeners.size(); i++) {
            mdListeners.get(i).onSecurityStatus(this.channel.getId(), secGroup, secAsset, securityId, tradeDate, matchEventIndicator,
                    secTrdStatus, haltRsn, secTrdEvnt);
        }
    }

    public void stopInstrumentFeeds() {
        channel.stopInstrumentFeedA();
        channel.stopInstrumentFeedB();
    }

    public void stopSnapshotFeeds() {
        channel.stopSnapshotFeeds();
    }

    public void startSnapshotFeeds() {
        channel.startSnapshotFeeds();
    }

    public void subscribeToSnapshotsForInstrument(final Integer securityId) {
        channel.subscribeToSnapshotsForInstrument(securityId);
    }

    public void unsubscribeToSnapshotsForInstrument(final Integer securityId) {
        channel.unsubscribeFromSnapshotsForInstrument(securityId);
    }

    public PacketQueue getIncrementQueue() {
        return getChannel().getController().getIncrementQueue();
    }

    public long getPrcdSeqNum() {
        return getChannel().getController().getPrcdSeqNum();
    }

    public ChannelState getChannelState() {
        return this.channel.getState();
    }
}
