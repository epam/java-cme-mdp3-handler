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

import com.epam.cme.mdp3.core.channel.MdpFeedContext;
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
     * Enables mode in which user application subscribed to all Securities with default Book Depth and Subscription options.
     */
    void enableAllSecuritiesMode();

    /**
     * Disabled mode in which user application subscribed to all Securities.
     */
    void disableAllSecuritiesMode();

    /**
     * Gets current value of Default Book Depth.
     *
     * @return Book Depth
     */
    byte getDefMaxBookDepth();

    /**
     * Set new value of Default Book Depth.
     *
     * @param defMaxBookDepth New Book Depth
     */
    void setDefMaxBookDepth(byte defMaxBookDepth);

    /**
     * Gets current value of Default Subscription Options.
     *
     * @return Default Subscription Options (bitset)
     * @see MdEventFlags
     */
    int getDefSubscriptionOptions();

    /**
     * Sets new Default Subscription Options.
     *
     * @param defSubscriptionOptions Default Subscription Options
     */
    void setDefSubscriptionOptions(int defSubscriptionOptions);

    /**
     * Gets current State of the channel.
     *
     * @return Channel State
     */
    ChannelState getState();

    /**
     * Sets the given Channel State forcibly (for specific use cases only. Do not use in usual use cases).
     *
     * @param state Channel state
     */
    void setStateForcibly(ChannelState state);

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
     * Registers Market Data Listener.
     *
     * @param mdListener Instance of Security Status Listener
     * @see MarketDataListener
     */
    void registerMarketDataListener(MarketDataListener mdListener);

    /**
     * Removes Market Data Listener.
     *
     * @param mdListener Instance of Security Status Listener
     * @see MarketDataListener
     */
    void removeMarketDataListener(MarketDataListener mdListener);

    /**
     * Gets all registered Channel Listeners.
     *
     * @return List of ChannelListeners
     * @see ChannelListener
     */
    List<ChannelListener> getListeners();

    /**
     * Gets all registered Market Data Listeners.
     *
     * @return List of MarketDataListener
     * @see MarketDataListener
     */
    List<MarketDataListener> getMdListeners();


    /**
     * Starts Incremental Feed A
     *
     * @throws MdpFeedException
     */
    void startIncrementalFeedA() throws MdpFeedException;

    /**
     * Starts Incremental Feed B
     *
     * @throws MdpFeedException
     */
    void startIncrementalFeedB() throws MdpFeedException;

    /**
     * Starts Snapshot Feed A.
     *
     * @throws MdpFeedException
     */
    void startSnapshotFeedA() throws MdpFeedException;

    /**
     * Starts Snapshot Feed B.
     *
     * @throws MdpFeedException
     */
    void startSnapshotFeedB() throws MdpFeedException;

    /**
     * Starts Snapshot MBO Feed A.
     *
     * @throws MdpFeedException
     */
    void startSnapshotMBOFeedA() throws MdpFeedException;

    /**
     * Starts Snapshot MBO Feed B.
     *
     * @throws MdpFeedException
     */
    void startSnapshotMBOFeedB() throws MdpFeedException;

    /**
     * Starts Instrumental Feed A.
     *
     * @throws MdpFeedException
     */
    void startInstrumentFeedA() throws MdpFeedException;

    /**
     * Starts Instrumental Feed B.
     *
     * @throws MdpFeedException
     */
    void startInstrumentFeedB() throws MdpFeedException;

    /**
     * Stops Incremental Feed A.
     */
    void stopIncrementalFeedA();

    /**
     * Stops Incremental Feed B.
     */
    void stopIncrementalFeedB();

    /**
     * Stops Snapshot Feed A.
     */
    void stopSnapshotFeedA();

    /**
     * Stops Snapshot Feed B.
     */
    void stopSnapshotFeedB();

    /**
     * Stops Snapshot MBO Feed A.
     */
    void stopSnapshotMBOFeedA();

    /**
     * Stops Snapshot MBO Feed B.
     */
    void stopSnapshotMBOFeedB();

    /**
     * Stops Instrumental Feed A.
     */
    void stopInstrumentFeedA();

    /**
     * Stops Instrumental Feed B.
     */
    void stopInstrumentFeedB();

    /**
     * Stops all Feeds.
     */
    void stopAllFeeds();

    /**
     * Starts Instrumental Feeds.
     */
    void startInstrumentFeeds();

    /**
     * Starts Snapshot Feeds.
     */
    void startSnapshotFeeds();

    void startSnapshotMBOFeeds();

    /**
     * Stops Snapshot Feeds.
     */
    void stopSnapshotFeeds();

    void stopSnapshotMBOFeeds();

    /**
     * Subscribes to the given Security with the given Subscription options and Depth.
     *
     * @param securityId  Security ID
     * @param subscrFlags Subscription Flags (bit set)
     * @param depth       Book depth
     * @return true if subscribed with success
     * @see MdEventFlags
     */
    boolean subscribe(int securityId, final String secDesc, int subscrFlags, byte depth);

    /**
     * Subscribes to the given Security with the given Subscription options and default Depth.
     *
     * @param securityId  Security ID
     * @param subscrFlags Subscription Flags (bit set)
     * @return true if subscribed with success
     * @see MdEventFlags
     * @see this#setDefMaxBookDepth(byte)
     */
    boolean subscribeWithDefDepth(int securityId, final String secDesc, int subscrFlags);

    /**
     * Subscribes to the given Security with the default Subscription options and the given Depth.
     *
     * @param securityId Security ID
     * @param depth      Book depth
     * @return true if subscribed with success
     * @see this#setDefSubscriptionOptions(int)
     */
    boolean subscribe(int securityId, final String secDesc, byte depth);

    /**
     * Subscribes to the given Security with the default Subscription options and the default Depth.
     *
     * @param securityId Security ID
     * @return true if subscribed with success
     * @see this#setDefMaxBookDepth(byte)
     * @see this#setDefSubscriptionOptions(int)
     */
    boolean subscribe(int securityId, final String secDesc);

    /**
     * Removes subscription to the given Security ID.
     *
     * @param securityId Security ID
     */
    void discontinueSecurity(int securityId);

    /**
     * Gets Subscription options of the given Security.
     *
     * @param securityId Security ID.
     * @return Subscription options of the given Security
     * @see MdEventFlags
     */
    int getSubscriptionFlags(int securityId);

    /**
     * Sets Subscription options to the given Security.
     *
     * @param securityId Security ID.
     * @param flags      Subscription options of the given Security
     * @see MdEventFlags
     */
    void setSubscriptionFlags(int securityId, int flags);

    /**
     * Adds the given Subscription options to the given Security.
     *
     * @param securityId Security ID.
     * @param flags      Subscription options of the given Security
     * @see MdEventFlags
     */
    void addSubscriptionFlags(int securityId, int flags);

    /**
     * Removes the given Subscription options to the given Security.
     *
     * @param securityId Security ID.
     * @param flags      Subscription options of the given Security
     * @see MdEventFlags
     */
    void removeSubscriptionFlags(int securityId, int flags);

    void handlePacket(final MdpFeedContext feedContext, final MdpPacket mdpPacket);
}
