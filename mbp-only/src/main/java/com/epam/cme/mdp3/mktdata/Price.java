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

package com.epam.cme.mdp3.mktdata;

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Price {
    private long mantissa;
    private byte exponent = -7;

    public long getMantissa() {
        return mantissa;
    }

    public void setMantissa(long mantissa) {
        this.mantissa = mantissa;
    }

    public boolean isNull() {
        return this.mantissa == SbePrimitiveType.Int64.getNullValue();
    }

    public void setNull() {
        this.mantissa = SbePrimitiveType.Int64.getNullValue();
    }

    public double asDouble() {
        return getMantissa() * Math.pow(10, exponent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        return new EqualsBuilder()
                .append(mantissa, price.mantissa)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mantissa)
                .toHashCode();
    }
}
