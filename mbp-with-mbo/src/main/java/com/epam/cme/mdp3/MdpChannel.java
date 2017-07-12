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

package com.epam.cme.mdp3;

import com.epam.cme.mdp3.core.channel.MdpFeedException;

import java.util.List;

/**
 * Interface to MDP Channel with its lifecycle and all included Feeds inside too.
 */
public interface MdpChannel {
    /**
     * Gets ID of MDP Channel.
     *
     * @return Channel ID
     */
    String getId();

    /**
     * Closes MDP Channel and releases all resources.
     */
    void close();


    /**
     * Gets current State of the channel.
     *
     * @return Channel State
     */
    ChannelState getState();

    /**
     * Registers Channel Listener.
     *
     * @param channelListener Instance of Channel Listener
     * @see ChannelListener
     */
    void registerListener(ChannelListener channelListener);

    /**
     * Removes Channel Listener.
     *
     * @param channelListener Instance of Channel Listener
     * @see ChannelListener
     */
    void removeListener(ChannelListener channelListener);

    /**
     * Gets all registered Channel Listeners.
     *
     * @return List of ChannelListeners
     * @see ChannelListener
     */
    List<ChannelListener> getListeners();

    /**
     * Starts defined feed.
     * @param feedType
     * @param feed
     * @throws MdpFeedException
     */
    void startFeed(FeedType feedType, Feed feed) throws MdpFeedException;

    /**
     * Stops defined feed.
     * @param feedType
     * @param feed
     */
    void stopFeed(FeedType feedType, Feed feed);

    /**
     * Stops all Feeds.
     */
    void stopAllFeeds();

    /**
     * Subscribes to the given security.
     *
     * @param securityId Security ID
     * @return true if subscribed with success
     */
    boolean subscribe(int securityId, final String secDesc);

    /**
     * Removes subscription to the given security.
     *
     * @param securityId Security ID
     */
    void discontinueSecurity(int securityId);
}
