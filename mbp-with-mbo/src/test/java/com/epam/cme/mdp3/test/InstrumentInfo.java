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

package com.epam.cme.mdp3.test;

public class InstrumentInfo {
    final int instrumentId;
    final String desc;

    public InstrumentInfo(final int instrumentId, final String desc) {
        this.instrumentId = instrumentId;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentInfo that = (InstrumentInfo) o;

        if (instrumentId != that.instrumentId) return false;
        return desc.equals(that.desc);

    }

    @Override
    public int hashCode() {
        int result = instrumentId;
        result = 31 * result + desc.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" + instrumentId + ", '" + desc + "'}";
    }
}
