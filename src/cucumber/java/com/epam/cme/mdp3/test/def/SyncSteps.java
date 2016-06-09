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

package com.epam.cme.mdp3.test.def;

import com.epam.cme.mdp3.MdpChannel;
import com.epam.cme.mdp3.VoidChannelListener;
import com.epam.cme.mdp3.core.control.InstrumentState;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SyncSteps {
    private final MdpHandlerHolder mdpHandlerHolder;
    private final BlockingQueue<InstrumentStateChangeEvent> stateChangeEvents = new LinkedBlockingQueue<>();

    public SyncSteps(MdpHandlerHolder mdpHandlerHolder) {
        this.mdpHandlerHolder = mdpHandlerHolder;
    }

    private static class InstrumentStateChangeEvent {
        int securityId;
        InstrumentState prevState;
        InstrumentState newState;

        public InstrumentStateChangeEvent(int securityId, InstrumentState prevState, InstrumentState newState) {
            this.securityId = securityId;
            this.prevState = prevState;
            this.newState = newState;
        }
    }

    @Given("^Subscription to Incremental Refresh Feed A of MDP Channel (\\d+) in handler #(\\d+)$")
    public void subscriptionToIncrementalRefreshFeedAOfMDPChannel(int channelId, int handlerId) throws Throwable {
        getOrCreateHandler(channelId, handlerId).startIncrementalFeedA();
    }

    @And("^Subscription to Snapshot Feed A of MDP Channel (\\d+) in handler #(\\d+)$")
    public void subscriptionToSnapshotFeedAOfMDPChannel(int channelId, int handlerId) throws Throwable {
        getOrCreateHandler(channelId, handlerId).startSnapshotFeedA();
    }

    private MdpChannel getOrCreateHandler(int channelId, int handlerId) throws Exception {
        MdpChannel handler = mdpHandlerHolder.get(handlerId);
        handler = handler != null ? handler : mdpHandlerHolder.instance(handlerId, channelId);
        return handler;
    }

    @When("^User application is subscribed to Security (\\d+) state changes in handler #(\\d+)$")
    public void userApplicationIsSubscribedToInstrumentStateChanges(int securityId, int handlerId) throws Throwable {
        final MdpChannel handler = mdpHandlerHolder.get(handlerId);
        handler.registerListener(new VoidChannelListener() {
            @Override
            public void onInstrumentStateChanged(String channelId, int securityId, InstrumentState prevState, InstrumentState newState) {
                //System.out.println(String.format("%1$d %2$s %3$s ", securityId, prevState.toString(), newState.toString()));
                stateChangeEvents.offer(new InstrumentStateChangeEvent(securityId, prevState, newState));
            }
        });
        //handler.enableAllSecuritiesMode();
        handler.subscribe(securityId, null);
    }

    @Then("^Security with id (\\d+) must be synchronized in (\\d+) secs or less$")
    public void instrumentWithIdMustBeSynchronizedInTheGivenPeriodOfTime(int securityId, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final InstrumentStateChangeEvent stateChangeEvent = this.stateChangeEvents.poll(100, TimeUnit.MILLISECONDS);
            if (stateChangeEvent != null) {
                if (stateChangeEvent.securityId == securityId && stateChangeEvent.newState == InstrumentState.SYNC) {
                    testResult = true;
                    break;
                }
            }
        }
        assertTrue(String.format("Failed to synchronize Security with Id '%1$d' in %2$d secs", securityId, periodInSecs), testResult);
    }
}
