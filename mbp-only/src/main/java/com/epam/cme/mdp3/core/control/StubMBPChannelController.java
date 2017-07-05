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

import com.epam.cme.mdp3.ChannelState;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;


public class StubMBPChannelController implements MBPChannelController {
    @Override
    public ChannelState getState() {
        return null;
    }

    @Override
    public void switchState(ChannelState state) {

    }

    @Override
    public void resetSnapshotCycleCount() {

    }

    @Override
    public void addOutOfSyncInstrument(Integer securityId) {

    }

    @Override
    public boolean removeOutOfSyncInstrument(Integer securityId) {
        return false;
    }

    @Override
    public boolean hasOutOfSyncInstruments() {
        return false;
    }

    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {

    }

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {

    }

    @Override
    public void preClose() {

    }

    @Override
    public void close() {

    }
}
