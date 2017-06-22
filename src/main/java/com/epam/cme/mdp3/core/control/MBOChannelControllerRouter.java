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

package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import static com.epam.cme.mdp3.mktdata.MdConstants.*;

public class MBOChannelControllerRouter implements MBOChannelController {
    private InstrumentManager instrumentManager;
    private MdpMessageTypes mdpMessageTypes;
    private MdpGroupEntry mdEntry = SbeGroupEntry.instance();
    private MdpGroup noMdEntriesGroup = SbeGroup.instance();

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
                if(isIncrementOnlyForMBO(mdpMessage)) {
                    mdpMessage.getGroup(NO_MD_ENTRIES, mdpGroup);
                    while (mdpGroup.hashNext()) {
                        mdpGroup.next();
                        mdpGroup.getEntry(mdpGroupEntry);
                        int securityId = getSecurityId(mdpGroupEntry);
                        routeEntry(securityId, mdpMessage, mdpGroupEntry, null, msgSeqNum);
                    }
                } else {
                    if(mdpMessage.getGroup(NO_ORDER_ID_ENTRIES, mdpGroup)) {
                        while (mdpGroup.hashNext()) {
                            mdpMessage.getGroup(NO_MD_ENTRIES, noMdEntriesGroup);
                            mdpGroup.next();
                            mdpGroup.getEntry(mdpGroupEntry);
                            short entryNum = mdpGroupEntry.getUInt8(REFERENCE_ID);
                            noMdEntriesGroup.getEntry(entryNum, mdEntry);
                            int securityId = mdEntry.getInt32(SECURITY_ID);
                            routeEntry(securityId, mdpMessage, mdpGroupEntry, mdEntry, msgSeqNum);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void preClose() {

    }

    @Override
    public void close() {

    }

    protected void routeEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry orderIDEntry, MdpGroupEntry mdEntry, long msgSeqNum){
        MBOInstrumentController mboInstrumentController = instrumentManager.getMBOInstrumentController(securityId);
        if (mboInstrumentController != null) {
            mboInstrumentController.handleIncrementMDEntry(mdpMessage, orderIDEntry, mdEntry, msgSeqNum);
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
