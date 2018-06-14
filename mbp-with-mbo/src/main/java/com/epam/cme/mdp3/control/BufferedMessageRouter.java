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
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import java.util.List;
import java.util.function.Consumer;

public class BufferedMessageRouter extends ChannelControllerRouter {
    private SnapshotCycleHandler cycleHandler;

    public BufferedMessageRouter(String channelId, InstrumentManager instrumentManager, MdpMessageTypes mdpMessageTypes,
                                 List<ChannelListener> channelListeners, SnapshotCycleHandler cycleHandler,
                                 InstrumentObserver instrumentObserver, List<Consumer<MdpMessage>> emptyBookConsumers,     
                                 List<Integer> mboIncrementMessageTemplateIds, List<Integer> mboSnapshotMessageTemplateIds) {
        super(channelId, instrumentManager, mdpMessageTypes, channelListeners, instrumentObserver, emptyBookConsumers, mboIncrementMessageTemplateIds, mboSnapshotMessageTemplateIds);
        this.cycleHandler = cycleHandler;
    }

    @Override
    protected void routeMBOEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry orderIDEntry, MdpGroupEntry mdEntry, long msgSeqNum){
        long snapshotSequence = cycleHandler.getSnapshotSequence(securityId);
        if(snapshotSequence < msgSeqNum) {
            super.routeMBOEntry(securityId, mdpMessage, orderIDEntry, mdEntry, msgSeqNum);
        }
    }

    @Override
    protected void routeMBPEntry(int securityId, MdpMessage mdpMessage, MdpGroupEntry mdEntry, long msgSeqNum){
        long snapshotSequence = cycleHandler.getSnapshotSequence(securityId);
        if(snapshotSequence < msgSeqNum) {
            super.routeMBPEntry(securityId, mdpMessage, mdEntry, msgSeqNum);
        }
    }
}
