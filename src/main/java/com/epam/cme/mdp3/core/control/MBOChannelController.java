package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.SemanticMsgType;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import static com.epam.cme.mdp3.mktdata.MdConstants.NO_ORDER_ID_ENTRIES;

public interface MBOChannelController extends ChannelController {
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
                    || (MBO_CONTAINS_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId && mdpMessage.hasField(NO_ORDER_ID_ENTRIES)) //todo it has not been implemented yet because I don't know how to resolve references with reusable structure and what structure we should give to client.
            );
        } else if(SemanticMsgType.MarketDataSnapshotFullRefresh.equals(semanticMsgType)){
            return MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID == schemaId;
        } else {
            return false;
        }
    }
}
