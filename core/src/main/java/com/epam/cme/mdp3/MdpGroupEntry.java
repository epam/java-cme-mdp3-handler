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

import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;

/**
 * Interface to MDP Group Entry.
 */
public interface MdpGroupEntry extends FieldSet {
    /**
     * Gets index of beginning of entry in buffer.
     *
     * @return index
     */
    int getAbsoluteEntryOffset();

    /**
     * Gets SBE metadata of this MDP Group.
     *
     * @return
     */
    SbeGroupType getSbeGroupType();

    /**
     * Gets length of entry block.
     *
     * @return
     */
    int getBlockLength();

    /**
     * Creates copy of the current Mdp Group Entry
     *
     * @return a copy instance of the of the current Mdp Group Entry
     */
    @Override
    MdpGroupEntry copy();
}
