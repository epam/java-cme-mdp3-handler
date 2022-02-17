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

import net.openhft.chronicle.bytes.BytesStore;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SbeBufferImpl extends AbstractSbeBuffer implements SbeBuffer {
    public static final byte BYTE_MASK = (byte) 0xff;
    protected BytesStore<?, ?> bytes;

    @Override
    public void wrap(final SbeBuffer sb) {
        final SbeBufferImpl buffer = (SbeBufferImpl) sb; // can cast because the handler always use one implementation of buffer in runtime
        this.bytes = buffer.bytes;
        this.offset = buffer.offset;
        this.length = buffer.length;
        this.position = buffer.position;
    }

    @Override
    public void copyTo(final BytesStore<?, ?> store) {
        store.write(0, this.bytes, 0, this.length());
    }

    @Override
    public void copyTo(final int offset, final BytesStore<?, ?> store, final int len) {
        store.write(0, this.bytes, offset, len);
    }

    @Override
    public void copyFrom(BytesStore<?, ?> store) {
        this.bytes.write(0, store, 0, store.length());
        this.length = store.length();
    }

    @Override
    public void copyFrom(SbeBuffer buffer) {
        final SbeBufferImpl bufferImpl = (SbeBufferImpl) buffer;
        final long fromLength = bufferImpl.bytes.length();
        if (this.bytes == null) {
            this.bytes = BytesStore.wrap(ByteBuffer.allocateDirect((int) bufferImpl.bytes.realCapacity()).order(ByteOrder.LITTLE_ENDIAN));
        }
        final long toLength = bytes.length();
        if(toLength < fromLength) {
            bytes.write(0, bufferImpl.bytes, 0, toLength);
        } else {
            bytes.write(0, bufferImpl.bytes, 0, fromLength);
        }
    }

    @Override
    public SbeBuffer copy() {
        final SbeBuffer copyInstance = new SbeBufferImpl();
        copyInstance.copyFrom(this);
        copyInstance.offset(offset());
        copyInstance.length(length());
        copyInstance.position(position());
        return copyInstance;
    }

    @Override
    public void wrapForParse(final ByteBuffer bb) {
        this.length = bb.limit();
        //this.bytes = Bytes.wrapForRead(bb);
        this.bytes = BytesStore.wrap(bb);
    }

    @Override
    public void release() {
        this.bytes.releaseLast();
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
        //this.bytes.
        return this;
    }

    @Override
    public int position() {
        return this.position;
    }

    @Override
    public SbeBuffer position(int pos) {
        this.position = offset() + pos;
        //this.bytes.readPosition(position);
        return this;
    }

    @Override
    public char getChar() {
        return (char) bytes.readByte(this.position);
    }

    @Override
    public short getUInt8() {
        return (short) bytes.readUnsignedByte(this.position);
    }

    @Override
    public byte getInt8() {
        return bytes.readByte(this.position);
    }

    @Override
    public short getInt16() {
        return bytes.readShort(this.position);
    }

    @Override
    public int getUInt16() {
        return bytes.readUnsignedShort(this.position);
    }

    @Override
    public int getInt32() {
        return bytes.readInt(this.position);
    }

    @Override
    public long getUInt32() {
        return bytes.readUnsignedInt(this.position);
    }

    @Override
    public long getInt64() {
        return bytes.readLong(this.position);
    }

    @Override
    public long getUInt64() {
        return bytes.readLong(this.position);
    }

    @Override
    public boolean isUInt64NULL() {
        for (int i = 0; i < Long.BYTES; i++) {
            final byte b = bytes.readByte(this.position+i);
            if (b != BYTE_MASK) return false;
        }
        return true;
    }

    @Override
    public void getChars(final char[] chars, final int len) {
        if (len > chars.length) {
            throw new IllegalArgumentException(String.format("Char array length %1$d less then requested length %2$d", chars.length, len));
        }

        for (int i = 0; i < len; i++) {
            chars[i] = (char) bytes.readByte(this.position+i);
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
