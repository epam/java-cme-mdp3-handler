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
import com.epam.cme.mdp3.sbe.message.SbeGroup;
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

public class IncrementalFeedSteps {
    private MdpFeedSimHelper mdpFeedSimHelper;
    private MdpHandlerHolder mdpHandlerHolder;
    private BlockingQueue<MdpPacket> packetQueue = new LinkedBlockingQueue<>(1000);
    private static final MdpGroup incrGroup = SbeGroup.instance();

    public IncrementalFeedSteps(MdpFeedSimHelper mdpFeedSimHelper, MdpHandlerHolder mdpHandlerHolder) {
        this.mdpFeedSimHelper = mdpFeedSimHelper;
        this.mdpHandlerHolder = mdpHandlerHolder;
    }

    @Given("^Incremental Refresh Feed A of MDP Channel (\\d+) in MDP simulator is started$")
    public void incrementalRefreshFeedAOfMDPChannelInMDPSimulatorIsStarted(int channelId) throws Throwable {
        this.mdpFeedSimHelper.runIncrementRefreshFeedA(channelId);
    }

    @When("^I connect to Incremental Refresh Feed A of Channel (\\d+)$")
    public void iConnectToIncrementalRefreshFeedAOfChannel(int channelId) throws Throwable {
        final MdpChannel mdpHandler = mdpHandlerHolder.instance(channelId, new VoidChannelListener() {
            @Override
            public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
                if (feedType == FeedType.I && feed == Feed.A) {
                    final MdpPacket copy = mdpPacket.copy();
                    packetQueue.offer(copy);
                }
            }
        });
        mdpHandler.startIncrementalFeedA();
    }


    @Then("^I receive MDP packets from Incremental Refresh Feed in (\\d+) sec$")
    public void iReceiveMDPPacketsFromIncrementalRefreshFeed(int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final MdpPacket incrPacket = this.packetQueue.poll(100, TimeUnit.MILLISECONDS);
            if (incrPacket != null) {
                testResult = true;
                break;
            }
        }
        assertTrue("Failed to receive a MDP packet from Incremental Refresh Feed.", testResult);
    }

    @And("^I receive Incremental Refresh packets for the Security (\\d+) in (\\d+) secs$")
    public void iReceiveIncrementalRefreshPacketsForTheSecurityInSec(int securityId, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final MdpPacket incrPacket = this.packetQueue.poll(100, TimeUnit.MILLISECONDS);
            if (incrPacket != null) {
                final Iterator<MdpMessage> mdpMessageIterator = incrPacket.iterator();
                while (mdpMessageIterator.hasNext()) {
                    final MdpMessage incrMessage = mdpMessageIterator.next();
                    final MdpMessageType messageType = this.mdpHandlerHolder.getMdpMessageTypes().getMessageType(incrMessage.getSchemaId());
                    final SemanticMsgType msgType = messageType.getSemanticMsgType();
                    if (msgType == SemanticMsgType.MarketDataIncrementalRefresh) {
                        incrMessage.setMessageType(messageType);

                        incrMessage.getGroup(268, incrGroup);
                        while (incrGroup.hashNext()) {
                            incrGroup.next();
                            if (incrGroup.hasField(48)) {
                                if (incrGroup.getInt32(48) == securityId) {
                                    testResult = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        assertTrue(String.format("Failed to receive the MDP packet from Incremental Refresh Feed for Security with Id '%1$d'", securityId), testResult);
    }
}
