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

public interface MdConstants {
    byte TOP_OF_THE_BOOK_LEVEL = 1;

    int LAST_MSG_SEQ_NUM_PROCESSED = 369;
    int SECURITY_ID = 48;
    int SEC_DESC_TAG = 55;
    int RPT_SEQ_NUM = 83;
    int NO_MD_ENTRIES = 268;
    int INCR_RFRSH_MD_ENTRY_TYPE = 269;
    int INCR_RFRSH_MD_ACTION = 279;
    int INCR_RFRSH_MD_PRICE_LEVEL = 1023;
    int INCR_RFRSH_MD_ENTRY_PX = 270;
    int INCR_RFRSH_MD_ENTRY_SIZE = 271;
    int INCR_RFRSH_MD_ORDER_NUM = 346;
    int NO_ORDER_ID_ENTRIES = 37705;
    int NO_CHUNKS = 37709;
    int CURRENT_CHUNK = 37710;
    int TOT_NUM_REPORTS = 911;
    int REFERENCE_ID = 9633;
}
