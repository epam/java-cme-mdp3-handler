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

import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.schema.vo.CompositeDataType;
import com.epam.cme.mdp3.sbe.schema.vo.FieldType;
import com.epam.cme.mdp3.sbe.schema.vo.GroupType;
import com.epam.cme.mdp3.sbe.schema.vo.MessageSchema;

/**
 * Builder of Metadata of the given MDP Message Type.
 */
public class MetadataContainerBuilder {
    private MetadataContainerBuilder() {
    }

    /**
     * Builds Metadata of the given MDP Message Type.
     *
     * @param messageType SBE definitions of MDP Message Type
     * @return Container of Metadata of the given MDP Message Type
     */
    public static MetadataContainer build(final MdpMessageType messageType) {
        final int schemaId = messageType.getMessageType().getId();
        final MetadataContainer container = allocate(messageType.getSchema(), schemaId,
                SbeConstants.HEADER_SIZE, findMaxFieldId(messageType), messageType.getMessageType().getGroup().size());

        for (FieldType fieldType : messageType.getMessageType().getField()) {
            container.addFieldType(new SbeFieldType(container, fieldType));
        }
        for (GroupType groupType : messageType.getMessageType().getGroup()) {
            final MetadataContainer fieldContainer = allocate(messageType.getSchema(), schemaId, findMaxFieldId(groupType));
            for (FieldType fieldType : groupType.getField()) {
                fieldContainer.addFieldType(new SbeFieldType(fieldContainer, fieldType));
            }
            container.addGroupType(new SbeGroupType(fieldContainer, groupType,
                    findDimensionType(messageType, groupType.getDimensionType())));
        }
        return container;
    }

    /**
     * Allocates a Metadata Container instance.
     *
     * @param schema     SBE schema of MDP messages
     * @param schemaId   Template ID of MDP message
     * @param maxFieldId Maximum Tag ID of fields in the given MDP message schema
     * @return Metadata Container instance
     */
    private static MetadataContainer allocate(final MessageSchema schema, final int schemaId, final int maxFieldId) {
        return new ArrayMetadataContainer(schema, schemaId, maxFieldId);
    }

    /**
     * Allocates a Metadata Container instance.
     *
     * @param schema        SBE schema of MDP messages
     * @param schemaId      Template ID of MDP message
     * @param offset        Coordinates of first field in message
     * @param maxFieldId    Maximum Tag ID of fields in the given MDP message schema
     * @param totalGroupNum Total number of Groups in the given MDP message
     * @return Metadata Container instance
     */
    private static MetadataContainer allocate(final MessageSchema schema, final int schemaId, final int offset, final int maxFieldId, final int totalGroupNum) {
        return new ArrayMetadataContainer(schema, schemaId, offset, maxFieldId, totalGroupNum);
    }

    /**
     * Finds Dimension Type in SBE schema of MDP Messages.
     *
     * @param messageType SBE definitions of MDP Message Type
     * @param dimTypeName name of Dimension Type
     * @return Definition of Dimension Type
     */
    private static CompositeDataType findDimensionType(final MdpMessageType messageType, final String dimTypeName) {
        for (final CompositeDataType compositeDataType : messageType.getSchema().getTypes().get(0).getComposite()) {
            if (compositeDataType.getName().equals(dimTypeName)) {
                return compositeDataType;
            }
        }
        throw new IllegalStateException("Not type found");
    }

    /**
     * Finds maximum Tag ID of Fields in the given MDP Message type.
     *
     * @param messageType SBE definitions of MDP Message Type
     * @return Maximum Tag ID
     */
    private static int findMaxFieldId(final MdpMessageType messageType) {
        int maxFieldId = 0;
        for (FieldType fieldType : messageType.getMessageType().getField()) {
            if (fieldType.getId() > maxFieldId) {
                maxFieldId = fieldType.getId();
            }
        }
        return maxFieldId;
    }

    /**
     * Finds maximum Tag ID of Fields in the given MDP Message Group type.
     *
     * @param groupType MDP Message Group type
     * @return Maximum Tag ID
     */
    private static int findMaxFieldId(final GroupType groupType) {
        int maxFieldId = 0;
        for (FieldType fieldType : groupType.getField()) {
            if (fieldType.getId() > maxFieldId) {
                maxFieldId = fieldType.getId();
            }
        }
        return maxFieldId;
    }
}
