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

package com.epam.cme.mdp3.test.mbpbook;

import com.epam.cme.mdp3.FieldSet;
import com.epam.cme.mdp3.MdEventFlags;
import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.mktdata.enums.MDUpdateAction;

public class MultipleDepthBookHandler extends AbstractOrderBookHandler<OrderBookPriceEntry> implements OrderBook {
//    private boolean subscribedToTop = false;
//    private boolean subscribedToEntireBook = false;
    private final OrderBookPriceEntry[] bidLevels;
    private final OrderBookPriceEntry[] offerLevels;
    private final byte depth;

    public MultipleDepthBookHandler(final int securityId, final int subscriptionFlags, final byte depth) {
        super(securityId, subscriptionFlags);
        setSubscriptionFlags(subscriptionFlags);
        this.depth = depth;
        bidLevels = new OrderBookPriceEntry[depth];
        offerLevels = new OrderBookPriceEntry[depth];
        init();
    }

    private void init() {
        for (int i = 0; i < depth; i++) {
            bidLevels[i] = new OrderBookPriceEntry();
            offerLevels[i] = new OrderBookPriceEntry();
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < depth; i++) {
            bidLevels[i].clear();
            offerLevels[i].clear();
        }
//        refreshedTop = false;
//        refreshedBook = false;
    }

    @Override
    public void setSubscriptionFlags(final int subscriptionFlags) {
        super.setSubscriptionFlags(subscriptionFlags);
//        this.subscribedToEntireBook = MdEventFlags.hasBook(this.subscriptionFlags);
//        this.subscribedToTop = MdEventFlags.hasTop(this.subscriptionFlags);
    }

    public void handleSnapshotBidEntry(final MdpGroup snptGroup) {
        byte level = snptGroup.getInt8(1023);
        if (level > depth) return;
        final OrderBookPriceEntry bookPriceLevel = (OrderBookPriceEntry) getBid(level);
        bookPriceLevel.refreshBookFromMessage(snptGroup);
    }

    public void handleSnapshotOfferEntry(final MdpGroup snptGroup) {
        byte level = snptGroup.getInt8(1023);
        if (level > depth) return;
        final OrderBookPriceEntry bookPriceLevel = (OrderBookPriceEntry) getOffer(level);
        bookPriceLevel.refreshBookFromMessage(snptGroup);
    }

    public void handleIncrementBidEntry(final FieldSet incrementEntry) {
        final byte level = (byte) incrementEntry.getUInt8(1023);
        if (level > depth) return;
        final MDUpdateAction updateAction = MDUpdateAction.fromFIX(incrementEntry.getUInt8(279));
        handleIncrementRefresh(bidLevels, level, updateAction, incrementEntry);
    }

    public void handleIncrementOfferEntry(final FieldSet incrementEntry) {
        final byte level = (byte) incrementEntry.getUInt8(1023);
        if (level > depth) return;
        final MDUpdateAction updateAction = MDUpdateAction.fromFIX(incrementEntry.getUInt8(279));
        handleIncrementRefresh(offerLevels, level, updateAction, incrementEntry);
    }

    protected void deleteEntry(final OrderBookPriceEntry[] levelEntries, final int level) {
        for (int i = level - 1; i < depth - 1; i++) {
            levelEntries[i].refreshFromAnotherEntry(levelEntries[i + 1]);
        }
        levelEntries[depth - 1].clear();
    }

    protected void deleteFrom(final OrderBookPriceEntry[] levelEntries, final int n) {
        if (n >= depth) deleteThru(levelEntries);
        for (int i = n; i < depth; i++) {
            levelEntries[i - n].refreshFromAnotherEntry(levelEntries[i]);
        }
        for (int i = depth - n; i < depth; i++) {
            levelEntries[i].clear();
        }
    }

    protected void deleteThru(final OrderBookPriceEntry[] levelEntries) {
        for (int i = 0; i < depth; i++) {
            levelEntries[i].clear();
        }
    }

    protected void insertEntry(final OrderBookPriceEntry[] levelEntries, final int level, final FieldSet fieldSet) {
        for (int i = depth - 1; i > level - 1; i--) {
            levelEntries[i].refreshFromAnotherEntry(levelEntries[i - 1]);
        }
        levelEntries[level - 1].refreshBookFromMessage(fieldSet);
    }

    @Override
    protected void modifyEntry(OrderBookPriceEntry[] levelEntries, int level, FieldSet fieldSet) {
        levelEntries[level - 1].refreshBookFromMessage(fieldSet);
    }

    @Override
    public byte getDepth() {
        return this.depth;
    }

    @Override
    public OrderBookPriceLevel getBid(final byte level) {
        return bidLevels[level - 1];
    }

    @Override
    public OrderBookPriceLevel getOffer(final byte level) {
        return offerLevels[level - 1];
    }
}
