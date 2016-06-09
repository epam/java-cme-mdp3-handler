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

package com.epam.cme.mdp3.mktdata.enums;

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;

public enum Side {
    Buy((byte) 1),
    Sell((byte) 2),
    Cross((byte) 8);

    final byte value;

    Side(final byte value) {
        this.value = value;
    }

    public static Side fromFIX(final byte val) {
        switch (val) {
            case 0:
                return null;
            case 1:
                return Buy;
            case 2:
                return Sell;
            case 8:
                return Cross;
            default:
                if (SbePrimitiveType.Int8.isNull(val)) return null;
                else throw new IllegalStateException("Not implemented value: " + (char) val);
        }
    }
}
