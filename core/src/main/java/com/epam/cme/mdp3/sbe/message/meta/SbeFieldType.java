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

import com.epam.cme.mdp3.sbe.message.SbeBuffer;
import com.epam.cme.mdp3.sbe.schema.vo.*;

/**
 * Metadata of MDP Field type
 */
public class SbeFieldType {
    private final static String PRESENCE_OPTIONAL = "optional";
    private final static String PRESENCE_CONSTANT = "constant";
    private final static String FLOAT_MANTISSA = "mantissa";
    private final static String FLOAT_EXPONENT = "exponent";
    private final FieldType fieldType;
    private final MetadataContainer metadataContainer;

    private SbePrimitiveType primitiveType;
    private boolean isComposite = false;
    private boolean isConstant = false;
    private boolean isOptional = false;
    private boolean isString = false;
    private int length;
    private boolean isFloat = false;
    private byte exponentVal;

    private String charPresenceVal;
    private byte int8PresenceVal;
    private short uInt8PresenceVal;
    private short int16PresenceVal;
    private int uInt16PresenceVal;
    private int int32PresenceVal;
    private long uInt32PresenceVal;
    private long int64PresenceVal;
    private long uInt64PresenceVal;

    /**
     * Constructs Field metadata.
     *
     * @param metadataContainer Parent Field set's metadata instance.
     * @param fieldType         SBE definition of MDP Field type
     */
    public SbeFieldType(final MetadataContainer metadataContainer, final FieldType fieldType) {
        this.metadataContainer = metadataContainer;
        this.fieldType = fieldType;
        init();
    }

    /**
     * Initializes Field metadata.
     */
    private void init() {
        for (final EncodedDataType encodedDataType : this.metadataContainer.getSchema().getTypes().get(0).getType()) {
            if (encodedDataType.getName().equals(this.fieldType.getType())) {
                initFromEncodedDataType(encodedDataType);
                return;
            }
        }
        for (final CompositeDataType compositeDataType : this.metadataContainer.getSchema().getTypes().get(0).getComposite()) {
            if (compositeDataType.getName().equals(this.fieldType.getType())) {
                initFromCompositeDataType(compositeDataType);
                return;
            }
        }
        for (final EnumType enumType : this.metadataContainer.getSchema().getTypes().get(0).getEnum()) {
            if (enumType.getName().equals(this.fieldType.getType())) {
                initFromEnumType(enumType);
                return;
            }
        }
        for (final SetType setType : this.metadataContainer.getSchema().getTypes().get(0).getSet()) {
            if (setType.getName().equals(this.fieldType.getType())) {
                initFromSetType(setType);
                return;
            }
        }
    }

    /**
     * Intializes Field metadata from SBE encoded Data type.
     *
     * @param encodedDataType Encoded Data type
     */
    private void initFromEncodedDataType(final EncodedDataType encodedDataType) {
        this.primitiveType = SbePrimitiveType.fromString(encodedDataType.getPrimitiveType());
        if (encodedDataType.getLength() != null && this.primitiveType == SbePrimitiveType.Char) {
            setLength(encodedDataType.getLength().intValue());
            setString(true);
        }
        if (encodedDataType.getPresence() != null) {
            if (encodedDataType.getPresence().equals(PRESENCE_OPTIONAL) && encodedDataType.getNullValue() != null) {
                setOptional(true);
                setPresenceValue(encodedDataType.getNullValue());
            } else if (encodedDataType.getPresence().equals(PRESENCE_CONSTANT) && encodedDataType.getValue() != null) {
                setConstant(true);
                setPresenceValue(encodedDataType.getValue());
            }
        }
    }

    /**
     * Initializes Field metadata from composite Data type.
     *
     * @param compositeDataType Composite Data type
     */
    private void initFromCompositeDataType(final CompositeDataType compositeDataType) {
        setComposite(true);

        if (compositeDataType.getType().size() == 2) {
            final EncodedDataType type1 = compositeDataType.getType().get(0);
            final EncodedDataType type2 = compositeDataType.getType().get(1);
            if (type1.getName().equals(FLOAT_MANTISSA) && type2.getName().equals(FLOAT_EXPONENT)) {
                setFloat(true);
                this.primitiveType = SbePrimitiveType.fromString(type1.getPrimitiveType());
                if (type1.getPresence() != null && type1.getPresence().equals(PRESENCE_OPTIONAL) && type1.getNullValue() != null) {
                    setOptional(true);
                    this.int64PresenceVal = Long.parseLong(type1.getNullValue());
                }
                this.exponentVal = Byte.parseByte(type2.getValue());
            }
        }
    }

    /**
     * Initializes Field metadata from Enum type.
     *
     * @param enumType Enum type
     */
    private void initFromEnumType(final EnumType enumType) {
        this.primitiveType = SbePrimitiveType.fromString(
                this.metadataContainer.getDataType(enumType.getEncodingType()).getPrimitiveType());
    }

    /**
     * Initializes Field metadata from Set type.
     *
     * @param setType Set type
     */
    private void initFromSetType(final SetType setType) {
        this.primitiveType = SbePrimitiveType.fromString(
                this.metadataContainer.getDataType(setType.getEncodingType()).getPrimitiveType());
    }

    /**
     * Sets "Null value" of the the Field. If field's value then will be equals to this "Null value" then it means Field value is Null.
     *
     * @param nullValue Null value of the field
     */
    private void setPresenceValue(final String nullValue) {
        switch (this.getPrimitiveType()) {
            case Char:
                this.charPresenceVal = nullValue;
                break;
            case Int8:
                this.int8PresenceVal = Byte.parseByte(nullValue);
                break;
            case UInt8:
                this.uInt8PresenceVal = Short.parseShort(nullValue);
                break;
            case Int16:
                this.int16PresenceVal = Short.parseShort(nullValue);
                break;
            case UInt16:
                this.uInt16PresenceVal = Integer.parseInt(nullValue);
                break;
            case Int32:
                this.int32PresenceVal = Integer.parseInt(nullValue);
                break;
            case UInt32:
                this.uInt32PresenceVal = Long.parseLong(nullValue);
                break;
            case Int64:
                this.int64PresenceVal = Long.parseLong(nullValue);
                break;
            case UInt64:
                this.uInt64PresenceVal = Long.parseUnsignedLong(nullValue);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public SbePrimitiveType getPrimitiveType() {
        return primitiveType;
    }

    protected void setPrimitiveType(SbePrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    public boolean isComposite() {
        return isComposite;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public boolean isOptional() {
        return isOptional;
    }

    protected void setComposite(final boolean isCompexType) {
        this.isComposite = isCompexType;
    }

    protected void setConstant(final boolean isConstant) {
        this.isConstant = isConstant;
    }

    protected void setOptional(final boolean isOptional) {
        this.isOptional = isOptional;
    }

    public boolean isFloat() {
        return isFloat;
    }

    protected void setFloat(boolean isFloat) {
        this.isFloat = isFloat;
    }

    public boolean isString() {
        return isString;
    }

    protected void setString(boolean isVarchar) {
        this.isString = isVarchar;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCharPresenceVal() {
        return charPresenceVal;
    }

    public short getInt16PresenceVal() {
        return int16PresenceVal;
    }

    public int getInt32PresenceVal() {
        return int32PresenceVal;
    }

    public long getInt64PresenceVal() {
        return int64PresenceVal;
    }

    public byte getInt8PresenceVal() {
        return int8PresenceVal;
    }

    public int getUInt16PresenceVal() {
        return uInt16PresenceVal;
    }

    public long getUInt64PresenceVal() {
        return uInt64PresenceVal;
    }

    public long getUInt32PresenceVal() {
        return uInt32PresenceVal;
    }

    public short getUInt8PresenceVal() {
        return uInt8PresenceVal;
    }

    public byte getExponentVal() {
        return exponentVal;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void seek(final SbeBuffer buffer) {
        buffer.position(metadataContainer.offset() + this.getFieldType().getOffset().intValue());
    }
}
