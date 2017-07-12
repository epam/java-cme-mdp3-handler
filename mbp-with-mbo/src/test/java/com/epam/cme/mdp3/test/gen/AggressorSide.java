/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.AggressorSide"})
public enum AggressorSide
{
    NoAggressor((short)0),
    Buy((short)1),
    Sell((short)2),
    NULL_VAL((short)255);

    private final short value;

    AggressorSide(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static AggressorSide get(final short value)
    {
        switch (value)
        {
            case 0: return NoAggressor;
            case 1: return Buy;
            case 2: return Sell;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
