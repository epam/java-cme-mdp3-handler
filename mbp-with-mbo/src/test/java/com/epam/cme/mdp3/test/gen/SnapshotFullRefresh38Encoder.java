/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SnapshotFullRefresh38Encoder"})
@SuppressWarnings("all")
public class SnapshotFullRefresh38Encoder
{
    public static final int BLOCK_LENGTH = 59;
    public static final int TEMPLATE_ID = 38;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final SnapshotFullRefresh38Encoder parentMessage = this;
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
        return "W";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public SnapshotFullRefresh38Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int lastMsgSeqNumProcessedEncodingOffset()
    {
        return 0;
    }

    public static int lastMsgSeqNumProcessedEncodingLength()
    {
        return 4;
    }

    public static long lastMsgSeqNumProcessedNullValue()
    {
        return 4294967294L;
    }

    public static long lastMsgSeqNumProcessedMinValue()
    {
        return 0L;
    }

    public static long lastMsgSeqNumProcessedMaxValue()
    {
        return 4294967293L;
    }

    public SnapshotFullRefresh38Encoder lastMsgSeqNumProcessed(final long value)
    {
        buffer.putInt(offset + 0, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int totNumReportsEncodingOffset()
    {
        return 4;
    }

    public static int totNumReportsEncodingLength()
    {
        return 4;
    }

    public static long totNumReportsNullValue()
    {
        return 4294967294L;
    }

    public static long totNumReportsMinValue()
    {
        return 0L;
    }

    public static long totNumReportsMaxValue()
    {
        return 4294967293L;
    }

    public SnapshotFullRefresh38Encoder totNumReports(final long value)
    {
        buffer.putInt(offset + 4, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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

    public SnapshotFullRefresh38Encoder securityID(final int value)
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

    public SnapshotFullRefresh38Encoder rptSeq(final long value)
    {
        buffer.putInt(offset + 12, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int transactTimeEncodingOffset()
    {
        return 16;
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

    public SnapshotFullRefresh38Encoder transactTime(final long value)
    {
        buffer.putLong(offset + 16, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int lastUpdateTimeEncodingOffset()
    {
        return 24;
    }

    public static int lastUpdateTimeEncodingLength()
    {
        return 8;
    }

    public static long lastUpdateTimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long lastUpdateTimeMinValue()
    {
        return 0x0L;
    }

    public static long lastUpdateTimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public SnapshotFullRefresh38Encoder lastUpdateTime(final long value)
    {
        buffer.putLong(offset + 24, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int tradeDateEncodingOffset()
    {
        return 32;
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

    public SnapshotFullRefresh38Encoder tradeDate(final int value)
    {
        buffer.putShort(offset + 32, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int mDSecurityTradingStatusEncodingOffset()
    {
        return 34;
    }

    public static int mDSecurityTradingStatusEncodingLength()
    {
        return 1;
    }

    public SnapshotFullRefresh38Encoder mDSecurityTradingStatus(final SecurityTradingStatus value)
    {
        buffer.putByte(offset + 34, (byte)value.value());
        return this;
    }

    public static int highLimitPriceEncodingOffset()
    {
        return 35;
    }

    public static int highLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder highLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder highLimitPrice()
    {
        highLimitPrice.wrap(buffer, offset + 35);
        return highLimitPrice;
    }

    public static int lowLimitPriceEncodingOffset()
    {
        return 43;
    }

    public static int lowLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder lowLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder lowLimitPrice()
    {
        lowLimitPrice.wrap(buffer, offset + 43);
        return lowLimitPrice;
    }

    public static int maxPriceVariationEncodingOffset()
    {
        return 51;
    }

    public static int maxPriceVariationEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder maxPriceVariation = new PRICENULLEncoder();

    public PRICENULLEncoder maxPriceVariation()
    {
        maxPriceVariation.wrap(buffer, offset + 51);
        return maxPriceVariation;
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
        private SnapshotFullRefresh38Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final SnapshotFullRefresh38Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)22);
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
            return 22;
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

        private final PRICENULLEncoder mDEntryPx = new PRICENULLEncoder();

        public PRICENULLEncoder mDEntryPx()
        {
            mDEntryPx.wrap(buffer, offset + 0);
            return mDEntryPx;
        }

        public static int mDEntrySizeEncodingOffset()
        {
            return 8;
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
            buffer.putInt(offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int numberOfOrdersEncodingOffset()
        {
            return 12;
        }

        public static int numberOfOrdersEncodingLength()
        {
            return 4;
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

        public NoMDEntriesEncoder numberOfOrders(final int value)
        {
            buffer.putInt(offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int mDPriceLevelEncodingOffset()
        {
            return 16;
        }

        public static int mDPriceLevelEncodingLength()
        {
            return 1;
        }

        public static byte mDPriceLevelNullValue()
        {
            return (byte)127;
        }

        public static byte mDPriceLevelMinValue()
        {
            return (byte)-127;
        }

        public static byte mDPriceLevelMaxValue()
        {
            return (byte)127;
        }

        public NoMDEntriesEncoder mDPriceLevel(final byte value)
        {
            buffer.putByte(offset + 16, value);
            return this;
        }


        public static int tradingReferenceDateEncodingOffset()
        {
            return 17;
        }

        public static int tradingReferenceDateEncodingLength()
        {
            return 2;
        }

        public static int tradingReferenceDateNullValue()
        {
            return 65535;
        }

        public static int tradingReferenceDateMinValue()
        {
            return 0;
        }

        public static int tradingReferenceDateMaxValue()
        {
            return 65534;
        }

        public NoMDEntriesEncoder tradingReferenceDate(final int value)
        {
            buffer.putShort(offset + 17, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int openCloseSettlFlagEncodingOffset()
        {
            return 19;
        }

        public static int openCloseSettlFlagEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder openCloseSettlFlag(final OpenCloseSettlFlag value)
        {
            buffer.putByte(offset + 19, (byte)value.value());
            return this;
        }

        public static int settlPriceTypeEncodingOffset()
        {
            return 20;
        }

        public static int settlPriceTypeEncodingLength()
        {
            return 1;
        }

        private final SettlPriceTypeEncoder settlPriceType = new SettlPriceTypeEncoder();

        public SettlPriceTypeEncoder settlPriceType()
        {
            settlPriceType.wrap(buffer, offset + 20);
            return settlPriceType;
        }

        public static int mDEntryTypeEncodingOffset()
        {
            return 21;
        }

        public static int mDEntryTypeEncodingLength()
        {
            return 1;
        }

        public NoMDEntriesEncoder mDEntryType(final MDEntryType value)
        {
            buffer.putByte(offset + 21, value.value());
            return this;
        }
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        SnapshotFullRefresh38Decoder writer = new SnapshotFullRefresh38Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
