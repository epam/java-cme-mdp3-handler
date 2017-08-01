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
 * High level subscription options to market data and events
 */
public class MdEventFlags {
    public static final int NOTHING = 0;
    public static final int MESSAGE = 1;
    public static final int BOOK = 1 << 10;
    public static final int TOP = 1 << 11;
    public static final int IMPLIEDBOOK = 1 << 12;
    public static final int IMPLIEDTOP = 1 << 13;
    public static final int CONSOLIDATEDBOOK = 1 << 14;
    public static final int CONSOLIDATEDTOP = 1 << 15;
    public static final int TRADESUMMARY = 1 << 16;
    public static final int STATISTICS = 1 << 17;
    public static final int ALL_OPTS = BOOK | TOP | IMPLIEDBOOK | IMPLIEDTOP | CONSOLIDATEDBOOK | CONSOLIDATEDTOP |
            TRADESUMMARY | STATISTICS;

    private MdEventFlags() {
    }

    /**
     * Checks if the given flags have no subscriptions at all
     * @param flags a high level subscription flags (bit set)
     * @return true if no subscriptions
     */
    public static boolean isNothing(final int flags) {
        return flags == NOTHING;
    }

    public static boolean hasMessage(final int flags) {
        return hasFlag(flags, MESSAGE);
    }

    /**
     * Checks if the given flags have subscription to order book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to order book
     */
    public static boolean hasBook(final int flags) {
        return hasFlag(flags, BOOK);
    }

    /**
     * Checks if the given flags have subscription to the top of the order book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the top of the order book
     */
    public static boolean hasTop(final int flags) {
        return hasFlag(flags, TOP);
    }

    /**
     * Checks if the given flags have subscription to the implied order book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the implied order book
     */
    public static boolean hasImpliedBook(final int flags) {
        return hasFlag(flags, IMPLIEDBOOK);
    }

    /**
     * Checks if the given flags have subscription to the top of the implied book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the top of the implied book
     */
    public static boolean hasImpliedTop(final int flags) {
        return hasFlag(flags, IMPLIEDTOP);
    }

    /**
     * Checks if the given flags have subscription to consolidated book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to consolidated book
     */
    public static boolean hasConsolidatedBook(final int flags) {
        return hasFlag(flags, CONSOLIDATEDBOOK);
    }

    /**
     * Checks if the given flags have subscription to the top of the consolidated book
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the top of the consolidated book
     */
    public static boolean hasConsolidatedTop(final int flags) {
        return hasFlag(flags, CONSOLIDATEDTOP);
    }

    /**
     * Checks if the given flags have subscription to the trade summaries
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the trade summaries
     */
    public static boolean hasTradeSummary(final int flags) {
        return hasFlag(flags, TRADESUMMARY);
    }

    /**
     * Checks if the given flags have subscription to the statistics
     * @param flags the given high level subscription flags (bit set)
     * @return true if flags contain subscription to the statistics
     */
    public static boolean hasStatistics(final int flags) {
        return hasFlag(flags, STATISTICS);
    }

    /**
     * Checks if the given flags have specific subscription type
     * @param flags the given high level subscription flags (bit set)
     * @param flag the given specific subscription type
     * @return true if flags contain subscription to order book
     */
    public static boolean hasFlag(final int flags, final int flag) {
        return (flags & flag) == flag;
    }
}
