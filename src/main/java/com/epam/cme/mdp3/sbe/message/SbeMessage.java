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
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.message.meta.MetadataContainer;
import com.epam.cme.mdp3.sbe.message.meta.SbeFieldType;
import com.epam.cme.mdp3.sbe.message.meta.SbeGroupType;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.*;

public class SbeMessage extends AbstractFieldSet implements FieldSet, MdpMessage {
    private MdpMessageType messageType;

    public SbeMessage() {
        super();
        this.sbeBuffer = new SbeBufferImpl();
    }

    public int getMsgSize() {
        buffer().position(MSG_SIZE_OFFSET);
        return buffer().getUInt16();
    }

    public int getBlockLength() {
        buffer().position(BLOCK_LENGTH_OFFSET);
        return buffer().getUInt16();
    }

    @Override
    public int getSchemaId() {
        buffer().position(TEMPLATE_ID_OFFSET);
        return buffer().getUInt16();
    }

    public int getVersion() {
        buffer().position(VERSION_OFFSET);
        return buffer().getUInt8();
    }

    public int getReserved() {
        buffer().position(RESERVED_OFFSET);
        return buffer().getUInt8();
    }

    public MdpMessageType getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(MdpMessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "MDPMessage{" +
                "schemaId='" + getSchemaId() + '\'' +
                ", blockLength='" + getBlockLength() + '\'' +
                ", msgSize='" + getMsgSize() + '\'' +
                '}';
    }

    @Override
    public SemanticMsgType getSemanticMsgType() {
        return getMessageType().getSemanticMsgType();
    }

    @Override
    protected MetadataContainer metadata() {
        return getMessageType().getMetadataContainer();
    }

    @Override
    protected void seek(final int tagId) {
        seek(metadata().findField(tagId));
    }

    @Override
    protected void seek(final SbeFieldType field) {
        field.seek(this.buffer());
    }

    @Override
    public boolean getGroup(final int tagId, final MdpGroup mdpGroup) {
        final MutableMdpGroup mutableMdpGroup = (MutableMdpGroup) mdpGroup;

        int groupOffset = SbeConstants.HEADER_SIZE + getBlockLength();

        for (final SbeGroupType groupType : messageType.getMetadataContainer().allGroups()) {
            this.buffer().position(groupOffset);
            final int blockLength = this.buffer().getUInt16();
            this.buffer().position(groupOffset + groupType.getNumInGroupOffset());
            final int numInGroup = this.buffer().getUInt8();

            if (groupType.getGroupType().getId() == tagId) {
                mutableMdpGroup.reset(this.buffer(), groupType, blockLength, numInGroup, groupOffset + groupType.getDimensionBlockLength());
                return true;
            } else {
                groupOffset = groupOffset + groupType.getDimensionBlockLength() + blockLength * numInGroup;
            }
        }
        return false;
    }

    @Override
    public MdpMessage copy() {
        final SbeMessage copyInstance = new SbeMessage();
        copyInstance.buffer().copyFrom(this.buffer());
        copyInstance.messageType = this.messageType;
        return copyInstance;
    }
}
