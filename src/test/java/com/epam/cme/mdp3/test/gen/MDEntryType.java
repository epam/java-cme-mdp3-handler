/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDEntryType"})
public enum MDEntryType
{
    Bid((byte)48),
    Offer((byte)49),
    Trade((byte)50),
    OpenPrice((byte)52),
    SettlementPrice((byte)54),
    TradingSessionHighPrice((byte)55),
    TradingSessionLowPrice((byte)56),
    ClearedVolume((byte)66),
    OpenInterest((byte)67),
    ImpliedBid((byte)69),
    ImpliedOffer((byte)70),
    BookReset((byte)74),
    SessionHighBid((byte)78),
    SessionLowOffer((byte)79),
    FixingPrice((byte)87),
    ElectronicVolume((byte)101),
    ThresholdLimitsandPriceBandVariation((byte)103),
    NULL_VAL((byte)0);

    private final byte value;

    MDEntryType(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static MDEntryType get(final byte value)
    {
        switch (value)
        {
            case 48: return Bid;
            case 49: return Offer;
            case 50: return Trade;
            case 52: return OpenPrice;
            case 54: return SettlementPrice;
            case 55: return TradingSessionHighPrice;
            case 56: return TradingSessionLowPrice;
            case 66: return ClearedVolume;
            case 67: return OpenInterest;
            case 69: return ImpliedBid;
            case 70: return ImpliedOffer;
            case 74: return BookReset;
            case 78: return SessionHighBid;
            case 79: return SessionLowOffer;
            case 87: return FixingPrice;
            case 101: return ElectronicVolume;
            case 103: return ThresholdLimitsandPriceBandVariation;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
