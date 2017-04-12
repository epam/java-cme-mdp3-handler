package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;

public interface ChannelController {
    void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket);
    void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket);
}
