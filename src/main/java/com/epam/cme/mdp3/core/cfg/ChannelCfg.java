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

import java.util.ArrayList;
import java.util.List;

/**
 * Holder of CME MDP Channel Configurations
 */
public class ChannelCfg {
    private final String id;
    private final String label;

    final List<ConnectionCfg> connections = new ArrayList<>();

    public ChannelCfg(final String id, final String label) {
        this.id = id;
        this.label = label;
    }

    /**
     * Adds Connection configuration to holder.
     *
     * @param connectionCfg Connection configuration
     */
    public void addConnection(final ConnectionCfg connectionCfg) {
        this.connections.add(connectionCfg);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Gets Connection configuration from holder.
     *
     * @param feedType Type of CME MDP Feed (e.g. Incremental or Snapshot)
     * @param feed     CME MDP Feed A or B
     * @return Connection configuration or NULL if not found
     */
    public ConnectionCfg getConnectionCfg(final FeedType feedType, final Feed feed) {
        for (ConnectionCfg cfg : connections) {
            if (cfg.getFeed() == feed && cfg.getFeedType() == feedType) {
                return cfg;
            }
        }
        return null;
    }
}
