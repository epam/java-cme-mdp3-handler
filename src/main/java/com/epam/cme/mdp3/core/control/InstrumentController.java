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

package com.epam.cme.mdp3.core.control;

import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.core.channel.MdpFeedContext;
import com.epam.cme.mdp3.mktdata.MdConstants;
import com.epam.cme.mdp3.sbe.message.SbeConstants;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Iterator;

import static com.epam.cme.mdp3.mktdata.MdConstants.RPT_SEQ_NUM;

public class InstrumentController {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentController.class);
    public static final int DEF_GAP_THRESHOLD = 50;

    private final Integer securityId;
    private String secDesc;
    private final ChannelContext channelContext;
    private InstrumentState state = InstrumentState.INITIAL;
    private long prcdRptSeqNum = 0;
    private final InstrumentMdHandler mdHandler;
    private int gapThreshold = DEF_GAP_THRESHOLD;

    public InstrumentController(final ChannelContext channelContext, final int securityId, final String secDesc, final int subscriptionFlags, final byte maxDepth, final int gapThreshold) {
        this.channelContext = channelContext;
        this.securityId = securityId;
        this.secDesc = secDesc;
        this.gapThreshold = gapThreshold;
        mdHandler = new InstrumentMdHandler(channelContext, securityId, subscriptionFlags, maxDepth);
        init(subscriptionFlags);
    }

    private void init(final int subscriptionFlags) {
        mdHandler.setSubscriptionFlags(subscriptionFlags);
        this.channelContext.subscribeToSnapshotsForInstrument(securityId);
        this.channelContext.notifyInstrumentStateListeners(this.securityId, InstrumentState.NEW, InstrumentState.INITIAL);
    }

    public int getSubscriptionFlags() {
        return mdHandler.getSubscriptionFlags();
    }

    public void setSubscriptionFlags(final int subscrFlags) {
        this.mdHandler.setSubscriptionFlags(subscrFlags);
    }

    public void addSubscriptionFlag(final int subscriptionFlags) {
        mdHandler.addSubscriptionFlag(subscriptionFlags);
    }

    public void removeSubscriptionFlag(final int subscriptionFlags) {
        mdHandler.removeSubscriptionFlag(subscriptionFlags);
    }

    public void setSecDesc(String secDesc) {
        this.secDesc = secDesc;
    }

    public void discontinue() {
        switchState(this.state, InstrumentState.DISCONTINUED);
        this.mdHandler.setSubscriptionFlags(MdEventFlags.NOTHING);
        this.prcdRptSeqNum = 0;
        resetMdHandlers();
    }

    private void resetMdHandlers() {
        mdHandler.reset();
    }

    public boolean onResubscribe(final int subscriptionFlags) {
        if (this.state == InstrumentState.DISCONTINUED) {
            setSubscriptionFlags(subscriptionFlags);
            switchState(InstrumentState.DISCONTINUED, InstrumentState.INITIAL);
            return true;
        }
        return false;
    }

    public void onChannelReset() {
        this.prcdRptSeqNum = 0;
        if (this.state != InstrumentState.DISCONTINUED) {
            switchState(state, InstrumentState.SYNC);
        }
        mdHandler.reset();
    }

    private void handleSnapshotFullRefreshEntries(final MdpFeedContext feedContext, final MdpMessage fullRefreshMsg) {
        channelContext.notifySnapshotFullRefreshListeners(this.secDesc, fullRefreshMsg);
        mdHandler.handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
    }

    void onSnapshotFullRefresh(final MdpFeedContext feedContext, final MdpMessage fullRefreshMsg) {
        final InstrumentState currentState = this.state;
        final long snptSeqNum = fullRefreshMsg.getUInt32(83);
        if (currentState == InstrumentState.INITIAL) {
            logger.debug("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got initial Snapshot. Initial prcdSeqNum is {}",
                        feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, snptSeqNum + 1);
            this.prcdRptSeqNum = snptSeqNum;
            final long msgSeqNum369 = fullRefreshMsg.getUInt32(369);
            switchState(currentState, InstrumentState.SYNC);
            handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
            handleIncrementalQueue(feedContext, msgSeqNum369);
        } else if (currentState == InstrumentState.OUTOFSYNC) {
            if (snptSeqNum > this.prcdRptSeqNum) {
                logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got Snapshot to restore. Fast forward from {} to {}",
                            feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, this.prcdRptSeqNum, snptSeqNum + 1);
                this.prcdRptSeqNum = snptSeqNum;
                this.mdHandler.reset();
                final long msgSeqNum369 = fullRefreshMsg.getUInt32(369);
                switchState(currentState, InstrumentState.SYNC);
                handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
                handleIncrementalQueue(feedContext, msgSeqNum369);
            }
        } else if (currentState == InstrumentState.SYNC && snptSeqNum > this.prcdRptSeqNum) {
            logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Snapshot with high sequence comes faster then Increments. Fast forward from {} to {}",
                        feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, prcdRptSeqNum, snptSeqNum + 1);
            this.prcdRptSeqNum = snptSeqNum;
            this.mdHandler.reset();
            handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
            final long msgSeqNum369 = fullRefreshMsg.getUInt32(369);
            handleIncrementalQueue(feedContext, msgSeqNum369);
        }
    }

    private boolean handleSecurityRefreshInQueue(final MdpPacket mdpPacket, final MdpGroup mdpGroupObj, final short matchEventIndicator) {
        final long rptSeqNum = mdpGroupObj.getUInt32(RPT_SEQ_NUM);
        final long expectedRptSeqNum = this.prcdRptSeqNum + 1;

        if (rptSeqNum == expectedRptSeqNum) {
            this.prcdRptSeqNum = rptSeqNum;
            channelContext.notifyIncrementalRefreshListeners(matchEventIndicator, this.securityId, this.secDesc, mdpPacket.getMsgSeqNum(), mdpGroupObj);
            mdHandler.handleIncrementalRefreshEntry(mdpGroupObj);
        } else if (rptSeqNum > (expectedRptSeqNum + gapThreshold)) {
            // next messages in queue are also with gap, so we have to resync again
            switchState(InstrumentState.SYNC, InstrumentState.OUTOFSYNC);
            return false;
        }
        return true;
    }

    private boolean handleMessageInQueue(final MdpPacket mdpPacket, final MdpGroup mdpGroupObj, final MdpMessage mdpMessage) {
        final MdpMessageType messageType = channelContext.getMdpMessageTypes().getMessageType(mdpMessage.getSchemaId());
        mdpMessage.setMessageType(messageType);
        final short matchEventIndicator = mdpMessage.getUInt8(SbeConstants.MATCHEVENTINDICATOR_TAG);

        if (messageType.getSemanticMsgType() == SemanticMsgType.MarketDataIncrementalRefresh) {
            mdpMessage.getGroup(MdConstants.INCR_RFRSH_GRP_TAG, mdpGroupObj);
            while (mdpGroupObj.hashNext()) {
                mdpGroupObj.next();
                final int secId = mdpGroupObj.getInt32(MdConstants.SECURITY_ID);

                if (secId == this.securityId && !handleSecurityRefreshInQueue(mdpPacket, mdpGroupObj, matchEventIndicator)) {
                    return false;
                }
            }
            if (channelContext.hasMdListeners() && MatchEventIndicator.hasEndOfEvent(matchEventIndicator)) {
                commitEvent();
            }
        }
        return true;
    }

    private void handleIncrementalQueue(final MdpFeedContext feedContext, final long prcdSeqNum) {
        logger.debug("Feed {}{} | handleIncrementalQueue: after {}",
                feedContext.getFeedType(), feedContext.getFeed(), prcdSeqNum);

        final MdpGroup mdpGroupObj = feedContext.getMdpGroupObj();
        final PacketQueue queue = this.channelContext.getIncrementQueue();
        final MdpPacket mdpPacket = feedContext.getMdpPacket();

        for (long i = prcdSeqNum + 1; i <= queue.getLastSeqNum(); i++) {
            if (queue.poll(i, mdpPacket) > 0) {
                logger.trace("Feed {}{} | Process packet #{} from queue", feedContext.getFeedType(), feedContext.getFeed(), i);

                final Iterator<MdpMessage> mdpMessageIterator = mdpPacket.iterator();
                while (mdpMessageIterator.hasNext()) {
                    final MdpMessage mdpMessage = mdpMessageIterator.next();
                    if (!handleMessageInQueue(mdpPacket, mdpGroupObj, mdpMessage)) {
                        return;
                    }
                }
            }
        }
    }

    private void switchState(final InstrumentState prevState, final InstrumentState newState) {
        this.state = newState;
        if (newState == InstrumentState.OUTOFSYNC) {
            this.channelContext.subscribeToSnapshotsForInstrument(securityId);
        } else if (newState == InstrumentState.SYNC || newState == InstrumentState.DISCONTINUED) {
            this.channelContext.unsubscribeToSnapshotsForInstrument(securityId);
        }
        notifyAboutChangedState(prevState, newState);
    }

    private void notifyAboutChangedState(final InstrumentState prevState, final InstrumentState newState) {
        this.channelContext.notifyInstrumentStateListeners(this.securityId, prevState, newState);
    }

    private void handleIncrementalRefreshEntry(final long msgSeqNum, final short matchEventIndicator, final FieldSet incrRefreshEntry) {
        channelContext.notifyIncrementalRefreshListeners(matchEventIndicator, this.securityId, this.secDesc, msgSeqNum, incrRefreshEntry);
        mdHandler.handleIncrementalRefreshEntry(incrRefreshEntry);
    }

    void onIncrementalRefresh(final MdpFeedContext feedContext, final long msgSeqNum, final short matchEventIndicator, final FieldSet incrRefreshEntry) {
        final InstrumentState currentState = this.state;
        if (incrRefreshEntry.hasField(RPT_SEQ_NUM)) {
            final long rptSeqNum = incrRefreshEntry.getUInt32(RPT_SEQ_NUM);
            logger.debug("Feed {}{} | #{} | RPT#{} | SecurityId={}, state={}, prcd={}",
                    feedContext.getFeedType(), feedContext.getFeed(), msgSeqNum, rptSeqNum, this.getSecurityId(), this.state, this.prcdRptSeqNum);
            final long expectedRptSeqNum = this.prcdRptSeqNum + 1;
            if (currentState == InstrumentState.SYNC) {
                if (rptSeqNum == expectedRptSeqNum) {
                    this.prcdRptSeqNum = rptSeqNum;
                    handleIncrementalRefreshEntry(msgSeqNum, matchEventIndicator, incrRefreshEntry);
                    handleIncrementalQueue(feedContext, msgSeqNum);
                } else if (rptSeqNum > (expectedRptSeqNum + gapThreshold)) {
                    logger.debug("Feed {}{} | PCKT#{} | SecurityId={}, GAP=[{},{}]",
                            feedContext.getFeedType(), feedContext.getFeed(), msgSeqNum, this.getSecurityId(), this.prcdRptSeqNum + 1, rptSeqNum - 1);
                    this.mdHandler.reset();
                    switchState(InstrumentState.SYNC, InstrumentState.OUTOFSYNC);
                }
            } else if (currentState == InstrumentState.OUTOFSYNC && rptSeqNum == expectedRptSeqNum) {
                logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got expected increment to restore: #{}",
                        feedContext.getFeedType(), feedContext.getFeed(), msgSeqNum, this.getSecurityId(), this.state, expectedRptSeqNum);
                this.prcdRptSeqNum = rptSeqNum;
                switchState(currentState, InstrumentState.SYNC);
                handleIncrementalRefreshEntry(msgSeqNum, matchEventIndicator, incrRefreshEntry);
                handleIncrementalQueue(feedContext, msgSeqNum);
            } else if (currentState == InstrumentState.INITIAL && prcdRptSeqNum == 0 && rptSeqNum == 1) {
                this.prcdRptSeqNum = rptSeqNum;
                switchState(currentState, InstrumentState.SYNC);
                handleIncrementalRefreshEntry(msgSeqNum, matchEventIndicator, incrRefreshEntry);
            }
        }
    }

    void commitEvent() {
        mdHandler.commitEvent();
    }

    public Integer getSecurityId() {
        return securityId;
    }
}
