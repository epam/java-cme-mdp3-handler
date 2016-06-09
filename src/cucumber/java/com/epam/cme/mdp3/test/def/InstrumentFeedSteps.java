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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.sbe.message.SbeString;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class InstrumentFeedSteps {
    private MdpFeedSimHelper mdpFeedSimHelper;
    private MdpHandlerHolder mdpHandlerHolder;
    private final CountDownLatch pcktRecStartSignal = new CountDownLatch(1);
    private final Set<String> receivedSymbols = Collections.synchronizedSet(new HashSet<>());
    private final SbeString tag55value = SbeString.allocate(20);
    private final Object newSecDefMonitor = new Object();

    public InstrumentFeedSteps(MdpFeedSimHelper mdpFeedSimHelper, MdpHandlerHolder mdpHandlerHolder) {
        this.mdpFeedSimHelper = mdpFeedSimHelper;
        this.mdpHandlerHolder = mdpHandlerHolder;
    }

    @Given("^Instrument Feed A of MDP Channel (\\d+) in MDP simulator$")
    public void instrumentFeedAOfMDPChannelInMdpSimulatorIsStarted(int channelId) throws Throwable {
        this.mdpFeedSimHelper.runInstrumentFeedA(channelId);
    }

    @When("^I connect to Instrument Feed A of Channel (\\d+)$")
    public void iConnectToInstrumentFeedAOfChannel(final int channelId) throws Throwable {
        final MdpChannel mdpHandler = mdpHandlerHolder.instance(channelId, new VoidChannelListener() {
            @Override
            public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
                if (feedType == FeedType.N) {
                    //System.out.println(mdpPacket);
                    pcktRecStartSignal.countDown();
                }
            }

            @Override
            public int onSecurityDefinition(String channelId, MdpMessage secDefMessage) {
                secDefMessage.getString(55, tag55value);
                receivedSymbols.add(tag55value.getString());
                synchronized (newSecDefMonitor) {
                    newSecDefMonitor.notify();
                }
                return MdEventFlags.NOTHING;
            }
        });
        mdpHandler.startInstrumentFeedA();
    }

    @Then("^I receive MDP packets from Instrument Feed$")
    public void iReceiveMDPPackets() throws Throwable {
        pcktRecStartSignal.await(1000, TimeUnit.MILLISECONDS);
        assertTrue("Failed to receive a MDP packet from Instrument Feed.", pcktRecStartSignal.getCount() == 0);
    }

    @And("^I receive Security Definition with Symbol '([^\"]*)' in (\\d+) sec$")
    public void iReceiveInstrumentDefinitionWithSymbol(String symbol, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        do {
            if (receivedSymbols.contains(symbol)) {
                break;
            } else {
                synchronized (newSecDefMonitor) {
                    newSecDefMonitor.wait(100);
                }
            }
        } while (System.currentTimeMillis() < maxTime);
        assertTrue(String.format("Failed to receive the MDP packet from Instrument Feed for Symbol '%1$s'", symbol), receivedSymbols.contains(symbol));
    }
}
