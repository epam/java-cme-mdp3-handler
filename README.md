## Synopsis

Java Market Data Handler for CME Market Data (MDP 3.0) was designed to take advantage of the new low-latency data feed.
It fully supports features of the CME Globex MDP3.0 market data platform(https://www.cmegroup.com/confluence/display/EPICSANDBOX/CME+MDP+3.0+Market+Data),
helps feeding CME market data directly into the client application. The handler delivers market data updates from socket to your application in a few microseconds.

The Market Data Handler has two modules mbp-only and mbp-with-mbo. Mbp-only module provides high level book API for Market By Price functionality.
Mbp-with-mbo module provides low level API for both Market by Order and Market By Price functionality.

## Motivation

With the third generation of its market data platform CME made several key improvements in the
application protocol and message encoding to allow more efficient market data processing on the
client side.

Matching engine event boundaries are now clearly indicated with the MatchEventIndicator (5799) tag to
allow a client application apply market data updates transactionally. The client application has explicit
knowledge of the moment when market data is consistent for analysis in the case of complex order
book updates or multiple instruments affected by a matching event. Matching events are now
independent of the UDP packet boundaries and a matching event may spread over several sequential UDP
packets and a UDP packet may contain several matching events. Market data updates of a matching
event are segregated by update type to allow the client application skip handling of messages not
relevant to specific algorithm to reduce processing time. New trade summary messages aggregate
trades by price level resulting in fewer trade updates disseminated in the case of large fill events.
(http://www.cmegroup.com/confluence/display/EPICSANDBOX/MDP+3.0+Packet+Structure+Examples)

The new Simple Binary Encoding (SBE) format which replaced the FAST encoding allows more efficient and
faster message decoding on the client side thanks to the fixed-length fields, native little-endian byte
order and proper field alignment. The client application can efficiently access message fields directly
in the buffer filled with data received from the network socket. The client application does not have to
perform complex sequential state-based decoding process to access required fields. Overall the
increased message encoding and decoding speed overcomes slight increase of disseminated data size
and bandwidth requirements compared to the FAST compression.

With the introduction of CME MDP 3.0 platform B2BITS EPAM Systems designed new open source market data handler
to take advantage of the new low-latency data feed and to provide developers with the required functionality.
The new handler is optimized for both single- and multi-channel usage scenarios,
the new SBE parser provides fast access to messages and message fields by tag number.
With this project developers have flexibility to include this library into own applications (with/without
required changes) instead of own implementation of all aspects of CME connectivity and market data handling.

## Features
* Connects and maintains connections to the CME Globex MDP 3.0 Platform;
* Provides client applications with listed Security Definitions;
* Provides client applications with all types of CME Market Data;
* Intraday Instrument subscription and synchronization;
* Packet gap detection logic based on packet sequence number to detect gaps as soon as possible;
* Per-instrument recovery logic to keep feeding Instruments not affected by a packet gap;
* Automatically arbitrates between feeds A and B to reduce probability of packet loss;
* Channel API to receive Instrument market data events; sequenced, recovered and synchronized by the handler;
* Raw Data API to receive Channel MDP Packets as they arrive from the socket layer;
* Fast SBE message decoder to access fields by tag id;
* Simple options to specify Market Data of application interest to maintain;
* Simple options to subscribe to Market Data events of application interest;
* Loading of CME SBE templates;
* Loading of CME XML channel configuration files;

## Distribution

In order to build a package from Gradle:

```
> gradlew
```

## Installation

This is java library which include just one jar file: b2bits-jmdp3-N.N.jar.

**Dependencies (July 10, 2017)**
- annotations-12.0.jar
- chronicle-bytes-1.2.4.jar
- chronicle-core-1.3.6.jar
- commons-collections-3.2.2.jar
- commons-configuration-1.10.jar
- commons-lang-2.6.jar
- commons-logging-1.1.1.jar
- koloboke-api-jdk8-0.6.8.jar
- koloboke-impl-jdk8-0.6.8.jar
- log4j-api-2.5.jar
- log4j-core-2.5.jar
- log4j-slf4j-impl-2.5.jar
- slf4j-api-1.7.14.jar
- snappy-java-1.1.2.1.jar
- agrona-0.9.5.jar
- commons-lang3-3.0.jar

## Code Examples

**Full low level listener subscription example (Channel 311. All instruments)**

```
import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.channel.MdpChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static class ChannelListenerImpl implements ChannelListener {
        @Override
        public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is started", channelId, feedType, feed);
        }

        @Override
        public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is stopped", channelId, feedType, feed);
        }

        @Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
            logger.info("{} {}: {}", feedType, feed , mdpPacket);
        }

        @Override
        public void onBeforeChannelReset(String channelId, MdpMessage resetMessage) {
            logger.info("Channel '{}' is broken, all books should be restored", channelId);
        }

        @Override
        public void onFinishedChannelReset(String channelId, MdpMessage resetMessage) {
            logger.info("Channel '{}' has been reset and restored", channelId);
        }

        @Override
        public void onChannelStateChanged(String channelId, ChannelState prevState, ChannelState newState) {
            logger.info("Channel '{}' state is changed from '{}' to '{}'", channelId, prevState, newState);
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            logger.info("Received SecurityDefinition(d). Schema Id: {}", mdpMessage.getSchemaId());
            return MdEventFlags.MESSAGE;
        }

        @Override
        public void onIncrementalMBORefresh(final String channelId, final short matchEventIndicator, final int securityId,
                                            final String secDesc, final long msgSeqNum, final FieldSet orderEntry, final FieldSet mdEntry){
            logger.info("[{}] onIncrementalMBORefresh: ChannelId: {}, SecurityId: {}-{}, OrderId: {}", msgSeqNum, channelId,
                    securityId, secDesc, orderEntry.getUInt64(37));
        }

        @Override
        public void onIncrementalMBPRefresh(String channelId, short matchEventIndicator, int securityId, String secDesc, 
                                            long msgSeqNum, FieldSet mdEntry) {
            logger.info("[{}] onIncrementalMBPRefresh: SecurityId: {}-{}. RptSeqNum(83): {}", msgSeqNum, securityId, 
                    secDesc, mdEntry.getUInt32(83));
        }

        @Override
        public void onSnapshotMBOFullRefresh(final String channelId, final String secDesc, final MdpMessage snptMessage){
            logger.info("onMBOFullRefresh: ChannelId: {}, SecurityId: {}-{}.", channelId, snptMessage.getInt32(48), secDesc);
        }

        @Override
        public void onSnapshotMBPFullRefresh(String channelId, String secDesc, MdpMessage snptMessage) {
            logger.info("onMBPFullRefresh: SecurityId: {}-{}. RptSeqNum(83): {}",
                    snptMessage.getInt32(48), secDesc, snptMessage.getUInt32(83));
        }

        @Override
        public void onRequestForQuote(String channelId, MdpMessage rfqMessage) {
            logger.info("onRequestForQuote");
        }

        @Override
        public void onSecurityStatus(String channelId, int securityId, MdpMessage secStatusMessage) {
            logger.info("onSecurityStatus. SecurityId: {}, RptSeqNum(83): {}", securityId, secStatusMessage.getUInt32(83));
        }

        public static void main(String args[]) {
            try {
                final MdpChannel mdpChannel311 = new MdpChannelBuilder("311",
                        Main.class.getResource("/config.xml").toURI(),
                        Main.class.getResource("/templates_FixBinary.xml").toURI())
                        .usingListener(new ChannelListenerImpl())
                        .usingGapThreshold(3)
                        .setMBOEnable(true)
                        .build();

                mdpChannel311.startFeed(FeedType.N, Feed.A);
                mdpChannel311.startFeed(FeedType.I, Feed.A);
                mdpChannel311.startFeed(FeedType.I, Feed.B);
                mdpChannel311.startFeed(FeedType.SMBO, Feed.A);
                System.out.println("Press enter to shutdown.");
                System.in.read();
                mdpChannel311.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
```
**Getting all Security Definitions of Channel 311**

```
import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class PrintAllSecuritiesTest {
    private static final Logger logger = LogManager.getLogger(PrintAllSecuritiesTest.class);
    private static final MdpGroup group1141 = SbeGroup.instance();
    private static final SbeString tag1022value = SbeString.allocate(3);
    private static final SbeString tag55value = SbeString.allocate(20);
    private static final SbeString tag1151value = SbeString.allocate(6);
    private static final SbeString tag6937value = SbeString.allocate(6);
    private static final SbeString tag167value = SbeString.allocate(6);
    private static final AtomicInteger counter = new AtomicInteger();
    private static final Object resultIsReady = new Object();

    private static class ChannelListenerImpl implements VoidChannelListener {
        @Override
        public void onFeedStarted(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is started", channelId, feedType, feed);
        }

        @Override
        public void onFeedStopped(String channelId, FeedType feedType, Feed feed) {
            logger.info("Channel '{}': {} feed {} is stopped", channelId, feedType, feed);
            synchronized (resultIsReady) {
                resultIsReady.notify();
            }
        }

        @Override
        public void onPacket(String channelId, FeedType feedType, Feed feed, MdpPacket mdpPacket) {
            logger.info("Channel '{}': {} feed {} received MDP packet {}", channelId, feedType, feed, mdpPacket.toString());
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            final int securityId = mdpMessage.getInt32(48);
            counter.incrementAndGet();
            logger.info("Channel {}'s security: {}", channelId, securityId);
            mdpMessage.getString(55, tag55value);
            logger.info("   Symbol : {}", tag55value.getString());
            mdpMessage.getString(1151, tag1151value);
            logger.info("   SecurityGroup : {}", tag1151value.getString());
            mdpMessage.getString(6937, tag6937value);
            logger.info("   Asset : {}", tag6937value.getString());
            mdpMessage.getString(167, tag167value);
            logger.info("   SecurityType : {}", tag167value.getString());
            while (group1141.hashNext()) {
                group1141.next();
                group1141.getString(1022, tag1022value);
                final int depth = group1141.getInt8(264);
                logger.info("   {} depth : {}", tag1022value.getString(), depth);
            }
            return MdEventFlags.NOTHING;
        }
    }

    public static void main(String args[]) {
        try {
            final MdpChannel mdpChannel311 = new MdpChannelBuilder("311",
                    PrintAllSecuritiesTest.class.getResource("/config.xml").toURI(),
                    PrintAllSecuritiesTest.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new ChannelListenerImpl())
                    .build();

            mdpChannel311.startFeed(FeedType.N, Feed.A);
            synchronized (resultIsReady) {
                resultIsReady.wait();
            }
            logger.info("Received packets in cycles: {}", counter.get());
            mdpChannel311.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
```

## API References

Builder parameters list (`com.epam.cme.mdp3.channel.MdpChannelBuilder`)

| Method name                                                                                     | Description                                                                                                                                                                                              | Default value     |
| ----------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- |
| `MdpChannelBuilder#setConfiguration(URI cfgURI)`                                                | Path to the CME Market Data Feed Channel configuration file                                                                                                                                              | null              |
| `MdpChannelBuilder#setSchema(URI schemaURI)`                                                    | Path to the CME SBE template file                                                                                                                                                                       | null              |
| `MdpChannelBuilder#setNetworkInterface(FeedType feedType, Feed feed, String networkInterface)`  | Local network interface that is used for receiving UDP packets. If it is set to null then default local network interface is used.                                                                       | null              |
| `MdpChannelBuilder#usingListener(ChannelListener channelListener)`                              | User's implementation of ChannelListener, whose methods will be called for received messages                                                                                                             | null              |
| `MdpChannelBuilder#usingScheduler(ScheduledExecutorService scheduler)`                          | Instance of scheduled executor service, which will be used for running TCP recovery tasks and updating state from snapshot in case of there are no messages from incremental channels for 100 seconds    | null(disabled)    |
| `MdpChannelBuilder#usingIncrQueueSize(int incrQueueSize)`                                       | Size of the queue that is used for buffering the messages from increment channel during recovery procedure                                                                                               | 15000             |
| `MdpChannelBuilder#usingGapThreshold(int gapThreshold)`                                         | Amount of lost messages after which recovery procedure starts                                                                                                                                            | 5                 |
| `MdpChannelBuilder#usingRcvBufSize(int rcvBufSize)`                                             | UDP socket buffer size                                                                                                                                                                                   | 4 * 1024 * 1024   |
| `MdpChannelBuilder#setTcpUsername(String tcpUsername)`                                          | Username to be used in the Logon (35=A) message to CME TCP replay feed                                                                                                                                   | CME               |
| `MdpChannelBuilder#setTcpPassword(String tcpPassword)`                                          | Password to be used in the Logon (35=A) message to CME TCP replay feed                                                                                                                                   | CME               |
| `MdpChannelBuilder#setMBOEnable(boolean enabled)`                                               | Enabling MBO mode in which order entries in the messages are processed and MBO snapshot feed is used                                                                                                     | false(disabled)   |
| `MdpChannelBuilder#build()`                                                                     | Build the channel with the specified parameters                                                                                                                                                          | -                 |

Channel parameter list (`com.epam.cme.mdp3.MdpChannel`)

| Method name                                                     | Description                                       |
| --------------------------------------------------------------- | ------------------------------------------------- |
| `MdpChannel#getId()`                                            | Gets ID of MDP Channel                            |
| `MdpChannel#close()`                                            | Closes MDP Channel and releases all resources     |
| `MdpChannel#getState()`                                         | Gets current State of the channel                 |
| `MdpChannel#registerListener(ChannelListener channelListene)`   | Registers Channel Listener                        |
| `MdpChannel#removeListener(ChannelListener channelListene)`     | Removes Channel Listener                          |
| `MdpChannel#getListeners()`                                     | Gets all registered Channel Listeners             |
| `MdpChannel#startFeed(FeedType feedType, Feed feed)`            | Starts defined feed                               |
| `MdpChannel#stopFeed(FeedType feedType, Feed feed)`             | Stops defined feed                                |
| `MdpChannel#stopAllFeeds()`                                     | Stops all Feeds                                   |
| `MdpChannel#subscribe(int securityId, final String secDesc)`    | Subscribes to the given security                  |
| `MdpChannel#discontinueSecurity(int securityId)`                | Removes subscription to the given security        |

The interface `com.epam.cme.mdp3.MarketDataListener` has to be implemented and
its instance should be set in `com.epam.cme.mdp3.MdpChannel.registerMarketDataListener` method from mbp-only module in order to work with
high level book API for Market By Price functionality.

## Performance tests

The project contains performance tests of incremental refresh handling. These tests are based on JMH and use test data similar to the data received on CME Certification Environment.
Performance tests have the following scenario:

1. MDP packet, which contains incremental refresh message (msgType = "X"), is generated;
2. Real feed is emulating based on the generated MDP packet (by adjusting the sequence number on each iteration);
3. MDP Handler is called to process the next MDP packet;
4. Market Data entries from the MDP packet are processed in user listener (retrieving the data from entries to make it similar to regular user applications).

In order to run tests from Gradle:

```
> gradlew jmh
```

### Example of results:

### Test machine hardware:

CPU - Intel(R) Core(TM) i5-4570 CPU @ 3.20GHz

Memory - 16 GB

Operating System - Windows 10.0, 64 bit Build 14393 (10.0.14393.1198)

### Market By Price mode(when MBO is disabled):

```
...
MDP Packet sample:
	msgSeqNum=1; secId=998350; rptSeqNum=1254; mdEntryType=Bid; mdAction=New; level=1; entrySize=4; orderNum=0; priceMantissa=98745000000
	msgSeqNum=1; secId=998350; rptSeqNum=1255; mdEntryType=Offer; mdAction=New; level=1; entrySize=1; orderNum=0; priceMantissa=987075000000
	msgSeqNum=1; secId=998350; rptSeqNum=1256; mdEntryType=Bid; mdAction=New; level=2; entrySize=45; orderNum=0; priceMantissa=987125000000
	msgSeqNum=1; secId=998350; rptSeqNum=1257; mdEntryType=Offer; mdAction=New; level=2; entrySize=22; orderNum=0; priceMantissa=98745000000
	msgSeqNum=1; secId=998350; rptSeqNum=1258; mdEntryType=Bid; mdAction=New; level=3; entrySize=98; orderNum=0; priceMantissa=98720000000
	msgSeqNum=1; secId=998350; rptSeqNum=1259; mdEntryType=Offer; mdAction=New; level=3; entrySize=43; orderNum=0; priceMantissa=98725000000
	msgSeqNum=1; secId=998350; rptSeqNum=1260; mdEntryType=Bid; mdAction=New; level=4; entrySize=12; orderNum=0; priceMantissa=98715000000
	msgSeqNum=1; secId=998350; rptSeqNum=1261; mdEntryType=Offer; mdAction=New; level=4; entrySize=83; orderNum=0; priceMantissa=98670000000
	msgSeqNum=1; secId=998350; rptSeqNum=1262; mdEntryType=Bid; mdAction=New; level=5; entrySize=38; orderNum=0; priceMantissa=98695000000
	msgSeqNum=1; secId=998350; rptSeqNum=1263; mdEntryType=Offer; mdAction=New; level=5; entrySize=99; orderNum=0; priceMantissa=98690000000
	msgSeqNum=1; secId=998350; rptSeqNum=1264; mdEntryType=Bid; mdAction=Delete; level=3; entrySize=1; orderNum=0; priceMantissa=987025000000
	msgSeqNum=1; secId=998350; rptSeqNum=1265; mdEntryType=Offer; mdAction=Delete; level=3; entrySize=99; orderNum=0; priceMantissa=98677500000
...
Percentiles, us/op:
      p(0.0000) =      0.641 us/op
     p(50.0000) =      0.642 us/op
     p(90.0000) =      0.962 us/op
     p(95.0000) =      0.963 us/op
     p(99.0000) =      0.963 us/op
     p(99.9000) =     10.896 us/op
     p(99.9900) =     17.952 us/op
     ...
Benchmark                                Mode     Cnt  Score   Error  Units
IncrementalRefreshPerfTest.MBPOnly     sample  496246  0.809   0.004  us/op
```

### Market By Price and Market By Order mode (MBO only template):

```
...
MDP Packet sample:
	securityId: 998350-testSymbol, orderId - '9926951995', mdOrderPriority - '414', priceMantissa - '98682500000', mdDisplayQty - '23',  mdEntryType - 'Bid', mdUpdateAction - 'Delete'
	securityId: 998350-testSymbol, orderId - '9926951993', mdOrderPriority - '412', priceMantissa - '98685000000', mdDisplayQty - '59',  mdEntryType - 'Offer', mdUpdateAction - 'Delete'
	securityId: 998350-testSymbol, orderId - '9926951992', mdOrderPriority - '411', priceMantissa - '98692500000', mdDisplayQty - '12',  mdEntryType - 'Bid', mdUpdateAction - 'Delete'
	securityId: 998350-testSymbol, orderId - '9926951997', mdOrderPriority - '416', priceMantissa - '98677500000', mdDisplayQty - '49',  mdEntryType - 'Offer', mdUpdateAction - 'Change'
	securityId: 998350-testSymbol, orderId - '9926951996', mdOrderPriority - '415', priceMantissa - '98687500000', mdDisplayQty - '92',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9926952003', mdOrderPriority - '422', priceMantissa - '98672500000', mdDisplayQty - '88',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9926952002', mdOrderPriority - '421', priceMantissa - '98677500000', mdDisplayQty - '32',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9926952001', mdOrderPriority - '420', priceMantissa - '98702500000', mdDisplayQty - '99',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9926952000', mdOrderPriority - '419', priceMantissa - '98680000000', mdDisplayQty - '94',  mdEntryType - 'Bid', mdUpdateAction - 'Delete'
	securityId: 998350-testSymbol, orderId - '9926952005', mdOrderPriority - '424', priceMantissa - '98675000000', mdDisplayQty - '49',  mdEntryType - 'Bid', mdUpdateAction - 'Change'
	securityId: 998350-testSymbol, orderId - '9926952004', mdOrderPriority - '423', priceMantissa - '98702500000', mdDisplayQty - '54',  mdEntryType - 'Offer', mdUpdateAction - 'Change'
	securityId: 998350-testSymbol, orderId - '9926951983', mdOrderPriority - '402', priceMantissa - '98670000000', mdDisplayQty - '16',  mdEntryType - 'Offer', mdUpdateAction - 'Delete'
... 
Percentiles, us/op:
      p(0.0000) =      0.320 us/op
     p(50.0000) =      0.641 us/op
     p(90.0000) =      0.642 us/op
     p(95.0000) =      0.642 us/op
     p(99.0000) =      0.642 us/op
     p(99.9000) =     10.576 us/op
     p(99.9900) =     16.032 us/op
     ...
Benchmark                                Mode     Cnt  Score   Error  Units
IncrementalRefreshPerfTest.MBOOnly     sample  333551  0.615   0.003  us/op
```

### Market By Price and Market By Order mode (MBO included in MBP template):

```
...
MDP Packet sample:
	msgSeqNum=1; secId=998350; rptSeqNum=1254; mdEntryType=Bid; mdAction=New; level=1; entrySize=4; orderNum=0; priceMantissa=98745000000
	msgSeqNum=1; secId=998350; rptSeqNum=1255; mdEntryType=Offer; mdAction=New; level=1; entrySize=1; orderNum=0; priceMantissa=987075000000
	msgSeqNum=1; secId=998350; rptSeqNum=1256; mdEntryType=Bid; mdAction=New; level=2; entrySize=45; orderNum=0; priceMantissa=987125000000
	msgSeqNum=1; secId=998350; rptSeqNum=1257; mdEntryType=Offer; mdAction=New; level=2; entrySize=22; orderNum=0; priceMantissa=98745000000
	msgSeqNum=1; secId=998350; rptSeqNum=1258; mdEntryType=Bid; mdAction=New; level=3; entrySize=98; orderNum=0; priceMantissa=98720000000
	msgSeqNum=1; secId=998350; rptSeqNum=1259; mdEntryType=Offer; mdAction=New; level=3; entrySize=43; orderNum=0; priceMantissa=98725000000
	msgSeqNum=1; secId=998350; rptSeqNum=1260; mdEntryType=Bid; mdAction=New; level=4; entrySize=12; orderNum=0; priceMantissa=98715000000
	msgSeqNum=1; secId=998350; rptSeqNum=1261; mdEntryType=Offer; mdAction=New; level=4; entrySize=83; orderNum=0; priceMantissa=98670000000
	msgSeqNum=1; secId=998350; rptSeqNum=1262; mdEntryType=Bid; mdAction=New; level=5; entrySize=38; orderNum=0; priceMantissa=98695000000
	msgSeqNum=1; secId=998350; rptSeqNum=1263; mdEntryType=Offer; mdAction=New; level=5; entrySize=99; orderNum=0; priceMantissa=98690000000
	msgSeqNum=1; secId=998350; rptSeqNum=1264; mdEntryType=Bid; mdAction=Delete; level=3; entrySize=1; orderNum=0; priceMantissa=987025000000
	msgSeqNum=1; secId=998350; rptSeqNum=1265; mdEntryType=Offer; mdAction=Delete; level=3; entrySize=99; orderNum=0; priceMantissa=98677500000
	securityId: 998350-testSymbol, orderId - '9927057956', mdOrderPriority - '5394', priceMantissa - '98745000000', mdDisplayQty - '61',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057957', mdOrderPriority - '5395', priceMantissa - '987075000000', mdDisplayQty - '3',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057958', mdOrderPriority - '5396', priceMantissa - '987125000000', mdDisplayQty - '7',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057959', mdOrderPriority - '5397', priceMantissa - '98745000000', mdDisplayQty - '44',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057960', mdOrderPriority - '5398', priceMantissa - '98720000000', mdDisplayQty - '55',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057961', mdOrderPriority - '5399', priceMantissa - '98725000000', mdDisplayQty - '68',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057962', mdOrderPriority - '5400', priceMantissa - '98715000000', mdDisplayQty - '70',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057963', mdOrderPriority - '5401', priceMantissa - '98670000000', mdDisplayQty - '56',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057964', mdOrderPriority - '5402', priceMantissa - '98695000000', mdDisplayQty - '6',  mdEntryType - 'Bid', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057965', mdOrderPriority - '5403', priceMantissa - '98690000000', mdDisplayQty - '20',  mdEntryType - 'Offer', mdUpdateAction - 'New'
	securityId: 998350-testSymbol, orderId - '9927057966', mdOrderPriority - '5404', priceMantissa - '987025000000', mdDisplayQty - '9',  mdEntryType - 'Bid', mdUpdateAction - 'Delete'
	securityId: 998350-testSymbol, orderId - '9927057967', mdOrderPriority - '5405', priceMantissa - '98677500000', mdDisplayQty - '29',  mdEntryType - 'Offer', mdUpdateAction - 'Delete'
...
Percentiles, us/op:
      p(0.0000) =      1.282 us/op
     p(50.0000) =      1.604 us/op
     p(90.0000) =      1.604 us/op
     p(95.0000) =      1.604 us/op
     p(99.0000) =      1.924 us/op
     p(99.9000) =     14.752 us/op
     p(99.9900) =     25.632 us/op
     ...
Benchmark                                Mode     Cnt  Score   Error  Units
IncrementalRefreshPerfTest.mboWithMBP  sample  480387  1.645   0.004  us/op
```

## License

[GNU Lesser General Public License, version 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).

## Contributors

OLEG VERAMEI

Software Engineering Team Leader in EPAM's Capital Markets Competency Center 

Email: oleg_veramei@epam.com

VIACHESLAV KOLYBELKIN

Senior Software Engineer in EPAM's Capital Markets Competency Center

Email: viacheslav_kolybelkin@epam.com

## Support

Should you have any questions or inquiries, please direct them to SupportFIXAntenna@epam.com

