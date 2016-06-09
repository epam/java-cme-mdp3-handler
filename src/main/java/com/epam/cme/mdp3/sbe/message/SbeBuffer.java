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

public interface SbeBuffer {
    void wrap(final SbeBuffer sb);

    /**
     * Wraps back buffer with data from byte buffer.
     *
     * @param bb Buffer
     */
    void wrapForParse(final ByteBuffer bb);

    void copyTo(BytesStore store);

    void copyFrom(BytesStore store);

    void copyFrom(SbeBuffer buffer);

    SbeBuffer copy();

    void release();

    int offset();

    SbeBuffer offset(final int offset);

    int length();

    SbeBuffer length(final int length);

    int position();

    SbeBuffer position(final int pos);

    char getChar();

    short getUInt8();

    byte getInt8();

    short getInt16();

    int getUInt16();

    int getInt32();

    long getUInt32();

    long getInt64();

    long getUInt64();

    void getChars(final char[] chars, final int len);
}
