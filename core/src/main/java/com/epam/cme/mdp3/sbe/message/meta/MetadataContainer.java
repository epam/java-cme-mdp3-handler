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

package com.epam.cme.mdp3.sbe.message.meta;

import com.epam.cme.mdp3.sbe.schema.vo.EncodedDataType;
import com.epam.cme.mdp3.sbe.schema.vo.MessageSchema;

/**
 * Interface to MDP Metadata Container
 */
public interface MetadataContainer {
    /**
     * Gets SBE schema of MDP Messages.
     *
     * @return SBE schema of MDP messages
     */
    MessageSchema getSchema();

    /**
     * Gets schema ID of MDP Message for which this metadata is built.
     *
     * @return schema ID
     */
    int getSchemaId();

    /**
     * Gets coordinate of first field in message.
     *
     * @return offset
     */
    int offset();

    /**
     * Method to check if this MDP Message type contains the given Field.
     *
     * @param fieldId ID of Field
     * @return true if message contains the given Field or false if else
     */
    boolean hasField(final int fieldId);

    /**
     * Method to check if this MDP Message type contains the given Group.
     *
     * @param groupId ID of Group
     * @return true if message contains the given Field or false if else
     */
    boolean hasGroup(final int groupId);

    /**
     * Gets Field's metadata.
     *
     * @param fieldId ID of Field
     * @return Field's metadata or NULL if not exists
     */
    SbeFieldType findField(final int fieldId);

    /**
     * Gets Group's metadata.
     *
     * @param groupId Group's metadata or NULL if not exists
     * @return
     */
    SbeGroupType findGroup(final int groupId);

    /**
     * Gets all Groups in this MDP Message type.
     *
     * @return Array of Groups
     */
    SbeGroupType[] allGroups();

    /**
     * Adds Field metadata to this Message type's metadata.
     *
     * @param fieldType Field metadata
     */
    void addFieldType(final SbeFieldType fieldType);

    /**
     * Adds Group metadata to this Message type's metadata.
     *
     * @param groupType Group metadata
     */
    void addGroupType(final SbeGroupType groupType);

    /**
     * Gets DataType metadata.
     *
     * @param name Name of DataType
     * @return DataType metadata
     */
    EncodedDataType getDataType(final String name);
}
