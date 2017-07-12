/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SecurityTradingEvent"})
public enum SecurityTradingEvent
{
    NoEvent((short)0),
    NoCancel((short)1),
    ResetStatistics((short)4),
    ImpliedMatchingON((short)5),
    ImpliedMatchingOFF((short)6),
    NULL_VAL((short)255);

    private final short value;

    SecurityTradingEvent(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static SecurityTradingEvent get(final short value)
    {
        switch (value)
        {
            case 0: return NoEvent;
            case 1: return NoCancel;
            case 4: return ResetStatistics;
            case 5: return ImpliedMatchingON;
            case 6: return ImpliedMatchingOFF;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
