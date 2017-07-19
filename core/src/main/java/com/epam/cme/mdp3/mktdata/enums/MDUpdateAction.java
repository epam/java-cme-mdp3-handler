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

public enum MDUpdateAction {
    New((short) 0),
    Change((short) 1),
    Delete((short) 2),
    DeleteThru((short) 3),
    DeleteFrom((short) 4),
    Overlay((short) 5);

    final short value;

    MDUpdateAction(final short value) {
        this.value = value;
    }

    public static MDUpdateAction fromFIX(final short val) {
        switch (val) {
            case 0:
                return New;
            case 1:
                return Change;
            case 2:
                return Delete;
            case 3:
                return DeleteThru;
            case 4:
                return DeleteFrom;
            case 5:
                return Overlay;
            default:
                throw new IllegalStateException("Not implemented value: " + val);
        }
    }
}
