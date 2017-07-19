/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.SettlPriceTypeDecoder"})
@SuppressWarnings("all")
public class SettlPriceTypeDecoder
{
    public static final int ENCODED_LENGTH = 1;
    private DirectBuffer buffer;
    private int offset;

    public SettlPriceTypeDecoder wrap(final DirectBuffer buffer, final int offset)
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

    public boolean final1()
    {
        return 0 != (buffer.getByte(offset) & (1 << 0));
    }

    public boolean actual()
    {
        return 0 != (buffer.getByte(offset) & (1 << 1));
    }

    public boolean rounded()
    {
        return 0 != (buffer.getByte(offset) & (1 << 2));
    }

    public boolean intraday()
    {
        return 0 != (buffer.getByte(offset) & (1 << 3));
    }

    public boolean reservedBits()
    {
        return 0 != (buffer.getByte(offset) & (1 << 4));
    }

    public boolean nullValue()
    {
        return 0 != (buffer.getByte(offset) & (1 << 7));
    }

    public String toString()
    {
        return appendTo(new StringBuilder(100)).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        builder.append('{');
        boolean atLeastOne = false;
        if (final1())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("final1");
            atLeastOne = true;
        }
        if (actual())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("actual");
            atLeastOne = true;
        }
        if (rounded())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("rounded");
            atLeastOne = true;
        }
        if (intraday())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("intraday");
            atLeastOne = true;
        }
        if (reservedBits())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("reservedBits");
            atLeastOne = true;
        }
        if (nullValue())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("nullValue");
            atLeastOne = true;
        }
        builder.append('}');

        return builder;
    }
}
