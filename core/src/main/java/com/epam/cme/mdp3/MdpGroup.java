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
 * Interface to MDP Group.
 */
public interface MdpGroup extends FieldSet {
    /**
     * Gets total number of entries in MDP this Group.
     *
     * @return Number of entries
     */
    int getNumInGroup();

    /**
     * Gets number of current entry in MDP this Group.
     *
     * @return number of current entry
     */
    int getEntryNum();

    /**
     * Check if the latest entry in this Group is reached.
     *
     * @return true if not latest entry
     */
    boolean hashNext();

    /**
     * Moves to next entry in this MDP Group.
     */
    void next();

    /**
     * Copies data of current MDP Group entry to the given instance.
     *
     * @param groupEntry MDP Group Entry
     */
    void getEntry(MdpGroupEntry groupEntry);

    /**
     * Copies data of defined MDP Group entry to the given instance.
     *
     * @param entryNum number of entry
     * @param groupEntry MDP Group Entry
     */
    void getEntry(int entryNum, MdpGroupEntry groupEntry);

    /**
     * Creates copy of the current Mdp Group
     *
     * @return a copy instance of the of the current Mdp Group
     */
    @Override
    MdpGroup copy();
}
