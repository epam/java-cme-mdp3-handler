/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDIncrementalRefreshLimitsBanding34Decoder"})
@SuppressWarnings("all")
public class MDIncrementalRefreshLimitsBanding34Decoder
{
    public static final int BLOCK_LENGTH = 11;
    public static final int TEMPLATE_ID = 34;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDIncrementalRefreshLimitsBanding34Decoder parentMessage = this;
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
        return "X";
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public MDIncrementalRefreshLimitsBanding34Decoder wrap(
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
        return 8;
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
        matchEventIndicator.wrap(buffer, offset + 8);
        return matchEventIndicator;
    }

    private final NoMDEntriesDecoder noMDEntries = new NoMDEntriesDecoder();

    public static long noMDEntriesDecoderId()
    {
        return 268;
    }

    public static int noMDEntriesDecoderSinceVersion()
    {
        return 0;
    }

    public NoMDEntriesDecoder noMDEntries()
    {
        noMDEntries.wrap(parentMessage, buffer);
        return noMDEntries;
    }

    public static class NoMDEntriesDecoder
        implements Iterable<NoMDEntriesDecoder>, java.util.Iterator<NoMDEntriesDecoder>
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeDecoder dimensions = new GroupSizeDecoder();
        private MDIncrementalRefreshLimitsBanding34Decoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        public void wrap(
            final MDIncrementalRefreshLimitsBanding34Decoder parentMessage, final DirectBuffer buffer)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            index = -1;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 32;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<NoMDEntriesDecoder> iterator()
        {
            return this;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        public NoMDEntriesDecoder next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int highLimitPriceId()
        {
            return 1149;
        }

        public static int highLimitPriceSinceVersion()
        {
            return 0;
        }

        public static int highLimitPriceEncodingOffset()
        {
            return 0;
        }

        public static int highLimitPriceEncodingLength()
        {
            return 8;
        }

        public static String highLimitPriceMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Price";
            }

            return "";
        }

        private final PRICENULLDecoder highLimitPrice = new PRICENULLDecoder();

        public PRICENULLDecoder highLimitPrice()
        {
            highLimitPrice.wrap(buffer, offset + 0);
            return highLimitPrice;
        }

        public static int lowLimitPriceId()
        {
            return 1148;
        }

        public static int lowLimitPriceSinceVersion()
        {
            return 0;
        }

        public static int lowLimitPriceEncodingOffset()
        {
            return 8;
        }

        public static int lowLimitPriceEncodingLength()
        {
            return 8;
        }

        public static String lowLimitPriceMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Price";
            }

            return "";
        }

        private final PRICENULLDecoder lowLimitPrice = new PRICENULLDecoder();

        public PRICENULLDecoder lowLimitPrice()
        {
            lowLimitPrice.wrap(buffer, offset + 8);
            return lowLimitPrice;
        }

        public static int maxPriceVariationId()
        {
            return 1143;
        }

        public static int maxPriceVariationSinceVersion()
        {
            return 0;
        }

        public static int maxPriceVariationEncodingOffset()
        {
            return 16;
        }

        public static int maxPriceVariationEncodingLength()
        {
            return 8;
        }

        public static String maxPriceVariationMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Price";
            }

            return "";
        }

        private final PRICENULLDecoder maxPriceVariation = new PRICENULLDecoder();

        public PRICENULLDecoder maxPriceVariation()
        {
            maxPriceVariation.wrap(buffer, offset + 16);
            return maxPriceVariation;
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
            return 24;
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
            return -2147483648;
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
            return buffer.getInt(offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int rptSeqId()
        {
            return 83;
        }

        public static int rptSeqSinceVersion()
        {
            return 0;
        }

        public static int rptSeqEncodingOffset()
        {
            return 28;
        }

        public static int rptSeqEncodingLength()
        {
            return 4;
        }

        public static String rptSeqMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static long rptSeqNullValue()
        {
            return 4294967294L;
        }

        public static long rptSeqMinValue()
        {
            return 0L;
        }

        public static long rptSeqMaxValue()
        {
            return 4294967293L;
        }

        public long rptSeq()
        {
            return (buffer.getInt(offset + 28, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        }


        public static int mDUpdateActionId()
        {
            return 279;
        }

        public static int mDUpdateActionSinceVersion()
        {
            return 0;
        }

        public static int mDUpdateActionEncodingOffset()
        {
            return 32;
        }

        public static int mDUpdateActionEncodingLength()
        {
            return 0;
        }

        public static String mDUpdateActionMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static byte mDUpdateActionNullValue()
        {
            return (byte)-128;
        }

        public static byte mDUpdateActionMinValue()
        {
            return (byte)-127;
        }

        public static byte mDUpdateActionMaxValue()
        {
            return (byte)127;
        }

        public byte mDUpdateAction()
        {
            return (byte)0;
        }

        public static int mDEntryTypeId()
        {
            return 269;
        }

        public static int mDEntryTypeSinceVersion()
        {
            return 0;
        }

        public static int mDEntryTypeEncodingOffset()
        {
            return 32;
        }

        public static int mDEntryTypeEncodingLength()
        {
            return 0;
        }

        public static String mDEntryTypeMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "char";
            }

            return "";
        }

        public static byte mDEntryTypeNullValue()
        {
            return (byte)0;
        }

        public static byte mDEntryTypeMinValue()
        {
            return (byte)32;
        }

        public static byte mDEntryTypeMaxValue()
        {
            return (byte)126;
        }

        private static final byte[] MDENTRYTYPE_VALUE = {103};

        public static int mDEntryTypeLength()
        {
            return 1;
        }

        public byte mDEntryType(final int index)
        {
            return MDENTRYTYPE_VALUE[index];
        }

        public int getMDEntryType(final byte[] dst, final int offset, final int length)
        {
            final int bytesCopied = Math.min(length, 1);
            System.arraycopy(MDENTRYTYPE_VALUE, 0, dst, offset, bytesCopied);

            return bytesCopied;
        }


        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='HighLimitPrice', referencedName='null', description='Upper price threshold for the instrument', id=1149, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Price'}}
            //Token{signal=BEGIN_COMPOSITE, name='PRICENULL', referencedName='null', description='Upper price threshold for the instrument', id=-1, version=0, deprecated=0, encodedLength=8, offset=0, componentTokenCount=4, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='Price'}}
            builder.append("highLimitPrice=");
            highLimitPrice().appendTo(builder);
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='LowLimitPrice', referencedName='null', description='Lower price threshold for the instrument', id=1148, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Price'}}
            //Token{signal=BEGIN_COMPOSITE, name='PRICENULL', referencedName='null', description='Lower price threshold for the instrument', id=-1, version=0, deprecated=0, encodedLength=8, offset=8, componentTokenCount=4, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='Price'}}
            builder.append("lowLimitPrice=");
            lowLimitPrice().appendTo(builder);
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='MaxPriceVariation', referencedName='null', description='Differential static value for price banding', id=1143, version=0, deprecated=0, encodedLength=0, offset=16, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Price'}}
            //Token{signal=BEGIN_COMPOSITE, name='PRICENULL', referencedName='null', description='Differential static value for price banding', id=-1, version=0, deprecated=0, encodedLength=8, offset=16, componentTokenCount=4, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='Price'}}
            builder.append("maxPriceVariation=");
            maxPriceVariation().appendTo(builder);
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='SecurityID', referencedName='null', description='Security ID ', id=48, version=0, deprecated=0, encodedLength=0, offset=24, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int32', referencedName='null', description='Security ID ', id=-1, version=0, deprecated=0, encodedLength=4, offset=24, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("securityID=");
            builder.append(securityID());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='RptSeq', referencedName='null', description='MD Entry sequence number per instrument update', id=83, version=0, deprecated=0, encodedLength=0, offset=28, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='uInt32', referencedName='null', description='MD Entry sequence number per instrument update', id=-1, version=0, deprecated=0, encodedLength=4, offset=28, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("rptSeq=");
            builder.append(rptSeq());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='MDUpdateAction', referencedName='null', description='Market Data entry update action. In order to delete banding value, high or low limit, the deleted price field is populated with a NULL ', id=279, version=0, deprecated=0, encodedLength=0, offset=32, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='MDUpdateActionNew', referencedName='null', description='Market Data entry update action. In order to delete banding value, high or low limit, the deleted price field is populated with a NULL ', id=-1, version=0, deprecated=0, encodedLength=0, offset=32, componentTokenCount=1, encoding=Encoding{presence=CONSTANT, primitiveType=INT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=0, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=BEGIN_FIELD, name='MDEntryType', referencedName='null', description='Market Data entry type   ', id=269, version=0, deprecated=0, encodedLength=0, offset=32, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='char'}}
            //Token{signal=ENCODING, name='MDEntryTypeLimits', referencedName='null', description='Market Data entry type   ', id=-1, version=0, deprecated=0, encodedLength=0, offset=32, componentTokenCount=1, encoding=Encoding{presence=CONSTANT, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=103, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='char'}}
            builder.append(')');
            return builder;
        }
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[MDIncrementalRefreshLimitsBanding34](sbeTemplateId=");
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
        //Token{signal=BEGIN_FIELD, name='TransactTime', referencedName='null', description='Start of event processing time in number of nanoseconds since Unix epoch', id=60, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='UTCTimestamp'}}
        //Token{signal=ENCODING, name='uInt64', referencedName='null', description='Start of event processing time in number of nanoseconds since Unix epoch', id=-1, version=0, deprecated=0, encodedLength=8, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT64, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='UTCTimestamp'}}
        builder.append("transactTime=");
        builder.append(transactTime());
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=5799, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=12, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='MultipleCharValue'}}
        //Token{signal=BEGIN_SET, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=-1, version=0, deprecated=0, encodedLength=1, offset=8, componentTokenCount=10, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='MultipleCharValue'}}
        builder.append("matchEventIndicator=");
        builder.append(matchEventIndicator());
        builder.append('|');
        //Token{signal=BEGIN_GROUP, name='NoMDEntries', referencedName='null', description='Number of entries in Market Data message', id=268, version=0, deprecated=0, encodedLength=32, offset=11, componentTokenCount=36, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("noMDEntries=[");
        NoMDEntriesDecoder noMDEntries = noMDEntries();
        if (noMDEntries.count() > 0)
        {
            while (noMDEntries.hasNext())
            {
                noMDEntries.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');

        limit(originalLimit);

        return builder;
    }
}
