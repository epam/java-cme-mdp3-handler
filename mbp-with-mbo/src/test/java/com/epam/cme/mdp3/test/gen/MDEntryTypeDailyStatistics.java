/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDEntryTypeDailyStatistics"})
public enum MDEntryTypeDailyStatistics
{
    SettlementPrice((byte)54),
    ClearedVolume((byte)66),
    OpenInterest((byte)67),
    FixingPrice((byte)87),
    NULL_VAL((byte)0);

    private final byte value;

    MDEntryTypeDailyStatistics(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static MDEntryTypeDailyStatistics get(final byte value)
    {
        switch (value)
        {
            case 54: return SettlementPrice;
            case 66: return ClearedVolume;
            case 67: return OpenInterest;
            case 87: return FixingPrice;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
