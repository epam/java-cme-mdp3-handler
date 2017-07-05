/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDInstrumentDefinitionOption41Encoder"})
@SuppressWarnings("all")
public class MDInstrumentDefinitionOption41Encoder
{
    public static final int BLOCK_LENGTH = 213;
    public static final int TEMPLATE_ID = 41;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 8;

    private final MDInstrumentDefinitionOption41Encoder parentMessage = this;
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

    public MDInstrumentDefinitionOption41Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public MDInstrumentDefinitionOption41Encoder totNumReports(final long value)
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

    public MDInstrumentDefinitionOption41Encoder securityUpdateAction(final SecurityUpdateAction value)
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

    public MDInstrumentDefinitionOption41Encoder lastUpdateTime(final long value)
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

    public MDInstrumentDefinitionOption41Encoder mDSecurityTradingStatus(final SecurityTradingStatus value)
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

    public MDInstrumentDefinitionOption41Encoder applID(final short value)
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

    public MDInstrumentDefinitionOption41Encoder marketSegmentID(final short value)
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

    public MDInstrumentDefinitionOption41Encoder underlyingProduct(final short value)
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

    public MDInstrumentDefinitionOption41Encoder putSecurityExchange(final byte[] src, final int srcOffset)
    {
        final int length = 4;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 19, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder securityExchange(final String src)
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

    public MDInstrumentDefinitionOption41Encoder putSecurityGroup(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 23, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder securityGroup(final String src)
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

    public MDInstrumentDefinitionOption41Encoder putAsset(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 29, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder asset(final String src)
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

    public MDInstrumentDefinitionOption41Encoder putSymbol(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 35, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder symbol(final String src)
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

    public MDInstrumentDefinitionOption41Encoder securityID(final int value)
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

    public MDInstrumentDefinitionOption41Encoder putSecurityType(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 59, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder securityType(final String src)
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

    public MDInstrumentDefinitionOption41Encoder putCFICode(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 65, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder cFICode(final String src)
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

    public static int putOrCallEncodingOffset()
    {
        return 71;
    }

    public static int putOrCallEncodingLength()
    {
        return 1;
    }

    public MDInstrumentDefinitionOption41Encoder putOrCall(final PutOrCall value)
    {
        buffer.putByte(offset + 71, (byte)value.value());
        return this;
    }

    public static int maturityMonthYearEncodingOffset()
    {
        return 72;
    }

    public static int maturityMonthYearEncodingLength()
    {
        return 5;
    }

    private final MaturityMonthYearEncoder maturityMonthYear = new MaturityMonthYearEncoder();

    public MaturityMonthYearEncoder maturityMonthYear()
    {
        maturityMonthYear.wrap(buffer, offset + 72);
        return maturityMonthYear;
    }

    public static int currencyEncodingOffset()
    {
        return 77;
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

        final int pos = this.offset + 77 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String currencyCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionOption41Encoder putCurrency(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 77, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder currency(final String src)
    {
        final int length = 3;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 77, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 77 + start, (byte)0);
        }

        return this;
    }

    public static int strikePriceEncodingOffset()
    {
        return 80;
    }

    public static int strikePriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder strikePrice = new PRICENULLEncoder();

    public PRICENULLEncoder strikePrice()
    {
        strikePrice.wrap(buffer, offset + 80);
        return strikePrice;
    }

    public static int strikeCurrencyEncodingOffset()
    {
        return 88;
    }

    public static int strikeCurrencyEncodingLength()
    {
        return 3;
    }

    public static byte strikeCurrencyNullValue()
    {
        return (byte)0;
    }

    public static byte strikeCurrencyMinValue()
    {
        return (byte)32;
    }

    public static byte strikeCurrencyMaxValue()
    {
        return (byte)126;
    }

    public static int strikeCurrencyLength()
    {
        return 3;
    }

    public void strikeCurrency(final int index, final byte value)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 88 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String strikeCurrencyCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionOption41Encoder putStrikeCurrency(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 88, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder strikeCurrency(final String src)
    {
        final int length = 3;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 88, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 88 + start, (byte)0);
        }

        return this;
    }

    public static int settlCurrencyEncodingOffset()
    {
        return 91;
    }

    public static int settlCurrencyEncodingLength()
    {
        return 3;
    }

    public static byte settlCurrencyNullValue()
    {
        return (byte)0;
    }

    public static byte settlCurrencyMinValue()
    {
        return (byte)32;
    }

    public static byte settlCurrencyMaxValue()
    {
        return (byte)126;
    }

    public static int settlCurrencyLength()
    {
        return 3;
    }

    public void settlCurrency(final int index, final byte value)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = this.offset + 91 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String settlCurrencyCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionOption41Encoder putSettlCurrency(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 91, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder settlCurrency(final String src)
    {
        final int length = 3;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 91, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 91 + start, (byte)0);
        }

        return this;
    }

    public static int minCabPriceEncodingOffset()
    {
        return 94;
    }

    public static int minCabPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder minCabPrice = new PRICENULLEncoder();

    public PRICENULLEncoder minCabPrice()
    {
        minCabPrice.wrap(buffer, offset + 94);
        return minCabPrice;
    }

    public static int matchAlgorithmEncodingOffset()
    {
        return 102;
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

    public MDInstrumentDefinitionOption41Encoder matchAlgorithm(final byte value)
    {
        buffer.putByte(offset + 102, value);
        return this;
    }


    public static int minTradeVolEncodingOffset()
    {
        return 103;
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

    public MDInstrumentDefinitionOption41Encoder minTradeVol(final long value)
    {
        buffer.putInt(offset + 103, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int maxTradeVolEncodingOffset()
    {
        return 107;
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

    public MDInstrumentDefinitionOption41Encoder maxTradeVol(final long value)
    {
        buffer.putInt(offset + 107, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int minPriceIncrementEncodingOffset()
    {
        return 111;
    }

    public static int minPriceIncrementEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder minPriceIncrement = new PRICENULLEncoder();

    public PRICENULLEncoder minPriceIncrement()
    {
        minPriceIncrement.wrap(buffer, offset + 111);
        return minPriceIncrement;
    }

    public static int minPriceIncrementAmountEncodingOffset()
    {
        return 119;
    }

    public static int minPriceIncrementAmountEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder minPriceIncrementAmount = new PRICENULLEncoder();

    public PRICENULLEncoder minPriceIncrementAmount()
    {
        minPriceIncrementAmount.wrap(buffer, offset + 119);
        return minPriceIncrementAmount;
    }

    public static int displayFactorEncodingOffset()
    {
        return 127;
    }

    public static int displayFactorEncodingLength()
    {
        return 8;
    }

    private final FLOATEncoder displayFactor = new FLOATEncoder();

    public FLOATEncoder displayFactor()
    {
        displayFactor.wrap(buffer, offset + 127);
        return displayFactor;
    }

    public static int tickRuleEncodingOffset()
    {
        return 135;
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

    public MDInstrumentDefinitionOption41Encoder tickRule(final byte value)
    {
        buffer.putByte(offset + 135, value);
        return this;
    }


    public static int mainFractionEncodingOffset()
    {
        return 136;
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

    public MDInstrumentDefinitionOption41Encoder mainFraction(final short value)
    {
        buffer.putByte(offset + 136, (byte)value);
        return this;
    }


    public static int subFractionEncodingOffset()
    {
        return 137;
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

    public MDInstrumentDefinitionOption41Encoder subFraction(final short value)
    {
        buffer.putByte(offset + 137, (byte)value);
        return this;
    }


    public static int priceDisplayFormatEncodingOffset()
    {
        return 138;
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

    public MDInstrumentDefinitionOption41Encoder priceDisplayFormat(final short value)
    {
        buffer.putByte(offset + 138, (byte)value);
        return this;
    }


    public static int unitOfMeasureEncodingOffset()
    {
        return 139;
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

        final int pos = this.offset + 139 + (index * 1);
        buffer.putByte(pos, value);
    }

    public static String unitOfMeasureCharacterEncoding()
    {
        return "UTF-8";
    }

    public MDInstrumentDefinitionOption41Encoder putUnitOfMeasure(final byte[] src, final int srcOffset)
    {
        final int length = 30;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        buffer.putBytes(this.offset + 139, src, srcOffset, length);

        return this;
    }

    public MDInstrumentDefinitionOption41Encoder unitOfMeasure(final String src)
    {
        final int length = 30;
        final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(this.offset + 139, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(this.offset + 139 + start, (byte)0);
        }

        return this;
    }

    public static int unitOfMeasureQtyEncodingOffset()
    {
        return 169;
    }

    public static int unitOfMeasureQtyEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder unitOfMeasureQty = new PRICENULLEncoder();

    public PRICENULLEncoder unitOfMeasureQty()
    {
        unitOfMeasureQty.wrap(buffer, offset + 169);
        return unitOfMeasureQty;
    }

    public static int tradingReferencePriceEncodingOffset()
    {
        return 177;
    }

    public static int tradingReferencePriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder tradingReferencePrice = new PRICENULLEncoder();

    public PRICENULLEncoder tradingReferencePrice()
    {
        tradingReferencePrice.wrap(buffer, offset + 177);
        return tradingReferencePrice;
    }

    public static int settlPriceTypeEncodingOffset()
    {
        return 185;
    }

    public static int settlPriceTypeEncodingLength()
    {
        return 1;
    }

    private final SettlPriceTypeEncoder settlPriceType = new SettlPriceTypeEncoder();

    public SettlPriceTypeEncoder settlPriceType()
    {
        settlPriceType.wrap(buffer, offset + 185);
        return settlPriceType;
    }

    public static int clearedVolumeEncodingOffset()
    {
        return 186;
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

    public MDInstrumentDefinitionOption41Encoder clearedVolume(final int value)
    {
        buffer.putInt(offset + 186, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int openInterestQtyEncodingOffset()
    {
        return 190;
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

    public MDInstrumentDefinitionOption41Encoder openInterestQty(final int value)
    {
        buffer.putInt(offset + 190, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int lowLimitPriceEncodingOffset()
    {
        return 194;
    }

    public static int lowLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder lowLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder lowLimitPrice()
    {
        lowLimitPrice.wrap(buffer, offset + 194);
        return lowLimitPrice;
    }

    public static int highLimitPriceEncodingOffset()
    {
        return 202;
    }

    public static int highLimitPriceEncodingLength()
    {
        return 8;
    }

    private final PRICENULLEncoder highLimitPrice = new PRICENULLEncoder();

    public PRICENULLEncoder highLimitPrice()
    {
        highLimitPrice.wrap(buffer, offset + 202);
        return highLimitPrice;
    }

    public static int userDefinedInstrumentEncodingOffset()
    {
        return 210;
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

    public MDInstrumentDefinitionOption41Encoder userDefinedInstrument(final byte value)
    {
        buffer.putByte(offset + 210, value);
        return this;
    }


    public static int tradingReferenceDateEncodingOffset()
    {
        return 211;
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

    public MDInstrumentDefinitionOption41Encoder tradingReferenceDate(final int value)
    {
        buffer.putShort(offset + 211, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
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
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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

    private final NoUnderlyingsEncoder noUnderlyings = new NoUnderlyingsEncoder();

    public static long noUnderlyingsId()
    {
        return 711;
    }

    public NoUnderlyingsEncoder noUnderlyingsCount(final int count)
    {
        noUnderlyings.wrap(parentMessage, buffer, count);
        return noUnderlyings;
    }

    public static class NoUnderlyingsEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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

        public NoUnderlyingsEncoder next()
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

        public static int underlyingSecurityIDEncodingOffset()
        {
            return 0;
        }

        public static int underlyingSecurityIDEncodingLength()
        {
            return 4;
        }

        public static int underlyingSecurityIDNullValue()
        {
            return -2147483648;
        }

        public static int underlyingSecurityIDMinValue()
        {
            return -2147483647;
        }

        public static int underlyingSecurityIDMaxValue()
        {
            return 2147483647;
        }

        public NoUnderlyingsEncoder underlyingSecurityID(final int value)
        {
            buffer.putInt(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int underlyingSecurityIDSourceEncodingOffset()
        {
            return 4;
        }

        public static int underlyingSecurityIDSourceEncodingLength()
        {
            return 0;
        }

        public static byte underlyingSecurityIDSourceNullValue()
        {
            return (byte)0;
        }

        public static byte underlyingSecurityIDSourceMinValue()
        {
            return (byte)32;
        }

        public static byte underlyingSecurityIDSourceMaxValue()
        {
            return (byte)126;
        }

        private static final byte[] UNDERLYINGSECURITYIDSOURCE_VALUE = {56};

        public static int underlyingSecurityIDSourceLength()
        {
            return 1;
        }

        public byte underlyingSecurityIDSource(final int index)
        {
            return UNDERLYINGSECURITYIDSOURCE_VALUE[index];
        }

        public int getUnderlyingSecurityIDSource(final byte[] dst, final int offset, final int length)
        {
            final int bytesCopied = Math.min(length, 1);
            System.arraycopy(UNDERLYINGSECURITYIDSOURCE_VALUE, 0, dst, offset, bytesCopied);

            return bytesCopied;
        }

        public static int underlyingSymbolEncodingOffset()
        {
            return 4;
        }

        public static int underlyingSymbolEncodingLength()
        {
            return 20;
        }

        public static byte underlyingSymbolNullValue()
        {
            return (byte)0;
        }

        public static byte underlyingSymbolMinValue()
        {
            return (byte)32;
        }

        public static byte underlyingSymbolMaxValue()
        {
            return (byte)126;
        }

        public static int underlyingSymbolLength()
        {
            return 20;
        }

        public void underlyingSymbol(final int index, final byte value)
        {
            if (index < 0 || index >= 20)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            final int pos = this.offset + 4 + (index * 1);
            buffer.putByte(pos, value);
        }

        public static String underlyingSymbolCharacterEncoding()
        {
            return "UTF-8";
        }

        public NoUnderlyingsEncoder putUnderlyingSymbol(final byte[] src, final int srcOffset)
        {
            final int length = 20;
            if (srcOffset < 0 || srcOffset > (src.length - length))
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }

            buffer.putBytes(this.offset + 4, src, srcOffset, length);

            return this;
        }

        public NoUnderlyingsEncoder underlyingSymbol(final String src)
        {
            final int length = 20;
            final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            if (bytes.length > length)
            {
                throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
            }

            buffer.putBytes(this.offset + 4, bytes, 0, bytes.length);

            for (int start = bytes.length; start < length; ++start)
            {
                buffer.putByte(this.offset + 4 + start, (byte)0);
            }

            return this;
        }
    }

    private final NoRelatedInstrumentsEncoder noRelatedInstruments = new NoRelatedInstrumentsEncoder();

    public static long noRelatedInstrumentsId()
    {
        return 1647;
    }

    public NoRelatedInstrumentsEncoder noRelatedInstrumentsCount(final int count)
    {
        noRelatedInstruments.wrap(parentMessage, buffer, count);
        return noRelatedInstruments;
    }

    public static class NoRelatedInstrumentsEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoder dimensions = new GroupSizeEncoder();
        private MDInstrumentDefinitionOption41Encoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final MDInstrumentDefinitionOption41Encoder parentMessage, final MutableDirectBuffer buffer, final int count)
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

        public NoRelatedInstrumentsEncoder next()
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

        public static int relatedSecurityIDEncodingOffset()
        {
            return 0;
        }

        public static int relatedSecurityIDEncodingLength()
        {
            return 4;
        }

        public static int relatedSecurityIDNullValue()
        {
            return -2147483648;
        }

        public static int relatedSecurityIDMinValue()
        {
            return -2147483647;
        }

        public static int relatedSecurityIDMaxValue()
        {
            return 2147483647;
        }

        public NoRelatedInstrumentsEncoder relatedSecurityID(final int value)
        {
            buffer.putInt(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int relatedSecurityIDSourceEncodingOffset()
        {
            return 4;
        }

        public static int relatedSecurityIDSourceEncodingLength()
        {
            return 0;
        }

        public static byte relatedSecurityIDSourceNullValue()
        {
            return (byte)0;
        }

        public static byte relatedSecurityIDSourceMinValue()
        {
            return (byte)32;
        }

        public static byte relatedSecurityIDSourceMaxValue()
        {
            return (byte)126;
        }

        private static final byte[] RELATEDSECURITYIDSOURCE_VALUE = {56};

        public static int relatedSecurityIDSourceLength()
        {
            return 1;
        }

        public byte relatedSecurityIDSource(final int index)
        {
            return RELATEDSECURITYIDSOURCE_VALUE[index];
        }

        public int getRelatedSecurityIDSource(final byte[] dst, final int offset, final int length)
        {
            final int bytesCopied = Math.min(length, 1);
            System.arraycopy(RELATEDSECURITYIDSOURCE_VALUE, 0, dst, offset, bytesCopied);

            return bytesCopied;
        }

        public static int relatedSymbolEncodingOffset()
        {
            return 4;
        }

        public static int relatedSymbolEncodingLength()
        {
            return 20;
        }

        public static byte relatedSymbolNullValue()
        {
            return (byte)0;
        }

        public static byte relatedSymbolMinValue()
        {
            return (byte)32;
        }

        public static byte relatedSymbolMaxValue()
        {
            return (byte)126;
        }

        public static int relatedSymbolLength()
        {
            return 20;
        }

        public void relatedSymbol(final int index, final byte value)
        {
            if (index < 0 || index >= 20)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            final int pos = this.offset + 4 + (index * 1);
            buffer.putByte(pos, value);
        }

        public static String relatedSymbolCharacterEncoding()
        {
            return "UTF-8";
        }

        public NoRelatedInstrumentsEncoder putRelatedSymbol(final byte[] src, final int srcOffset)
        {
            final int length = 20;
            if (srcOffset < 0 || srcOffset > (src.length - length))
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }

            buffer.putBytes(this.offset + 4, src, srcOffset, length);

            return this;
        }

        public NoRelatedInstrumentsEncoder relatedSymbol(final String src)
        {
            final int length = 20;
            final byte[] bytes = src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            if (bytes.length > length)
            {
                throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
            }

            buffer.putBytes(this.offset + 4, bytes, 0, bytes.length);

            for (int start = bytes.length; start < length; ++start)
            {
                buffer.putByte(this.offset + 4 + start, (byte)0);
            }

            return this;
        }
    }


    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        MDInstrumentDefinitionOption41Decoder writer = new MDInstrumentDefinitionOption41Decoder();
        writer.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return writer.appendTo(builder);
    }
}
