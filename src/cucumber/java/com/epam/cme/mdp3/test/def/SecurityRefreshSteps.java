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
import cucumber.api.java.en.And;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SecurityRefreshSteps {
    private final MdpFeedSimHelper mdpFeedSimHelper;
    private final MdpHandlerHolder mdpHandlerHolder;
    private final BlockingQueue<MdpMessage> snptEvents = new LinkedBlockingQueue<>();
    private final BlockingQueue<SecurityIncrementalRefreshEvent> incrEvents = new LinkedBlockingQueue<>();

    public SecurityRefreshSteps(MdpFeedSimHelper mdpFeedSimHelper, MdpHandlerHolder mdpHandlerHolder) {
        this.mdpFeedSimHelper = mdpFeedSimHelper;
        this.mdpHandlerHolder = mdpHandlerHolder;
    }

    private static class SecurityIncrementalRefreshEvent {
        short matchEventIndicator;
        int securityId;
        String secDesc;
        long msgSeqNum;
        FieldSet incrRefreshEntry;

        public SecurityIncrementalRefreshEvent(short matchEventIndicator, int securityId, String secDesc, long msgSeqNum, FieldSet incrRefreshEntry) {
            this.matchEventIndicator = matchEventIndicator;
            this.securityId = securityId;
            this.secDesc = secDesc;
            this.msgSeqNum = msgSeqNum;
            this.incrRefreshEntry = incrRefreshEntry;
        }
    }

    @And("^User application is subscribed to Security (\\d+) snapshot full refresh in handler #(\\d+)$")
    public void userApplicationIsSubscribedToSecuritySnapshotFullRefreshInHandler(int securityId, int handlerId) throws Throwable {
        final MdpChannel handler = mdpHandlerHolder.get(handlerId);
        handler.registerListener(new VoidChannelListener() {
            @Override
            public void onSnapshotFullRefresh(String channelId, String secDesc, MdpMessage snptMessage) {
                snptEvents.offer(snptMessage.copy());
            }
        });
        if (handler.getSubscriptionFlags(securityId) == MdEventFlags.NOTHING) {
            handler.subscribe(securityId, null);
        }
    }

    @And("^User application is subscribed to Security (\\d+) incremental refresh in handler #(\\d+)$")
    public void userApplicationIsSubscribedToSecurityIncrementalRefreshInHandler(int securityId, int handlerId) throws Throwable {
        final MdpChannel handler = mdpHandlerHolder.get(handlerId);
        handler.registerListener(new VoidChannelListener() {
            @Override
            public void onIncrementalRefresh(String channelId, short matchEventIndicator, int securityId, String secDesc, long msgSeqNum, FieldSet incrRefreshEntry) {
                incrEvents.offer(new SecurityIncrementalRefreshEvent(matchEventIndicator, securityId, secDesc, msgSeqNum, incrRefreshEntry.copy()));
            }
        });
        if (handler.getSubscriptionFlags(securityId) == MdEventFlags.NOTHING) {
            handler.subscribe(securityId, null);
        }
    }

    @And("^Security with id (\\d+) must get snapshot full refresh in (\\d+) secs or less$")
    public void securityWithIdMustGetSnapshotFullRefreshInSecsOrLess(int securityId, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final MdpMessage snptMessage = this.snptEvents.poll(100, TimeUnit.MILLISECONDS);

            if (snptMessage != null) {
                final int snptSecId = snptMessage.getInt32(48);
                if (snptSecId == securityId) {
                    testResult = true;
                    break;
                }
            }
        }
        assertTrue(String.format("Failed to get snapshot full refresh  of Security with Id '%1$d' in %2$d secs", securityId, periodInSecs), testResult);
    }

    @And("^Security with id (\\d+) must get incremental refresh in (\\d+) secs or less$")
    public void securityWithIdMustGetIncrementalRefreshInSecsOrLess(int securityId, int periodInSecs) throws Throwable {
        final long period = TimeUnit.SECONDS.toMillis(periodInSecs);
        final long maxTime = System.currentTimeMillis() + period;
        boolean testResult = false;

        while (System.currentTimeMillis() < maxTime) {
            final SecurityIncrementalRefreshEvent incrementalRefreshEvent = this.incrEvents.poll(100, TimeUnit.MILLISECONDS);
            if (incrementalRefreshEvent != null) {
                if (incrementalRefreshEvent.securityId == securityId) {
                    testResult = true;
                    break;
                }
            }
        }
        assertTrue(String.format("Failed to get incremental refresh of Security with Id '%1$d' in %2$d secs", securityId, periodInSecs), testResult);
    }
}
