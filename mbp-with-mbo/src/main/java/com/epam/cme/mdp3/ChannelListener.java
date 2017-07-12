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


import com.epam.cme.mdp3.core.channel.CoreChannelListener;

public interface ChannelListener extends CoreChannelListener {
    /**
     * Called when MDP Incremental Refresh Message is received and Security-related entry is processed.
     *
     * Only when MBO is enabled.
     *
     * @param channelId           ID of MDP Channel
     * @param matchEventIndicator MDP Event indicator (bitset, @see <a href="http://www.cmegroup.com/confluence/display/EPICSANDBOX/MDP+3.0+-+Market+Data+Incremental+Refresh">MDP 3.0 - Market Data Incremental Refresh</a>)
     * @param secDesc             Security description
     * @param msgSeqNum           Message sequence number of message.
     * @param securityId          Security ID
     * @param orderEntry          MBO Entry of Group from MDP Incremental Refresh Message. It can not be null.
     * @param mdEntry             MBP Entry of Group from MDP Incremental Refresh Message. It can be null when MBO Incremental Refresh is received in separated template.
     */
    void onIncrementalMBORefresh(final String channelId, final short matchEventIndicator, final int securityId,
                                         final String secDesc, final long msgSeqNum, final FieldSet orderEntry, final FieldSet mdEntry);

    /**
     *
     * @param channelId             ID of MDP Channel
     * @param matchEventIndicator   MDP Event indicator (bitset, @see <a href="http://www.cmegroup.com/confluence/display/EPICSANDBOX/MDP+3.0+-+Market+Data+Incremental+Refresh">MDP 3.0 - Market Data Incremental Refresh</a>)
     * @param securityId            Security ID
     * @param secDesc               Security description
     * @param msgSeqNum             Message sequence number of message.
     * @param mdEntry               MBP Entry of Group from MDP Incremental Refresh Message. It can not be null.
     */
    void onIncrementalMBPRefresh(final String channelId, final short matchEventIndicator, final int securityId,
                              final String secDesc, final long msgSeqNum, final FieldSet mdEntry);

    /**
     * Called when MDP Snapshot Full Refresh Message for MBO is received and processed.
     *
     * @param channelId   ID of MDP Channel
     * @param secDesc     Security description
     * @param snptMessage MDP Snapshot Full Refresh Message for MBO
     */
    void onSnapshotMBOFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage);

    /**
     * Called when MDP Snapshot Full Refresh Message is received and processed.
     *
     * @param channelId   ID of MDP Channel
     * @param secDesc     Security description
     * @param snptMessage MDP Snapshot Full Refresh Message for MBP
     */
    void onSnapshotMBPFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage);

}
