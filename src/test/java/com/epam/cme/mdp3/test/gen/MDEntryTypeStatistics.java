/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDEntryTypeStatistics"})
public enum MDEntryTypeStatistics
{
    OpenPrice((byte)52),
    HighTrade((byte)55),
    LowTrade((byte)56),
    HighestBid((byte)78),
    LowestOffer((byte)79),
    NULL_VAL((byte)0);

    private final byte value;

    MDEntryTypeStatistics(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static MDEntryTypeStatistics get(final byte value)
    {
        switch (value)
        {
            case 52: return OpenPrice;
            case 55: return HighTrade;
            case 56: return LowTrade;
            case 78: return HighestBid;
            case 79: return LowestOffer;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
