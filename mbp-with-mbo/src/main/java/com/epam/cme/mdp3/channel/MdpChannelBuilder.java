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

package com.epam.cme.mdp3.channel;

import com.epam.cme.mdp3.ChannelListener;
import com.epam.cme.mdp3.Feed;
import com.epam.cme.mdp3.FeedType;
import com.epam.cme.mdp3.MdpChannel;
import com.epam.cme.mdp3.control.GapChannelController;
import com.epam.cme.mdp3.core.cfg.Configuration;
import com.epam.cme.mdp3.core.channel.MdpFeedWorker;
import com.epam.cme.mdp3.core.channel.tcp.MdpTCPMessageRequester;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypeBuildException;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class MdpChannelBuilder {
    public static final int DEF_INCR_QUEUE_SIZE = 15000;
    public static final int DEF_GAP_THRESHOLD = 5;
    private final String channelId;
    private URI cfgURI;
    private URI schemaURI;
    private Map<FeedType, String> feedANetworkInterfaces = new HashMap<>();
    private Map<FeedType, String> feedBNetworkInterfaces = new HashMap<>();
    private ChannelListener channelListener;
    private ScheduledExecutorService scheduler;
    private int incrQueueSize = DEF_INCR_QUEUE_SIZE;
    private int gapThreshold = DEF_GAP_THRESHOLD;
    private int rcvBufSize = MdpFeedWorker.RCV_BUFFER_SIZE;
    private String tcpUsername = MdpTCPMessageRequester.DEFAULT_USERNAME;
    private String tcpPassword = MdpTCPMessageRequester.DEFAULT_PASSWORD;
    private int maxNumberOfTCPAttempts = GapChannelController.MAX_NUMBER_OF_TCP_ATTEMPTS;
    private boolean mboEnabled;
    private List<Integer> incrementMessageTemplateIds;
    private List<Integer> snapshotMessageTemplateIds;
    private Feed snptFeedToUse = Feed.A;
    
    public MdpChannelBuilder(final String channelId) {
        this.channelId = channelId;
    }

    public MdpChannelBuilder(final String channelId, final URI cfgURI, final URI schemaURI) throws MdpMessageTypeBuildException {
        this.channelId = channelId;
        this.cfgURI = cfgURI;
        setSchema(schemaURI);
    }

    public MdpChannelBuilder setConfiguration(final URI cfgURI) {
        this.cfgURI = cfgURI;
        return this;
    }

    public MdpChannelBuilder setSchema(final URI schemaURI) throws MdpMessageTypeBuildException {
        this.schemaURI = schemaURI;
        return this;
    }

    public MdpChannelBuilder setNetworkInterface(final FeedType feedType, final Feed feed, final String networkInterface) {
        if (feed == Feed.A) {
            feedANetworkInterfaces.put(feedType, networkInterface);
        } else if (feed == Feed.B) {
            feedBNetworkInterfaces.put(feedType, networkInterface);
        }
        return this;
    }

    public MdpChannelBuilder usingListener(final ChannelListener channelListener) {
        this.channelListener = channelListener;
        return this;
    }

    public MdpChannelBuilder usingScheduler(final ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public MdpChannelBuilder usingIncrQueueSize(final int incrQueueSize) {
        this.incrQueueSize = incrQueueSize;
        return this;
    }

    public MdpChannelBuilder usingGapThreshold(final int gapThreshold) {
        this.gapThreshold = gapThreshold;
        return this;
    }

    public MdpChannelBuilder usingRcvBufSize(final int rcvBufSize) {
        this.rcvBufSize = rcvBufSize;
        return this;
    }

    public MdpChannelBuilder usingMaxNumberOfTCPAttempts(final int maxTcpAttempts) {
    	this.maxNumberOfTCPAttempts = maxTcpAttempts;
    	return this;
    }
    
    public MdpChannelBuilder setTcpUsername(final String tcpUsername) {
        this.tcpUsername = tcpUsername;
        return this;
    }

    public MdpChannelBuilder setTcpPassword(final String tcpPassword) {
        this.tcpPassword = tcpPassword;
        return this;
    }

    public MdpChannelBuilder setMBOEnable(final boolean enabled) {
        mboEnabled = enabled;
        return this;
    }
    
    public MdpChannelBuilder setIncrementMessageTemplateIds(final List<Integer> ids) {
    	incrementMessageTemplateIds = ids;
        return this;
    }
    
    public MdpChannelBuilder setSnapshotMessageTemplateIds(final List<Integer> ids) {
    	snapshotMessageTemplateIds = ids;
        return this;
    }

    public MdpChannelBuilder setSnapshotFeedToUse(final Feed snptFeedToUse) {
    	this.snptFeedToUse = snptFeedToUse;
    	return this;
    }
    
    public MdpChannel build() {
        try {
            final Configuration cfg = new Configuration(this.cfgURI);
            final MdpMessageTypes mdpMessageTypes = new MdpMessageTypes(this.schemaURI);
            MdpChannel mdpChannel = new LowLevelMdpChannel(scheduler, cfg.getChannel(this.channelId), mdpMessageTypes,
                     incrQueueSize, rcvBufSize, gapThreshold, maxNumberOfTCPAttempts, tcpUsername, tcpPassword, feedANetworkInterfaces, feedBNetworkInterfaces, 
                     mboEnabled, incrementMessageTemplateIds, snapshotMessageTemplateIds, snptFeedToUse);
            if (channelListener != null) mdpChannel.registerListener(channelListener);
            return mdpChannel;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build MDP Channel", e);
        }
    }
}
