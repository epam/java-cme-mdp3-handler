/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SettlPriceTypeEncoder"})
@SuppressWarnings("all")
public class SettlPriceTypeEncoder
{
    public static final int ENCODED_LENGTH = 1;
    private MutableDirectBuffer buffer;
    private int offset;

    public SettlPriceTypeEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public SettlPriceTypeEncoder clear()
    {
        buffer.putByte(offset, (byte)(short)0);
        return this;
    }

    public SettlPriceTypeEncoder final1(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 0) : bits & ~(1 << 0));
        buffer.putByte(offset, bits);
        return this;
    }

    public SettlPriceTypeEncoder actual(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 1) : bits & ~(1 << 1));
        buffer.putByte(offset, bits);
        return this;
    }

    public SettlPriceTypeEncoder rounded(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 2) : bits & ~(1 << 2));
        buffer.putByte(offset, bits);
        return this;
    }

    public SettlPriceTypeEncoder intraday(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 3) : bits & ~(1 << 3));
        buffer.putByte(offset, bits);
        return this;
    }

    public SettlPriceTypeEncoder reservedBits(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 4) : bits & ~(1 << 4));
        buffer.putByte(offset, bits);
        return this;
    }

    public SettlPriceTypeEncoder nullValue(final boolean value)
    {
        byte bits = buffer.getByte(offset);
        bits = (byte)(value ? bits | (1 << 7) : bits & ~(1 << 7));
        buffer.putByte(offset, bits);
        return this;
    }
}
