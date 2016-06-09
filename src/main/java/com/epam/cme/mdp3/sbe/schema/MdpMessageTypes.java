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

package com.epam.cme.mdp3.sbe.schema;

import com.epam.cme.mdp3.SemanticMsgType;
import com.epam.cme.mdp3.mktdata.MdConstants;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.vo.GroupType;
import com.epam.cme.mdp3.sbe.schema.vo.MessageSchema;
import com.epam.cme.mdp3.sbe.schema.vo.MessageType;

import java.net.URI;
import java.util.List;

public class MdpMessageTypes {
    private MdpMessageType[] messageTypes;
    private final MessageSchema schema;
    private final int maxIncrBodyLen;

    public MdpMessageTypes(final URI uri) throws MdpMessageTypeBuildException {
        try {
            schema = MessageSchemaUnmarshaller.unmarshall(uri);
        } catch (Exception e) {
            throw new MdpMessageTypeBuildException("Failed to unmarshall Message Schema: " + e.getMessage(), e);
        }
        this.messageTypes = buildMessageTypes(schema.getMessage());
        this.maxIncrBodyLen = findMaxIncrBodyLen();
        if (this.maxIncrBodyLen == 0) {
            throw new MdpMessageTypeBuildException("Maximum Increment body block length is not found");
        }
    }

    public MdpMessageType[] getMessageTypes() {
        return this.messageTypes;
    }

    public int getMaxIncrBodyLen() {
        return maxIncrBodyLen;
    }

    public MdpMessageType getMessageType(final int id) {
        return this.messageTypes[id];
    }

    private MdpMessageType[] buildMessageTypes(final List<MessageType> messageTypeList) {
        final MdpMessageType[] mdpMessageTypes = new MdpMessageType[findMaxMsgTypeId(messageTypeList) + 1];

        for (MessageType messageType : messageTypeList) {
            mdpMessageTypes[messageType.getId()] = new MdpMessageType(schema, messageType);
        }
        return mdpMessageTypes;
    }

    private int findMaxMsgTypeId(final List<MessageType> messageTypeList) {
        int maxMsgTypeId = 0;
        for (MessageType messageType : messageTypeList) {
            if (messageType.getId() > maxMsgTypeId) {
                maxMsgTypeId = messageType.getId();
            }
        }
        return maxMsgTypeId;
    }

    private int findMaxIncrBodyLen() {
        int maxFoundIncrBodyLen = 0;
        for (final MdpMessageType messageType : this.messageTypes) {
            if (messageType != null) {
                final int incrBodyLen = extractIncrBodyLen(messageType);
                maxFoundIncrBodyLen = incrBodyLen > maxFoundIncrBodyLen ? incrBodyLen : maxFoundIncrBodyLen;
            }
        }
        return maxFoundIncrBodyLen;
    }

    private int extractIncrBodyLen(final MdpMessageType messageType) {
        int maxFoundIncrBodyLen = 0;
        final SemanticMsgType semanticMsgType = SemanticMsgType.fromFixValue(messageType.getMessageType().getSemanticType());
        if (semanticMsgType == SemanticMsgType.MarketDataIncrementalRefresh) {
            for (final GroupType groupType : messageType.getMessageType().getGroup()) {
                if (groupType.getId() == MdConstants.INCR_RFRSH_GRP_TAG && groupType.getBlockLength().intValue() > maxFoundIncrBodyLen) {
                    maxFoundIncrBodyLen = groupType.getBlockLength().intValue();
                }
            }
        }
        return maxFoundIncrBodyLen;
    }
}
