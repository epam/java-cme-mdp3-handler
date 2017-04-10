/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.OpenCloseSettlFlag"})
public enum OpenCloseSettlFlag
{
    DailyOpenPrice((short)0),
    IndicativeOpeningPrice((short)5),
    NULL_VAL((short)255);

    private final short value;

    OpenCloseSettlFlag(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OpenCloseSettlFlag get(final short value)
    {
        switch (value)
        {
            case 0: return DailyOpenPrice;
            case 5: return IndicativeOpeningPrice;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
