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

package com.epam.cme.mdp3.test.def;

import com.epam.cme.mdp3.ChannelListener;
import com.epam.cme.mdp3.MdpChannel;
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import cucumber.api.java.After;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MdpHandlerHolder {
    private final Map<String, MdpChannel> openHandlers = new HashMap<>();
    private MdpMessageTypes mdpMessageTypes;

    {
        try {
            mdpMessageTypes = new MdpMessageTypes(getClass().getResource("/templates_FixBinary.xml").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MdpChannel get(final int id) {
        return openHandlers.get(String.valueOf(id));
    }

    public MdpChannel get(final String id) {
        return openHandlers.get(id);
    }

    public MdpChannel instance(final int channelId, final ChannelListener listener) throws Exception {
        return instance(UUID.randomUUID().toString(), channelId, listener);
    }

    public MdpChannel instance(final int channelId) throws Exception {
        return instance(channelId, null);
    }

    public MdpChannel instance(final int id, final int channelId) throws Exception {
        return instance(String.valueOf(id), channelId);
    }

    public MdpChannel instance(final String id, final int channelId) throws Exception {
        return instance(id, channelId, null);
    }

    public MdpChannel instance(final String id, final int channelId, final ChannelListener listener) throws Exception {
        final MdpChannel mdpHandler = new MdpChannelBuilder(String.valueOf(channelId),
                getClass().getResource("/config.xml").toURI(), getClass().getResource("/templates_FIXBinary.xml").toURI())
                .usingListener(listener)
                .build();

        registerOpenChannel(id, mdpHandler);
        return mdpHandler;
    }

    private void registerOpenChannel(final String id, final MdpChannel handler) {
        openHandlers.put(id, handler);
    }

    @After
    public void release() {
        openHandlers.values().forEach(MdpChannel::close);
        openHandlers.clear();
        //System.runFinalization();
        System.gc();
    }

    public MdpMessageTypes getMdpMessageTypes() {
        return mdpMessageTypes;
    }
}
