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

package com.epam.cme.mdp3;

import com.epam.cme.mdp3.sbe.message.SbeBuffer;
import com.epam.cme.mdp3.sbe.message.SbeBufferImpl;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.SbeMessage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.*;

/**
 * MDP Packet. Contains methods to create and to iterate MDP Messages in it.
 */
public class MdpPacket implements Iterable<MdpMessage> {
    private SbeBuffer sbeBuffer;
    private MdpMessageIterator mdpMessageIterator = new MdpMessageIterator();
    private SbeMessage sbeMessage;

    public MdpPacket() {
        this.sbeBuffer = new SbeBufferImpl();
        this.sbeMessage = new SbeMessage();
    }

    public MdpPacket(final SbeBuffer sbeBuffer) {
        this.sbeBuffer = sbeBuffer;
        this.sbeMessage = new SbeMessage();
    }

    /**
     * Creates MDP packet in off-heap buffer.
     *
     * @return MDP Packet instance with allocates back buffer
     */
    public static MdpPacket instance() {
        return new MdpPacket();
    }

    /**
     * Allocates new instance of MDP Packet and related byte buffers
     *
     * @return new instance of MDP Packet
     */
    public static MdpPacket allocate() {
        final MdpPacket packet = instance();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SbeConstants.MDP_PACKET_MAX_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        packet.wrapFromBuffer(byteBuffer);
        return packet;
    }

    /**
     * Creates copy of this MDP Packet instance
     *
     * @return copy instance of MDP Packet
     */
    public MdpPacket copy() {
        final MdpPacket copyInstance = allocate();
        copyInstance.buffer().copyFrom(this.buffer());
        return copyInstance;
    }

    /**
     * Gets underlying buffer.
     *
     * @return Buffer
     */
    public SbeBuffer buffer() {
        return sbeBuffer;
    }

    /**
     * Wraps underlying buffer with another instance.
     *
     * @param sbeBuffer Buffer
     */
    public void wrapFromBuffer(final SbeBuffer sbeBuffer) {
        this.sbeBuffer = sbeBuffer;
    }

    /**
     * Wraps back buffer with data from byte buffer.
     *
     * @param bb Buffer
     */
    public void wrapFromBuffer(final ByteBuffer bb) {
        this.sbeBuffer.wrapForParse(bb);
    }

    /**
     * Gets actual MDP Packet size
     *
     * @return MDP Packet size
     */
    public int getPacketSize() {
        return sbeBuffer.length();
    }

    /**
     * Extracts and gets sequence number.
     *
     * @return Packet sequence number as long
     */
    public long getMsgSeqNum() {
        sbeBuffer.position(MESSAGE_SEQ_NUM_OFFSET);
        return sbeBuffer.getUInt32();
    }

    /**
     * Extracts sending time from MDP Packet
     *
     * @return sending time (timestamp)
     */
    public long getSendingTime() {
        sbeBuffer.position(MESSAGE_SENDING_TIME_OFFSET);
        return sbeBuffer.getUInt64();
    }

    @Override
    public String toString() {
        return "MdpPacket{" +
                "msgSeqNum=" + getMsgSeqNum() +
                ", sbeBuffer=" + sbeBuffer.toString() +
                '}';
    }

    /**
     * Releases this MDP Packets and allocated byte buffers
     */
    public void release() {
        this.buffer().release();
    }

    /**
     * Initializes and gets Iterator of MDP Messages in this MDP Packet.
     *
     * @return Iterator of MDP Messages in this MDP Packet
     */
    @Override
    public Iterator<MdpMessage> iterator() {
        return mdpMessageIterator.init(this.buffer().offset() + MDP_HEADER_SIZE, this.buffer().length() - MDP_HEADER_SIZE);
    }

    /**
     * Sets actual received length to this MDP Packet holder
     * @param length actual received length to this MDP Packet holder
     * @return this MDP Packet instance
     */
    public MdpPacket length(final int length) {
        this.sbeBuffer.length(length);
        return this;
    }

    /**
     * Iterator of MDP Messages in MDP Packet.
     */
    class MdpMessageIterator implements Iterator<MdpMessage> {
        // offset of the message start in the byteBuffer
        int offset;
        // the max packet offset in the byteBuffer
        int packetMaxOffset;

        MdpMessageIterator() {
        }

        MdpMessageIterator init(int offset, int packetSize) {
            this.offset = offset;
            this.packetMaxOffset = offset + packetSize;
            return this;
        }

        @Override
        public boolean hasNext() {
            return offset < packetMaxOffset;
        }

        @Override
        public SbeMessage next() {
            if (hasNext()) {
                sbeMessage.buffer().wrap(sbeBuffer);
                sbeMessage.buffer().offset(offset);
                final int messageLength = sbeMessage.getMsgSize();
                sbeMessage.buffer().length(messageLength);
                offset += messageLength;
                return sbeMessage;
            } else {
                throw new NoSuchElementException("MDPMessageIterator error: no more items");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("MDEntryIterator error: remove operation is not supported");
        }
    }
}
