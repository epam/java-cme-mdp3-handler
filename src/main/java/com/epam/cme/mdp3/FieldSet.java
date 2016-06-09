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
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import com.epam.cme.mdp3.sbe.message.SbeMonthYear;
import com.epam.cme.mdp3.sbe.message.SbeString;

/**
 * Interface to Fields in MDP Message or Group.
 */
public interface FieldSet {
    /**
     * Gets SBE schema ID of the MDP Message.
     *
     * @return Template ID
     */
    int getSchemaId();

    /**
     * Gets back buffer.
     *
     * @return Back buffer
     */
    SbeBuffer buffer();

    /**
     * Allows to check if the given Field exists in the given MDP Message or Group.
     *
     * @param tagId Tag ID
     * @return True if exists else False
     */
    boolean hasField(final int tagId);

    /**
     * Allows to check if value of the given Field is NULL value.
     *
     * @param tagId Tag ID
     * @return True if exists else False
     */
    boolean isNull(final int tagId);

    /**
     * Gets char value of the given Field.
     *
     * @param tagId Tag ID
     * @return Char value
     */
    char getChar(final int tagId);

    /**
     * Gets uint8 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Uint8 value as short
     */
    short getUInt8(final int tagId);

    /**
     * Gets int8 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Int8 value as byte
     */
    byte getInt8(final int tagId);

    /**
     * Gets int16 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Int16 value as int
     */
    short getInt16(final int tagId);

    /**
     * Gets uint16 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Uint16 value as int
     */
    int getUInt16(final int tagId);

    /**
     * Gets int32 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Int32 value as int
     */
    int getInt32(final int tagId);

    /**
     * Gets uint32 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Uint32 value as long
     */
    long getUInt32(final int tagId);

    /**
     * Gets int64 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Int64 value as long
     */
    long getInt64(final int tagId);

    /**
     * Gets uint64 value of the given Field.
     *
     * @param tagId Tag ID
     * @return Uint64 value as long
     */
    long getUInt64(final int tagId);

    /**
     * Gets Double value of the given Field.
     *
     * @param tagId     Tag ID
     * @param doubleVal User application's Double instance
     * @return true if copies double value to user Double with success
     */
    boolean getDouble(final int tagId, final SbeDouble doubleVal);

    /**
     * Gets String value of the given Field.
     *
     * @param tagId     Tag ID
     * @param stringVal User application's String instance
     * @return true if copies String value to user String instance with success
     */
    boolean getString(final int tagId, final SbeString stringVal);

    /**
     * Gets char value of the given Field.
     *
     * @param tagId  Tag ID
     * @param dayVal User application's MonthYear instance
     * @return true if copies monthyear value to user instance with success
     */
    boolean getMonthYear(final int tagId, final SbeMonthYear dayVal);

    /**
     * Gets MDP Group to the User applications's Group instance.
     *
     * @param tagId    Group's Tag ID
     * @param mdpGroup User applications's Group instance
     * @return true if initialize user Group instance with success
     */
    boolean getGroup(final int tagId, final MdpGroup mdpGroup);

    /**
     * Creates copy of this FieldSet
     *
     * @return a copy instance;
     */
    FieldSet copy();
}
