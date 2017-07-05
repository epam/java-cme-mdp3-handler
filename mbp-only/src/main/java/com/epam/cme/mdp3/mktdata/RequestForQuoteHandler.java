/*
 * Copyright 2004-2016 EPAM Systems
 * This file is part of Java Market Data Handler for CME Market Data (MDP 3.0).
 * Java Market Data Handler for CME Market Data (MDP 3.0) is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Java Market Data Handler for CME Market Data (MDP 3.0) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Java Market Data Handler for CME Market Data (MDP 3.0).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.epam.cme.mdp3.mktdata;

import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.mktdata.enums.QuoteType;
import com.epam.cme.mdp3.mktdata.enums.Side;
import com.epam.cme.mdp3.sbe.message.SbeString;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class RequestForQuoteHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestForQuoteHandler.class);
    private final SbeString quoReqId = SbeString.allocate(40);
    private final ChannelContext channelContext;

    public RequestForQuoteHandler(final ChannelContext channelContext) {
        this.channelContext = channelContext;
    }

    public void handle(final MdpFeedContext feedContext, final MdpMessage quoReqMessage) {
        logger.debug("R: {}", quoReqMessage);
        channelContext.notifyRequestForQuote(quoReqMessage);

        if (channelContext.hasMdListeners()) {
            final MdpGroup quoReqGroup = feedContext.getMdpGroupObj();

            quoReqMessage.getString(131, quoReqId);

            quoReqMessage.getGroup(146, quoReqGroup);
            while (quoReqGroup.hashNext()) {
                quoReqGroup.next();
                final int secId = quoReqGroup.getInt32(48);
                final int orderQty = quoReqGroup.getInt32(38);
                final Side side = Side.fromFIX(quoReqGroup.getInt8(54));
                this.channelContext.notifyRequestForQuote(quoReqId, quoReqGroup.getEntryNum(), quoReqGroup.getNumInGroup(),
                        secId, QuoteType.Tradable, orderQty, side);
            }
        }
    }
}
