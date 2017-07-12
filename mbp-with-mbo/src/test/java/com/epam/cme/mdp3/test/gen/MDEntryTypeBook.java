/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MDEntryTypeBook"})
public enum MDEntryTypeBook
{
    Bid((byte)48),
    Offer((byte)49),
    ImpliedBid((byte)69),
    ImpliedOffer((byte)70),
    BookReset((byte)74),
    NULL_VAL((byte)0);

    private final byte value;

    MDEntryTypeBook(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static MDEntryTypeBook get(final byte value)
    {
        switch (value)
        {
            case 48: return Bid;
            case 49: return Offer;
            case 69: return ImpliedBid;
            case 70: return ImpliedOffer;
            case 74: return BookReset;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
