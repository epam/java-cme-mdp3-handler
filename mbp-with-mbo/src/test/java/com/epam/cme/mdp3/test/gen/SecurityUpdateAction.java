/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SecurityUpdateAction"})
public enum SecurityUpdateAction
{
    Add((byte)65),
    Delete((byte)68),
    Modify((byte)77),
    NULL_VAL((byte)0);

    private final byte value;

    SecurityUpdateAction(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static SecurityUpdateAction get(final byte value)
    {
        switch (value)
        {
            case 65: return Add;
            case 68: return Delete;
            case 77: return Modify;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
