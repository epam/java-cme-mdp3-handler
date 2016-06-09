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
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SnapshotFeedSteps {
    private MdpFeedSimHelper mdpFeedSimHelper;
    private MdpHandlerHolder mdpHandlerHolder;
    private BlockingQueue<MdpPacket> packetQueue = new LinkedBlockingQueue<>(1000);

    public SnapshotFeedSteps(MdpFeedSimHelper mdpFeedSimHelper, MdpHandlerHolder mdpHandlerHolder) {
        this.mdpFeedSimHelper = mdpFeedSimHelper;
        this.mdpHandlerHolder = mdpHandlerHolder;
    }

    @Given("^Snapshot Feed A of MDP Channel (\\d+) in MDP simulator is started$")
    public void snapshotFeedAOfMDPChannelInMDPSimulatorIsStarted(int channelId) throws Throwable {
        this.mdpFeedSimHelper.runSnapshotFeedA(channelId);
    }

    @When("^I connect to Snapshot Feed A of Channel (\\d+)$")
    public void iConnectToSnapshotFeedAOfChannel(int channelId) throws Throwable {
        final MdpChannel mdpHandler = mdpHandlerHolder.instance(channelId, new VoidChannelListener() {
            @Override
            public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
                if (feedType == FeedType.S && feed == Feed.A) {
                    final MdpPacket copy = mdpPacket.copy();
                    packetQueue.offer(copy);
                }
            }
        });
        mdpHandler.startSnapshotFeedA();
    }

    @Then("^I receive MDP packets from Snapshot Feed$")
    public void iReceiveMDPPacketsFromSnapshotFeed() throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(20);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testRes = false;

        while (System.currentTimeMillis() < maxTime) {
            final MdpPacket snptPacket = this.packetQueue.poll(100, TimeUnit.MILLISECONDS);
            if (snptPacket != null) {
                testRes = true;
                break;
            }
        }
        assertTrue("Failed to receive a MDP packet from Snapshot Feed", testRes);
    }

    @And("^I receive Snapshot for the Security (\\d+) in (\\d+) sec$")
    public void iReceiveSnapshotForTheInstrumentInSec(int securityId, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final MdpPacket snptPacket = this.packetQueue.poll(100, TimeUnit.MILLISECONDS);
            if (snptPacket != null) {
                final Iterator<MdpMessage> mdpMessageIterator = snptPacket.iterator();
                while (mdpMessageIterator.hasNext()) {
                    final MdpMessage snptMessage = mdpMessageIterator.next();
                    final MdpMessageType messageType = this.mdpHandlerHolder.getMdpMessageTypes().getMessageType(snptMessage.getSchemaId());
                    final SemanticMsgType msgType = messageType.getSemanticMsgType();
                    if (msgType == SemanticMsgType.MarketDataSnapshotFullRefresh) {
                        snptMessage.setMessageType(messageType);
                        if (snptMessage.getInt32(48) == securityId) {
                            testResult = true;
                        }
                    }
                }
            }
        }
        assertTrue(String.format("Failed to receive the MDP packet from Snapshot Feed for Security with Id '%1$d'", securityId), testResult);
    }

    @When("^I wait$")
    public void iWait() throws Throwable {
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Now listen");
        try {
            Thread.currentThread().sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
