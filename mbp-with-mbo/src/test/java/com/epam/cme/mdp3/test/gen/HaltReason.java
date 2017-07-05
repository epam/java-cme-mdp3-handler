/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.HaltReason"})
public enum HaltReason
{
    GroupSchedule((short)0),
    SurveillanceIntervention((short)1),
    MarketEvent((short)2),
    InstrumentActivation((short)3),
    InstrumentExpiration((short)4),
    Unknown((short)5),
    RecoveryInProcess((short)6),
    NULL_VAL((short)255);

    private final short value;

    HaltReason(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static HaltReason get(final short value)
    {
        switch (value)
        {
            case 0: return GroupSchedule;
            case 1: return SurveillanceIntervention;
            case 2: return MarketEvent;
            case 3: return InstrumentActivation;
            case 4: return InstrumentExpiration;
            case 5: return Unknown;
            case 6: return RecoveryInProcess;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
