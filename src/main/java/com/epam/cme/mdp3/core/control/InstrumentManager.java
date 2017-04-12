package com.epam.cme.mdp3.core.control;


public interface InstrumentManager {
    MBOInstrumentController getMBOInstrumentController(int securityId);
    void registerSecurity(int securityId, String secDesc, int subscriptionFlags, byte maxDepth);
    void discontinueSecurity(int securityId);
}
