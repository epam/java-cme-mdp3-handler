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

package com.epam.cme.mdp3.core.channel;


import com.epam.cme.mdp3.*;

public interface CoreChannelListener {
    /**
     * Called when a Channel Feed is started.
     *
     * @param channelId ID of MDP Channel
     * @param feedType  Type of MDP Feed (e.g. snapshot or incremental)
     * @param feed      Feed (e.g. A or B)
     */
    void onFeedStarted(final String channelId, final FeedType feedType, final Feed feed);

    /**
     * Called when a Channel Feed is stopped.
     *
     * @param channelId ID of MDP Channel
     * @param feedType  type of MDP Feed (e.g. snapshot or incremental)
     * @param feed      feed (e.g. A or B)
     */
    void onFeedStopped(final String channelId, final FeedType feedType, final Feed feed);

    /**
     * Called when a Channel Feed received new MDP Packet.
     *
     * @param channelId ID of MDP Channel
     * @param feedType  Type of MDP Feed (e.g. snapshot or incremental)
     * @param feed      Feed (e.g. A or B)
     * @param mdpPacket MDP Packet which is just received and will be handled after this callback
     */
    void onPacket(final String channelId, final FeedType feedType, final Feed feed, final MdpPacket mdpPacket);

    /**
     * Called when a Channel has to start its reset.
     *
     * @param channelId    ID of MDP Channel
     * @param resetMessage MDP Reset Message
     */
    void onBeforeChannelReset(final String channelId, final MdpMessage resetMessage);

    /**
     * Called when a Channel just finished its reset.
     *
     * @param channelId    ID of MDP Channel
     * @param resetMessage MDP Reset Message
     */
    void onFinishedChannelReset(final String channelId, final MdpMessage resetMessage);

    /**
     * Called when a Channel state is changed.
     *
     * @param channelId ID of MDP Channel
     * @param prevState Previous state
     * @param newState  New state
     */
    void onChannelStateChanged(final String channelId, final ChannelState prevState, final ChannelState newState);

    /**
     * Called when MDP Security Definition Message is received and processed.
     *
     * @param channelId     ID of MDP Channel
     * @param secDefMessage MDP Security Definition Message
     * @return Required Subscription flags
     * @see MdEventFlags
     */
    int onSecurityDefinition(final String channelId, final MdpMessage secDefMessage);

    /**
     * Called when MDP RFQ Message is received and processed.
     *
     * @param channelId  ID of MDP Channel
     * @param rfqMessage MDP TFQ Message
     */
    void onRequestForQuote(final String channelId, final MdpMessage rfqMessage);

    /**
     * Called when MDP Security Status Message is received and processed.
     *
     * @param channelId        ID of MDP Channel
     * @param securityId       Security ID
     * @param secStatusMessage MDP Security Status Message
     */
    void onSecurityStatus(final String channelId, final int securityId, final MdpMessage secStatusMessage);
}
