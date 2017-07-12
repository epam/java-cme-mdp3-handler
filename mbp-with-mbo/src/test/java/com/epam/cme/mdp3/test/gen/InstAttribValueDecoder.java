/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.InstAttribValueDecoder"})
@SuppressWarnings("all")
public class InstAttribValueDecoder
{
    public static final int ENCODED_LENGTH = 4;
    private DirectBuffer buffer;
    private int offset;

    public InstAttribValueDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public boolean electronicMatchEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 0));
    }

    public boolean orderCrossEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 1));
    }

    public boolean blockTradeEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 2));
    }

    public boolean eFPEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 3));
    }

    public boolean eBFEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 4));
    }

    public boolean eFSEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 5));
    }

    public boolean eFREligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 6));
    }

    public boolean oTCEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 7));
    }

    public boolean iLinkIndicativeMassQuotingEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 8));
    }

    public boolean negativeStrikeEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 9));
    }

    public boolean negativePriceOutrightEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 10));
    }

    public boolean isFractional()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 11));
    }

    public boolean volatilityQuotedOption()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 12));
    }

    public boolean rFQCrossEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 13));
    }

    public boolean zeroPriceOutrightEligible()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 14));
    }

    public boolean decayingProductEligibility()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 15));
    }

    public boolean variableProductEligibility()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 16));
    }

    public boolean dailyProductEligibility()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 17));
    }

    public boolean gTOrdersEligibility()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 18));
    }

    public boolean impliedMatchingEligibility()
    {
        return 0 != (buffer.getInt(offset, java.nio.ByteOrder.LITTLE_ENDIAN) & (1 << 19));
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('{');
        boolean atLeastOne = false;
        if (electronicMatchEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("electronicMatchEligible");
            atLeastOne = true;
        }
        if (orderCrossEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("orderCrossEligible");
            atLeastOne = true;
        }
        if (blockTradeEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("blockTradeEligible");
            atLeastOne = true;
        }
        if (eFPEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("eFPEligible");
            atLeastOne = true;
        }
        if (eBFEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("eBFEligible");
            atLeastOne = true;
        }
        if (eFSEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("eFSEligible");
            atLeastOne = true;
        }
        if (eFREligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("eFREligible");
            atLeastOne = true;
        }
        if (oTCEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("oTCEligible");
            atLeastOne = true;
        }
        if (iLinkIndicativeMassQuotingEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("iLinkIndicativeMassQuotingEligible");
            atLeastOne = true;
        }
        if (negativeStrikeEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("negativeStrikeEligible");
            atLeastOne = true;
        }
        if (negativePriceOutrightEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("negativePriceOutrightEligible");
            atLeastOne = true;
        }
        if (isFractional())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("isFractional");
            atLeastOne = true;
        }
        if (volatilityQuotedOption())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("volatilityQuotedOption");
            atLeastOne = true;
        }
        if (rFQCrossEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("rFQCrossEligible");
            atLeastOne = true;
        }
        if (zeroPriceOutrightEligible())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("zeroPriceOutrightEligible");
            atLeastOne = true;
        }
        if (decayingProductEligibility())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("decayingProductEligibility");
            atLeastOne = true;
        }
        if (variableProductEligibility())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("variableProductEligibility");
            atLeastOne = true;
        }
        if (dailyProductEligibility())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("dailyProductEligibility");
            atLeastOne = true;
        }
        if (gTOrdersEligibility())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("gTOrdersEligibility");
            atLeastOne = true;
        }
        if (impliedMatchingEligibility())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("impliedMatchingEligibility");
            atLeastOne = true;
        }
        builder.append('}');

        return builder;
    }
}
