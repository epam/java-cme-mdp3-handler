/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDIncrementalRefreshTradeSummary42Decoder"})
@SuppressWarnings("all")
public class MDIncrementalRefreshTradeSummary42Decoder
{
    public static final int BLOCK_LENGTH = 11;
    public static final int TEMPLATE_ID = 42;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDIncrementalRefreshTradeSummary42Decoder parentMessage = this;
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

    public MDIncrementalRefreshTradeSummary42Decoder wrap(
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
        private MDIncrementalRefreshTradeSummary42Decoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        public void wrap(
            final MDIncrementalRefreshTradeSummary42Decoder parentMessage, final DirectBuffer buffer)
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

        public static int mDEntryPxId()
        {
            return 270;
        }

        public static int mDEntryPxSinceVersion()
        {
            return 0;
        }

        public static int mDEntryPxEncodingOffset()
        {
            return 0;
        }

        public static int mDEntryPxEncodingLength()
        {
            return 8;
        }

        public static String mDEntryPxMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Price";
            }

            return "";
        }

        private final PRICEDecoder mDEntryPx = new PRICEDecoder();

        public PRICEDecoder mDEntryPx()
        {
            mDEntryPx.wrap(buffer, offset + 0);
            return mDEntryPx;
        }

        public static int mDEntrySizeId()
        {
            return 271;
        }

        public static int mDEntrySizeSinceVersion()
        {
            return 0;
        }

        public static int mDEntrySizeEncodingOffset()
        {
            return 8;
        }

        public static int mDEntrySizeEncodingLength()
        {
            return 4;
        }

        public static String mDEntrySizeMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Qty";
            }

            return "";
        }

        public static int mDEntrySizeNullValue()
        {
            return -2147483648;
        }

        public static int mDEntrySizeMinValue()
        {
            return -2147483647;
        }

        public static int mDEntrySizeMaxValue()
        {
            return 2147483647;
        }

        public int mDEntrySize()
        {
            return buffer.getInt(offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
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
            return 12;
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
            return buffer.getInt(offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
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
            return 16;
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
            return (buffer.getInt(offset + 16, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        }


        public static int numberOfOrdersId()
        {
            return 346;
        }

        public static int numberOfOrdersSinceVersion()
        {
            return 0;
        }

        public static int numberOfOrdersEncodingOffset()
        {
            return 20;
        }

        public static int numberOfOrdersEncodingLength()
        {
            return 4;
        }

        public static String numberOfOrdersMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static int numberOfOrdersNullValue()
        {
            return 2147483647;
        }

        public static int numberOfOrdersMinValue()
        {
            return -2147483647;
        }

        public static int numberOfOrdersMaxValue()
        {
            return 2147483647;
        }

        public int numberOfOrders()
        {
            return buffer.getInt(offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int aggressorSideId()
        {
            return 5797;
        }

        public static int aggressorSideSinceVersion()
        {
            return 0;
        }

        public static int aggressorSideEncodingOffset()
        {
            return 24;
        }

        public static int aggressorSideEncodingLength()
        {
            return 1;
        }

        public static String aggressorSideMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public AggressorSide aggressorSide()
        {
            return AggressorSide.get(((short)(buffer.getByte(offset + 24) & 0xFF)));
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
            return 25;
        }

        public static int mDUpdateActionEncodingLength()
        {
            return 1;
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

        public MDUpdateAction mDUpdateAction()
        {
            return MDUpdateAction.get(((short)(buffer.getByte(offset + 25) & 0xFF)));
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
            return 26;
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

        private static final byte[] MDENTRYTYPE_VALUE = {50};

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

        public static int mDTradeEntryIDId()
        {
            return 37711;
        }

        public static int mDTradeEntryIDSinceVersion()
        {
            return 7;
        }

        public static int mDTradeEntryIDEncodingOffset()
        {
            return 26;
        }

        public static int mDTradeEntryIDEncodingLength()
        {
            return 4;
        }

        public static String mDTradeEntryIDMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static long mDTradeEntryIDNullValue()
        {
            return 4294967295L;
        }

        public static long mDTradeEntryIDMinValue()
        {
            return 0L;
        }

        public static long mDTradeEntryIDMaxValue()
        {
            return 4294967293L;
        }

        public long mDTradeEntryID()
        {
            if (parentMessage.actingVersion < 7)
            {
                return 4294967295L;
            }

            return (buffer.getInt(offset + 26, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
        }



        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='MDEntryPx', referencedName='null', description='Trade price', id=270, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=6, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Price'}}
            //Token{signal=BEGIN_COMPOSITE, name='PRICE', referencedName='null', description='Trade price', id=-1, version=0, deprecated=0, encodedLength=8, offset=0, componentTokenCount=4, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='Price'}}
            builder.append("mDEntryPx=");
            mDEntryPx().appendTo(builder);
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='MDEntrySize', referencedName='null', description='Consolidated trade quantity', id=271, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            //Token{signal=ENCODING, name='Int32', referencedName='null', description='Consolidated trade quantity', id=-1, version=0, deprecated=0, encodedLength=4, offset=8, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            builder.append("mDEntrySize=");
            builder.append(mDEntrySize());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='SecurityID', referencedName='null', description='Security ID as defined by CME', id=48, version=0, deprecated=0, encodedLength=0, offset=12, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int32', referencedName='null', description='Security ID as defined by CME', id=-1, version=0, deprecated=0, encodedLength=4, offset=12, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("securityID=");
            builder.append(securityID());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='RptSeq', referencedName='null', description='Sequence number per instrument update', id=83, version=0, deprecated=0, encodedLength=0, offset=16, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='uInt32', referencedName='null', description='Sequence number per instrument update', id=-1, version=0, deprecated=0, encodedLength=4, offset=16, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("rptSeq=");
            builder.append(rptSeq());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='NumberOfOrders', referencedName='null', description='The total number of real orders per instrument that participated in a match step within a match event', id=346, version=0, deprecated=0, encodedLength=0, offset=20, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int32NULL', referencedName='null', description='The total number of real orders per instrument that participated in a match step within a match event', id=-1, version=0, deprecated=0, encodedLength=4, offset=20, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=2147483647, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("numberOfOrders=");
            builder.append(numberOfOrders());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='AggressorSide', referencedName='null', description='Indicates which side is the aggressor or if there is no aggressor', id=5797, version=0, deprecated=0, encodedLength=0, offset=24, componentTokenCount=7, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=BEGIN_ENUM, name='AggressorSide', referencedName='null', description='Indicates which side is the aggressor or if there is no aggressor', id=-1, version=0, deprecated=0, encodedLength=1, offset=24, componentTokenCount=5, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='int'}}
            builder.append("aggressorSide=");
            builder.append(aggressorSide());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='MDUpdateAction', referencedName='null', description='Market Data update action', id=279, version=0, deprecated=0, encodedLength=0, offset=25, componentTokenCount=10, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=BEGIN_ENUM, name='MDUpdateAction', referencedName='null', description='Market Data update action', id=-1, version=0, deprecated=0, encodedLength=1, offset=25, componentTokenCount=8, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='int'}}
            builder.append("mDUpdateAction=");
            builder.append(mDUpdateAction());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='MDEntryType', referencedName='null', description='Market Data entry type', id=269, version=0, deprecated=0, encodedLength=0, offset=26, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='char'}}
            //Token{signal=ENCODING, name='MDEntryTypeTrade', referencedName='null', description='Market Data entry type', id=-1, version=0, deprecated=0, encodedLength=0, offset=26, componentTokenCount=1, encoding=Encoding{presence=CONSTANT, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=50, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='char'}}
            //Token{signal=BEGIN_FIELD, name='MDTradeEntryID', referencedName='null', description='Market Data Trade entry ID', id=37711, version=7, deprecated=0, encodedLength=0, offset=26, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='uInt32NULL', referencedName='null', description='Market Data Trade entry ID', id=-1, version=7, deprecated=0, encodedLength=4, offset=26, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=UINT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=4294967295, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("mDTradeEntryID=");
            builder.append(mDTradeEntryID());
            builder.append(')');
            return builder;
        }
    }

    private final NoOrderIDEntriesDecoder noOrderIDEntries = new NoOrderIDEntriesDecoder();

    public static long noOrderIDEntriesDecoderId()
    {
        return 37705;
    }

    public static int noOrderIDEntriesDecoderSinceVersion()
    {
        return 0;
    }

    public NoOrderIDEntriesDecoder noOrderIDEntries()
    {
        noOrderIDEntries.wrap(parentMessage, buffer);
        return noOrderIDEntries;
    }

    public static class NoOrderIDEntriesDecoder
        implements Iterable<NoOrderIDEntriesDecoder>, java.util.Iterator<NoOrderIDEntriesDecoder>
    {
        private static final int HEADER_SIZE = 8;
        private final GroupSize8ByteDecoder dimensions = new GroupSize8ByteDecoder();
        private MDIncrementalRefreshTradeSummary42Decoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        public void wrap(
            final MDIncrementalRefreshTradeSummary42Decoder parentMessage, final DirectBuffer buffer)
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
            return 16;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<NoOrderIDEntriesDecoder> iterator()
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

        public NoOrderIDEntriesDecoder next()
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

        public static int orderIDId()
        {
            return 37;
        }

        public static int orderIDSinceVersion()
        {
            return 0;
        }

        public static int orderIDEncodingOffset()
        {
            return 0;
        }

        public static int orderIDEncodingLength()
        {
            return 8;
        }

        public static String orderIDMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static long orderIDNullValue()
        {
            return 0xffffffffffffffffL;
        }

        public static long orderIDMinValue()
        {
            return 0x0L;
        }

        public static long orderIDMaxValue()
        {
            return 0xfffffffffffffffeL;
        }

        public long orderID()
        {
            return buffer.getLong(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int lastQtyId()
        {
            return 32;
        }

        public static int lastQtySinceVersion()
        {
            return 0;
        }

        public static int lastQtyEncodingOffset()
        {
            return 8;
        }

        public static int lastQtyEncodingLength()
        {
            return 4;
        }

        public static String lastQtyMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Qty";
            }

            return "";
        }

        public static int lastQtyNullValue()
        {
            return -2147483648;
        }

        public static int lastQtyMinValue()
        {
            return -2147483647;
        }

        public static int lastQtyMaxValue()
        {
            return 2147483647;
        }

        public int lastQty()
        {
            return buffer.getInt(offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
        }



        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='OrderID', referencedName='null', description='Unique order identifier as assigned by the exchange', id=37, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='uInt64', referencedName='null', description='Unique order identifier as assigned by the exchange', id=-1, version=0, deprecated=0, encodedLength=8, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=UINT64, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("orderID=");
            builder.append(orderID());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='LastQty', referencedName='null', description='Quantity bought or sold on this last fill', id=32, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            //Token{signal=ENCODING, name='Int32', referencedName='null', description='Quantity bought or sold on this last fill', id=-1, version=0, deprecated=0, encodedLength=4, offset=8, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            builder.append("lastQty=");
            builder.append(lastQty());
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
        builder.append("[MDIncrementalRefreshTradeSummary42](sbeTemplateId=");
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
        //Token{signal=BEGIN_GROUP, name='NoMDEntries', referencedName='null', description='Number of Trade Summary entries', id=268, version=0, deprecated=0, encodedLength=32, offset=11, componentTokenCount=47, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
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
        builder.append('|');
        //Token{signal=BEGIN_GROUP, name='NoOrderIDEntries', referencedName='null', description='Number of OrderID entries', id=37705, version=0, deprecated=0, encodedLength=16, offset=-1, componentTokenCount=12, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("noOrderIDEntries=[");
        NoOrderIDEntriesDecoder noOrderIDEntries = noOrderIDEntries();
        if (noOrderIDEntries.count() > 0)
        {
            while (noOrderIDEntries.hasNext())
            {
                noOrderIDEntries.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');

        limit(originalLimit);

        return builder;
    }
}
