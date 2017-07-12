/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDIncrementalRefreshSessionStatistics35Encoder"})
@SuppressWarnings("all")
public class MDIncrementalRefreshSessionStatistics35Encoder
{
    public static final int BLOCK_LENGTH = 11;
    public static final int TEMPLATE_ID = 35;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDIncrementalRefreshSessionStatistics35Encoder parentMessage = this;
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

    public MDIncrementalRefreshSessionStatistics35Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public MDIncrementalRefreshSessionStatistics35Encoder transactTime(final long value)
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
        private MDIncrementalRefreshSessionStatistics35Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDIncrementalRefreshSessionStatistics35Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)24);
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
            return 24;
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

        public static int mDEntryPxEncodingOffset()
        {
            return 0;
        }

        public static int mDEntryPxEncodingLength()
        {
            return 8;
        }

        private final PRICEEncoder mDEntryPx = new PRICEEncoder();

        public PRICEEncoder mDEntryPx()
        {
            mDEntryPx.wrap(buffer, offset + 0);
            return mDEntryPx;
        }

        public static int securityIDEncodingOffset()
        {
            return 8;
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
            buffer.putInt(offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int rptSeqEncodingOffset()
        {
            return 12;
        }

        public static int rptSeqEncodingLength()
        {
            return 4;
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

        public NoMDEntriesEncoder rptSeq(final long value)
        {
            buffer.putInt(offset + 12, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int openCloseSettlFlagEncodingOffset()
        {
            return 16;
        }

        public static int openCloseSettlFlagEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder openCloseSettlFlag(final OpenCloseSettlFlag value)
        {
            buffer.putByte(offset + 16, (byte)value.value());
            return this;
        }

        public static int mDUpdateActionEncodingOffset()
        {
            return 17;
        }

        public static int mDUpdateActionEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder mDUpdateAction(final MDUpdateAction value)
        {
            buffer.putByte(offset + 17, (byte)value.value());
            return this;
        }

        public static int mDEntryTypeEncodingOffset()
        {
            return 18;
        }

        public static int mDEntryTypeEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder mDEntryType(final MDEntryTypeStatistics value)
        {
            buffer.putByte(offset + 18, value.value());
            return this;
        }

        public static int mDEntrySizeEncodingOffset()
        {
            return 19;
        }

        public static int mDEntrySizeEncodingLength()
        {
            return 4;
        }

        public static int mDEntrySizeNullValue()
        {
            return 2147483647;
        }

        public static int mDEntrySizeMinValue()
        {
            return -2147483647;
        }

        public static int mDEntrySizeMaxValue()
        {
            return 2147483647;
        }

        public NoMDEntriesEncoder mDEntrySize(final int value)
        {
            buffer.putInt(offset + 19, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        MDIncrementalRefreshSessionStatistics35Decoder writer = new MDIncrementalRefreshSessionStatistics35Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
