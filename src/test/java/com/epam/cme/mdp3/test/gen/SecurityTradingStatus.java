/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SecurityTradingStatus"})
public enum SecurityTradingStatus
{
    TradingHalt((short)2),
    Close((short)4),
    NewPriceIndication((short)15),
    ReadyToTrade((short)17),
    NotAvailableForTrading((short)18),
    UnknownorInvalid((short)20),
    PreOpen((short)21),
    PreCross((short)24),
    Cross((short)25),
    PostClose((short)26),
    NoChange((short)103),
    NULL_VAL((short)255);

    private final short value;

    SecurityTradingStatus(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static SecurityTradingStatus get(final short value)
    {
        switch (value)
        {
            case 2: return TradingHalt;
            case 4: return Close;
            case 15: return NewPriceIndication;
            case 17: return ReadyToTrade;
            case 18: return NotAvailableForTrading;
            case 20: return UnknownorInvalid;
            case 21: return PreOpen;
            case 24: return PreCross;
            case 25: return Cross;
            case 26: return PostClose;
            case 103: return NoChange;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
