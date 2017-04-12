package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;

public class MBOChannelControllerRouter implements MBOChannelController {
    private InstrumentManager instrumentManager;
    private MdpMessageTypes mdpMessageTypes;

    public MBOChannelControllerRouter(InstrumentManager instrumentManager, MdpMessageTypes mdpMessageTypes){
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
                if(mboInstrumentController != null) {
                    mboInstrumentController.handleSnapshotMDEntry(mdpMessage);
                }
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
                    if(mboInstrumentController != null) {
                        mboInstrumentController.handleIncrementMDEntry(mdpMessage, mdpGroupEntry, msgSeqNum);
                    }
                }
            }
        });
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
