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

package com.epam.cme.mdp3.mktdata.enums;

public enum MDEntryType {
    Bid('0'),
    Offer('1'),
    Trade('2'),
    OpeningPrice('4'),
    SettlementPrice('6'),
    TradingSessionHighPrice('7'),
    TradingSessionLowPrice('8'),
    TradeVolume('B'),
    OpenInterest('C'),
    ImpliedBid('E'),
    ImpliedOffer('F'),
    EmptyBook('J'),
    SessionHighBid('N'),
    SessionLowOffer('O'),
    FixingPrice('W'),
    ElectronicVolume('e'),
    ThresholdLimitsandPriceBandVariation('g');

    final char value;

    MDEntryType(final char value) {
        this.value = value;
    }

    public static MDEntryType fromFIX(final char val) {
        switch (val) {
            case '0':
                return Bid;
            case '1':
                return Offer;
            case '2':
                return Trade;
            case '4':
                return OpeningPrice;
            case '6':
                return SettlementPrice;
            case '7':
                return TradingSessionHighPrice;
            case '8':
                return TradingSessionLowPrice;
            case 'B':
                return TradeVolume;
            case 'C':
                return OpenInterest;
            case 'E':
                return ImpliedBid;
            case 'F':
                return ImpliedOffer;
            case 'J':
                return EmptyBook;
            case 'N':
                return SessionHighBid;
            case 'O':
                return SessionLowOffer;
            case 'W':
                return FixingPrice;
            case 'e':
                return ElectronicVolume;
            case 'g':
                return ThresholdLimitsandPriceBandVariation;
            default:
                throw new IllegalStateException("Not implemented value: " + val);
        }
    }
}
