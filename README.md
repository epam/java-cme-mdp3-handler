## Synopsis

Java Market Data Handler for CME Market Data (MDP 3.0) was designed to take advantage of the new low-latency data feed. It fully supports features of the CME Globex MDP3.0 market data platform, helps feeding CME market data directly into the client application. The handler delivers market data updates from socket to your application in a few microseconds.

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

## Code Examples

**Full low level listener subscription example (Channel 311. All instruments)**

```
import com.epam.cme.mdp3.*;
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
import com.epam.cme.mdp3.core.control.InstrumentState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

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
        public void onInstrumentStateChanged(final String channelId, int securityId, final String secDesc,
                                             InstrumentState prevState, InstrumentState newState) {
            logger.info("Channel '{}'s instrument {} state is changed from '{}' to '{}'",
                channelId, securityId, prevState, newState);
        }

        @Override
        public int onSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
            logger.info("Received SecurityDefinition(d). Schema Id: {}", mdpMessage.getSchemaId());
            return MdEventFlags.NOTHING;
        }

        @Override
        public void onIncrementalRefresh(final String channelId, final short matchEventIndicator,
                                         int securityId, String secDesc,
                                         long msgSeqNum, final FieldSet incrRefreshEntry) {
            logger.info("[{}] onIncrementalRefresh: SecurityId: {}-{}. RptSeqNum(83): {}", msgSeqNum, securityId, secDesc, incrRefreshEntry.getUInt32(83));
        }

        @Override
        public void onSnapshotFullRefresh(final String channelId, String secDesc, final MdpMessage snptMessage) {
            logger.info("onFullRefresh: SecurityId: {}-{}. RptSeqNum(83): {}",
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
    }

    public static void main(String args[]) {
        try {
            final MdpChannel mdpChannel311 = new MdpChannelBuilder("311",
                    Main.class.getResource("/config.xml").toURI(),
                    Main.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new ChannelListenerImpl())
                    .usingGapThreshold(20)
                    .build();

            mdpChannel311.enableAllSecuritiesMode();
            mdpChannel311.startIncrementalFeedA();
            mdpChannel311.startIncrementalFeedB();
            mdpChannel311.startSnapshotFeedA();
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
import com.epam.cme.mdp3.core.channel.MdpChannelBuilder;
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

    public static void main(String args[ ]) {
        try {
            final MdpChannel mdpChannel311 = new MdpChannelBuilder("311",
                    Main.class.getResource("/config.xml").toURI(),
                    Main.class.getResource("/templates_FixBinary.xml").toURI())
                    .usingListener(new ChannelListenerImpl())
                    .build();

            mdpChannel311.startInstrumentFeedA();
            synchronized (resultIsReady) {
                resultIsReady.wait();
            }
            logger.info("Received packets in cycles: {}", counter.get());
            mdpChannel311.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Motivation

With the third generation of its market data platform CME made several key improvements in the
application protocol and message encoding to allow more efficient market data processing on the
client side.

Matching engine event boundaries are now clearly indicated with the MatchEventIndicator (5799) tag to
allow a client application apply market data updates transactionally. The client application has explicit
knowledge of the moment when market data is consistent for analysis in the case of complex order
book updates or multiple instruments affected by a matching event. Matching events are now
independent of the UDP packet boundaries – a matching event may spread over several sequential UDP
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

## Installation

This is java library which include just one jar file: b2bits-jmdp3-N.N.jar.

**Dependencies (May 4, 2016)**
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

## API Reference

TBD

## Tests

There are acceptance tests in the project. Test scenarios are based on [Cucumber](https://cucumber.io/) and test data files with MDP Messages (received via CME Certification Environment).
To run tests from Gradle (warning: unpack data files in /src/cucumber/sim/data):

```
> gradlew cucumber
```

There is also performance test of incremental refresh handling in the project. Test is based on JMH and use test data file with MDP Messages (received via CME Certification Environment).
Performance test has the following scenario:

1. Find sample incremental refresh MDP packet from data file (for instance, incremental entries from 10 to 14, refreshed securities from 4 to 6)

2. Generate a sequence of test packets from the sample packet (e.g. change sequence numbers of packet and securities)

3. Perform test. Each iteration fills buffers with next test packet and calls MDP Handler to process it

To run tests from Gradle (warning: unpack data files in /src/cucumber/sim/data):

```
> gradlew jmh
```

Example of results:

```
...
Loading test data dump...
Loading test data dump...Done
Generating test MDP packets...
The found MDP Packet sample:
        msgSeqNum=59343896; secId=556449; rptSeqNum=910008; mdEntryType=Bid; mdAction=Change; level=1; entrySize=137; orderNum=5; priceMa12000000000
        msgSeqNum=59343896; secId=556449; rptSeqNum=910009; mdEntryType=Bid; mdAction=Change; level=2; entrySize=147; orderNum=7; priceMantissa=11750000000
        msgSeqNum=59343896; secId=556449; rptSeqNum=910010; mdEntryType=Offer; mdAction=Change; level=1; entrySize=147; orderNum=6; priceMantissa=12500000000
        msgSeqNum=59343896; secId=556449; rptSeqNum=910011; mdEntryType=Offer; mdAction=Change; level=2; entrySize=79; orderNum=4; priceMantissa=12750000000
        msgSeqNum=59343896; secId=221807; rptSeqNum=824039; mdEntryType=Offer; mdAction=Change; level=1; entrySize=73; orderNum=3; priceMantissa=17250000000
        msgSeqNum=59343896; secId=221807; rptSeqNum=824040; mdEntryType=Offer; mdAction=Change; level=2; entrySize=57; orderNum=4; priceMantissa=17500000000
        msgSeqNum=59343896; secId=575632; rptSeqNum=831830; mdEntryType=Bid; mdAction=Change; level=1; entrySize=128; orderNum=2; priceMantissa=8500000000
        msgSeqNum=59343896; secId=575632; rptSeqNum=831831; mdEntryType=Bid; mdAction=Change; level=2; entrySize=159; orderNum=6; priceMantissa=8250000000
        msgSeqNum=59343896; secId=127203; rptSeqNum=815942; mdEntryType=Offer; mdAction=Change; level=1; entrySize=73; orderNum=3; priceMantissa=15750000000
        msgSeqNum=59343896; secId=127203; rptSeqNum=815943; mdEntryType=Offer; mdAction=Change; level=2; entrySize=61; orderNum=4; priceMantissa=16000000000
        msgSeqNum=59343896; secId=248452; rptSeqNum=953666; mdEntryType=Bid; mdAction=Change; level=1; entrySize=132; orderNum=7; priceMantissa=16250000000
        msgSeqNum=59343896; secId=248452; rptSeqNum=953667; mdEntryType=Bid; mdAction=Change; level=2; entrySize=12; orderNum=4; priceMantissa=16000000000
Generating test MDP packets...Done
Creating Data Handler instance...
Creating Data Handler instance...Done
...
  Percentiles, us/op:
      p(0.0000) =      0.729 us/op
     p(50.0000) =      1.094 us/op
     p(90.0000) =      1.094 us/op
     p(95.0000) =      1.458 us/op
     p(99.0000) =      1.824 us/op
     p(99.9000) =     14.944 us/op
     p(99.9900) =     33.152 us/op
     ...
```


## Contributors

OLEG VERAMEI

Software Engineering Team Leader in EPAM's Capital Markets Competency Center 

Email: oleg_veramei@epam.com 

## License

[GNU Lesser General Public License, version 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
