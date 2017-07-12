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

package com.epam.cme.mdp3.core.cfg;

import com.epam.cme.mdp3.Feed;
import com.epam.cme.mdp3.FeedType;

import java.util.List;

/**
 * CME MDP Channel's Connection Configuration
 */
public class ConnectionCfg {
    private final String id;
    private final FeedType feedType;
    private final TransportProtocol protocol;
    private final String ip;
    private final List<String> hostIPs;
    private final int port;
    private final Feed feed;
    private String fullDesc;

    public ConnectionCfg(final Feed feed, final String id, final FeedType feedType, final TransportProtocol protocol, final String ip, final List<String> hostIPs, final int port) {
        this.feed = feed;
        this.id = id;
        this.feedType = feedType;
        this.protocol = protocol;
        this.ip = ip;
        this.hostIPs = hostIPs;
        this.port = port;
        this.fullDesc = createFullDesc();
    }

    public Feed getFeed() {
        return feed;
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public List<String> getHostIPs() {
        return hostIPs;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public TransportProtocol getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return fullDesc;
    }

    public String createFullDesc() {
        return "ConnectionCfg{" +
                "feed=" + feed +
                ", id='" + id + '\'' +
                ", feedType=" + feedType +
                ", protocol=" + protocol +
                ", ip='" + ip + '\'' +
                ", hostIPs=" + hostIPs +
                ", port=" + port +
                '}';
    }
}
