/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.OrderUpdateAction"})
public enum OrderUpdateAction
{
    New((short)0),
    Update((short)1),
    Delete((short)2),
    NULL_VAL((short)255);

    private final short value;

    OrderUpdateAction(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrderUpdateAction get(final short value)
    {
        switch (value)
        {
            case 0: return New;
            case 1: return Update;
            case 2: return Delete;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
