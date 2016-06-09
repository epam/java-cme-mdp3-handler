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
 * Market Data Application Messages, from business point of view.
 */
public enum SemanticMsgType {
    MarketDataIncrementalRefresh,
    MarketDataSnapshotFullRefresh,
    SecurityDefinition,
    SecurityStatus,
    QuoteRequest,
    Heartbeat,
    Logon,
    Logout;

    public static SemanticMsgType fromFixValue(final String fixValue) {
        switch (fixValue.charAt(0)) {
            case 'X':
                return MarketDataIncrementalRefresh;
            case 'W':
                return MarketDataSnapshotFullRefresh;
            case 'd':
                return SecurityDefinition;
            case 'f':
                return SecurityStatus;
            case 'R':
                return QuoteRequest;
            case '0':
                return Heartbeat;
            case 'A':
                return Logon;
            case '5':
                return Logout;
            default:
                throw new IllegalStateException();
        }
    }
}
