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

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;

public class SbeGroup extends SbeGroupFieldSet implements FieldSet, MdpGroup, MutableMdpGroup {
    private int numInGroup;
    private int groupBodyOffset;
    private int entryNum = 0;

    public static MdpGroup instance() {
        return new SbeGroup();
    }

    @Override
    public void reset(final SbeBuffer buffer, final SbeGroupType sbeGroupType, final int blockLength, final int numInGroup, final int groupBodyOffset) {
        this.sbeBuffer = buffer;
        this.entryNum = 0;
        this.sbeGroupType = sbeGroupType;
        this.blockLength = blockLength;
        this.numInGroup = numInGroup;
        this.groupBodyOffset = groupBodyOffset;
    }

    @Override
    public int getNumInGroup() {
        return this.numInGroup;
    }

    @Override
    public int getEntryNum() {
        return entryNum;
    }

    @Override
    public boolean hashNext() {
        return entryNum < numInGroup;
    }

    @Override
    public void next() {
        if (entryNum <= numInGroup) {
            this.entryNum++;
            this.entryOffset = groupBodyOffset + (entryNum - 1) * blockLength;
        } else {
            throw new IllegalStateException("Out of group size");
        }
    }

    @Override
    public void getEntry(final MdpGroupEntry groupEntry) {
        ((MutableMdpGroupEntry) groupEntry).reset(this.buffer(), this.sbeGroupType, this.entryOffset, this.blockLength);
    }

    @Override
    public void getEntry(int entryNum, MdpGroupEntry groupEntry) {
        if (entryNum > numInGroup) {
            throw new IllegalArgumentException("Out of group size");
        }
        int entryOffset = groupBodyOffset + ((entryNum -1) * blockLength);
        ((MutableMdpGroupEntry) groupEntry).reset(this.buffer(), this.sbeGroupType, entryOffset, this.blockLength);
    }

    @Override
    public MdpGroup copy() {
        final MutableMdpGroup copyInstance = (MutableMdpGroup) instance();
        copyInstance.reset(buffer().copy(), this.sbeGroupType, this.blockLength, this.numInGroup, this.groupBodyOffset);
        return copyInstance;
    }
}
