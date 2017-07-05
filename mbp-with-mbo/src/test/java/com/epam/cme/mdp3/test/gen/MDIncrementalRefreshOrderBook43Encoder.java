/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDIncrementalRefreshOrderBook43Encoder"})
@SuppressWarnings("all")
public class MDIncrementalRefreshOrderBook43Encoder
{
    public static final int BLOCK_LENGTH = 11;
    public static final int TEMPLATE_ID = 43;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDIncrementalRefreshOrderBook43Encoder parentMessage = this;
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
        return "X";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public MDIncrementalRefreshOrderBook43Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public MDIncrementalRefreshOrderBook43Encoder transactTime(final long value)
    {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int matchEventIndicatorEncodingOffset()
    {
        return 8;
    }

    public static int matchEventIndicatorEncodingLength()
    {
        return 1;
    }

    private final MatchEventIndicatorEncoder matchEventIndicator = new MatchEventIndicatorEncoder();

    public MatchEventIndicatorEncoder matchEventIndicator()
    {
        matchEventIndicator.wrap(buffer, offset + 8);
        return matchEventIndicator;
    }

    private final NoMDEntriesEncoder noMDEntries = new NoMDEntriesEncoder();

    public static long noMDEntriesId()
    {
        return 268;
    }

    public NoMDEntriesEncoder noMDEntriesCount(final int count)
    {
        noMDEntries.wrap(parentMessage, buffer, count);
        return noMDEntries;
    }

    public static class NoMDEntriesEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDIncrementalRefreshOrderBook43Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDIncrementalRefreshOrderBook43Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)40);
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
            return 40;
        }

        public NoMDEntriesEncoder next()
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

        public static int orderIDEncodingOffset()
        {
            return 0;
        }

        public static int orderIDEncodingLength()
        {
            return 8;
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

        public NoMDEntriesEncoder orderID(final long value)
        {
            buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int mDOrderPriorityEncodingOffset()
        {
            return 8;
        }

        public static int mDOrderPriorityEncodingLength()
        {
            return 8;
        }

        public static long mDOrderPriorityNullValue()
        {
            return 0xffffffffffffffffL;
        }

        public static long mDOrderPriorityMinValue()
        {
            return 0x0L;
        }

        public static long mDOrderPriorityMaxValue()
        {
            return 0xfffffffffffffffeL;
        }

        public NoMDEntriesEncoder mDOrderPriority(final long value)
        {
            buffer.putLong(offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int mDEntryPxEncodingOffset()
        {
            return 16;
        }

        public static int mDEntryPxEncodingLength()
        {
            return 8;
        }

        private final PRICENULLEncoder mDEntryPx = new PRICENULLEncoder();

        public PRICENULLEncoder mDEntryPx()
        {
            mDEntryPx.wrap(buffer, offset + 16);
            return mDEntryPx;
        }

        public static int mDDisplayQtyEncodingOffset()
        {
            return 24;
        }

        public static int mDDisplayQtyEncodingLength()
        {
            return 4;
        }

        public static int mDDisplayQtyNullValue()
        {
            return 2147483647;
        }

        public static int mDDisplayQtyMinValue()
        {
            return -2147483647;
        }

        public static int mDDisplayQtyMaxValue()
        {
            return 2147483647;
        }

        public NoMDEntriesEncoder mDDisplayQty(final int value)
        {
            buffer.putInt(offset + 24, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int securityIDEncodingOffset()
        {
            return 28;
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

        public NoMDEntriesEncoder securityID(final int value)
        {
            buffer.putInt(offset + 28, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int mDUpdateActionEncodingOffset()
        {
            return 32;
        }

        public static int mDUpdateActionEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder mDUpdateAction(final MDUpdateAction value)
        {
            buffer.putByte(offset + 32, (byte)value.value());
            return this;
        }

        public static int mDEntryTypeEncodingOffset()
        {
            return 33;
        }

        public static int mDEntryTypeEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder mDEntryType(final MDEntryTypeBook value)
        {
            buffer.putByte(offset + 33, value.value());
            return this;
        }
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        MDIncrementalRefreshOrderBook43Decoder writer = new MDIncrementalRefreshOrderBook43Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
