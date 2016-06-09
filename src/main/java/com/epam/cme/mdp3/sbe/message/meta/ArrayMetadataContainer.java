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
 * Array-based holder of MDP Message or Group metadata.
 */
public class ArrayMetadataContainer implements MetadataContainer {
    private final MessageSchema schema;
    private int schemaId;
    private int offset;
    private SbeFieldType[] fieldTypes;
    private SbeGroupType[] groupTypes;

    /**
     * Constructor of MDP Fieldset metadata.
     *
     * @param schema     SBE schema of MDP Message types
     * @param schemaId   ID of MDP Message type's schema
     * @param maxFieldId Maximum Tag ID of Fields in the MDP Message Type
     */
    public ArrayMetadataContainer(final MessageSchema schema, final int schemaId, final int maxFieldId) {
        this.schema = schema;
        this.schemaId = schemaId;
        this.fieldTypes = new SbeFieldType[maxFieldId + 1];
    }

    /**
     * Constructor of MDP Fieldset metadata.
     *
     * @param schema        SBE schema of MDP Message types
     * @param schemaId      ID of MDP Message type's schema
     * @param offset        Coordinates of first Field in the Message
     * @param maxFieldId    Maximum Field Tag ID
     * @param totalGroupNum Total number of Groups in the Message
     */
    public ArrayMetadataContainer(final MessageSchema schema, final int schemaId, final int offset, final int maxFieldId, final int totalGroupNum) {
        this.schema = schema;
        this.schemaId = schemaId;
        this.offset = offset;
        this.fieldTypes = new SbeFieldType[maxFieldId + 1];
        if (totalGroupNum > 0) groupTypes = new SbeGroupType[totalGroupNum];
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public int getSchemaId() {
        return schemaId;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public MessageSchema getSchema() {
        return this.schema;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public int offset() {
        return offset;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public void addFieldType(final SbeFieldType fieldType) {
        fieldTypes[fieldType.getFieldType().getId()] = fieldType;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public void addGroupType(final SbeGroupType groupType) {
        for (int i = 0; i < groupTypes.length; i++) {
            final SbeGroupType groupTypeElement = groupTypes[i];
            if (groupTypeElement == null) {
                groupTypes[i] = groupType;
                return;
            }
        }
        throw new IllegalStateException("Incorrect pre-allocated number of Groups");
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public boolean hasField(final int fieldId) {
        return this.fieldTypes[fieldId] != null;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public boolean hasGroup(final int groupId) {
        for (int i = 0; i < groupTypes.length; i++) {
            final SbeGroupType groupTypeElement = groupTypes[i];
            if (groupTypeElement.getGroupType().getId() == groupId) return true;
        }
        return false;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public SbeFieldType findField(final int fieldId) {
        if (fieldId <= this.fieldTypes.length) return this.fieldTypes[fieldId];
        else return null;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public SbeGroupType findGroup(final int groupId) {
        for (int i = 0; i < groupTypes.length; i++) {
            final SbeGroupType groupTypeElement = groupTypes[i];
            if (groupTypeElement.getGroupType().getId() == groupId) return groupTypeElement;
        }
        return null;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public SbeGroupType[] allGroups() {
        return this.groupTypes;
    }

    /**
     * @see MetadataContainer
     */
    @Override
    public EncodedDataType getDataType(String typeName) {
        for (MessageSchema.Types types : getSchema().getTypes()) {
            for (EncodedDataType dataType : types.getType()) {
                if (typeName.equals(dataType.getName())) {
                    return dataType;
                }
            }
        }
        return null;
    }
}
