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

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;

public class SbeString {
    private char[] chars;
    private int length;

    public static SbeString allocate(final int capacity) {
        return new SbeString(new char[capacity]);
    }

    private SbeString(final char[] chars) {
        this.chars = chars;
    }

    public char[] getChars() {
        return chars;
    }

    public char getCharAt(final int idx) {
        return chars[idx];
    }

    public int getCapacity() {
        return this.chars.length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length <= chars.length ? length : chars.length;
    }

    public String getString() {
        return new String(chars, 0, length).trim();
    }

    @Override
    public String toString() {
        return getString();
    }

    public SbeString reset() {
        setLength(0);
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) SbePrimitiveType.Char.getNullValue();
        }
        return this;
    }
}
