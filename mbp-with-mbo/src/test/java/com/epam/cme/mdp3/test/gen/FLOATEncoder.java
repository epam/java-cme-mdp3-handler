/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.FLOATEncoder"})
@SuppressWarnings("all")
public class FLOATEncoder
{
    public static final int ENCODED_LENGTH = 8;
    private int offset;
    private MutableDirectBuffer buffer;

    public FLOATEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public static int mantissaEncodingOffset()
    {
        return 0;
    }

    public static int mantissaEncodingLength()
    {
        return 8;
    }

    public static long mantissaNullValue()
    {
        return -9223372036854775808L;
    }

    public static long mantissaMinValue()
    {
        return -9223372036854775807L;
    }

    public static long mantissaMaxValue()
    {
        return 9223372036854775807L;
    }

    public FLOATEncoder mantissa(final long value)
    {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int exponentEncodingOffset()
    {
        return 8;
    }

    public static int exponentEncodingLength()
    {
        return 0;
    }

    public static byte exponentNullValue()
    {
        return (byte)-128;
    }

    public static byte exponentMinValue()
    {
        return (byte)-127;
    }

    public static byte exponentMaxValue()
    {
        return (byte)127;
    }

    public byte exponent()
    {
        return (byte)-7;
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        FLOATDecoder writer = new FLOATDecoder();
        writer.wrap(buffer, offset);

        return writer.appendTo(builder);
    }
}
