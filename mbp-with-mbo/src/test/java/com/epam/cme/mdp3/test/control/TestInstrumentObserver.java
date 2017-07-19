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

package com.epam.cme.mdp3.test.control;

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.control.InstrumentObserver;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.test.Constants;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestInstrumentObserver implements InstrumentObserver {
    private BlockingQueue<Pair<String,MdpMessage>> securitiesQueue = new LinkedBlockingQueue<>();
    private String channelId;

    public TestInstrumentObserver(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void onPacket(MdpFeedContext feedContext, MdpPacket instrumentPacket) {

    }

    @Override
    public void onMessage(MdpFeedContext feedContext, MdpMessage secDefMsg) {
        securitiesQueue.add(new ImmutablePair<>(channelId, secDefMsg));
    }

    public Pair<String,MdpMessage> nextSecurityMessage() throws InterruptedException {
        return securitiesQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }
}
