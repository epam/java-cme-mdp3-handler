package com.epam.cme.mdp3.test.control;


import java.nio.ByteBuffer;

import static com.epam.cme.mdp3.test.Constants.*;
import static org.junit.Assert.assertNotNull;

public class MessageProcessing {
//    private TestMBOChannelListener testListener = new TestMBOChannelListener();
//    private MdpChannel mdpChannel;
//    private MdpMessageTypes mdpMessageTypes;
//
//    @Before
//    public void init() throws Exception {
//        ClassLoader classLoader = getClass().getClassLoader();
//        mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(Constants.TEMPLATE_NAME).toURI());
//        MdpChannelBuilder mdpHandlerBuilder = new MdpChannelBuilder(String.valueOf(648),
//                classLoader.getResource(Constants.CONFIG_NAME).toURI(),
//                classLoader.getResource(Constants.TEMPLATE_NAME).toURI())
//                .usingListener(testListener);
//        mdpChannel = mdpHandlerBuilder.build();
//    }
//
//    @Ignore
//    @Test
//    public void handlerMustProcessAndResendMBOSnapshotMessageToClient() throws InterruptedException {
//        final MdpFeedContext smboContext = new MdpFeedContext(Feed.A, FeedType.SMBO);
//        final MdpFeedContext instrumentContext = new MdpFeedContext(Feed.A, FeedType.N);
//        final int security = 99;
//        ByteBuffer securityDefinition = ModelUtils.getMDInstrumentDefinitionFuture27(1, security);
//        ByteBuffer mboSnapshotTestMessage = ModelUtils.getMBOSnapshotTestMessage(2, security);
//        mdpChannel.subscribe(security, "Test security");
//        final MdpPacket mdpPacketWithSecurityDefinition = MdpPacket.instance();
//        mdpPacketWithSecurityDefinition.wrapFromBuffer(securityDefinition);
//        mdpChannel.handlePacket(instrumentContext, mdpPacketWithSecurityDefinition);
//        assertNotNull(testListener.nextSecurityMessage());
//
//        final MdpPacket mdpPacketWithSnapshot = MdpPacket.instance();
//        mdpPacketWithSnapshot.wrapFromBuffer(mboSnapshotTestMessage);
//        mdpChannel.handlePacket(smboContext, mdpPacketWithSnapshot);
//        assertNotNull(testListener.nextMBOSnapshotMessage());
//    }

}
