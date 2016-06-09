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

import com.epam.cme.mdp3.SemanticMsgType;
import com.epam.cme.mdp3.sbe.schema.vo.MessageSchema;
import com.epam.cme.mdp3.sbe.schema.vo.MessageType;

/**
 * Holder of MDP Message Type definitions.
 */
public class MdpMessageType {
    private final MessageSchema schema;
    private final MessageType messageType;
    private final SemanticMsgType semanticMsgType;
    private final MetadataContainer metadataContainer;

    /**
     * Constructs of holder of MDP Message Type definitions.
     *
     * @param schema      SBE schema of MDP Messages
     * @param messageType SBE definition of MDP Message Type
     */
    public MdpMessageType(final MessageSchema schema, final MessageType messageType) {
        this.schema = schema;
        this.messageType = messageType;
        this.semanticMsgType = SemanticMsgType.fromFixValue(messageType.getSemanticType());
        this.metadataContainer = MetadataContainerBuilder.build(this);
    }

    /**
     * Gets SBE schema of MDP Messages.
     *
     * @return SBE schema of MDP Messages
     */
    public MessageSchema getSchema() {
        return schema;
    }

    /**
     * Gets SBE definition of MDP Message Type.
     *
     * @return SBE definition of MDP Message Type
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Gets semantic type of the message (e.g. IncrementalRefresh).
     *
     * @return Semantic type of the message
     */
    public SemanticMsgType getSemanticMsgType() {
        return semanticMsgType;
    }

    /**
     * Gets Metadata of the MDP Message.
     *
     * @return Metadata Container
     */
    public MetadataContainer getMetadataContainer() {
        return metadataContainer;
    }
}
