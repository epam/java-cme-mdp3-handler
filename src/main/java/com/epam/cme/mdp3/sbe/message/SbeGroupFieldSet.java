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

import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.sbe.message.meta.MetadataContainer;
import com.epam.cme.mdp3.sbe.message.meta.SbeFieldType;
import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;

abstract class SbeGroupFieldSet extends AbstractFieldSet {
    SbeGroupType sbeGroupType;
    int entryOffset;
    int blockLength;

    public SbeGroupType getSbeGroupType() {
        return sbeGroupType;
    }

    public int getAbsoluteEntryOffset() {
        return this.buffer().offset() + this.entryOffset;
    }

    public int getBlockLength() {
        return this.blockLength;
    }

    @Override
    protected MetadataContainer metadata() {
        return getSbeGroupType().getMetadataContainer();
    }

    @Override
    protected void seek(final SbeFieldType field) {
        int offset;
        if (field.getFieldType().getOffset() != null) {
            offset = entryOffset + field.getFieldType().getOffset().intValue();
        } else {
            offset = entryOffset + this.blockLength - field.getPrimitiveType().getSize();
        }
        this.buffer().position(offset);
    }

    @Override
    protected void seek(final int tagId) {
        seek(metadata().findField(tagId));
    }

    @Override
    public boolean getGroup(int tagId, MdpGroup mdpGroup) {
        throw new UnsupportedOperationException();
    }
}
