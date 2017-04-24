package com.epam.cme.mdp3.test;


import com.epam.cme.mdp3.test.gen.*;
import net.openhft.chronicle.bytes.NativeBytesStore;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;

import java.nio.ByteBuffer;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.*;

public class ModelUtils {

    public static ByteBuffer getMBOSnapshotTestMessage(long sequence, int securityId){
        return getMBOSnapshotTestMessage(sequence, securityId, 100, 1, 1, 1);
    }

    public static ByteBuffer getMBOOnlyIncrementWith12TestMessages(long sequence){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        MDIncrementalRefreshOrderBook43Encoder incrementalRefreshOrderBook43Encoder = new MDIncrementalRefreshOrderBook43Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(incrementalRefreshOrderBook43Encoder.sbeBlockLength())
                .templateId(incrementalRefreshOrderBook43Encoder.sbeTemplateId())
                .schemaId(incrementalRefreshOrderBook43Encoder.sbeSchemaId())
                .version(incrementalRefreshOrderBook43Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();
        incrementalRefreshOrderBook43Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .transactTime(System.currentTimeMillis());
        MatchEventIndicatorEncoder matchEventIndicatorEncoder = incrementalRefreshOrderBook43Encoder.matchEventIndicator();
        matchEventIndicatorEncoder.lastTradeMsg(true);
        MDIncrementalRefreshOrderBook43Encoder.NoMDEntriesEncoder noMDEntriesEncoder = incrementalRefreshOrderBook43Encoder.noMDEntriesCount(12);
        noMDEntriesEncoder.next().orderID(9926951995L).mDOrderPriority(414).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(23).mDEntryPx().mantissa(986825);
        noMDEntriesEncoder.next().orderID(9926951993L).mDOrderPriority(412).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(59).mDEntryPx().mantissa(98685);
        noMDEntriesEncoder.next().orderID(9926951992L).mDOrderPriority(411).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(12).mDEntryPx().mantissa(986925);
        noMDEntriesEncoder.next().orderID(9926951997L).mDOrderPriority(416).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(49).mDEntryPx().mantissa(986775);
        noMDEntriesEncoder.next().orderID(9926951996L).mDOrderPriority(415).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(92).mDEntryPx().mantissa(986875);
        noMDEntriesEncoder.next().orderID(9926952003L).mDOrderPriority(422).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(88).mDEntryPx().mantissa(986725);
        noMDEntriesEncoder.next().orderID(9926952002L).mDOrderPriority(421).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(32).mDEntryPx().mantissa(986775);
        noMDEntriesEncoder.next().orderID(9926952001L).mDOrderPriority(420).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(99).mDEntryPx().mantissa(987025);
        noMDEntriesEncoder.next().orderID(9926952000L).mDOrderPriority(419).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(94).mDEntryPx().mantissa(98680);
        noMDEntriesEncoder.next().orderID(9926952005L).mDOrderPriority(424).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(49).mDEntryPx().mantissa(98675);
        noMDEntriesEncoder.next().orderID(9926952004L).mDOrderPriority(423).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(54).mDEntryPx().mantissa(987025);
        noMDEntriesEncoder.next().orderID(9926951983L).mDOrderPriority(402).securityID(998350).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(16).mDEntryPx().mantissa(98670);
        bufferOffset += incrementalRefreshOrderBook43Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMBPWithMBOIncrementTestMessage(long sequence, int[] securityIds, short[] referenceIDs){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        MDIncrementalRefreshBook32Encoder mdIncrementalRefreshBook32Encoder = new MDIncrementalRefreshBook32Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(mdIncrementalRefreshBook32Encoder.sbeBlockLength())
                .templateId(mdIncrementalRefreshBook32Encoder.sbeTemplateId())
                .schemaId(mdIncrementalRefreshBook32Encoder.sbeSchemaId())
                .version(mdIncrementalRefreshBook32Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();
        MDIncrementalRefreshBook32Encoder.NoMDEntriesEncoder noMDEntriesEncoder = mdIncrementalRefreshBook32Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .transactTime(System.currentTimeMillis())
                .noMDEntriesCount(securityIds.length);
        MatchEventIndicatorEncoder matchEventIndicatorEncoder = mdIncrementalRefreshBook32Encoder.matchEventIndicator();
        matchEventIndicatorEncoder.lastTradeMsg(true);
        for (int i = 0; i < securityIds.length; i++) {
            noMDEntriesEncoder.next()
                    .mDEntrySize(2)
                    .mDEntryType(MDEntryTypeBook.Bid)
                    .mDPriceLevel((short) 3)
                    .rptSeq(4)
                    .securityID(securityIds[i]);
            PRICENULLEncoder pricenullEncoder = noMDEntriesEncoder.mDEntryPx();
            pricenullEncoder.mantissa(5);
        }
        if(referenceIDs != null){
            MDIncrementalRefreshBook32Encoder.NoOrderIDEntriesEncoder noOrderIDEntriesEncoder = mdIncrementalRefreshBook32Encoder.noOrderIDEntriesCount(referenceIDs.length);
            for (int i = 0; i < referenceIDs.length; i++) {
                noOrderIDEntriesEncoder.next()
                        .orderUpdateAction(OrderUpdateAction.New)
                        .mDDisplayQty(213)
                        .mDOrderPriority(324324)
                        .orderID(3243324)
                        .referenceID(referenceIDs[i]);
            }
        }
        bufferOffset += mdIncrementalRefreshBook32Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMBOSnapshotTestMessage(long sequence, int securityId, long lastMsgSeqNumProcessed, long noChunks, long currentChunk, long totNumReports){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        SnapshotFullRefreshOrderBook44Encoder snapshotFullRefreshOrderBook44Encoder = new SnapshotFullRefreshOrderBook44Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(snapshotFullRefreshOrderBook44Encoder.sbeBlockLength())
                .templateId(snapshotFullRefreshOrderBook44Encoder.sbeTemplateId())
                .schemaId(snapshotFullRefreshOrderBook44Encoder.sbeSchemaId())
                .version(snapshotFullRefreshOrderBook44Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();

        snapshotFullRefreshOrderBook44Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .totNumReports(totNumReports)
                .lastMsgSeqNumProcessed(lastMsgSeqNumProcessed)
                .noChunks(noChunks)
                .currentChunk(currentChunk)
                .securityID(securityId);
        SnapshotFullRefreshOrderBook44Encoder.NoMDEntriesEncoder noMDEntriesEncoder = snapshotFullRefreshOrderBook44Encoder.noMDEntriesCount(1)
                .next()
                .mDDisplayQty(1)
                .mDOrderPriority(10)
                .orderID(123);
        PRICEEncoder priceEncoder = noMDEntriesEncoder.mDEntryPx();
        priceEncoder.mantissa(5);
        bufferOffset += snapshotFullRefreshOrderBook44Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMBOIncrementTestMessage(long sequence){
        return getMBOIncrementTestMessage(sequence, 1, 1, 1, (short)1, (byte)48, 1 ,1 );
    }

    public static ByteBuffer getMBOIncrementTestMessage(long sequence, int securityId, long orderID, long mDOrderPriority,
                                                        short mDUpdateAction, byte mDEntryType, int mDDisplayQty, int mDEntryPx){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        MDIncrementalRefreshOrderBook43Encoder incrementalRefreshOrderBook43Encoder = new MDIncrementalRefreshOrderBook43Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(incrementalRefreshOrderBook43Encoder.sbeBlockLength())
                .templateId(incrementalRefreshOrderBook43Encoder.sbeTemplateId())
                .schemaId(incrementalRefreshOrderBook43Encoder.sbeSchemaId())
                .version(incrementalRefreshOrderBook43Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();
        incrementalRefreshOrderBook43Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .transactTime(System.currentTimeMillis());
        MatchEventIndicatorEncoder matchEventIndicatorEncoder = incrementalRefreshOrderBook43Encoder.matchEventIndicator();
        matchEventIndicatorEncoder.lastTradeMsg(true);
        incrementalRefreshOrderBook43Encoder.noMDEntriesCount(1)
                .next()
                .orderID(orderID)
                .mDOrderPriority(mDOrderPriority)
                .securityID(securityId)
                .mDUpdateAction(MDUpdateAction.get(mDUpdateAction))
                .mDEntryType(MDEntryTypeBook.get(mDEntryType))
                .mDDisplayQty(mDDisplayQty).mDEntryPx().mantissa(mDEntryPx);
        bufferOffset += incrementalRefreshOrderBook43Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMDInstrumentDefinitionFuture27(long sequence, int securityId){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        MDInstrumentDefinitionFuture27Encoder definitionFuture27Encoder = new MDInstrumentDefinitionFuture27Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(definitionFuture27Encoder.sbeBlockLength())
                .templateId(definitionFuture27Encoder.sbeTemplateId())
                .schemaId(definitionFuture27Encoder.sbeSchemaId())
                .version(definitionFuture27Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();
        definitionFuture27Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .securityID(securityId)
                .symbol("testSymbol");
        definitionFuture27Encoder.noEventsCount(1)
                .next()
                .eventTime(System.currentTimeMillis())
                .eventType(EventType.Activation);
        definitionFuture27Encoder.noMDFeedTypesCount(1)
                .next()
                .marketDepth((byte)10)
                .mDFeedType("GBX");
        bufferOffset += definitionFuture27Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    private static byte[] getMDPHeader(long sequence, long sendingTime){
        final NativeBytesStore mdpHeader = NativeBytesStore.nativeStoreWithFixedCapacity(MDP_HEADER_SIZE);
        mdpHeader.writeUnsignedInt(MESSAGE_SEQ_NUM_OFFSET, sequence);
        mdpHeader.writeLong(MESSAGE_SENDING_TIME_OFFSET, sendingTime);
        return mdpHeader.toByteArray();
    }

    private static ByteBuffer packMessage(long sequence, byte[] encodedMessage, short actualLength){
        final int fieldLengthSize = 2;
        byte[] mdpHeader = getMDPHeader(sequence, System.currentTimeMillis());
        ByteBuffer result = ByteBuffer.allocateDirect(mdpHeader.length + fieldLengthSize + actualLength);
        result.put(mdpHeader);
        result.putShort(actualLength);
        result.put(encodedMessage, 0, actualLength);
        result.flip();
        return result;
    }


}
