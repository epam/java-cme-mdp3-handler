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

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;

public enum SecurityTradingStatus {
    TradingHalt((short) 2),
    Close((short) 4),
    NewPriceIndication((short) 15),
    ReadyToTrade((short) 17),
    NotAvailableForTrading((short) 18),
    UnknownOrInvalid((short) 20),
    PreOpen((short) 21),
    PreCross((short) 24),
    Cross((short) 25),
    PostClose((short) 26),
    NoChange((short) 103);

    final short value;

    SecurityTradingStatus(final short value) {
        this.value = value;
    }

    public static SecurityTradingStatus fromFIX(final short val) {
        switch (val) {
            case 2:
                return TradingHalt;
            case 4:
                return Close;
            case 15:
                return NewPriceIndication;
            case 17:
                return ReadyToTrade;
            case 18:
                return NotAvailableForTrading;
            case 20:
                return UnknownOrInvalid;
            case 21:
                return PreOpen;
            case 24:
                return PreCross;
            case 25:
                return Cross;
            case 26:
                return PostClose;
            case 103:
                return NoChange;
            default:
                if (SbePrimitiveType.UInt8.isNull(val)) return null;
                else throw new IllegalStateException("Not implemented value: " + val);
        }
    }
}
