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

package com.epam.cme.mdp3.sbe.message;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesStore;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SbeBufferImpl extends AbstractSbeBuffer implements SbeBuffer {
    protected Bytes<ByteBuffer> bytes;

    @Override
    public void wrap(final SbeBuffer sb) {
        final SbeBufferImpl buffer = (SbeBufferImpl) sb; // can cast because the handler always use one implementation of buffer in runtime
        this.bytes = buffer.bytes;
        this.offset = buffer.offset;
        this.length = buffer.length;
        this.position = buffer.position;
    }

    @Override
    public int copyTo(BytesStore store) {
        return (int) this.bytes.copyTo(store);
    }

    @Override
    public int copyFrom(BytesStore store) {
        final int len = (int) store.copyTo(this.bytes);
        this.length = len;
        return len;
    }

    @Override
    public int copyFrom(SbeBuffer buffer) {
        final SbeBufferImpl bufferImpl = (SbeBufferImpl) buffer;
        if (this.bytes == null) {
            this.bytes = Bytes.wrapForRead(ByteBuffer.allocateDirect((int) bufferImpl.bytes.realCapacity()).order(ByteOrder.LITTLE_ENDIAN));
        }
        final int len = (int) bufferImpl.bytes.copyTo(this.bytes);
        this.offset(buffer.offset());
        this.length = buffer.length();
        return len;
    }

    @Override
    public SbeBuffer copy() {
        final SbeBuffer copyInstance = new SbeBufferImpl();
        copyInstance.copyFrom(this);
        return copyInstance;
    }

    @Override
    public void wrapForParse(final ByteBuffer bb) {
        this.length = bb.limit();
        this.bytes = Bytes.wrapForRead(bb);
    }

    @Override
    public void release() {
        this.bytes.release();
    }

    @Override
    public int offset() {
        return offset;
    }

    @Override
    public SbeBuffer offset(final int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public SbeBuffer length(final int length) {
        this.length = length;
        return this;
    }

    @Override
    public int position() {
        return this.position;
    }

    @Override
    public SbeBuffer position(int pos) {
        this.position = offset() + pos;
        this.bytes.readPosition(position);
        return this;
    }

    @Override
    public char getChar() {
        return (char) bytes.readByte();
    }

    @Override
    public short getUInt8() {
        return (short) bytes.readUnsignedByte();
    }

    @Override
    public byte getInt8() {
        return bytes.readByte();
    }

    @Override
    public short getInt16() {
        return bytes.readShort();
    }

    @Override
    public int getUInt16() {
        return bytes.readUnsignedShort();
    }

    @Override
    public int getInt32() {
        return bytes.readInt();
    }

    @Override
    public long getUInt32() {
        return bytes.readUnsignedInt();
    }

    @Override
    public long getInt64() {
        return bytes.readLong();
    }

    @Override
    public long getUInt64() {
        return bytes.readLong();
    }

    @Override
    public void getChars(final char[] chars, final int len) {
        if (len > chars.length) {
            throw new IllegalArgumentException(String.format("Char array length %1$d less then requested length %2$d", chars.length, len));
        }

        for (int i = 0; i < len; i++) {
            chars[i] = (char) bytes.readByte();
        }
    }

    @Override
    public String toString() {
        return "SbeBufferImpl{" +
                "offset=" + offset +
                ", length=" + length +
                ", position=" + position +
                '}';
    }
}
