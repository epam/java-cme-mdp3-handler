package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;

public class StubChannelController implements ChannelController {
    @Override
    public void handleSnapshotPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {}

    @Override
    public void handleIncrementalPacket(MdpFeedContext feedContext, MdpPacket mdpPacket) {}
}
