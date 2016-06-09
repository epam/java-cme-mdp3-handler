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

package com.epam.cme.mdp3.sbe.message.meta;

import com.epam.cme.mdp3.sbe.schema.vo.CompositeDataType;
import com.epam.cme.mdp3.sbe.schema.vo.EncodedDataType;
import com.epam.cme.mdp3.sbe.schema.vo.GroupType;

public class SbeGroupType {
    private final MetadataContainer fieldContainer;
    private final GroupType groupType;
    private final CompositeDataType dimensionType;
    private int numInGroupOffset;
    private int dimensionBlockLength;

    public SbeGroupType(final MetadataContainer metadataContainer, final GroupType groupType, final CompositeDataType dimensionType) {
        this.fieldContainer = metadataContainer;
        this.groupType = groupType;
        this.dimensionType = dimensionType;
        calcDimensionBlockFields();
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public int getNumInGroupOffset() {
        return numInGroupOffset;
    }

    public int getDimensionBlockLength() {
        return dimensionBlockLength;
    }

    public void calcDimensionBlockFields() {
        final EncodedDataType blockLength = this.dimensionType.getType().get(0);
        this.numInGroupOffset = SbePrimitiveType.fromString(blockLength.getPrimitiveType()).getSize();
        final EncodedDataType numInGroup = this.dimensionType.getType().get(1);
        this.dimensionBlockLength = this.numInGroupOffset + SbePrimitiveType.fromString(numInGroup.getPrimitiveType()).getSize();
    }

    public MetadataContainer getMetadataContainer() {
        return fieldContainer;
    }
}
