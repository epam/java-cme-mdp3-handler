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

import java.util.Arrays;
import java.util.List;

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.SemanticMsgType;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

public interface MdpChannelController extends ChannelController {
    int MBO_INCREMENT_MESSAGE_TEMPLATE_ID_43 = 43;
    int MBO_INCREMENT_MESSAGE_TEMPLATE_ID_47 = 47;
    int MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID_44 = 44;
    int MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID_53 = 53;
    
    default List<Integer> getMBOIncrementMessageTemplateIds() {
    	return Arrays.asList(new Integer[]  { MBO_INCREMENT_MESSAGE_TEMPLATE_ID_43, MBO_INCREMENT_MESSAGE_TEMPLATE_ID_47 });
    }
    
    default List<Integer> getMBOSnapshotMessageTemplateIds() {
    	return Arrays.asList(new Integer[]  { MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID_44, MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID_53 });
    }

    default void updateSemanticMsgType(MdpMessageTypes mdpMessageTypes, MdpMessage mdpMessage) {
        int schemaId = mdpMessage.getSchemaId();
        MdpMessageType messageType = mdpMessageTypes.getMessageType(schemaId);
        mdpMessage.setMessageType(messageType);
    }

    default boolean isIncrementalMessageSupported(MdpMessage mdpMessage){
        SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
        return SemanticMsgType.MarketDataIncrementalRefresh.equals(semanticMsgType);
    }

    default boolean isIncrementOnlyForMBO(MdpMessage mdpMessage){
        SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
        int schemaId = mdpMessage.getSchemaId();
        return SemanticMsgType.MarketDataIncrementalRefresh.equals(semanticMsgType) && getMBOIncrementMessageTemplateIds().contains(schemaId);
    }

    default boolean isMBOSnapshot(MdpMessage mdpMessage){
        int schemaId = mdpMessage.getSchemaId();
        return getMBOSnapshotMessageTemplateIds().contains(schemaId);
    }
}
