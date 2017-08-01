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
import com.epam.cme.mdp3.core.cfg.ConnectionCfg;
import com.epam.cme.mdp3.sbe.message.SbeDouble;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.message.SbeString;

public class MdpFeedContext {
    private final Feed feed;
    private final FeedType feedType;
    private final MdpPacket mdpPacket = MdpPacket.allocate();
    private final MdpGroup mdpGroupObj = SbeGroup.instance();
    private final MdpGroupEntry mdpGroupEntryObj = SbeGroupEntry.instance();
    private final SbeDouble doubleValObj = SbeDouble.instance();
    private final SbeString strValObj = SbeString.allocate(256);

    public MdpFeedContext(final Feed feed, final FeedType feedType) {
        this.feed = feed;
        this.feedType = feedType;
    }

    public MdpFeedContext(final ConnectionCfg cfg) {
        this.feed = cfg.getFeed();
        this.feedType = cfg.getFeedType();
    }

    public MdpPacket getMdpPacket() {
        return mdpPacket;
    }

    public MdpGroup getMdpGroupObj() {
        return mdpGroupObj;
    }

    public SbeDouble getDoubleValObj() {
        return doubleValObj;
    }

    public SbeString getStrValObj() {
        return strValObj;
    }

    public MdpGroupEntry getMdpGroupEntryObj() {
        return mdpGroupEntryObj;
    }

    public FeedType getFeedType() {
        return this.feedType;
    }

    public Feed getFeed() {
        return this.feed;
    }
}
