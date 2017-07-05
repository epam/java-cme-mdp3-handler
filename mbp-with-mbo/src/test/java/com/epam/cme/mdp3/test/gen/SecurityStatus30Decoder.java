/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SecurityStatus30Decoder"})
@SuppressWarnings("all")
public class SecurityStatus30Decoder
{
    public static final int BLOCK_LENGTH = 30;
    public static final int TEMPLATE_ID = 30;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final SecurityStatus30Decoder parentMessage = this;
    private DirectBuffer buffer;
    protected int offset;
    protected int limit;
    protected int actingBlockLength;
    protected int actingVersion;

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

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public SecurityStatus30Decoder wrap(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

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

    public static int transactTimeId()
    {
        return 60;
    }

    public static int transactTimeSinceVersion()
    {
        return 0;
    }

    public static int transactTimeEncodingOffset()
    {
        return 0;
    }

    public static int transactTimeEncodingLength()
    {
        return 8;
    }

    public static String transactTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "UTCTimestamp";
        }

        return "";
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

    public long transactTime()
    {
        return buffer.getLong(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int securityGroupId()
    {
        return 1151;
    }

    public static int securityGroupSinceVersion()
    {
        return 0;
    }

    public static int securityGroupEncodingOffset()
    {
        return 8;
    }

    public static int securityGroupEncodingLength()
    {
        return 6;
    }

    public static String securityGroupMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "String";
        }

        return "";
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

    public byte securityGroup(final int index)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String securityGroupCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getSecurityGroup(final byte[] dst, final int dstOffset)
    {
        final int length = 6;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        buffer.getBytes(this.offset + 8, dst, dstOffset, length);

        return length;
    }

    public String securityGroup()
    {
        final byte[] dst = new byte[6];
        buffer.getBytes(this.offset + 8, dst, 0, 6);

        int end = 0;
        for (; end < 6 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.UTF_8);
    }


    public static int assetId()
    {
        return 6937;
    }

    public static int assetSinceVersion()
    {
        return 0;
    }

    public static int assetEncodingOffset()
    {
        return 14;
    }

    public static int assetEncodingLength()
    {
        return 6;
    }

    public static String assetMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "String";
        }

        return "";
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

    public byte asset(final int index)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 14 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String assetCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getAsset(final byte[] dst, final int dstOffset)
    {
        final int length = 6;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        buffer.getBytes(this.offset + 14, dst, dstOffset, length);

        return length;
    }

    public String asset()
    {
        final byte[] dst = new byte[6];
        buffer.getBytes(this.offset + 14, dst, 0, 6);

        int end = 0;
        for (; end < 6 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.UTF_8);
    }


    public static int securityIDId()
    {
        return 48;
    }

    public static int securityIDSinceVersion()
    {
        return 0;
    }

    public static int securityIDEncodingOffset()
    {
        return 20;
    }

    public static int securityIDEncodingLength()
    {
        return 4;
    }

    public static String securityIDMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
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

    public int securityID()
    {
        return buffer.getInt(offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int tradeDateId()
    {
        return 75;
    }

    public static int tradeDateSinceVersion()
    {
        return 0;
    }

    public static int tradeDateEncodingOffset()
    {
        return 24;
    }

    public static int tradeDateEncodingLength()
    {
        return 2;
    }

    public static String tradeDateMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "LocalMktDate";
        }

        return "";
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

    public int tradeDate()
    {
        return (buffer.getShort(offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }


    public static int matchEventIndicatorId()
    {
        return 5799;
    }

    public static int matchEventIndicatorSinceVersion()
    {
        return 0;
    }

    public static int matchEventIndicatorEncodingOffset()
    {
        return 26;
    }

    public static int matchEventIndicatorEncodingLength()
    {
        return 1;
    }

    public static String matchEventIndicatorMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "MultipleCharValue";
        }

        return "";
    }

    private final MatchEventIndicatorDecoder matchEventIndicator = new MatchEventIndicatorDecoder();

    public MatchEventIndicatorDecoder matchEventIndicator()
    {
        matchEventIndicator.wrap(buffer, offset + 26);
        return matchEventIndicator;
    }

    public static int securityTradingStatusId()
    {
        return 326;
    }

    public static int securityTradingStatusSinceVersion()
    {
        return 0;
    }

    public static int securityTradingStatusEncodingOffset()
    {
        return 27;
    }

    public static int securityTradingStatusEncodingLength()
    {
        return 1;
    }

    public static String securityTradingStatusMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public SecurityTradingStatus securityTradingStatus()
    {
        return SecurityTradingStatus.get(((short)(buffer.getByte(offset + 27) & 0xFF)));
    }


    public static int haltReasonId()
    {
        return 327;
    }

    public static int haltReasonSinceVersion()
    {
        return 0;
    }

    public static int haltReasonEncodingOffset()
    {
        return 28;
    }

    public static int haltReasonEncodingLength()
    {
        return 1;
    }

    public static String haltReasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public HaltReason haltReason()
    {
        return HaltReason.get(((short)(buffer.getByte(offset + 28) & 0xFF)));
    }


    public static int securityTradingEventId()
    {
        return 1174;
    }

    public static int securityTradingEventSinceVersion()
    {
        return 0;
    }

    public static int securityTradingEventEncodingOffset()
    {
        return 29;
    }

    public static int securityTradingEventEncodingLength()
    {
        return 1;
    }

    public static String securityTradingEventMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "int";
        }

        return "";
    }

    public SecurityTradingEvent securityTradingEvent()
    {
        return SecurityTradingEvent.get(((short)(buffer.getByte(offset + 29) & 0xFF)));
    }



    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[SecurityStatus30](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        //Token{signal=BEGIN_FIELD, name='TransactTime', referencedName='null', description='Start of event processing time in number of nanoseconds since Unix epoch.', id=60, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='UTCTimestamp'}}
        //Token{signal=ENCODING, name='uInt64', referencedName='null', description='Start of event processing time in number of nanoseconds since Unix epoch.', id=-1, version=0, deprecated=0, encodedLength=8, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT64, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='UTCTimestamp'}}
        builder.append("transactTime=");
        builder.append(transactTime());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='SecurityGroup', referencedName='null', description='Security Group', id=1151, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        //Token{signal=ENCODING, name='SecurityGroup', referencedName='null', description='Security Group', id=-1, version=0, deprecated=0, encodedLength=6, offset=8, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        builder.append("securityGroup=");
        for (int i = 0; i < securityGroupLength() && securityGroup(i) > 0; i++)
        {
            builder.append((char)securityGroup(i));
        }
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='Asset', referencedName='null', description='Product Code within Security Group specified', id=6937, version=0, deprecated=0, encodedLength=0, offset=14, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        //Token{signal=ENCODING, name='Asset', referencedName='null', description='Product Code within Security Group specified', id=-1, version=0, deprecated=0, encodedLength=6, offset=14, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        builder.append("asset=");
        for (int i = 0; i < assetLength() && asset(i) > 0; i++)
        {
            builder.append((char)asset(i));
        }
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='SecurityID', referencedName='null', description='If this tag is present, 35=f message is sent for the instrument', id=48, version=0, deprecated=0, encodedLength=0, offset=20, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
        //Token{signal=ENCODING, name='Int32NULL', referencedName='null', description='If this tag is present, 35=f message is sent for the instrument', id=-1, version=0, deprecated=0, encodedLength=4, offset=20, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=2147483647, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
        builder.append("securityID=");
        builder.append(securityID());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='TradeDate', referencedName='null', description='Trade Session Date', id=75, version=0, deprecated=0, encodedLength=0, offset=24, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='LocalMktDate'}}
        //Token{signal=ENCODING, name='LocalMktDate', referencedName='null', description='Trade Session Date', id=-1, version=0, deprecated=0, encodedLength=2, offset=24, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT16, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=65535, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='LocalMktDate'}}
        builder.append("tradeDate=");
        builder.append(tradeDate());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=5799, version=0, deprecated=0, encodedLength=0, offset=26, componentTokenCount=12, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='MultipleCharValue'}}
        //Token{signal=BEGIN_SET, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=-1, version=0, deprecated=0, encodedLength=1, offset=26, componentTokenCount=10, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='MultipleCharValue'}}
        builder.append("matchEventIndicator=");
        builder.append(matchEventIndicator());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='SecurityTradingStatus', referencedName='null', description='Identifies the trading status applicable to the instrument or Security Group', id=326, version=0, deprecated=0, encodedLength=0, offset=27, componentTokenCount=15, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
        //Token{signal=BEGIN_ENUM, name='SecurityTradingStatus', referencedName='null', description='Identifies the trading status applicable to the instrument or Security Group', id=-1, version=0, deprecated=0, encodedLength=1, offset=27, componentTokenCount=13, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='int'}}
        builder.append("securityTradingStatus=");
        builder.append(securityTradingStatus());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='HaltReason', referencedName='null', description='Identifies the reason for the status change', id=327, version=0, deprecated=0, encodedLength=0, offset=28, componentTokenCount=11, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
        //Token{signal=BEGIN_ENUM, name='HaltReason', referencedName='null', description='Identifies the reason for the status change', id=-1, version=0, deprecated=0, encodedLength=1, offset=28, componentTokenCount=9, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='int'}}
        builder.append("haltReason=");
        builder.append(haltReason());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='SecurityTradingEvent', referencedName='null', description='Identifies an additional event or a rule related to the status', id=1174, version=0, deprecated=0, encodedLength=0, offset=29, componentTokenCount=9, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
        //Token{signal=BEGIN_ENUM, name='SecurityTradingEvent', referencedName='null', description='Identifies an additional event or a rule related to the status', id=-1, version=0, deprecated=0, encodedLength=1, offset=29, componentTokenCount=7, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='int'}}
        builder.append("securityTradingEvent=");
        builder.append(securityTradingEvent());

        limit(originalLimit);

        return builder;
    }
}
