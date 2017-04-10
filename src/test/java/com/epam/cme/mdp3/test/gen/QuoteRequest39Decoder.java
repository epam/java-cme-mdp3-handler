/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.QuoteRequest39Decoder"})
@SuppressWarnings("all")
public class QuoteRequest39Decoder
{
    public static final int BLOCK_LENGTH = 35;
    public static final int TEMPLATE_ID = 39;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final QuoteRequest39Decoder parentMessage = this;
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
        return "R";
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public QuoteRequest39Decoder wrap(
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


    public static int quoteReqIDId()
    {
        return 131;
    }

    public static int quoteReqIDSinceVersion()
    {
        return 0;
    }

    public static int quoteReqIDEncodingOffset()
    {
        return 8;
    }

    public static int quoteReqIDEncodingLength()
    {
        return 23;
    }

    public static String quoteReqIDMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "String";
        }

        return "";
    }

    public static byte quoteReqIDNullValue()
    {
        return (byte)0;
    }

    public static byte quoteReqIDMinValue()
    {
        return (byte)32;
    }

    public static byte quoteReqIDMaxValue()
    {
        return (byte)126;
    }

    public static int quoteReqIDLength()
    {
        return 23;
    }

    public byte quoteReqID(final int index)
    {
        if (index < 0 || index >= 23)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String quoteReqIDCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getQuoteReqID(final byte[] dst, final int dstOffset)
    {
        final int length = 23;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        buffer.getBytes(this.offset + 8, dst, dstOffset, length);

        return length;
    }

    public String quoteReqID()
    {
        final byte[] dst = new byte[23];
        buffer.getBytes(this.offset + 8, dst, 0, 23);

        int end = 0;
        for (; end < 23 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.UTF_8);
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
        return 31;
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
        matchEventIndicator.wrap(buffer, offset + 31);
        return matchEventIndicator;
    }

    private final NoRelatedSymDecoder noRelatedSym = new NoRelatedSymDecoder();

    public static long noRelatedSymDecoderId()
    {
        return 146;
    }

    public static int noRelatedSymDecoderSinceVersion()
    {
        return 0;
    }

    public NoRelatedSymDecoder noRelatedSym()
    {
        noRelatedSym.wrap(parentMessage, buffer);
        return noRelatedSym;
    }

    public static class NoRelatedSymDecoder
        implements Iterable<NoRelatedSymDecoder>, java.util.Iterator<NoRelatedSymDecoder>
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeDecoder dimensions = new GroupSizeDecoder();
        private QuoteRequest39Decoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        public void wrap(
            final QuoteRequest39Decoder parentMessage, final DirectBuffer buffer)
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

        public java.util.Iterator<NoRelatedSymDecoder> iterator()
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

        public NoRelatedSymDecoder next()
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

        public static int symbolId()
        {
            return 55;
        }

        public static int symbolSinceVersion()
        {
            return 0;
        }

        public static int symbolEncodingOffset()
        {
            return 0;
        }

        public static int symbolEncodingLength()
        {
            return 20;
        }

        public static String symbolMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "String";
            }

            return "";
        }

        public static byte symbolNullValue()
        {
            return (byte)0;
        }

        public static byte symbolMinValue()
        {
            return (byte)32;
        }

        public static byte symbolMaxValue()
        {
            return (byte)126;
        }

        public static int symbolLength()
        {
            return 20;
        }

        public byte symbol(final int index)
        {
            if (index < 0 || index >= 20)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            final int pos = this.offset + 0 + (index * 1);

            return buffer.getByte(pos);
        }


        public static String symbolCharacterEncoding()
        {
            return "UTF-8";
        }

        public int getSymbol(final byte[] dst, final int dstOffset)
        {
            final int length = 20;
            if (dstOffset < 0 || dstOffset > (dst.length - length))
            {
                throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
            }

            buffer.getBytes(this.offset + 0, dst, dstOffset, length);

            return length;
        }

        public String symbol()
        {
            final byte[] dst = new byte[20];
            buffer.getBytes(this.offset + 0, dst, 0, 20);

            int end = 0;
            for (; end < 20 && dst[end] != 0; ++end);

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
            return buffer.getInt(offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int orderQtyId()
        {
            return 38;
        }

        public static int orderQtySinceVersion()
        {
            return 0;
        }

        public static int orderQtyEncodingOffset()
        {
            return 24;
        }

        public static int orderQtyEncodingLength()
        {
            return 4;
        }

        public static String orderQtyMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "Qty";
            }

            return "";
        }

        public static int orderQtyNullValue()
        {
            return 2147483647;
        }

        public static int orderQtyMinValue()
        {
            return -2147483647;
        }

        public static int orderQtyMaxValue()
        {
            return 2147483647;
        }

        public int orderQty()
        {
            return buffer.getInt(offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN);
        }


        public static int quoteTypeId()
        {
            return 537;
        }

        public static int quoteTypeSinceVersion()
        {
            return 0;
        }

        public static int quoteTypeEncodingOffset()
        {
            return 28;
        }

        public static int quoteTypeEncodingLength()
        {
            return 1;
        }

        public static String quoteTypeMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static byte quoteTypeNullValue()
        {
            return (byte)-128;
        }

        public static byte quoteTypeMinValue()
        {
            return (byte)-127;
        }

        public static byte quoteTypeMaxValue()
        {
            return (byte)127;
        }

        public byte quoteType()
        {
            return buffer.getByte(offset + 28);
        }


        public static int sideId()
        {
            return 54;
        }

        public static int sideSinceVersion()
        {
            return 0;
        }

        public static int sideEncodingOffset()
        {
            return 29;
        }

        public static int sideEncodingLength()
        {
            return 1;
        }

        public static String sideMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "int";
            }

            return "";
        }

        public static byte sideNullValue()
        {
            return (byte)127;
        }

        public static byte sideMinValue()
        {
            return (byte)-127;
        }

        public static byte sideMaxValue()
        {
            return (byte)127;
        }

        public byte side()
        {
            return buffer.getByte(offset + 29);
        }



        public String toString()
        {
            return appendTo(new StringBuilder(100)).toString();
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            builder.append('(');
            //Token{signal=BEGIN_FIELD, name='Symbol', referencedName='null', description='Instrument Name or Symbol', id=55, version=0, deprecated=0, encodedLength=0, offset=0, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
            //Token{signal=ENCODING, name='Symbol', referencedName='null', description='Instrument Name or Symbol', id=-1, version=0, deprecated=0, encodedLength=20, offset=0, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
            builder.append("symbol=");
            for (int i = 0; i < symbolLength() && symbol(i) > 0; i++)
            {
                builder.append((char)symbol(i));
            }
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='SecurityID', referencedName='null', description='Security ID', id=48, version=0, deprecated=0, encodedLength=0, offset=20, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int32', referencedName='null', description='Security ID', id=-1, version=0, deprecated=0, encodedLength=4, offset=20, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("securityID=");
            builder.append(securityID());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='OrderQty', referencedName='null', description='Quantity requested', id=38, version=0, deprecated=0, encodedLength=0, offset=24, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            //Token{signal=ENCODING, name='Int32NULL', referencedName='null', description='Quantity requested', id=-1, version=0, deprecated=0, encodedLength=4, offset=24, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=INT32, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=2147483647, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='Qty'}}
            builder.append("orderQty=");
            builder.append(orderQty());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='QuoteType', referencedName='null', description='Type of quote requested', id=537, version=0, deprecated=0, encodedLength=0, offset=28, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int8', referencedName='null', description='Type of quote requested', id=-1, version=0, deprecated=0, encodedLength=1, offset=28, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=INT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("quoteType=");
            builder.append(quoteType());
            builder.append('|');
            //Token{signal=BEGIN_FIELD, name='Side', referencedName='null', description='Side requested', id=54, version=0, deprecated=0, encodedLength=0, offset=29, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            //Token{signal=ENCODING, name='Int8NULL', referencedName='null', description='Side requested', id=-1, version=0, deprecated=0, encodedLength=1, offset=29, componentTokenCount=1, encoding=Encoding{presence=OPTIONAL, primitiveType=INT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=127, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='int'}}
            builder.append("side=");
            builder.append(side());
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
        builder.append("[QuoteRequest39](sbeTemplateId=");
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
        //Token{signal=BEGIN_FIELD, name='QuoteReqID', referencedName='null', description='Quote Request ID defined by the exchange', id=131, version=0, deprecated=0, encodedLength=0, offset=8, componentTokenCount=3, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        //Token{signal=ENCODING, name='QuoteReqId', referencedName='null', description='Quote Request ID defined by the exchange', id=-1, version=0, deprecated=0, encodedLength=23, offset=8, componentTokenCount=1, encoding=Encoding{presence=REQUIRED, primitiveType=CHAR, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='UTF-8', epoch='unix', timeUnit=nanosecond, semanticType='String'}}
        builder.append("quoteReqID=");
        for (int i = 0; i < quoteReqIDLength() && quoteReqID(i) > 0; i++)
        {
            builder.append((char)quoteReqID(i));
        }
        builder.append('|');
        //Token{signal=BEGIN_FIELD, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=5799, version=0, deprecated=0, encodedLength=0, offset=31, componentTokenCount=12, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='unix', timeUnit=nanosecond, semanticType='MultipleCharValue'}}
        //Token{signal=BEGIN_SET, name='MatchEventIndicator', referencedName='null', description='Bitmap field of eight Boolean type indicators reflecting the end of updates for a given Globex event', id=-1, version=0, deprecated=0, encodedLength=1, offset=31, componentTokenCount=10, encoding=Encoding{presence=REQUIRED, primitiveType=UINT8, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='MultipleCharValue'}}
        builder.append("matchEventIndicator=");
        builder.append(matchEventIndicator());
        builder.append('|');
        //Token{signal=BEGIN_GROUP, name='NoRelatedSym', referencedName='null', description='Indicates the number of repeating symbols specified', id=146, version=0, deprecated=0, encodedLength=32, offset=35, componentTokenCount=21, encoding=Encoding{presence=REQUIRED, primitiveType=null, byteOrder=LITTLE_ENDIAN, minValue=null, maxValue=null, nullValue=null, constValue=null, characterEncoding='null', epoch='null', timeUnit=null, semanticType='null'}}
        builder.append("noRelatedSym=[");
        NoRelatedSymDecoder noRelatedSym = noRelatedSym();
        if (noRelatedSym.count() > 0)
        {
            while (noRelatedSym.hasNext())
            {
                noRelatedSym.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');

        limit(originalLimit);

        return builder;
    }
}
