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

import java.util.HashMap;
import java.util.Map;

/*
    Primitive types provide basic SBE properties
 */
public enum SbePrimitiveType {
    Char("char", 1, true, -127L, 127L, 127L, "String"),
    Int8("int8", 1, true, -127L, 127L, 127L, "byte"),
    UInt8("uint8", 1, false, 0L, 0xfeL, 255, "int"),
    Int16("int16", 2, true, -32767L, 32767L, 32767L, "int"),
    UInt16("uint16", 2, false, 0, 0xfffe, 0xfffe, "int"),
    Int32("int32", 4, true, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, Integer.MAX_VALUE, "int"),
    UInt32("uint32", 4, false, 0, 0xfffffffeL, 4294967295L, "long"),
    Int64("int64", 8, true, Long.MIN_VALUE + 1, Long.MAX_VALUE, Long.MAX_VALUE, "long"),
    UInt64("uint64", 8, false, 0, Long.MAX_VALUE, Long.MAX_VALUE, "long");

    /*
        @param name of primitive type in XML
        @param size raw size in bytes
        @param isSigned true if signed false if unsigned
        @param minValue min Value
        @param maxValue max Value
        @param nullValue value that represents null
        @param javaType source code type (unused)
     */
    SbePrimitiveType(String name,
                     int size,
                     boolean isSigned,
                     long minValue,
                     long maxValue,
                     long nullValue,
                     String javaType) {

        this.name = name;
        this.size = size;
        this.isSigned = isSigned;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.nullValue = nullValue;
        this.javaType = javaType;
    }

    private String name;
    private int size;
    private boolean isSigned;
    private long minValue;
    private long maxValue;
    private long nullValue;
    private String javaType;

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean getIsSigned() {
        return isSigned;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public long getNullValue() {
        return nullValue;
    }

    public boolean isNull(final long value) {
        return value == this.nullValue;
    }

    /*
        name -> PrimitiveType map
     */
    static private Map<String, SbePrimitiveType> primitiveTypes;

    /*
        Multitone object provider
        @param name type name (as in XML).
     */
    public static SbePrimitiveType fromString(String name) {
        return primitiveTypes.get(name);
    }

    /*
        Build name -> PrimitiveType map
     */
    static {
        primitiveTypes = new HashMap<>();
        for (SbePrimitiveType type : SbePrimitiveType.values()) {
            primitiveTypes.put(type.getName(), type);
        }
    }
}
