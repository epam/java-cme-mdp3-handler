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

import com.epam.cme.mdp3.sbe.message.SbeBuffer;
import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;

/**
 * Interface to modify Group Entry. Usually not available via the most public API
 */
public interface MutableMdpGroupEntry extends MdpGroupEntry {
    void reset(final SbeBuffer buffer, final SbeGroupType sbeGroupType, final int entryOffset, final int blockLength);
}
