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

public class SettlPriceType {
    private SettlPriceType() {
    }

    public static final byte NOTHING = 0;
    public static final byte FINAL = (byte) Math.pow(2, 0);
    public static final byte ACTUAL = (byte) Math.pow(2, 1);
    public static final byte ROUNDED = (byte) Math.pow(2, 2);
    public static final byte INTRADAY = (byte) Math.pow(2, 3);
    public static final byte NULLVALUE = (byte) Math.pow(2, 7);


    public static boolean hasIndicator(final int flags, final int flag) {
        return (flags & flag) == flag;
    }

    public static boolean isNothing(final byte indicator) {
        return indicator == NOTHING;
    }

    public static boolean hasFinal(final byte indicator) {
        return hasIndicator(indicator, FINAL);
    }

    public static boolean hasActual(final byte indicator) {
        return hasIndicator(indicator, ACTUAL);
    }

    public static boolean hasRounded(final byte indicator) {
        return hasIndicator(indicator, ROUNDED);
    }

    public static boolean hasIntraday(final byte indicator) {
        return hasIndicator(indicator, INTRADAY);
    }

    public static boolean hasNullValue(final byte indicator) {
        return hasIndicator(indicator, NULLVALUE);
    }
}
