package com.epam.cme.mdp3.test;


import com.epam.cme.mdp3.test.gen.*;
import net.openhft.chronicle.bytes.internal.NativeBytesStore;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;

import java.nio.ByteBuffer;

import static com.epam.cme.mdp3.sbe.message.SbeConstants.*;

public class ModelUtils {

    public static ByteBuffer getMBOSnapshotTestMessage(long sequence, int securityId){
        return getMBOSnapshotTestMessage(sequence, securityId, 100, 1, 1, 1);
    }

    public static ByteBuffer getMBPSnapshotTestMessage(long sequence, int securityId) {
        return getMBPSnapshotTestMessage(sequence, securityId, 100, 1);
    }

    public static ByteBuffer getMBOOnlyIncrementWith12TestEntries(long sequence, int securityId){
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
        matchEventIndicatorEncoder.lastQuoteMsg(true);
        MDIncrementalRefreshOrderBook43Encoder.NoMDEntriesEncoder noMDEntriesEncoder = incrementalRefreshOrderBook43Encoder.noMDEntriesCount(12);
        noMDEntriesEncoder.next().orderID(9926951995L).mDOrderPriority(414).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(23).mDEntryPx().mantissa(98682500000L);
        noMDEntriesEncoder.next().orderID(9926951993L).mDOrderPriority(412).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(59).mDEntryPx().mantissa(98685000000L);
        noMDEntriesEncoder.next().orderID(9926951992L).mDOrderPriority(411).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(12).mDEntryPx().mantissa(98692500000L);
        noMDEntriesEncoder.next().orderID(9926951997L).mDOrderPriority(416).securityID(securityId).mDUpdateAction(MDUpdateAction.Change).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(49).mDEntryPx().mantissa(98677500000L);
        noMDEntriesEncoder.next().orderID(9926951996L).mDOrderPriority(415).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(92).mDEntryPx().mantissa(98687500000L);
        noMDEntriesEncoder.next().orderID(9926952003L).mDOrderPriority(422).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(88).mDEntryPx().mantissa(98672500000L);
        noMDEntriesEncoder.next().orderID(9926952002L).mDOrderPriority(421).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(32).mDEntryPx().mantissa(98677500000L);
        noMDEntriesEncoder.next().orderID(9926952001L).mDOrderPriority(420).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(99).mDEntryPx().mantissa(98702500000L);
        noMDEntriesEncoder.next().orderID(9926952000L).mDOrderPriority(419).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(94).mDEntryPx().mantissa(98680000000L);
        noMDEntriesEncoder.next().orderID(9926952005L).mDOrderPriority(424).securityID(securityId).mDUpdateAction(MDUpdateAction.Change).mDEntryType(MDEntryTypeBook.Bid).mDDisplayQty(49).mDEntryPx().mantissa(98675000000L);
        noMDEntriesEncoder.next().orderID(9926952004L).mDOrderPriority(423).securityID(securityId).mDUpdateAction(MDUpdateAction.Change).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(54).mDEntryPx().mantissa(98702500000L);
        noMDEntriesEncoder.next().orderID(9926951983L).mDOrderPriority(402).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryType(MDEntryTypeBook.Offer).mDDisplayQty(16).mDEntryPx().mantissa(98670000000L);
        bufferOffset += incrementalRefreshOrderBook43Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMBOWithMBPIncrementWith12TestEntries(int sequence, int securityId) {
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
                .transactTime(System.currentTimeMillis()).noMDEntriesCount(12);
        MatchEventIndicatorEncoder matchEventIndicatorEncoder = mdIncrementalRefreshBook32Encoder.matchEventIndicator();
        matchEventIndicatorEncoder.lastTradeMsg(true);
        noMDEntriesEncoder.next().mDEntrySize(4 ).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 1).rptSeq(1254).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98745000000L);
        noMDEntriesEncoder.next().mDEntrySize(1 ).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 1).rptSeq(1255).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(987075000000L);
        noMDEntriesEncoder.next().mDEntrySize(45).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 2).rptSeq(1256).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(987125000000L);
        noMDEntriesEncoder.next().mDEntrySize(22).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 2).rptSeq(1257).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98745000000L);
        noMDEntriesEncoder.next().mDEntrySize(98).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 3).rptSeq(1258).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98720000000L);
        noMDEntriesEncoder.next().mDEntrySize(43).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 3).rptSeq(1259).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98725000000L);
        noMDEntriesEncoder.next().mDEntrySize(12).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 4).rptSeq(1260).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98715000000L);
        noMDEntriesEncoder.next().mDEntrySize(83).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 4).rptSeq(1261).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98670000000L);
        noMDEntriesEncoder.next().mDEntrySize(38).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 5).rptSeq(1262).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98695000000L);
        noMDEntriesEncoder.next().mDEntrySize(99).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 5).rptSeq(1263).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98690000000L);
        noMDEntriesEncoder.next().mDEntrySize(1 ).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 3).rptSeq(1264).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryPx().mantissa(987025000000L);
        noMDEntriesEncoder.next().mDEntrySize(99).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 3).rptSeq(1265).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryPx().mantissa(98677500000L);

        MDIncrementalRefreshBook32Encoder.NoOrderIDEntriesEncoder noOrderIDEntriesEncoder = mdIncrementalRefreshBook32Encoder.noOrderIDEntriesCount(12);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(61).mDOrderPriority(5394).orderID(9927057956L).referenceID((short)1);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(3 ).mDOrderPriority(5395).orderID(9927057957L).referenceID((short)2);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(7 ).mDOrderPriority(5396).orderID(9927057958L).referenceID((short)3);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(44).mDOrderPriority(5397).orderID(9927057959L).referenceID((short)4);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(55).mDOrderPriority(5398).orderID(9927057960L).referenceID((short)5);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(68).mDOrderPriority(5399).orderID(9927057961L).referenceID((short)6);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(70).mDOrderPriority(5400).orderID(9927057962L).referenceID((short)7);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(56).mDOrderPriority(5401).orderID(9927057963L).referenceID((short)8);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(6 ).mDOrderPriority(5402).orderID(9927057964L).referenceID((short)9);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.New   ).mDDisplayQty(20).mDOrderPriority(5403).orderID(9927057965L).referenceID((short)10);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.Delete).mDDisplayQty(9 ).mDOrderPriority(5404).orderID(9927057966L).referenceID((short)11);
        noOrderIDEntriesEncoder.next().orderUpdateAction(OrderUpdateAction.Delete).mDDisplayQty(29).mDOrderPriority(5405).orderID(9927057967L).referenceID((short)12);
        bufferOffset += mdIncrementalRefreshBook32Encoder.encodedLength();
        return packMessage(sequence, mutableDirectBuffer.byteArray(), bufferOffset);
    }

    public static ByteBuffer getMBPOnlyIncrementWith12TestEntries(int sequence, int securityId) {
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
                .transactTime(System.currentTimeMillis()).noMDEntriesCount(12);
        MatchEventIndicatorEncoder matchEventIndicatorEncoder = mdIncrementalRefreshBook32Encoder.matchEventIndicator();
        matchEventIndicatorEncoder.lastTradeMsg(true);
        noMDEntriesEncoder.next().mDEntrySize(4 ).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 1).rptSeq(1254).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98745000000L);
        noMDEntriesEncoder.next().mDEntrySize(1 ).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 1).rptSeq(1255).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(987075000000L);
        noMDEntriesEncoder.next().mDEntrySize(45).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 2).rptSeq(1256).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(987125000000L);
        noMDEntriesEncoder.next().mDEntrySize(22).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 2).rptSeq(1257).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98745000000L);
        noMDEntriesEncoder.next().mDEntrySize(98).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 3).rptSeq(1258).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98720000000L);
        noMDEntriesEncoder.next().mDEntrySize(43).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 3).rptSeq(1259).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98725000000L);
        noMDEntriesEncoder.next().mDEntrySize(12).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 4).rptSeq(1260).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98715000000L);
        noMDEntriesEncoder.next().mDEntrySize(83).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 4).rptSeq(1261).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98670000000L);
        noMDEntriesEncoder.next().mDEntrySize(38).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 5).rptSeq(1262).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98695000000L);
        noMDEntriesEncoder.next().mDEntrySize(99).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 5).rptSeq(1263).securityID(securityId).mDUpdateAction(MDUpdateAction.New).mDEntryPx().mantissa(98690000000L);
        noMDEntriesEncoder.next().mDEntrySize(1 ).mDEntryType(MDEntryTypeBook.Bid  ).mDPriceLevel((short) 3).rptSeq(1264).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryPx().mantissa(987025000000L);
        noMDEntriesEncoder.next().mDEntrySize(99).mDEntryType(MDEntryTypeBook.Offer).mDPriceLevel((short) 3).rptSeq(1265).securityID(securityId).mDUpdateAction(MDUpdateAction.Delete).mDEntryPx().mantissa(98677500000L);
        bufferOffset += mdIncrementalRefreshBook32Encoder.encodedLength();
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

    public static ByteBuffer getMBPSnapshotTestMessage(long sequence, int securityId, long lastMsgSeqNumProcessed, long totNumReports){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        SnapshotFullRefresh38Encoder snapshotFullRefresh38Encoder = new SnapshotFullRefresh38Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(snapshotFullRefresh38Encoder.sbeBlockLength())
                .templateId(snapshotFullRefresh38Encoder.sbeTemplateId())
                .schemaId(snapshotFullRefresh38Encoder.sbeSchemaId())
                .version(snapshotFullRefresh38Encoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();

        snapshotFullRefresh38Encoder.wrap(mutableDirectBuffer, bufferOffset)
                .totNumReports(totNumReports)
                .lastMsgSeqNumProcessed(lastMsgSeqNumProcessed)
                .securityID(securityId);
        SnapshotFullRefresh38Encoder.NoMDEntriesEncoder noMDEntriesEncoder = snapshotFullRefresh38Encoder.noMDEntriesCount(1)
                .next()
                .mDEntrySize(10)
                .mDPriceLevel((byte)1);
        PRICENULLEncoder pricenullEncoder = noMDEntriesEncoder.mDEntryPx();
        pricenullEncoder.mantissa(5);
        bufferOffset += snapshotFullRefresh38Encoder.encodedLength();
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

    public static ByteBuffer getLogin(long sequence){
        short bufferOffset = 0;
        final MutableDirectBuffer mutableDirectBuffer = new ExpandableArrayBuffer();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        AdminLogin15Encoder loginEncoder = new AdminLogin15Encoder();
        messageHeaderEncoder.wrap(mutableDirectBuffer, bufferOffset)
                .blockLength(loginEncoder.sbeBlockLength())
                .templateId(loginEncoder.sbeTemplateId())
                .schemaId(loginEncoder.sbeSchemaId())
                .version(loginEncoder.sbeSchemaVersion());
        bufferOffset += messageHeaderEncoder.encodedLength();
        loginEncoder.wrap(mutableDirectBuffer, bufferOffset);
        loginEncoder.heartBtInt((byte)2);
        bufferOffset += loginEncoder.encodedLength();
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
