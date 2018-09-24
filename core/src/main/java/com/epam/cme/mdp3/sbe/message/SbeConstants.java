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

package com.epam.cme.mdp3.sbe.message;

public class SbeConstants {
    private SbeConstants() {
    }

    public final static int MDP_PACKET_MAX_SIZE = 32768;
    public final static int MESSAGE_SEQ_NUM_OFFSET = 0;
    public final static int MESSAGE_SENDING_TIME_OFFSET = 4;
    public final static int MDP_HEADER_SIZE = 12;

    public final static int HEADER_SIZE = 10;
    public final static int BLOCK_LENGTH_OFFSET = 2;

    public final static int MSG_SIZE_OFFSET = 0;
    public final static int TEMPLATE_ID_OFFSET = 4;
    public final static int VERSION_OFFSET = 6;
    public final static int RESERVED_OFFSET = 7;
    public final static int MATCHEVENTINDICATOR_TAG = 5799;
    public final static int TRANSACT_TIME_TAG = 60;
}
