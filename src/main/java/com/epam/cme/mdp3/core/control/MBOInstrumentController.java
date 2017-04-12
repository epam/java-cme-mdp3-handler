package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.ChannelListener;
import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.sbe.message.SbeConstants;

import java.util.List;

public class MBOInstrumentController {
    private List<ChannelListener> listeners;
    private final String channelId;
    private final int securityId;
    private final String secDesc;
    private volatile boolean enable = true;

    public MBOInstrumentController(List<ChannelListener> listeners, String channelId, int securityId, String secDesc) {
        this.listeners = listeners;
        this.channelId = channelId;
        this.securityId = securityId;
        this.secDesc = secDesc;
    }

    public void handleIncrementMDEntry(MdpMessage mdpMessage, MdpGroupEntry mdpGroup, long msgSeqNum){
        if(enable) {
            short matchEventIndicator = mdpMessage.getUInt8(SbeConstants.MATCHEVENTINDICATOR_TAG);
            listeners.forEach(channelListener -> channelListener.onIncrementalRefresh(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, mdpGroup));
        }
    }

    public void handleSnapshotMDEntry(MdpMessage mdpMessage){
        if(enable) {
            listeners.forEach(channelListener -> channelListener.onSnapshotFullRefresh(channelId, secDesc, mdpMessage));
        }
    }

    public void enable(){
        enable = true;
    }

    public void disable(){
        enable = false;
    }
}
