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

import com.epam.cme.mdp3.core.control.InstrumentState;

/**
 * Default Channel Listener without any activity in all callbacks.
 * Can be used to easy create user application listener via extending of this default listener and overriding only required callbacks
 */
public interface VoidChannelListener extends ChannelListener {
    @Override
    default void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
    }

    @Override
    default void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
    }

    @Override
    default void onPacket(final String channelId, final FeedType feedType, final Feed feed, final MdpPacket mdpPacket) {
    }

    @Override
    default void onBeforeChannelReset(final String channelId, final MdpMessage resetMessage) {

    }

    @Override
    default void onFinishedChannelReset(final String channelId, final MdpMessage resetMessage) {

    }

    @Override
    default void onChannelStateChanged(final String channelId, final ChannelState prevState, final ChannelState newState) {

    }

    @Override
    default void onInstrumentStateChanged(final String channelId, final int securityId, final String secDesc, final InstrumentState prevState, final InstrumentState newState) {

    }

    @Override
    default int onSecurityDefinition(final String channelId, final MdpMessage secDefMessage) {
        return MdEventFlags.NOTHING;
    }

    @Override
    default void onIncrementalRefresh(final String channelId, final short matchEventIndicator, final int securityId, final String secDesc, final long msgSeqNum, final FieldSet incrRefreshEntry) {

    }

    @Override
    default void onSnapshotFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage) {

    }

    @Override
    default void onRequestForQuote(final String channelId, final MdpMessage rfqMessage) {

    }

    @Override
    default void onSecurityStatus(final String channelId, final int securityId, final MdpMessage secStatusMessage) {

    }
}
