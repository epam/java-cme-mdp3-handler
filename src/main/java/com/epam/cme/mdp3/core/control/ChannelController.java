package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

public interface ChannelController {
    void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket);
    void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket);

    default void updateSemanticMsgType(MdpMessageTypes mdpMessageTypes, MdpMessage mdpMessage) {
        int schemaId = mdpMessage.getSchemaId();
        MdpMessageType messageType = mdpMessageTypes.getMessageType(schemaId);
        mdpMessage.setMessageType(messageType);
    }
}
