package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;

public class MBOChannelController implements ChannelController {
    private static final int MBO_INCREMENT_MESSAGE_TEMPLATE_ID = 43;
    private static final int MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID = 44;
    private static final int MBO_CONTAINS_INCREMENT_MESSAGE_TEMPLATE_ID = 32;
    private InstrumentManager instrumentManager;
    private MdpMessageTypes mdpMessageTypes;

    public MBOChannelController(InstrumentManager instrumentManager, MdpMessageTypes mdpMessageTypes){
        this.instrumentManager = instrumentManager;
        this.mdpMessageTypes = mdpMessageTypes;
    }

    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        mdpPacket.forEach(mdpMessage -> {
            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
            if(isMessageSupported(mdpMessage)){
                int securityId = getSecurityId(mdpMessage);
                MBOInstrumentController mboInstrumentController = instrumentManager.getMBOInstrumentController(securityId);
                mboInstrumentController.handleSnapshotMDEntry(mdpMessage);
            }
        });
    }

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {
        MdpGroup mdpGroup = feedContext.getMdpGroupObj();
        MdpGroupEntry mdpGroupEntry = feedContext.getMdpGroupEntryObj();
        long msgSeqNum = mdpPacket.getMsgSeqNum();
        mdpPacket.forEach(mdpMessage -> {
            updateSemanticMsgType(mdpMessageTypes, mdpMessage);
            if(isMessageSupported(mdpMessage)){
                mdpMessage.getGroup(NO_MD_ENTRIES, mdpGroup);
                while (mdpGroup.hashNext()){
                    mdpGroup.next();
                    mdpGroup.getEntry(mdpGroupEntry);
                    int securityId = getSecurityId(mdpGroupEntry);
                    MBOInstrumentController mboInstrumentController = instrumentManager.getMBOInstrumentController(securityId);
                    mboInstrumentController.handleIncrementMDEntry(mdpMessage, mdpGroupEntry, msgSeqNum);
                }
            }
        });
    }

    private boolean isMessageSupported(MdpMessage mdpMessage){
        SemanticMsgType semanticMsgType = mdpMessage.getSemanticMsgType();
        int schemaId = mdpMessage.getSchemaId();
        if(SemanticMsgType.MarketDataIncrementalRefresh.equals(semanticMsgType)) {
            return (MBO_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId
                    || (MBO_CONTAINS_INCREMENT_MESSAGE_TEMPLATE_ID == schemaId && mdpMessage.hasField(NO_ORDER_ID_ENTRIES))
            );
        } else if(SemanticMsgType.MarketDataSnapshotFullRefresh.equals(semanticMsgType)){
            return MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID == schemaId;
        } else {
            return false;
        }
    }

    private int getSecurityId(MdpMessage mdpMessage){
        int schemaId = mdpMessage.getSchemaId();
        switch (schemaId){
            case MBO_SNAPSHOT_MESSAGE_TEMPLATE_ID:
                return mdpMessage.getInt32(SECURITY_ID);
            default:
                throw new UnsupportedOperationException("This type has not been supported yet");
        }
    }

    private int getSecurityId(MdpGroupEntry mdpGroupEntry){
        return mdpGroupEntry.getInt32(SECURITY_ID);
    }
}
