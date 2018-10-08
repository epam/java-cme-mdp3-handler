package com.epam.cme.mdp3.test;

import com.epam.cme.mdp3.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.epam.cme.mdp3.sbe.message.SbeConstants;

public class TestChannelListener implements ChannelListener {
    private ChannelState prevSate;
    private ChannelState currentSate;
    private BlockingQueue<IncrementalRefreshEntity> incrementMBOQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<IncrementalRefreshEntity> incrementMBPQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Pair<String,MdpMessage>> mboSnapshotQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Pair<String,MdpMessage>> mbpSnapshotQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Pair<String,MdpMessage>> securitiesQueue = new LinkedBlockingQueue<>();

    @Override
    public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {

    }

    @Override
    public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {

    }

    @Override
    public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {

    }

    @Override
    public void onBeforeChannelReset(String channelId, MdpMessage resetMessage) {

    }

    @Override
    public void onFinishedChannelReset(String channelId, MdpMessage resetMessage) {

    }

    @Override
    public void onChannelStateChanged(String channelId, ChannelState prevState, ChannelState newState) {
        this.prevSate = prevState;
        this.currentSate = newState;
    }

    @Override
    public int onSecurityDefinition(String channelId, MdpMessage secDefMessage) {
        securitiesQueue.add(new ImmutablePair<>(channelId, secDefMessage));
        return 0;
    }

    @Override
    public void onIncrementalMBORefresh(final MdpMessage mdpMessage, final String channelId, final int securityId, final String secDesc, final long msgSeqNum, final FieldSet orderIDEntry, final FieldSet mdEntry){        
        incrementMBOQueue.add(new IncrementalRefreshEntity(mdpMessage, channelId, securityId, secDesc, msgSeqNum, orderIDEntry != null ? orderIDEntry.copy() : null, mdEntry != null ? mdEntry.copy() : null));
    }

    @Override
    public void onIncrementalMBPRefresh(final MdpMessage mdpMessage, final String channelId, final int securityId, final String secDesc, final long msgSeqNum, final FieldSet mdEntry){
        incrementMBPQueue.add(new IncrementalRefreshEntity(mdpMessage, channelId, securityId, secDesc, msgSeqNum, (mdEntry != null) ? mdEntry.copy() : null));
    }
    
    @Override
    public void onIncrementalComplete(final MdpMessage mdpMessage, final String channelId, final int securityId, final String secDesc, final long msgSeqNum){        
    }

    @Override
    public void onSnapshotMBOFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage){
        mboSnapshotQueue.add(new ImmutablePair<>(channelId, snptMessage.copy()));
    }

    @Override
    public void onSnapshotMBPFullRefresh(String channelId, String secDesc, MdpMessage snptMessage) {
        mbpSnapshotQueue.add(new ImmutablePair<>(channelId, snptMessage.copy()));
    }

    @Override
    public void onRequestForQuote(String channelId, MdpMessage rfqMessage) {

    }

    @Override
    public void onSecurityStatus(String channelId, int securityId, MdpMessage secStatusMessage) {

    }

    public Pair<String,MdpMessage> nextMBOSnapshotMessage() throws InterruptedException {
        return mboSnapshotQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public Pair<String,MdpMessage> nextMBPSnapshotMessage() throws InterruptedException {
        return mbpSnapshotQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public Pair<String,MdpMessage> nextSecurityMessage() throws InterruptedException {
        return securitiesQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public IncrementalRefreshEntity nextMBOIncrementMessage() throws InterruptedException {
        return incrementMBOQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public IncrementalRefreshEntity nextMBPIncrementMessage() throws InterruptedException {
        return incrementMBPQueue.poll(Constants.WAITING_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public ChannelState getCurrentSate() {
        return currentSate;
    }

    public ChannelState getPrevSate() {
        return prevSate;
    }

    public class IncrementalRefreshEntity {
        private MdpMessage mdpMessage;
        private String channelId;        
        private int securityId;
        private String secDesc;
        private long msgSeqNum;
        private FieldSet orderIDEntry;
        private FieldSet mdEntry;

        public IncrementalRefreshEntity(MdpMessage mdpMessage, String channelId, int securityId, String secDesc, long msgSeqNum, FieldSet mdEntry) {
            this(mdpMessage, channelId, securityId, secDesc, msgSeqNum, null, mdEntry);
        }

        public IncrementalRefreshEntity(MdpMessage mdpMessage, String channelId, int securityId, String secDesc, long msgSeqNum, FieldSet orderIDEntry, FieldSet mdEntry) {
            this.mdpMessage = mdpMessage;
            this.channelId = channelId;
            this.securityId = securityId;
            this.secDesc = secDesc;
            this.msgSeqNum = msgSeqNum;
            this.orderIDEntry = orderIDEntry;
            this.mdEntry = mdEntry;
        }

        public MdpMessage getMDPMEssage() {
            return mdpMessage;
        }
        
        public String getChannelId() {
            return channelId;
        }

        public int getSecurityId() {
            return securityId;
        }

        public String getSecDesc() {
            return secDesc;
        }

        public long getMsgSeqNum() {
            return msgSeqNum;
        }
        
        public FieldSet getOrderIDEntry() {
            return orderIDEntry;
        }

        public FieldSet getMdEntry() {
            return mdEntry;
        }
    }

}
