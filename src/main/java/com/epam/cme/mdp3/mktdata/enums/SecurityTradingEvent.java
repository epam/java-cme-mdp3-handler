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

public enum SecurityTradingEvent {
    NoEvent((short) 0),
    NoCancel((short) 1),
    ResetStatistics((short) 4),
    ImpliedMatchingON((short) 5),
    ImpliedMatchingOFF((short) 6);

    final short value;

    SecurityTradingEvent(final short value) {
        this.value = value;
    }

    public static SecurityTradingEvent fromFIX(final byte val) {
        switch (val) {
            case 1:
                return NoEvent;
            case 2:
                return NoCancel;
            case 4:
                return ResetStatistics;
            case 5:
                return ImpliedMatchingON;
            case 6:
                return ImpliedMatchingOFF;
            default:
                throw new IllegalStateException("Not implemented value: " + val);
        }
    }

}
