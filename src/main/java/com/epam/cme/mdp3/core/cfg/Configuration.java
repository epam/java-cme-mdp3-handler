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
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CME MDP Configuration
 */
public class Configuration {
    private final Map<String, ChannelCfg> channelCfgs = new HashMap<>();
    private final Map<String, ConnectionCfg> connCfgs = new HashMap<>();

    public Configuration(final URI uri) throws ConfigurationException, MalformedURLException {
        load(uri);
    }

    /**
     * Loads and parse CME MDP Configuration.
     *
     * @param uri URI to CME MDP Configuration (config.xml, usually available on CME FTP)
     * @throws ConfigurationException if failed to parse configuration file
     * @throws MalformedURLException  if URI to Configuration is malformed
     */
    private void load(final URI uri) throws ConfigurationException, MalformedURLException {
        // todo: if to implement the same via standard JAXB then dep to apache commons configuration will be not required
        final XMLConfiguration configuration = new XMLConfiguration();
        configuration.setDelimiterParsingDisabled(true);
        configuration.load(uri.toURL());
        for (final HierarchicalConfiguration channelCfg : configuration.configurationsAt("channel")) {
            final ChannelCfg channel = new ChannelCfg(channelCfg.getString("[@id]"), channelCfg.getString("[@label]"));

            for (final HierarchicalConfiguration connCfg : channelCfg.configurationsAt("connections.connection")) {
                final String id = connCfg.getString("[@id]");
                final FeedType type = FeedType.valueOf(connCfg.getString("type[@feed-type]"));
                final TransportProtocol protocol = TransportProtocol.valueOf(connCfg.getString("protocol").substring(0, 3));
                final Feed feed = Feed.valueOf(connCfg.getString("feed"));
                final String ip = connCfg.getString("ip");
                final int port = connCfg.getInt("port");
                final List<String> hostIPs = Arrays.asList(connCfg.getStringArray("host-ip"));

                final ConnectionCfg connection = new ConnectionCfg(feed, id, type, protocol, ip, hostIPs, port);
                channel.addConnection(connection);
                connCfgs.put(connection.getId(), connection);
            }
            channelCfgs.put(channel.getId(), channel);
        }
    }

    /**
     * Get configuration of Channel by Channel Id.
     *
     * @param id Channel Id
     * @return Channel configuration or NULL if not contains
     */
    public ChannelCfg getChannel(final String id) {
        return channelCfgs.get(id);
    }

    /**
     * Get configuration of Connection by Connection Id.
     *
     * @param id Connection Id
     * @return Connection configuration or NULL if not contains
     */
    public ConnectionCfg getConnection(final String id) {
        return connCfgs.get(id);
    }
}
