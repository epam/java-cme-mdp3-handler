/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MaturityMonthYearEncoder"})
@SuppressWarnings("all")
public class MaturityMonthYearEncoder
{
    public static final int ENCODED_LENGTH = 5;
    private int offset;
    private MutableDirectBuffer buffer;

    public MaturityMonthYearEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int yearEncodingOffset()
    {
        return 0;
    }

    public static int yearEncodingLength()
    {
        return 2;
    }

    public static int yearNullValue()
    {
        return 65535;
    }

    public static int yearMinValue()
    {
        return 0;
    }

    public static int yearMaxValue()
    {
        return 65534;
    }

    public MaturityMonthYearEncoder year(final int value)
    {
        buffer.putShort(offset + 0, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int monthEncodingOffset()
    {
        return 2;
    }

    public static int monthEncodingLength()
    {
        return 1;
    }

    public static short monthNullValue()
    {
        return (short)255;
    }

    public static short monthMinValue()
    {
        return (short)0;
    }

    public static short monthMaxValue()
    {
        return (short)254;
    }

    public MaturityMonthYearEncoder month(final short value)
    {
        buffer.putByte(offset + 2, (byte)value);
        return this;
    }


    public static int dayEncodingOffset()
    {
        return 3;
    }

    public static int dayEncodingLength()
    {
        return 1;
    }

    public static short dayNullValue()
    {
        return (short)255;
    }

    public static short dayMinValue()
    {
        return (short)0;
    }

    public static short dayMaxValue()
    {
        return (short)254;
    }

    public MaturityMonthYearEncoder day(final short value)
    {
        buffer.putByte(offset + 3, (byte)value);
        return this;
    }


    public static int weekEncodingOffset()
    {
        return 4;
    }

    public static int weekEncodingLength()
    {
        return 1;
    }

    public static short weekNullValue()
    {
        return (short)255;
    }

    public static short weekMinValue()
    {
        return (short)0;
    }

    public static short weekMaxValue()
    {
        return (short)254;
    }

    public MaturityMonthYearEncoder week(final short value)
    {
        buffer.putByte(offset + 4, (byte)value);
        return this;
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        MaturityMonthYearDecoder writer = new MaturityMonthYearDecoder();
        writer.wrap(buffer, offset);

        return writer.appendTo(builder);
    }
}
