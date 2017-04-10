package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.MdpMessage;

public class MBOInstrumentController {
    public void handleIncrementMDEntry(MdpMessage mdpMessage, MdpGroupEntry mdpGroup){
        System.out.println(mdpGroup);
    }

    public void handleSnapshotMDEntry(MdpMessage mdpMessage, MdpGroupEntry mdpGroup){
        System.out.println(mdpGroup);
    }
}
