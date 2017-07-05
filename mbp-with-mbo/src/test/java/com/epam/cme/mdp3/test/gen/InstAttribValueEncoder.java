/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.MutableDirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.InstAttribValueEncoder"})
@SuppressWarnings("all")
public class InstAttribValueEncoder
{
    public static final int ENCODED_LENGTH = 4;
    private MutableDirectBuffer buffer;
    private int offset;

    public InstAttribValueEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public InstAttribValueEncoder clear()
    {
        buffer.putInt(offset, (int)0L, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder electronicMatchEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 0) : bits & ~(1 << 0);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder orderCrossEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 1) : bits & ~(1 << 1);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder blockTradeEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 2) : bits & ~(1 << 2);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder eFPEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 3) : bits & ~(1 << 3);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder eBFEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 4) : bits & ~(1 << 4);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder eFSEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 5) : bits & ~(1 << 5);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder eFREligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 6) : bits & ~(1 << 6);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder oTCEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 7) : bits & ~(1 << 7);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder iLinkIndicativeMassQuotingEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 8) : bits & ~(1 << 8);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder negativeStrikeEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 9) : bits & ~(1 << 9);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder negativePriceOutrightEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 10) : bits & ~(1 << 10);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder isFractional(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 11) : bits & ~(1 << 11);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder volatilityQuotedOption(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 12) : bits & ~(1 << 12);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder rFQCrossEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 13) : bits & ~(1 << 13);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder zeroPriceOutrightEligible(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 14) : bits & ~(1 << 14);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder decayingProductEligibility(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 15) : bits & ~(1 << 15);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder variableProductEligibility(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 16) : bits & ~(1 << 16);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder dailyProductEligibility(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 17) : bits & ~(1 << 17);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder gTOrdersEligibility(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 18) : bits & ~(1 << 18);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public InstAttribValueEncoder impliedMatchingEligibility(final boolean value)
    {
        int bits = buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
        bits = value ? bits | (1 << 19) : bits & ~(1 << 19);
        buffer.putInt(offset, bits, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
