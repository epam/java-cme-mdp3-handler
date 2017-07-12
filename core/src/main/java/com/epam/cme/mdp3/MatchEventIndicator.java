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

/**
 * <p>MDE Event Indicator.</p>
 * @see <a href="http://www.cmegroup.com/confluence/display/EPICSANDBOX/MDP+3.0+-+Market+Data+Incremental+Refresh">MDP 3.0 - Market Data Incremental Refresh</a>
 * <p>
 * Bitmap field of eight Boolean type indicators reflecting the end of updates for a given CME Globex Event:
 * <ul>
 * <li>
 * Bit 0: (least significant bit) Last Trade Summary message for a given event
 * </li>
 * <li>
 * Bit 1: Last electronic volume message for a given event
 * </li>
 * <li>
 * Bit 2: Last real quote message for a given event
 * </li>
 * <li>
 * Bit 3: Last statistic message for a given event
 * </li>
 * <li>
 * Bit 4: Last implied quote message for a given event
 * </li>
 * <li>
 * Bit 5: Message resent during recovery
 * </li>
 * <li>
 * Bit 6: Reserved for future use
 * </li>
 * <li>
 * Bit 7: (most significant bit) Last message for a given event
 * </li>
 * </ul>
 */
public class MatchEventIndicator {
    public static final short NOTHING = 0;
    public static final short LASTTRADEMSG = 1;
    public static final short LASTVOLUMEMSG = 1 << 1;
    public static final short LASTQUOTEMSG = 1 << 2;
    public static final short LASTSTATSMSG = 1 << 3;
    public static final short LASTIMPLIEDMSG = 1 << 4;
    public static final short RECOVERYMSG = 1 << 5;
    public static final short RESERVED = 1 << 6;
    public static final short ENDOFEVENT = 1 << 7;

    private MatchEventIndicator() {
    }

    public static boolean hasIndicator(final int flags, final int flag) {
        return (flags & flag) == flag;
    }

    public static boolean isNothing(final short indicator) {
        return indicator == NOTHING;
    }

    public static boolean hasLastTradeMsg(final short indicator) {
        return hasIndicator(indicator, LASTTRADEMSG);
    }

    public static boolean hasLastVolumeMsg(final short indicator) {
        return hasIndicator(indicator, LASTVOLUMEMSG);
    }

    public static boolean hasLastQuoteMsg(final short indicator) {
        return hasIndicator(indicator, LASTQUOTEMSG);
    }

    public static boolean hasLastStatsMsg(final short indicator) {
        return hasIndicator(indicator, LASTSTATSMSG);
    }

    public static boolean hasLastImpliedMsg(final short indicator) {
        return hasIndicator(indicator, LASTIMPLIEDMSG);
    }

    public static boolean hasRecoveryMsg(final short indicator) {
        return hasIndicator(indicator, RECOVERYMSG);
    }

    public static boolean hasReserved(final short indicator) {
        return hasIndicator(indicator, RESERVED);
    }

    public static boolean hasEndOfEvent(final short indicator) {
        return hasIndicator(indicator, ENDOFEVENT);
    }
}
