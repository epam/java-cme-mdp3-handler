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

import com.epam.cme.mdp3.FieldSet;
import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.sbe.message.meta.MetadataContainer;
import com.epam.cme.mdp3.sbe.message.meta.SbeFieldType;
import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;

public abstract class AbstractFieldSet implements FieldSet {
    private final static String MATURITY_MONTH_YEAR = "MaturityMonthYear";

    protected SbeBuffer sbeBuffer;

    @Override
    public SbeBuffer buffer() {
        return this.sbeBuffer;
    }

    @Override
    public int getSchemaId() {
        return metadata().getSchemaId();
    }

    protected abstract MetadataContainer metadata();

    protected abstract void seek(final int tagId);

    protected abstract void seek(final SbeFieldType field);

    @Override
    public boolean hasField(final int tagId) {
        return metadata().findField(tagId) != null;
    }

    private boolean isCompositeNull(final SbeFieldType fieldType) {
        if (fieldType.isFloat()) {
            if (fieldType.getPrimitiveType() == SbePrimitiveType.Int32) {
                return fieldType.getInt32PresenceVal() == this.buffer().getInt32();
            } else if (fieldType.getPrimitiveType() == SbePrimitiveType.Int64) {
                return fieldType.getInt64PresenceVal() == this.buffer().getInt64();
            }
        }
        return false;
    }

    private boolean isPrimitiveNull(final SbeFieldType fieldType) {
        switch (fieldType.getPrimitiveType()) {
            case Char:
                //handles only single char val now. Should possible generic case be also implemented?
                return fieldType.getCharPresenceVal().charAt(0) == this.buffer().getChar();
            case Int8:
                return fieldType.getInt8PresenceVal() == this.buffer().getInt8();
            case UInt8:
                return fieldType.getUInt8PresenceVal() == this.buffer().getUInt8();
            case Int16:
                return fieldType.getUInt16PresenceVal() == this.buffer().getInt16();
            case UInt16:
                return fieldType.getUInt16PresenceVal() == this.buffer().getUInt16();
            case Int32:
                return fieldType.getInt32PresenceVal() == this.buffer().getInt32();
            case UInt32:
                return fieldType.getUInt32PresenceVal() == this.buffer().getUInt32();
            case Int64:
                return fieldType.getInt64PresenceVal() == this.buffer().getInt64();
            case UInt64:
                return this.buffer().isUInt64NULL();
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public boolean isNull(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isOptional()) {
            seek(fieldType);
            if (fieldType.isComposite()) {
                return isCompositeNull(fieldType);
            } else {
                return isPrimitiveNull(fieldType);
            }
        }
        return false;
    }

    @Override
    public char getChar(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getCharPresenceVal().charAt(0);
        }
        seek(fieldType);
        return this.buffer().getChar();
    }

    @Override
    public short getInt16(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getInt16PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getInt16();
    }

    @Override
    public short getUInt8(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getUInt8PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getUInt8();
    }

    @Override
    public byte getInt8(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getInt8PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getInt8();
    }

    @Override
    public int getUInt16(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getUInt16PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getUInt16();
    }

    @Override
    public int getInt32(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getInt32PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getInt32();
    }

    @Override
    public long getUInt32(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getUInt32PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getUInt32();
    }

    @Override
    public long getInt64(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getInt64PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getInt64();
    }

    @Override
    public long getUInt64(final int tagId) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isConstant()) {
            return fieldType.getUInt64PresenceVal();
        }
        seek(fieldType);
        return this.buffer().getUInt64();
    }

    @Override
    public boolean getDouble(final int tagId, final SbeDouble doubleVal) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isFloat()) {
            doubleVal.reset();
            doubleVal.setExponent(fieldType.getExponentVal());
            seek(fieldType);
            if (fieldType.getPrimitiveType() == SbePrimitiveType.Int32) {
                doubleVal.setMantissa(this.buffer().getInt32());
            } else if (fieldType.getPrimitiveType() == SbePrimitiveType.Int64) {
                doubleVal.setMantissa(this.buffer().getInt64());
            }
            if (fieldType.isOptional()) {
                doubleVal.setNull(doubleVal.getMantissa() == fieldType.getInt64PresenceVal());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean getMonthYear(final int tagId, final SbeMonthYear dayVal) {
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isComposite() && fieldType.getFieldType().getName().equals(MATURITY_MONTH_YEAR)) {
            dayVal.reset();
            seek(fieldType);
            int valuePos = this.buffer().position();
            dayVal.setYear(this.buffer().getUInt16());
            valuePos += SbePrimitiveType.UInt16.getSize();
            this.buffer().position(valuePos);
            dayVal.setMonth(this.buffer().getUInt8());
            valuePos += SbePrimitiveType.UInt8.getSize();
            this.buffer().position(valuePos);
            dayVal.setDay(this.buffer().getUInt8());
            valuePos += SbePrimitiveType.UInt8.getSize();
            this.buffer().position(valuePos);
            dayVal.setWeek(this.buffer().getUInt8());
            return true;
        }
        return false;
    }

    @Override
    public boolean getString(final int tagId, final SbeString stringVal) {
        stringVal.reset(); // optional action?
        final SbeFieldType fieldType = metadata().findField(tagId);
        if (fieldType.isString()) {
            seek(fieldType);
            this.buffer().getChars(stringVal.getChars(), fieldType.getLength());
            stringVal.setLength(fieldType.getLength());
            return true;
        }
        return false;
    }

    @Override
    public abstract boolean getGroup(int tagId, MdpGroup mdpGroup);
}
