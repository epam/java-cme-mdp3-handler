/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.PutOrCall"})
public enum PutOrCall
{
    Put((short)0),
    Call((short)1),
    NULL_VAL((short)255);

    private final short value;

    PutOrCall(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static PutOrCall get(final short value)
    {
        switch (value)
        {
            case 0: return Put;
            case 1: return Call;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
