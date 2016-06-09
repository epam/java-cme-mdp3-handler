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

public class SbeDouble {
    public static SbeDouble instance() {
        return new SbeDouble();
    }

    private boolean isNull;
    private long mantissa;
    private byte exponent;

    public byte getExponent() {
        return exponent;
    }

    public void setExponent(byte exponent) {
        this.exponent = exponent;
    }

    public long getMantissa() {
        return mantissa;
    }

    public void setMantissa(long mantissa) {
        this.mantissa = mantissa;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    public double asDouble() {
        return getMantissa() * Math.pow(10, getExponent());
    }

    void reset() {
        setNull(false);
        setMantissa(SbePrimitiveType.UInt64.getNullValue());
        setExponent((byte) SbePrimitiveType.Int8.getNullValue());
    }

    SbeDouble duplicate() {
        SbeDouble copy = new SbeDouble();
        copy.setMantissa(getMantissa());
        copy.setExponent(getExponent());
        copy.setNull(isNull());
        return copy;
    }
}
