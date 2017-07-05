/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDInstrumentDefinitionSpread29Encoder"})
@SuppressWarnings("all")
public class MDInstrumentDefinitionSpread29Encoder
{
    public static final int BLOCK_LENGTH = 195;
    public static final int TEMPLATE_ID = 29;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDInstrumentDefinitionSpread29Encoder parentMessage = this;
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
        return "d";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public MDInstrumentDefinitionSpread29Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int matchEventIndicatorEncodingOffset()
    {
        return 0;
    }

    public static int matchEventIndicatorEncodingLength()
    {
        return 1;
    }

    private final MatchEventIndicatorEncoder matchEventIndicator = new MatchEventIndicatorEncoder();

    public MatchEventIndicatorEncoder matchEventIndicator()
    {
        matchEventIndicator.wrap(buffer, offset + 0);
        return matchEventIndicator;
    }

    public static int totNumReportsEncodingOffset()
    {
        return 1;
    }

    public static int totNumReportsEncodingLength()
    {
        return 4;
    }

    public static long totNumReportsNullValue()
    {
        return 4294967295L;
    }

    public static long totNumReportsMinValue()
    {
        return 0L;
    }

    public static long totNumReportsMaxValue()
    {
        return 4294967293L;
    }

    public MDInstrumentDefinitionSpread29Encoder totNumReports(final long value)
    {
        buffer.putInt(offset + 1, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int securityUpdateActionEncodingOffset()
    {
        return 5;
    }

    public static int securityUpdateActionEncodingLength()
    {
        return 1;
    }

    public MDInstrumentDefinitionSpread29Encoder securityUpdateAction(final SecurityUpdateAction value)
    {
        buffer.putByte(offset + 5, value.value());
        return this;
    }

    public static int lastUpdateTimeEncodingOffset()
    {
        return 6;
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

    public MDInstrumentDefinitionSpread29Encoder lastUpdateTime(final long value)
    {
        buffer.putLong(offset + 6, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int mDSecurityTradingStatusEncodingOffset()
    {
        return 14;
    }

    public static int mDSecurityTradingStatusEncodingLength()
    {
        return 1;
    }

    public MDInstrumentDefinitionSpread29Encoder mDSecurityTradingStatus(final SecurityTradingStatus value)
    {
        buffer.putByte(offset + 14, (byte)value.value());
        return this;
    }

    public static int applIDEncodingOffset()
    {
        return 15;
    }

    public static int applIDEncodingLength()
    {
        return 2;
    }

    public static short applIDNullValue()
    {
        return (short)-32768;
    }

    public static short applIDMinValue()
    {
        return (short)-32767;
    }

    public static short applIDMaxValue()
    {
        return (short)32767;
    }

    public MDInstrumentDefinitionSpread29Encoder applID(final short value)
    {
        buffer.putShort(offset + 15, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int marketSegmentIDEncodingOffset()
    {
        return 17;
    }

    public static int marketSegmentIDEncodingLength()
    {
        return 1;
    }

    public static short marketSegmentIDNullValue()
    {
        return (short)255;
    }

    public static short marketSegmentIDMinValue()
    {
        return (short)0;
    }

    public static short marketSegmentIDMaxValue()
    {
        return (short)254;
    }

    public MDInstrumentDefinitionSpread29Encoder marketSegmentID(final short value)
    {
        buffer.putByte(offset + 17, (byte)value);
        return this;
    }


    public static int underlyingProductEncodingOffset()
    {
        return 18;
    }

    public static int underlyingProductEncodingLength()
    {
        return 1;
    }

    public static short underlyingProductNullValue()
    {
        return (short)255;
    }

    public static short underlyingProductMinValue()
    {
        return (short)0;
    }

    public static short underlyingProductMaxValue()
    {
        return (short)254;
    }

    public MDInstrumentDefinitionSpread29Encoder underlyingProduct(final short value)
    {
        buffer.putByte(offset + 18, (byte)value);
        return this;
    }


    public static int securityExchangeEncodingOffset()
    {
        return 19;
    }

    public static int securityExchangeEncodingLength()
    {
        return 4;
    }

    public static byte securityExchangeNullValue()
    {
        return (byte)0;
    }

    public static byte securityExchangeMinValue()
    {
        return (byte)32;
    }

    public static byte securityExchangeMaxValue()
    {
        return (byte)126;
    }

    public static int securityExchangeLength()
    {
        return 4;
    }

    public void securityExchange(final int index, final byte value)
    {
        if (index < 0 || index >= 4)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 19 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String securityExchangeCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putSecurityExchange(final byte[] src, final int srcOffset)
    {
        final int length = 4;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 19, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder securityExchange(final String src)
    {
        final int length = 4;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 19, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 19 + start, (byte)0);
        }

        return this;
    }

    public static int securityGroupEncodingOffset()
    {
        return 23;
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

        final int pos = this.offset + 23 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String securityGroupCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putSecurityGroup(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 23, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder securityGroup(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 23, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 23 + start, (byte)0);
        }

        return this;
    }

    public static int assetEncodingOffset()
    {
        return 29;
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

        final int pos = this.offset + 29 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String assetCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putAsset(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 29, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder asset(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 29, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 29 + start, (byte)0);
        }

        return this;
    }

    public static int symbolEncodingOffset()
    {
        return 35;
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

        final int pos = this.offset + 35 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String symbolCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putSymbol(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 35, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder symbol(final String src)
    {
        final int length = 20;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 35, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 35 + start, (byte)0);
        }

        return this;
    }

    public static int securityIDEncodingOffset()
    {
        return 55;
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

    public MDInstrumentDefinitionSpread29Encoder securityID(final int value)
    {
        buffer.putInt(offset + 55, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int securityIDSourceEncodingOffset()
    {
        return 59;
    }

    public static int securityIDSourceEncodingLength()
    {
        return 0;
    }

    public static byte securityIDSourceNullValue()
    {
        return (byte)0;
    }

    public static byte securityIDSourceMinValue()
    {
        return (byte)32;
    }

    public static byte securityIDSourceMaxValue()
    {
        return (byte)126;
    }

    private static final byte[] SECURITYIDSOURCE_VALUE = {56};

    public static int securityIDSourceLength()
    {
        return 1;
    }

    public byte securityIDSource(final int index)
    {
        return SECURITYIDSOURCE_VALUE[index];
    }

    public int getSecurityIDSource(final byte[] dst, final int offset, final int length)
    {
        final int bytesCopied = Math.min(length, 1);
        System.arraycopy(SECURITYIDSOURCE_VALUE, 0, dst, offset, bytesCopied);

        return bytesCopied;
    }

    public static int securityTypeEncodingOffset()
    {
        return 59;
    }

    public static int securityTypeEncodingLength()
    {
        return 6;
    }

    public static byte securityTypeNullValue()
    {
        return (byte)0;
    }

    public static byte securityTypeMinValue()
    {
        return (byte)32;
    }

    public static byte securityTypeMaxValue()
    {
        return (byte)126;
    }

    public static int securityTypeLength()
    {
        return 6;
    }

    public void securityType(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 59 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String securityTypeCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putSecurityType(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 59, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder securityType(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 59, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 59 + start, (byte)0);
        }

        return this;
    }

    public static int cFICodeEncodingOffset()
    {
        return 65;
    }

    public static int cFICodeEncodingLength()
    {
        return 6;
    }

    public static byte cFICodeNullValue()
    {
        return (byte)0;
    }

    public static byte cFICodeMinValue()
    {
        return (byte)32;
    }

    public static byte cFICodeMaxValue()
    {
        return (byte)126;
    }

    public static int cFICodeLength()
    {
        return 6;
    }

    public void cFICode(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 65 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String cFICodeCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putCFICode(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 65, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder cFICode(final String src)
    {
        final int length = 6;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 65, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 65 + start, (byte)0);
        }

        return this;
    }

    public static int maturityMonthYearEncodingOffset()
    {
        return 71;
    }

    public static int maturityMonthYearEncodingLength()
    {
        return 5;
    }

    private final MaturityMonthYearEncoder maturityMonthYear = new MaturityMonthYearEncoder();

    public MaturityMonthYearEncoder maturityMonthYear()
    {
        maturityMonthYear.wrap(buffer, offset + 71);
        return maturityMonthYear;
    }

    public static int currencyEncodingOffset()
    {
        return 76;
    }

    public static int currencyEncodingLength()
    {
        return 3;
    }

    public static byte currencyNullValue()
    {
        return (byte)0;
    }

    public static byte currencyMinValue()
    {
        return (byte)32;
    }

    public static byte currencyMaxValue()
    {
        return (byte)126;
    }

    public static int currencyLength()
    {
        return 3;
    }

    public void currency(final int index, final byte value)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 76 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String currencyCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putCurrency(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 76, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder currency(final String src)
    {
        final int length = 3;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 76, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 76 + start, (byte)0);
        }

        return this;
    }

    public static int securitySubTypeEncodingOffset()
    {
        return 79;
    }

    public static int securitySubTypeEncodingLength()
    {
        return 5;
    }

    public static byte securitySubTypeNullValue()
    {
        return (byte)0;
    }

    public static byte securitySubTypeMinValue()
    {
        return (byte)32;
    }

    public static byte securitySubTypeMaxValue()
    {
        return (byte)126;
    }

    public static int securitySubTypeLength()
    {
        return 5;
    }

    public void securitySubType(final int index, final byte value)
    {
        if (index < 0 || index >= 5)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 79 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String securitySubTypeCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putSecuritySubType(final byte[] src, final int srcOffset)
    {
        final int length = 5;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 79, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder securitySubType(final String src)
    {
        final int length = 5;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 79, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 79 + start, (byte)0);
        }

        return this;
    }

    public static int userDefinedInstrumentEncodingOffset()
    {
        return 84;
    }

    public static int userDefinedInstrumentEncodingLength()
    {
        return 1;
    }

    public static byte userDefinedInstrumentNullValue()
    {
        return (byte)0;
    }

    public static byte userDefinedInstrumentMinValue()
    {
        return (byte)32;
    }

    public static byte userDefinedInstrumentMaxValue()
    {
        return (byte)126;
    }

    public MDInstrumentDefinitionSpread29Encoder userDefinedInstrument(final byte value)
    {
        buffer.putByte(offset + 84, value);
        return this;
    }


    public static int matchAlgorithmEncodingOffset()
    {
        return 85;
    }

    public static int matchAlgorithmEncodingLength()
    {
        return 1;
    }

    public static byte matchAlgorithmNullValue()
    {
        return (byte)0;
    }

    public static byte matchAlgorithmMinValue()
    {
        return (byte)32;
    }

    public static byte matchAlgorithmMaxValue()
    {
        return (byte)126;
    }

    public MDInstrumentDefinitionSpread29Encoder matchAlgorithm(final byte value)
    {
        buffer.putByte(offset + 85, value);
        return this;
    }


    public static int minTradeVolEncodingOffset()
    {
        return 86;
    }

    public static int minTradeVolEncodingLength()
    {
        return 4;
    }

    public static long minTradeVolNullValue()
    {
        return 4294967294L;
    }

    public static long minTradeVolMinValue()
    {
        return 0L;
    }

    public static long minTradeVolMaxValue()
    {
        return 4294967293L;
    }

    public MDInstrumentDefinitionSpread29Encoder minTradeVol(final long value)
    {
        buffer.putInt(offset + 86, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int maxTradeVolEncodingOffset()
    {
        return 90;
    }

    public static int maxTradeVolEncodingLength()
    {
        return 4;
    }

    public static long maxTradeVolNullValue()
    {
        return 4294967294L;
    }

    public static long maxTradeVolMinValue()
    {
        return 0L;
    }

    public static long maxTradeVolMaxValue()
    {
        return 4294967293L;
    }

    public MDInstrumentDefinitionSpread29Encoder maxTradeVol(final long value)
    {
        buffer.putInt(offset + 90, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int minPriceIncrementEncodingOffset()
    {
        return 94;
    }

    public static int minPriceIncrementEncodingLength()
    {
        return 8;
    }

    private final PRICEEncoder minPriceIncrement = new PRICEEncoder();

    public PRICEEncoder minPriceIncrement()
    {
        minPriceIncrement.wrap(buffer, offset + 94);
        return minPriceIncrement;
    }

    public static int displayFactorEncodingOffset()
    {
        return 102;
    }

    public static int displayFactorEncodingLength()
    {
        return 8;
    }

    private final FLOATEncoder displayFactor = new FLOATEncoder();

    public FLOATEncoder displayFactor()
    {
        displayFactor.wrap(buffer, offset + 102);
        return displayFactor;
    }

    public static int priceDisplayFormatEncodingOffset()
    {
        return 110;
    }

    public static int priceDisplayFormatEncodingLength()
    {
        return 1;
    }

    public static short priceDisplayFormatNullValue()
    {
        return (short)255;
    }

    public static short priceDisplayFormatMinValue()
    {
        return (short)0;
    }

    public static short priceDisplayFormatMaxValue()
    {
        return (short)254;
    }

    public MDInstrumentDefinitionSpread29Encoder priceDisplayFormat(final short value)
    {
        buffer.putByte(offset + 110, (byte)value);
        return this;
    }


    public static int priceRatioEncodingOffset()
    {
        return 111;
    }

    public static int priceRatioEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder priceRatio = new PRICENULLEncoder();

    public PRICENULLEncoder priceRatio()
    {
        priceRatio.wrap(buffer, offset + 111);
        return priceRatio;
    }

    public static int tickRuleEncodingOffset()
    {
        return 119;
    }

    public static int tickRuleEncodingLength()
    {
        return 1;
    }

    public static byte tickRuleNullValue()
    {
        return (byte)127;
    }

    public static byte tickRuleMinValue()
    {
        return (byte)-127;
    }

    public static byte tickRuleMaxValue()
    {
        return (byte)127;
    }

    public MDInstrumentDefinitionSpread29Encoder tickRule(final byte value)
    {
        buffer.putByte(offset + 119, value);
        return this;
    }


    public static int unitOfMeasureEncodingOffset()
    {
        return 120;
    }

    public static int unitOfMeasureEncodingLength()
    {
        return 30;
    }

    public static byte unitOfMeasureNullValue()
    {
        return (byte)0;
    }

    public static byte unitOfMeasureMinValue()
    {
        return (byte)32;
    }

    public static byte unitOfMeasureMaxValue()
    {
        return (byte)126;
    }

    public static int unitOfMeasureLength()
    {
        return 30;
    }

    public void unitOfMeasure(final int index, final byte value)
    {
        if (index < 0 || index >= 30)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 120 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String unitOfMeasureCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionSpread29Encoder putUnitOfMeasure(final byte[] src, final int srcOffset)
    {
        final int length = 30;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 120, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionSpread29Encoder unitOfMeasure(final String src)
    {
        final int length = 30;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 120, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 120 + start, (byte)0);
        }

        return this;
    }

    public static int tradingReferencePriceEncodingOffset()
    {
        return 150;
    }

    public static int tradingReferencePriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder tradingReferencePrice = new PRICENULLEncoder();

    public PRICENULLEncoder tradingReferencePrice()
    {
        tradingReferencePrice.wrap(buffer, offset + 150);
        return tradingReferencePrice;
    }

    public static int settlPriceTypeEncodingOffset()
    {
        return 158;
    }

    public static int settlPriceTypeEncodingLength()
    {
        return 1;
    }

    private final SettlPriceTypeEncoder settlPriceType = new SettlPriceTypeEncoder();

    public SettlPriceTypeEncoder settlPriceType()
    {
        settlPriceType.wrap(buffer, offset + 158);
        return settlPriceType;
    }

    public static int openInterestQtyEncodingOffset()
    {
        return 159;
    }

    public static int openInterestQtyEncodingLength()
    {
        return 4;
    }

    public static int openInterestQtyNullValue()
    {
        return 2147483647;
    }

    public static int openInterestQtyMinValue()
    {
        return -2147483647;
    }

    public static int openInterestQtyMaxValue()
    {
        return 2147483647;
    }

    public MDInstrumentDefinitionSpread29Encoder openInterestQty(final int value)
    {
        buffer.putInt(offset + 159, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int clearedVolumeEncodingOffset()
    {
        return 163;
    }

    public static int clearedVolumeEncodingLength()
    {
        return 4;
    }

    public static int clearedVolumeNullValue()
    {
        return 2147483647;
    }

    public static int clearedVolumeMinValue()
    {
        return -2147483647;
    }

    public static int clearedVolumeMaxValue()
    {
        return 2147483647;
    }

    public MDInstrumentDefinitionSpread29Encoder clearedVolume(final int value)
    {
        buffer.putInt(offset + 163, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int highLimitPriceEncodingOffset()
    {
        return 167;
    }

    public static int highLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder highLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder highLimitPrice()
    {
        highLimitPrice.wrap(buffer, offset + 167);
        return highLimitPrice;
    }

    public static int lowLimitPriceEncodingOffset()
    {
        return 175;
    }

    public static int lowLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder lowLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder lowLimitPrice()
    {
        lowLimitPrice.wrap(buffer, offset + 175);
        return lowLimitPrice;
    }

    public static int maxPriceVariationEncodingOffset()
    {
        return 183;
    }

    public static int maxPriceVariationEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder maxPriceVariation = new PRICENULLEncoder();

    public PRICENULLEncoder maxPriceVariation()
    {
        maxPriceVariation.wrap(buffer, offset + 183);
        return maxPriceVariation;
    }

    public static int mainFractionEncodingOffset()
    {
        return 191;
    }

    public static int mainFractionEncodingLength()
    {
        return 1;
    }

    public static short mainFractionNullValue()
    {
        return (short)255;
    }

    public static short mainFractionMinValue()
    {
        return (short)0;
    }

    public static short mainFractionMaxValue()
    {
        return (short)254;
    }

    public MDInstrumentDefinitionSpread29Encoder mainFraction(final short value)
    {
        buffer.putByte(offset + 191, (byte)value);
        return this;
    }


    public static int subFractionEncodingOffset()
    {
        return 192;
    }

    public static int subFractionEncodingLength()
    {
        return 1;
    }

    public static short subFractionNullValue()
    {
        return (short)255;
    }

    public static short subFractionMinValue()
    {
        return (short)0;
    }

    public static short subFractionMaxValue()
    {
        return (short)254;
    }

    public MDInstrumentDefinitionSpread29Encoder subFraction(final short value)
    {
        buffer.putByte(offset + 192, (byte)value);
        return this;
    }


    public static int tradingReferenceDateEncodingOffset()
    {
        return 193;
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

    public MDInstrumentDefinitionSpread29Encoder tradingReferenceDate(final int value)
    {
        buffer.putShort(offset + 193, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    private final NoEventsEncoder noEvents = new NoEventsEncoder();

    public static long noEventsId()
    {
        return 864;
    }

    public NoEventsEncoder noEventsCount(final int count)
    {
        noEvents.wrap(parentMessage, buffer, count);
        return noEvents;
    }

    public static class NoEventsEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionSpread29Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionSpread29Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)9);
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
            return 9;
        }

        public NoEventsEncoder next()
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

        public static int eventTypeEncodingOffset()
        {
            return 0;
        }

        public static int eventTypeEncodingLength()
        {
            return 1;
        }

        public NoEventsEncoder eventType(final EventType value)
        {
            buffer.putByte(offset + 0, (byte)value.value());
            return this;
        }

        public static int eventTimeEncodingOffset()
        {
            return 1;
        }

        public static int eventTimeEncodingLength()
        {
            return 8;
        }

        public static long eventTimeNullValue()
        {
            return 0xffffffffffffffffL;
        }

        public static long eventTimeMinValue()
        {
            return 0x0L;
        }

        public static long eventTimeMaxValue()
        {
            return 0xfffffffffffffffeL;
        }

        public NoEventsEncoder eventTime(final long value)
        {
            buffer.putLong(offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

    }

    private final NoMDFeedTypesEncoder noMDFeedTypes = new NoMDFeedTypesEncoder();

    public static long noMDFeedTypesId()
    {
        return 1141;
    }

    public NoMDFeedTypesEncoder noMDFeedTypesCount(final int count)
    {
        noMDFeedTypes.wrap(parentMessage, buffer, count);
        return noMDFeedTypes;
    }

    public static class NoMDFeedTypesEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionSpread29Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionSpread29Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)4);
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
            return 4;
        }

        public NoMDFeedTypesEncoder next()
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

        public static int mDFeedTypeEncodingOffset()
        {
            return 0;
        }

        public static int mDFeedTypeEncodingLength()
        {
            return 3;
        }

        public static byte mDFeedTypeNullValue()
        {
            return (byte)0;
        }

        public static byte mDFeedTypeMinValue()
        {
            return (byte)32;
        }

        public static byte mDFeedTypeMaxValue()
        {
            return (byte)126;
        }

        public static int mDFeedTypeLength()
        {
            return 3;
        }

        public void mDFeedType(final int index, final byte value)
        {
            if (index < 0 || index >= 3)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            final int pos = this.offset + 0 + (index * 1);
            buffer.putByte(pos, value);
        }

        public static String mDFeedTypeCharacterEncoding()
        {
            return "UTF-8";
        }

        public NoMDFeedTypesEncoder putMDFeedType(final byte[] src, final int srcOffset)
        {
            final int length = 3;
            if (srcOffset < 0 || srcOffset > (src.length - length))
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }

            buffer.putBytes(this.offset + 0, src, srcOffset, length);

            return this;
        }

        public NoMDFeedTypesEncoder mDFeedType(final String src)
        {
            final int length = 3;
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

        public static int marketDepthEncodingOffset()
        {
            return 3;
        }

        public static int marketDepthEncodingLength()
        {
            return 1;
        }

        public static byte marketDepthNullValue()
        {
            return (byte)-128;
        }

        public static byte marketDepthMinValue()
        {
            return (byte)-127;
        }

        public static byte marketDepthMaxValue()
        {
            return (byte)127;
        }

        public NoMDFeedTypesEncoder marketDepth(final byte value)
        {
            buffer.putByte(offset + 3, value);
            return this;
        }

    }

    private final NoInstAttribEncoder noInstAttrib = new NoInstAttribEncoder();

    public static long noInstAttribId()
    {
        return 870;
    }

    public NoInstAttribEncoder noInstAttribCount(final int count)
    {
        noInstAttrib.wrap(parentMessage, buffer, count);
        return noInstAttrib;
    }

    public static class NoInstAttribEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionSpread29Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionSpread29Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)4);
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
            return 4;
        }

        public NoInstAttribEncoder next()
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

        public static int instAttribTypeEncodingOffset()
        {
            return 0;
        }

        public static int instAttribTypeEncodingLength()
        {
            return 0;
        }

        public static byte instAttribTypeNullValue()
        {
            return (byte)-128;
        }

        public static byte instAttribTypeMinValue()
        {
            return (byte)-127;
        }

        public static byte instAttribTypeMaxValue()
        {
            return (byte)127;
        }

        public byte instAttribType()
        {
            return (byte)24;
        }

        public static int instAttribValueEncodingOffset()
        {
            return 0;
        }

        public static int instAttribValueEncodingLength()
        {
            return 4;
        }

        private final InstAttribValueEncoder instAttribValue = new InstAttribValueEncoder();

        public InstAttribValueEncoder instAttribValue()
        {
            instAttribValue.wrap(buffer, offset + 0);
            return instAttribValue;
        }
    }

    private final NoLotTypeRulesEncoder noLotTypeRules = new NoLotTypeRulesEncoder();

    public static long noLotTypeRulesId()
    {
        return 1234;
    }

    public NoLotTypeRulesEncoder noLotTypeRulesCount(final int count)
    {
        noLotTypeRules.wrap(parentMessage, buffer, count);
        return noLotTypeRules;
    }

    public static class NoLotTypeRulesEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionSpread29Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionSpread29Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)5);
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
            return 5;
        }

        public NoLotTypeRulesEncoder next()
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

        public static int lotTypeEncodingOffset()
        {
            return 0;
        }

        public static int lotTypeEncodingLength()
        {
            return 1;
        }

        public static byte lotTypeNullValue()
        {
            return (byte)-128;
        }

        public static byte lotTypeMinValue()
        {
            return (byte)-127;
        }

        public static byte lotTypeMaxValue()
        {
            return (byte)127;
        }

        public NoLotTypeRulesEncoder lotType(final byte value)
        {
            buffer.putByte(offset + 0, value);
            return this;
        }


        public static int minLotSizeEncodingOffset()
        {
            return 1;
        }

        public static int minLotSizeEncodingLength()
        {
            return 4;
        }

        private final DecimalQtyEncoder minLotSize = new DecimalQtyEncoder();

        public DecimalQtyEncoder minLotSize()
        {
            minLotSize.wrap(buffer, offset + 1);
            return minLotSize;
        }
    }

    private final NoLegsEncoder noLegs = new NoLegsEncoder();

    public static long noLegsId()
    {
        return 555;
    }

    public NoLegsEncoder noLegsCount(final int count)
    {
        noLegs.wrap(parentMessage, buffer, count);
        return noLegs;
    }

    public static class NoLegsEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionSpread29Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionSpread29Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 254)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)18);
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
            return 18;
        }

        public NoLegsEncoder next()
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

        public static int legSecurityIDEncodingOffset()
        {
            return 0;
        }

        public static int legSecurityIDEncodingLength()
        {
            return 4;
        }

        public static int legSecurityIDNullValue()
        {
            return -2147483648;
        }

        public static int legSecurityIDMinValue()
        {
            return -2147483647;
        }

        public static int legSecurityIDMaxValue()
        {
            return 2147483647;
        }

        public NoLegsEncoder legSecurityID(final int value)
        {
            buffer.putInt(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int legSecurityIDSourceEncodingOffset()
        {
            return 4;
        }

        public static int legSecurityIDSourceEncodingLength()
        {
            return 0;
        }

        public static byte legSecurityIDSourceNullValue()
        {
            return (byte)0;
        }

        public static byte legSecurityIDSourceMinValue()
        {
            return (byte)32;
        }

        public static byte legSecurityIDSourceMaxValue()
        {
            return (byte)126;
        }

        private static final byte[] LEGSECURITYIDSOURCE_VALUE = {56};

        public static int legSecurityIDSourceLength()
        {
            return 1;
        }

        public byte legSecurityIDSource(final int index)
        {
            return LEGSECURITYIDSOURCE_VALUE[index];
        }

        public int getLegSecurityIDSource(final byte[] dst, final int offset, final int length)
        {
            final int bytesCopied = Math.min(length, 1);
            System.arraycopy(LEGSECURITYIDSOURCE_VALUE, 0, dst, offset, bytesCopied);

            return bytesCopied;
        }

        public static int legSideEncodingOffset()
        {
            return 4;
        }

        public static int legSideEncodingLength()
        {
            return 1;
        }

        public NoLegsEncoder legSide(final LegSide value)
        {
            buffer.putByte(offset + 4, (byte)value.value());
            return this;
        }

        public static int legRatioQtyEncodingOffset()
        {
            return 5;
        }

        public static int legRatioQtyEncodingLength()
        {
            return 1;
        }

        public static byte legRatioQtyNullValue()
        {
            return (byte)-128;
        }

        public static byte legRatioQtyMinValue()
        {
            return (byte)-127;
        }

        public static byte legRatioQtyMaxValue()
        {
            return (byte)127;
        }

        public NoLegsEncoder legRatioQty(final byte value)
        {
            buffer.putByte(offset + 5, value);
            return this;
        }


        public static int legPriceEncodingOffset()
        {
            return 6;
        }

        public static int legPriceEncodingLength()
        {
            return 8;
        }

        private final PRICENULLEncoder legPrice = new PRICENULLEncoder();

        public PRICENULLEncoder legPrice()
        {
            legPrice.wrap(buffer, offset + 6);
            return legPrice;
        }

        public static int legOptionDeltaEncodingOffset()
        {
            return 14;
        }

        public static int legOptionDeltaEncodingLength()
        {
            return 4;
        }

        private final DecimalQtyEncoder legOptionDelta = new DecimalQtyEncoder();

        public DecimalQtyEncoder legOptionDelta()
        {
            legOptionDelta.wrap(buffer, offset + 14);
            return legOptionDelta;
        }
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        MDInstrumentDefinitionSpread29Decoder writer = new MDInstrumentDefinitionSpread29Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
