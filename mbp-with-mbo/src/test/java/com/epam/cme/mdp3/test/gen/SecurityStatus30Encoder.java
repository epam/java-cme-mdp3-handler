/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SecurityStatus30Encoder"})
@SuppressWarnings("all")
public class SecurityStatus30Encoder
{
    public static final int BLOCK_LENGTH = 30;
    public static final int TEMPLATE_ID = 30;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final SecurityStatus30Encoder parentMessage = this;
    private MutableDirectBuffer buffer;
    protected int offset;
    protected int limit;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "f";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public SecurityStatus30Encoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int transactTimeEncodingOffset()
    {
        return 0;
    }

    public static int transactTimeEncodingLength()
    {
        return 8;
    }

    public static long transactTimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long transactTimeMinValue()
    {
        return 0x0L;
    }

    public static long transactTimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public SecurityStatus30Encoder transactTime(final long value)
    {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int securityGroupEncodingOffset()
    {
        return 8;
    }

    public static int securityGroupEncodingLength()
    {
        return 6;
    }

    public static byte securityGroupNullValue()
    {
        return (byte)0;
    }

    public static byte securityGroupMinValue()
    {
        return (byte)32;
    }

    public static byte securityGroupMaxValue()
    {
        return (byte)126;
    }

    public static int securityGroupLength()
    {
        return 6;
    }

    public void securityGroup(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 8 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String securityGroupCharacterEncoding()
    {
        return "UTF-8";
    }

    public SecurityStatus30Encoder putSecurityGroup(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 8, src, srcOffset, length);

        return this;
    }

    public SecurityStatus30Encoder securityGroup(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 8, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 8 + start, (byte)0);
        }

        return this;
    }

    public static int assetEncodingOffset()
    {
        return 14;
    }

    public static int assetEncodingLength()
    {
        return 6;
    }

    public static byte assetNullValue()
    {
        return (byte)0;
    }

    public static byte assetMinValue()
    {
        return (byte)32;
    }

    public static byte assetMaxValue()
    {
        return (byte)126;
    }

    public static int assetLength()
    {
        return 6;
    }

    public void asset(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 14 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String assetCharacterEncoding()
    {
        return "UTF-8";
    }

    public SecurityStatus30Encoder putAsset(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 14, src, srcOffset, length);

        return this;
    }

    public SecurityStatus30Encoder asset(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 14, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 14 + start, (byte)0);
        }

        return this;
    }

    public static int securityIDEncodingOffset()
    {
        return 20;
    }

    public static int securityIDEncodingLength()
    {
        return 4;
    }

    public static int securityIDNullValue()
    {
        return 2147483647;
    }

    public static int securityIDMinValue()
    {
        return -2147483647;
    }

    public static int securityIDMaxValue()
    {
        return 2147483647;
    }

    public SecurityStatus30Encoder securityID(final int value)
    {
        buffer.putInt(offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int tradeDateEncodingOffset()
    {
        return 24;
    }

    public static int tradeDateEncodingLength()
    {
        return 2;
    }

    public static int tradeDateNullValue()
    {
        return 65535;
    }

    public static int tradeDateMinValue()
    {
        return 0;
    }

    public static int tradeDateMaxValue()
    {
        return 65534;
    }

    public SecurityStatus30Encoder tradeDate(final int value)
    {
        buffer.putShort(offset + 24, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int matchEventIndicatorEncodingOffset()
    {
        return 26;
    }

    public static int matchEventIndicatorEncodingLength()
    {
        return 1;
    }

    private final MatchEventIndicatorEncoder matchEventIndicator = new MatchEventIndicatorEncoder();

    public MatchEventIndicatorEncoder matchEventIndicator()
    {
        matchEventIndicator.wrap(buffer, offset + 26);
        return matchEventIndicator;
    }

    public static int securityTradingStatusEncodingOffset()
    {
        return 27;
    }

    public static int securityTradingStatusEncodingLength()
    {
        return 1;
    }

    public SecurityStatus30Encoder securityTradingStatus(final SecurityTradingStatus value)
    {
        buffer.putByte(offset + 27, (byte)value.value());
        return this;
    }

    public static int haltReasonEncodingOffset()
    {
        return 28;
    }

    public static int haltReasonEncodingLength()
    {
        return 1;
    }

    public SecurityStatus30Encoder haltReason(final HaltReason value)
    {
        buffer.putByte(offset + 28, (byte)value.value());
        return this;
    }

    public static int securityTradingEventEncodingOffset()
    {
        return 29;
    }

    public static int securityTradingEventEncodingLength()
    {
        return 1;
    }

    public SecurityStatus30Encoder securityTradingEvent(final SecurityTradingEvent value)
    {
        buffer.putByte(offset + 29, (byte)value.value());
        return this;
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        SecurityStatus30Decoder writer = new SecurityStatus30Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
