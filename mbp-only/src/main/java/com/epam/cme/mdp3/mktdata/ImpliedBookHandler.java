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

package com.epam.cme.mdp3.mktdata;

import com.epam.cme.mdp3.FieldSet;
import com.epam.cme.mdp3.MdEventFlags;
import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.mktdata.enums.MDUpdateAction;

public class ImpliedBookHandler extends AbstractOrderBookHandler<ImpliedBookPriceEntry> implements ImpliedBook {
    private boolean subscribedToTop = false;
    private boolean subscribedToEntireBook = false;
    private final ImpliedBookPriceEntry[] bidLevels;
    private final ImpliedBookPriceEntry[] offerLevels;

    public ImpliedBookHandler(final ChannelContext channelContext, final int securityId, final int subscriptionFlags) {
        super(channelContext, securityId, subscriptionFlags);
        setSubscriptionFlags(subscriptionFlags);
        bidLevels = new ImpliedBookPriceEntry[PLATFORM_IMPLIED_BOOK_DEPTH];
        offerLevels = new ImpliedBookPriceEntry[PLATFORM_IMPLIED_BOOK_DEPTH];
        init();
    }

    private void init() {
        for (int i = 0; i < PLATFORM_IMPLIED_BOOK_DEPTH; i++) {
            bidLevels[i] = new OrderBookPriceEntry();
            offerLevels[i] = new OrderBookPriceEntry();
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < PLATFORM_IMPLIED_BOOK_DEPTH; i++) {
            bidLevels[i].clear();
            offerLevels[i].clear();
        }
        refreshedTop = false;
        refreshedBook = false;
    }

    @Override
    public void setSubscriptionFlags(final int subscriptionFlags) {
        super.setSubscriptionFlags(subscriptionFlags);
        this.subscribedToEntireBook = MdEventFlags.hasImpliedBook(this.subscriptionFlags);
        this.subscribedToTop = MdEventFlags.hasImpliedTop(this.subscriptionFlags);
    }

    public void handleSnapshotBidEntry(final MdpGroup snptGroup) {
        final byte level = snptGroup.getInt8(1023);
        if (level > 1 && !subscribedToEntireBook) return;
        final ImpliedBookPriceEntry bookPriceLevel = (ImpliedBookPriceEntry) getBid(level);
        bookPriceLevel.refreshFromMessage(snptGroup);
    }

    public void handleSnapshotOfferEntry(final MdpGroup snptGroup) {
        final byte level = snptGroup.getInt8(1023);
        if (level > 1 && !subscribedToEntireBook) return;
        final ImpliedBookPriceEntry bookPriceLevel = (ImpliedBookPriceEntry) getOffer(level);
        bookPriceLevel.refreshFromMessage(snptGroup);
    }

    public void handleIncrementBidEntry(final FieldSet incrementEntry) {
        final byte level = (byte) incrementEntry.getUInt8(1023);
        if (level > 1 && !subscribedToEntireBook) return;
        this.refreshedBook = true;
        if (level == 1) this.refreshedTop = true;
        final MDUpdateAction updateAction = MDUpdateAction.fromFIX(incrementEntry.getUInt8(279));
        handleIncrementRefresh(bidLevels, level, updateAction, incrementEntry);
    }

    public void handleIncrementOfferEntry(final FieldSet incrementEntry) {
        final byte level = (byte) incrementEntry.getUInt8(1023);
        if (level > 1 && !subscribedToEntireBook) return;
        this.refreshedBook = true;
        if (level == 1) this.refreshedTop = true;
        final MDUpdateAction updateAction = MDUpdateAction.fromFIX(incrementEntry.getUInt8(279));
        handleIncrementRefresh(offerLevels, level, updateAction, incrementEntry);
    }

    protected void deleteEntry(final ImpliedBookPriceEntry[] levelEntries, final int level) {
        for (int i = level - 1; i < ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH - 1; i++) {
            levelEntries[i].refreshFromAnotherEntry(levelEntries[i + 1]);
        }
        levelEntries[ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH - 1].clear();
    }


    protected void deleteFrom(final ImpliedBookPriceEntry[] levelEntries, final int n) {
        if (n >= ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH) deleteThru(levelEntries);
        for (int i = n; i < PLATFORM_IMPLIED_BOOK_DEPTH; i++) {
            levelEntries[i - n].refreshFromAnotherEntry(levelEntries[i]);
        }
        for (int i = ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH - n; i < ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH; i++) {
            levelEntries[i].clear();
        }
    }

    protected void deleteThru(final ImpliedBookPriceEntry[] levelEntries) {
        for (int i = 0; i < ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH; i++) {
            levelEntries[i].clear();
        }
    }

    protected void insertEntry(final ImpliedBookPriceEntry[] levelEntries, final int level, final FieldSet fieldSet) {
        for (int i = ImpliedBook.PLATFORM_IMPLIED_BOOK_DEPTH - 1; i > level - 1; i--) {
            levelEntries[i].refreshFromAnotherEntry(levelEntries[i - 1]);
        }
        levelEntries[level - 1].refreshFromMessage(fieldSet);
    }

    @Override
    protected void modifyEntry(ImpliedBookPriceEntry[] levelEntries, int level, FieldSet fieldSet) {
        levelEntries[level - 1].refreshFromMessage(fieldSet);
    }

    public boolean isSubscribedToTop() {
        return subscribedToTop;
    }

    public boolean isSubscribedToEntireBook() {
        return subscribedToEntireBook;
    }

    @Override
    public ImpliedBookPriceLevel getBid(final byte level) {
        return bidLevels[level - 1];
    }

    @Override
    public ImpliedBookPriceLevel getOffer(final byte level) {
        return offerLevels[level - 1];
    }

    public void commitEvent() {
        if (subscribedToTop && refreshedTop) channelContext.notifyImpliedTopOfBookRefresh(this);
        if (subscribedToEntireBook && refreshedBook) channelContext.notifyImpliedBookRefresh(this);
        refreshedTop = false;
        refreshedBook = false;
    }
}
