package com.epam.cme.mdp3.test;

import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.MdpMessage;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.message.SbeString;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class SecurityDefinitionUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDefinitionUtil.class);

    private static final MdpGroup group1141 = SbeGroup.instance();
    private static final MdpGroupEntry mdpGroupEntry = SbeGroupEntry.instance();
    private static final SbeString tag1022value = SbeString.allocate(3);
    private static final SbeString tag55value = SbeString.allocate(20);
    private static final SbeString tag1151value = SbeString.allocate(6);
    private static final SbeString tag6937value = SbeString.allocate(6);
    private static final SbeString tag167value = SbeString.allocate(6);

    public static void printSecurityDefinition(final String channelId, final MdpMessage mdpMessage) {
        final int securityId = mdpMessage.getInt32(48);
        logger.info("Channel {}'s security: {}", channelId, securityId);
        mdpMessage.getString(55, tag55value);
        logger.info("   Symbol : {}", tag55value.getString());
        mdpMessage.getString(1151, tag1151value);
        logger.info("   SecurityGroup : {}", tag1151value.getString());
        mdpMessage.getString(6937, tag6937value);
        logger.info("   Asset : {}", tag6937value.getString());
        mdpMessage.getString(167, tag167value);
        logger.info("   SecurityType : {}", tag167value.getString());
        short marketSegmentID = mdpMessage.getUInt8(1300);
        logger.info("   MarketSegmentID : {}", marketSegmentID);
        double strikePrice = 0;
        if(mdpMessage.hasField(202)) {
            strikePrice = mdpMessage.getInt64(202) * Math.pow(10, -7);
        }
        logger.info("   StrikePrice : {}", strikePrice);

        while (group1141.hashNext()) {
            group1141.next();
            group1141.getString(1022, tag1022value);
            final int depth = group1141.getInt8(264);
            logger.info("   {} depth : {}", tag1022value.getString(), depth);
        }
        MdpGroup mdpGroup = SbeGroup.instance();
        mdpMessage.getGroup(864, mdpGroup);

        while (mdpGroup.hashNext()){
            mdpGroup.next();
            mdpGroup.getEntry(mdpGroupEntry);
            long eventTime = mdpGroupEntry.getUInt64(1145);
            short eventType = mdpGroupEntry.getUInt8(865);
            logger.info("eventTime - '{}'({}), eventType - '{}'", DateFormatUtils.format(eventTime/1000000, "yyyyMMdd HHmm", TimeZone.getTimeZone("UTC")), eventTime, eventType);
        }
    }

}
