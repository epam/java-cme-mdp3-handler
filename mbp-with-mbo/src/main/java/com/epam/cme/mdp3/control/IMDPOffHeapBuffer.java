package com.epam.cme.mdp3.control;

import com.epam.cme.mdp3.MdpPacket;

public interface IMDPOffHeapBuffer {
    boolean exist(final long msgSeqNum);
    MdpPacket remove(final long msgSeqNum);
    void add(final long msgSeqNum, final MdpPacket packet);
    long getLastMsgSeqNum();
    void clear();
    void clear(final long msgSeqNum);
}
