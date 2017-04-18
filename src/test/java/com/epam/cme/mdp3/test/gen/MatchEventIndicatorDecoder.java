/* Generated SBE (Simple Binary Encoding) message codec */
package com.epam.cme.mdp3.test.gen;

import org.agrona.DirectBuffer;

@javax.annotation.Generated(value = {"com.epam.cme.mdp3.test.gen.MatchEventIndicatorDecoder"})
@SuppressWarnings("all")
public class MatchEventIndicatorDecoder
{
    public static final int ENCODED_LENGTH = 1;
    private DirectBuffer buffer;
    private int offset;

    public MatchEventIndicatorDecoder wrap(final DirectBuffer buffer, final int offset)
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

    public boolean lastTradeMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 0));
    }

    public boolean lastVolumeMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 1));
    }

    public boolean lastQuoteMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 2));
    }

    public boolean lastStatsMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 3));
    }

    public boolean lastImpliedMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 4));
    }

    public boolean recoveryMsg()
    {
        return 0 != (buffer.getByte(offset) & (1 << 5));
    }

    public boolean reserved()
    {
        return 0 != (buffer.getByte(offset) & (1 << 6));
    }

    public boolean endOfEvent()
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
        if (lastTradeMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("lastTradeMsg");
            atLeastOne = true;
        }
        if (lastVolumeMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("lastVolumeMsg");
            atLeastOne = true;
        }
        if (lastQuoteMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("lastQuoteMsg");
            atLeastOne = true;
        }
        if (lastStatsMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("lastStatsMsg");
            atLeastOne = true;
        }
        if (lastImpliedMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("lastImpliedMsg");
            atLeastOne = true;
        }
        if (recoveryMsg())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("recoveryMsg");
            atLeastOne = true;
        }
        if (reserved())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("reserved");
            atLeastOne = true;
        }
        if (endOfEvent())
        {
            if (atLeastOne)
            {
                builder.append(',');
            }
            builder.append("endOfEvent");
            atLeastOne = true;
        }
        builder.append('}');

        return builder;
    }
}
