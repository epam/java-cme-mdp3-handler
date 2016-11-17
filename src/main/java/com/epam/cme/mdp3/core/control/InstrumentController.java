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
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.epam.cme.mdp3.core.control.IncrementalRefreshQueue.IncrementalRefreshQueueEntry;
import static com.epam.cme.mdp3.mktdata.MdConstants.RPT_SEQ_NUM;

public class InstrumentController {
    public static final int DEF_QUEUE_SLOT_INIT_BUFFER_SIZE = 50;
    public static final int DEF_INCR_QUEUE_SIZE = 10000;
    public static final int DEF_GAP_THRESHOLD = 5;

    private static final Logger logger = LoggerFactory.getLogger(InstrumentController.class);

    private final Integer securityId;
    private String secDesc;
    private final ChannelContext channelContext;
    private InstrumentState state = InstrumentState.INITIAL;
    private long prcdRptSeqNum = 0;
    private final InstrumentMdHandler mdHandler;
    private int gapThreshold = DEF_GAP_THRESHOLD;
    private final IncrementalRefreshQueue incrRefreshQueue;
    private final MdpGroupEntry incrGroupEntry = SbeGroupEntry.instance();
    private final IncrementalRefreshQueueEntry incrQueueEntry = new IncrementalRefreshQueueEntry(incrGroupEntry);

    public InstrumentController(final ChannelContext channelContext, final int securityId, final String secDesc, final int subscriptionFlags, final byte maxDepth, final int gapThreshold) {
        this.channelContext = channelContext;
        this.securityId = securityId;
        this.secDesc = secDesc;
        this.gapThreshold = gapThreshold;
        mdHandler = new InstrumentMdHandler(channelContext, securityId, subscriptionFlags, maxDepth);
        incrRefreshQueue = new IncrementalRefreshQueue(channelContext.getIncrQueueSize(), channelContext.getQueueSlotInitBufferSize());
        init(subscriptionFlags);
    }

    private void init(final int subscriptionFlags) {
        mdHandler.setSubscriptionFlags(subscriptionFlags);
        this.channelContext.subscribeToSnapshotsForInstrument(securityId);
        this.channelContext.notifyInstrumentStateListeners(this.securityId, this.secDesc, InstrumentState.NEW, InstrumentState.INITIAL);
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
        this.incrRefreshQueue.clear();
        if (this.state != InstrumentState.DISCONTINUED) {
            switchState(state, InstrumentState.SYNC);
        }
        mdHandler.reset();
    }

    private void handleSnapshotFullRefreshEntries(final MdpFeedContext feedContext, final MdpMessage fullRefreshMsg) {
        channelContext.notifySnapshotFullRefreshListeners(this.secDesc, fullRefreshMsg);
        mdHandler.handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
    }

    private boolean handleSecurityRefreshInQueue(final IncrementalRefreshQueueEntry incrQueueEntry) {
        final long rptSeqNum = incrQueueEntry.groupEntry.getUInt32(RPT_SEQ_NUM);
        final long expectedRptSeqNum = this.prcdRptSeqNum + 1;
        final short matchEventIndicator = incrQueueEntry.matchEventIndicator;
        final MdpGroupEntry incrGroupEntry = incrQueueEntry.groupEntry;

        if (rptSeqNum == expectedRptSeqNum) {
            this.prcdRptSeqNum = rptSeqNum;
            channelContext.notifyIncrementalRefreshListeners(
                    matchEventIndicator, this.securityId, this.secDesc, incrQueueEntry.incrPcktSeqNum, incrGroupEntry);
            mdHandler.handleIncrementalRefreshEntry(incrQueueEntry.groupEntry);
        } else if (rptSeqNum > (expectedRptSeqNum + gapThreshold)) {
            // next messages in queue are also with gap, so we have to resync again
            switchState(InstrumentState.SYNC, InstrumentState.OUTOFSYNC);
            return false;
        }
        return true;
    }

    private void handleIncrementalQueue(final MdpFeedContext feedContext, final long prcdSeqNum) {
        logger.debug("Feed {}{} | handleIncrementalQueue: after {}",
                feedContext.getFeedType(), feedContext.getFeed(), prcdSeqNum);

        final IncrementalRefreshQueue queue = this.incrRefreshQueue;

        for (long i = prcdSeqNum + 1; i <= queue.getLastRptSeqNum(); i++) {
            if (queue.poll(i, this.incrQueueEntry) > 0) {
                logger.trace("Feed {}{} | Process incremental entry #{} from queue",
                        feedContext.getFeedType(), feedContext.getFeed(), i);

                if (!handleSecurityRefreshInQueue(this.incrQueueEntry)) {
                    return;
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
        this.channelContext.notifyInstrumentStateListeners(this.securityId, this.secDesc, prevState, newState);
    }

    private void handleIncrementalRefreshEntry(final long msgSeqNum, final short matchEventIndicator, final FieldSet incrRefreshEntry) {
        channelContext.notifyIncrementalRefreshListeners(matchEventIndicator, this.securityId, this.secDesc, msgSeqNum, incrRefreshEntry);
        mdHandler.handleIncrementalRefreshEntry(incrRefreshEntry);
    }

    private void pushIncrementalRefreshEntryInQueue(final long msgSeqNum, final short matchEventIndicator,
                                            final long rptSeqNum, final MdpGroupEntry incrRefreshEntry) {
        this.incrQueueEntry.incrPcktSeqNum = msgSeqNum;
        this.incrQueueEntry.matchEventIndicator = matchEventIndicator;
        this.incrQueueEntry.groupEntry = incrRefreshEntry;

        this.incrRefreshQueue.push(rptSeqNum, this.incrQueueEntry);
    }

    void commitEvent() {
        mdHandler.commitEvent();
    }

    public Integer getSecurityId() {
        return securityId;
    }

    void onSnapshotFullRefresh(final MdpFeedContext feedContext, final MdpMessage fullRefreshMsg) {
        final InstrumentState currentState = this.state;
        final long snptSeqNum = fullRefreshMsg.getUInt32(RPT_SEQ_NUM);
        if (currentState == InstrumentState.INITIAL) {
            /*logger.debug("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got initial Snapshot. Initial prcdSeqNum is {}",
                    feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, snptSeqNum + 1);*/
            this.prcdRptSeqNum = snptSeqNum;
            switchState(currentState, InstrumentState.SYNC);
            handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
            handleIncrementalQueue(feedContext, snptSeqNum);
            /*logger.info("Feed {}{} | Instrument: '{}-({})'. Got initial Snapshot. Initial prcdSeqNum: {}. Last rptSeqNum in queue: {}",
                    feedContext.getFeedType(), feedContext.getFeed(), this.getSecurityId(), this.secDesc,
                    snptSeqNum, this.incrRefreshQueue.getLastRptSeqNum());*/
        } else if (currentState == InstrumentState.OUTOFSYNC) {
            if (snptSeqNum > this.prcdRptSeqNum) {
                /*logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got Snapshot to restore. Fast forward from {} to {}",
                        feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, this.prcdRptSeqNum, snptSeqNum + 1);*/
                this.prcdRptSeqNum = snptSeqNum;
                this.mdHandler.reset();
                switchState(currentState, InstrumentState.SYNC);
                handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
                handleIncrementalQueue(feedContext, snptSeqNum);
                /*logger.info("Feed {}{} | Instrument: '{}-({})'. Recovered from Snapshot. Snapshot prcdSeqNum: {}. Last rptSeqNum in queue: {}",
                        feedContext.getFeedType(), feedContext.getFeed(), this.getSecurityId(), this.secDesc,
                        snptSeqNum, this.incrRefreshQueue.getLastRptSeqNum());*/
            }
        } else if (currentState == InstrumentState.SYNC && snptSeqNum > this.prcdRptSeqNum) {
            logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Snapshot with high sequence comes faster then Increments. Fast forward from {} to {}",
                    feedContext.getFeedType(), feedContext.getFeed(), snptSeqNum, this.getSecurityId(), this.state, prcdRptSeqNum, snptSeqNum + 1);
            this.prcdRptSeqNum = snptSeqNum;
            this.mdHandler.reset();
            handleSnapshotFullRefreshEntries(feedContext, fullRefreshMsg);
            handleIncrementalQueue(feedContext, snptSeqNum);
        }
    }

    void onIncrementalRefresh(final MdpFeedContext feedContext, final long msgSeqNum, final short matchEventIndicator, final MdpGroupEntry incrRefreshEntry) {
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
                    handleIncrementalQueue(feedContext, rptSeqNum);
                } else if (rptSeqNum > expectedRptSeqNum) {
                    pushIncrementalRefreshEntryInQueue(msgSeqNum, matchEventIndicator, rptSeqNum, incrRefreshEntry);
                    if (rptSeqNum > (expectedRptSeqNum + gapThreshold)) {
                        /*logger.info("Feed {}{} | SecurityId={}-({}), Processed rptSeqNum:{}. Received:{}",
                                feedContext.getFeedType(), feedContext.getFeed(), this.getSecurityId(), this.secDesc,
                                this.prcdRptSeqNum, rptSeqNum);*/
                        this.mdHandler.reset();
                        switchState(InstrumentState.SYNC, InstrumentState.OUTOFSYNC);
                    }
                }
            } else if (currentState == InstrumentState.OUTOFSYNC) {
                if (rptSeqNum == expectedRptSeqNum) {
                    logger.trace("Feed {}{} | #{} | Instrument: '{}'. State: {}. Got expected increment to restore: #{}",
                            feedContext.getFeedType(), feedContext.getFeed(), msgSeqNum, this.getSecurityId(), this.state, expectedRptSeqNum);
                    this.prcdRptSeqNum = rptSeqNum;
                    switchState(currentState, InstrumentState.SYNC);
                    handleIncrementalRefreshEntry(msgSeqNum, matchEventIndicator, incrRefreshEntry);
                    handleIncrementalQueue(feedContext, rptSeqNum);
                } else if (rptSeqNum > expectedRptSeqNum) {
                    pushIncrementalRefreshEntryInQueue(msgSeqNum, matchEventIndicator, rptSeqNum, incrRefreshEntry);
                }
            } else if (currentState == InstrumentState.INITIAL) {
                if (prcdRptSeqNum == 0 && rptSeqNum == 1) {
                    this.prcdRptSeqNum = rptSeqNum;
                    switchState(currentState, InstrumentState.SYNC);
                    handleIncrementalRefreshEntry(msgSeqNum, matchEventIndicator, incrRefreshEntry);
                } else {
                    pushIncrementalRefreshEntryInQueue(msgSeqNum, matchEventIndicator, rptSeqNum, incrRefreshEntry);
                }
            }
        }
    }
}
