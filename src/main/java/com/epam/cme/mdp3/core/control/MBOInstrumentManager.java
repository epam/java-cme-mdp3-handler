package com.epam.cme.mdp3.core.control;


import com.epam.cme.mdp3.ChannelListener;
import net.openhft.koloboke.collect.map.IntObjMap;
import net.openhft.koloboke.collect.map.hash.HashIntObjMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class MBOInstrumentManager implements InstrumentManager {
    private static final Logger log = LoggerFactory.getLogger(MBOInstrumentManager.class);
    private final IntObjMap<MBOInstrumentController> instruments = HashIntObjMaps.newMutableMap();
    private final String channelId;
    private final List<ChannelListener> listeners;

    public MBOInstrumentManager(String channelId, List<ChannelListener> listeners) {
        this.channelId = channelId;
        this.listeners = listeners;
    }

    @Override
    public MBOInstrumentController getMBOInstrumentController(int securityId) {
        return instruments.get(securityId);
    }

    @Override
    public void registerSecurity(int securityId, String secDesc, int subscriptionFlags, byte maxDepth) {
        MBOInstrumentController mboInstrumentController = instruments.get(securityId);
        if(mboInstrumentController == null) {
            instruments.put(securityId, new MBOInstrumentController(listeners, channelId, securityId, secDesc));
        } else {
            mboInstrumentController.enable();
        }
    }

    @Override
    public void discontinueSecurity(int securityId) {
        MBOInstrumentController mboInstrumentController = instruments.get(securityId);
        if(mboInstrumentController == null) {
            log.warn("discontinueSecurity method was called but there is no security with id '{}'", securityId);
        } else {
            mboInstrumentController.disable();
        }
    }
}
