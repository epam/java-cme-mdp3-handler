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
        ByteBuffer result = ByteBuffer.allocate(mdpHeader.length + fieldLengthSize + actualLength);
        result.put(mdpHeader);
        result.putShort(actualLength);
        result.put(encodedMessage, 0, actualLength);
        result.flip();
        return result;
    }


}
