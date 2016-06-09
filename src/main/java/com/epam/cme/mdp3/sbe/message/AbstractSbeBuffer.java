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

public abstract class AbstractSbeBuffer implements SbeBuffer {
    protected int offset = 0;
    protected int length;
    protected int position = 0;

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public int offset() {
        return this.offset;
    }

    @Override
    public SbeBuffer offset(final int offset) {
        this.offset = offset;
        if (this.position < offset) this.position = offset;
        return this;
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
    public SbeBuffer position(final int pos) {
        this.position = offset() + pos;
        return this;
    }
}
