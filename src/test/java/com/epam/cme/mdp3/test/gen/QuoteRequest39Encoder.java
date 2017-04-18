/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.QuoteRequest39Encoder"})
@SuppressWarnings("all")
public class QuoteRequest39Encoder
{
    public static final int BLOCK_LENGTH = 35;
    public static final int TEMPLATE_ID = 39;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final QuoteRequest39Encoder parentMessage = this;
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
        return "R";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public QuoteRequest39Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public QuoteRequest39Encoder transactTime(final long value)
    {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int quoteReqIDEncodingOffset()
    {
        return 8;
    }

    public static int quoteReqIDEncodingLength()
    {
        return 23;
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

    public void quoteReqID(final int index, final byte value)
    {
        if (index < 0 || index >= 23)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 8 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String quoteReqIDCharacterEncoding()
    {
        return "UTF-8";
    }

    public QuoteRequest39Encoder putQuoteReqID(final byte[] src, final int srcOffset)
    {
        final int length = 23;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 8, src, srcOffset, length);

        return this;
    }

    public QuoteRequest39Encoder quoteReqID(final String src)
    {
        final int length = 23;
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

    public static int matchEventIndicatorEncodingOffset()
    {
        return 31;
    }

    public static int matchEventIndicatorEncodingLength()
    {
        return 1;
    }

    private final MatchEventIndicatorEncoder matchEventIndicator = new MatchEventIndicatorEncoder();

    public MatchEventIndicatorEncoder matchEventIndicator()
    {
        matchEventIndicator.wrap(buffer, offset + 31);
        return matchEventIndicator;
    }

    private final NoRelatedSymEncoder noRelatedSym = new NoRelatedSymEncoder();

    public static long noRelatedSymId()
    {
        return 146;
    }

    public NoRelatedSymEncoder noRelatedSymCount(final int count)
    {
        noRelatedSym.wrap(parentMessage, buffer, count);
        return noRelatedSym;
    }

    public static class NoRelatedSymEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private QuoteRequest39Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final QuoteRequest39Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)32);
            dimensions.numInGroup((short)count);
            index = -1;
            this.count = count;
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

        public NoRelatedSymEncoder next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + sbeBlockLength());
            ++index;

            return this;
        }

        public static int symbolEncodingOffset()
        {
            return 0;
        }

        public static int symbolEncodingLength()
        {
            return 20;
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

        public void symbol(final int index, final byte value)
        {
            if (index < 0 || index >= 20)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            final int pos = this.offset + 0 + (index * 1);
            buffer.putByte(pos, value);
        }

        public static String symbolCharacterEncoding()
        {
            return "UTF-8";
        }

        public NoRelatedSymEncoder putSymbol(final byte[] src, final int srcOffset)
        {
            final int length = 20;
            if (srcOffset < 0 || srcOffset > (src.length - length))
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }

            buffer.putBytes(this.offset + 0, src, srcOffset, length);

            return this;
        }

        public NoRelatedSymEncoder symbol(final String src)
        {
            final int length = 20;
            final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            if (bytes.length > length)
            {
                throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
            }

            buffer.putBytes(this.offset + 0, bytes, 0, bytes.length);

            for (int start = bytes.length; start < length; ++start)
            {
                buffer.putByte(this.offset + 0 + start, (byte)0);
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

        public NoRelatedSymEncoder securityID(final int value)
        {
            buffer.putInt(offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int orderQtyEncodingOffset()
        {
            return 24;
        }

        public static int orderQtyEncodingLength()
        {
            return 4;
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

        public NoRelatedSymEncoder orderQty(final int value)
        {
            buffer.putInt(offset + 24, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int quoteTypeEncodingOffset()
        {
            return 28;
        }

        public static int quoteTypeEncodingLength()
        {
            return 1;
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

        public NoRelatedSymEncoder quoteType(final byte value)
        {
            buffer.putByte(offset + 28, value);
            return this;
        }


        public static int sideEncodingOffset()
        {
            return 29;
        }

        public static int sideEncodingLength()
        {
            return 1;
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

        public NoRelatedSymEncoder side(final byte value)
        {
            buffer.putByte(offset + 29, value);
            return this;
        }

    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        QuoteRequest39Decoder writer = new QuoteRequest39Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
