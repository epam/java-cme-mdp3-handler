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
import com.epam.cme.mdp3.core.channel.ChannelContext;

// implementation is not complete
public class StatisticsHandler extends AbstractMktDataHandler {
    private final static short FLAG_DAILYOPENPRICE = 0;
    private final static short FLAG_INDICATIVEOPENINGPRICE = 5;

    private final Price openingPrice = new Price();
    private final Price fixingPrice = new Price();
    private final Price indicativeOpeningPrice = new Price();
    private final Price sessionHighBid = new Price();
    private final Price sessionLowOffer = new Price();
    private final Price sessionHighPrice = new Price();
    private final Price sessionLowPrice = new Price();
    private int openInterest = 0;
    private int clearedVolume = 0;

    public StatisticsHandler(final ChannelContext channelContext, final int securityId, final int subscriptionFlags) {
        super(channelContext, securityId, subscriptionFlags);
        this.subscriptionFlags = subscriptionFlags;
    }

    @Override
    public void setSubscriptionFlags(int subscriptionFlags) {
        this.subscriptionFlags = subscriptionFlags;
    }

    public void updateOpeningPrice(final FieldSet incrementEntry) {
        final short openCloseSettlFlag = incrementEntry.getUInt8(286);
        if (openCloseSettlFlag == FLAG_DAILYOPENPRICE) {
            this.openingPrice.setMantissa(incrementEntry.getInt64(270));
        } else if (openCloseSettlFlag == FLAG_INDICATIVEOPENINGPRICE) {
            this.indicativeOpeningPrice.setMantissa(incrementEntry.getInt64(270));
        }
    }

    public void updateSettlementPrice(final FieldSet incrementEntry) {
        throw new UnsupportedOperationException();
    }

    public void updateTradingSessionHighPrice(final FieldSet incrementEntry) {
        this.sessionHighPrice.setMantissa(incrementEntry.getInt64(270));
    }

    public void updateTradingSessionLowPrice(final FieldSet incrementEntry) {
        this.sessionLowPrice.setMantissa(incrementEntry.getInt64(270));
    }

    public void updateTradeVolume(final FieldSet incrementEntry) {
        this.clearedVolume = incrementEntry.getInt32(271);
    }

    public void updateOpenInterest(final FieldSet incrementEntry) {
        this.openInterest = incrementEntry.getInt32(271);
    }

    public void updateSessionHighBid(final FieldSet incrementEntry) {
        this.sessionHighBid.setMantissa(incrementEntry.getInt64(270));
    }

    public void updateSessionLowOffer(final FieldSet incrementEntry) {
        this.sessionLowOffer.setMantissa(incrementEntry.getInt64(270));
    }

    public void updateFixingPrice(final FieldSet incrementEntry) {
        this.fixingPrice.setMantissa(incrementEntry.getInt64(270));
    }

    public void updateThresholdLimitsAndPriceBandVariation(final FieldSet incrementEntry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
