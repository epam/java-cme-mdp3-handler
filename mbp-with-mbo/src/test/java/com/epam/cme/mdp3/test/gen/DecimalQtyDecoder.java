/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.DecimalQtyDecoder"})
@SuppressWarnings("all")
public class DecimalQtyDecoder
{
    public static final int ENCODED_LENGTH = 4;
    private int offset;
    private DirectBuffer buffer;

    public DecimalQtyDecoder wrap(final DirectBuffer buffer, final int offset)
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

    public static int mantissaEncodingOffset()
    {
        return 0;
    }

    public static int mantissaEncodingLength()
    {
        return 4;
    }

    public static int mantissaNullValue()
    {
        return 2147483647;
    }

    public static int mantissaMinValue()
    {
        return -2147483647;
    }

    public static int mantissaMaxValue()
    {
        return 2147483647;
    }

    public int mantissa()
    {
        return buffer.getInt(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int exponentEncodingOffset()
    {
        return 4;
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
        return (byte)-4;
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('(');
        //Token{signal=ENCODING, name='mantissa', referencedName='null', description='mantissa ', id=-1, version=0, deprecated=0, encodedLength=4, offset=0, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=2147483647, constValue=null, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("mantissa=");
        builder.append(mantissa());
        builder.append('|');
        //Token{signal=ENCODING, name='exponent', referencedName='null', description='exponent', id=-1, version=0, deprecated=0, encodedLength=0, offset=4, componentTokenCount=1, encoding=Encoding{presence=CONSTANT, primitiveType=INT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=-4, characterEncoding='UTF-8', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append(')');

        return builder;
    }
}
