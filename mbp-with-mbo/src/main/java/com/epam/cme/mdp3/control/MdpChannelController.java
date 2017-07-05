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

package com.epam.cme.mdp3.control;

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.SemanticMsgType;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

public interface MdpChannelController extends ChannelController {
    int MBO_INCREMENT_MESSAGE_TEMPLATE_ID = 43;
    int MBO_CONTAINS_INCREMENT_MESSAGE_TEMPLATE_ID = 32;
    int MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID = 44;

    default void updateSemanticMsgType(MdpMessageTypes mdpMessageTypes, MdpMessage mdpMessage) {
        int schemaId = mdpMessage.getSchemaId();
        MdpMessageType messageType = mdpMessageTypes.getMessageType(schemaId);
        mdpMessage.setMessageType(messageType);
    }

    default boolean isMessageSupported(MdpMessage mdpMessage){
        SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
        int schemaId = mdpMessage.getSchemaId();
        if(SemanticMsgType.MarketDataIncrementalRefresh.equals(semanticMsgType)) {
            return (MBO_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId
                    || (MBO_CONTAINS_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId)
            );
        } else if(SemanticMsgType.MarketDataSnapshotFullRefresh.equals(semanticMsgType)){
            return MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID == schemaId;
        } else {
            return false;
        }
    }

    default boolean isIncrementOnlyForMBO(MdpMessage mdpMessage){
        SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
        int schemaId = mdpMessage.getSchemaId();
        return SemanticMsgType.MarketDataIncrementalRefresh.equals(semanticMsgType)
                && MBO_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId;
    }
}
