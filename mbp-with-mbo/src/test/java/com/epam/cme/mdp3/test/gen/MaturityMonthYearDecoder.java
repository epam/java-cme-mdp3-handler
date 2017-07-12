/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MaturityMonthYearDecoder"})
@SuppressWarnings("all")
public class MaturityMonthYearDecoder
{
    public static final int ENCODED_LENGTH = 5;
    private int offset;
    private DirectBuffer buffer;

    public MaturityMonthYearDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer()
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

    public int year()
    {
        return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
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

    public short month()
    {
        return ((short)(buffer.getByte(offset + 2) & 0xFF));
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

    public short day()
    {
        return ((short)(buffer.getByte(offset + 3) & 0xFF));
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

    public short week()
    {
        return ((short)(buffer.getByte(offset + 4) & 0xFF));
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('(');
        //Token{signal=ENCODING, name='year', referencedName='null', description='YYYY', id=-1, version=0, deprecated=0, encodedLength=2, offset=0, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=65535, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("year=");
        builder.append(year());
        builder.append('|');
        //Token{signal=ENCODING, name='month', referencedName='null', description='MM', id=-1, version=0, deprecated=0, encodedLength=1, offset=2, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=255, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("month=");
        builder.append(month());
        builder.append('|');
        //Token{signal=ENCODING, name='day', referencedName='null', description='DD', id=-1, version=0, deprecated=0, encodedLength=1, offset=3, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=255, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("day=");
        builder.append(day());
        builder.append('|');
        //Token{signal=ENCODING, name='week', referencedName='null', description='WW', id=-1, version=0, deprecated=0, encodedLength=1, offset=4, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=255, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("week=");
        builder.append(week());
        builder.append(')');

        return builder;
    }
}
