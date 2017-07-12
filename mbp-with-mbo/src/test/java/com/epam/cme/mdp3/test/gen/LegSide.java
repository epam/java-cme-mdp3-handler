/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.LegSide"})
public enum LegSide
{
    BuySide((short)1),
    SellSide((short)2),
    NULL_VAL((short)255);

    private final short value;

    LegSide(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static LegSide get(final short value)
    {
        switch (value)
        {
            case 1: return BuySide;
            case 2: return SellSide;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
