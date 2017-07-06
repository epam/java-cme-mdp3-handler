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

import com.epam.cme.mdp3.ChannelListener;
import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.sbe.message.SbeConstants;

import java.util.List;

public class InstrumentController {
    private List<ChannelListener> listeners;
    private final String channelId;
    private final int securityId;
    private String secDesc;
    private volatile boolean enable = true;

    public InstrumentController(List<ChannelListener> listeners, String channelId, int securityId, String secDesc) {
        this.listeners = listeners;
        this.channelId = channelId;
        this.securityId = securityId;
        this.secDesc = secDesc;
    }

    public void handleIncrementMDEntry(MdpMessage mdpMessage, MdpGroupEntry orderIDEntry, MdpGroupEntry mdEntry, long msgSeqNum){
        if(enable) {
            short matchEventIndicator = mdpMessage.getUInt8(SbeConstants.MATCHEVENTINDICATOR_TAG);
            for (ChannelListener channelListener : listeners) {
                channelListener.onIncrementalMBORefresh(channelId, matchEventIndicator, securityId, secDesc, msgSeqNum, orderIDEntry, mdEntry);
            }
        }
    }

    public void handleSnapshotMDEntry(MdpMessage mdpMessage){
        if(enable) {
            for (ChannelListener channelListener : listeners) {
                channelListener.onSnapshotMBOFullRefresh(channelId, secDesc, mdpMessage);
            }
        }
    }

    public void enable(){
        enable = true;
    }

    public void disable(){
        enable = false;
    }

    public void updateSecDesc(String secDesc) {
        this.secDesc = secDesc;
    }
}
