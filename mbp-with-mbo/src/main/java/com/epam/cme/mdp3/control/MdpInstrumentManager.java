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

package com.epam.cme.mdp3.control;


import com.epam.cme.mdp3.ChannelListener;
import net.openhft.koloboke.collect.map.IntObjMap;
import net.openhft.koloboke.collect.map.hash.HashIntObjMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class MdpInstrumentManager implements InstrumentManager {
    private static final Logger log = LoggerFactory.getLogger(MdpInstrumentManager.class);
    private final IntObjMap<InstrumentController> instruments = HashIntObjMaps.newMutableMap();
    private final String channelId;
    private final List<ChannelListener> listeners;

    public MdpInstrumentManager(String channelId, List<ChannelListener> listeners) {
        this.channelId = channelId;
        this.listeners = listeners;
    }

    @Override
    public InstrumentController getInstrumentController(int securityId) {
        return instruments.get(securityId);
    }

    @Override
    public void registerSecurity(int securityId, String secDesc) {
        InstrumentController instrumentController = instruments.get(securityId);
        if(instrumentController == null) {
            instruments.put(securityId, new InstrumentController(listeners, channelId, securityId, secDesc));
        } else {
            instrumentController.enable();
        }
    }

    @Override
    public void discontinueSecurity(int securityId) {
        InstrumentController instrumentController = instruments.get(securityId);
        if(instrumentController == null) {
            log.warn("discontinueSecurity method was called but there is no security with id '{}'", securityId);
        } else {
            instrumentController.disable();
        }
    }

    @Override
    public void updateSecDesc(int securityId, String secDesc) {
        InstrumentController instrumentController = instruments.get(securityId);
        if(instrumentController == null) {
            log.warn("updateSecDesc method was called but there is no security with id '{}'", securityId);
        } else {
            instrumentController.updateSecDesc(secDesc);
        }
    }
}
