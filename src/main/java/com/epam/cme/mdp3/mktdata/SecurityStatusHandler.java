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

import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.mktdata.enums.HaltReason;
import com.epam.cme.mdp3.mktdata.enums.SecurityTradingEvent;
import com.epam.cme.mdp3.mktdata.enums.SecurityTradingStatus;
import com.epam.cme.mdp3.sbe.message.SbeString;

public class SecurityStatusHandler {
    private final ChannelContext channelContext;
    private final SbeString secGroup = SbeString.allocate(10);
    private final SbeString secAsset = SbeString.allocate(10);

    public SecurityStatusHandler(final ChannelContext channelContext) {
        this.channelContext = channelContext;
    }

    public void handle(final MdpMessage statusMessage, final short matchEventIndicator) {
        final int securityId = statusMessage.getInt32(48);

        channelContext.notifySecurityStatus(securityId, statusMessage);

        if (channelContext.hasMdListeners()) {
            statusMessage.getString(1151, secGroup);
            statusMessage.getString(6937, secAsset);
            final int tradeDate = statusMessage.getUInt16(75);
            final SecurityTradingStatus securityTradingStatus = SecurityTradingStatus.fromFIX(statusMessage.getInt8(326));
            final HaltReason haltReason = HaltReason.fromFIX(statusMessage.getInt8(327));
            final SecurityTradingEvent securityTradingEvent = SecurityTradingEvent.fromFIX(statusMessage.getInt8(1174));

            channelContext.notifySecurityStatus(secGroup, secAsset, securityId, tradeDate, matchEventIndicator,
                    securityTradingStatus, haltReason, securityTradingEvent);
        }
    }
}
