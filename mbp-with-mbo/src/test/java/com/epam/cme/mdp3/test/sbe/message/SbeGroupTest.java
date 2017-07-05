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

package com.epam.cme.mdp3.test.sbe.message;

import com.epam.cme.mdp3.MdpGroup;
import com.epam.cme.mdp3.MdpGroupEntry;
import com.epam.cme.mdp3.MdpPacket;
import com.epam.cme.mdp3.sbe.message.SbeGroup;
import com.epam.cme.mdp3.sbe.message.SbeGroupEntry;
import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;
import com.epam.cme.mdp3.sbe.schema.MdpMessageTypes;
import com.epam.cme.mdp3.test.ModelUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static com.epam.cme.mdp3.test.Constants.TEMPLATE_NAME;

public class SbeGroupTest {
    private MdpMessageTypes mdpMessageTypes;

    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        mdpMessageTypes = new MdpMessageTypes(classLoader.getResource(TEMPLATE_NAME).toURI());
    }

    @Test
    public void groupEntryByEntryNum(){
        MdpPacket packet = new MdpPacket();
        MdpGroup mdpGroup = SbeGroup.instance();
        MdpGroupEntry groupEntry = SbeGroupEntry.instance();
        ByteBuffer mbpWithMBOIncrementTestMessage = ModelUtils.getMBPWithMBOIncrementTestMessage(1, new int[]{1, 3, 6}, null);
        packet.wrapFromBuffer(mbpWithMBOIncrementTestMessage);
        packet.forEach(mdpMessage -> {
            int schemaId = mdpMessage.getSchemaId();
            MdpMessageType messageType = mdpMessageTypes.getMessageType(schemaId);
            mdpMessage.setMessageType(messageType);
            mdpMessage.getGroup(268, mdpGroup);
            mdpGroup.getEntry(2, groupEntry);
            int securityId = groupEntry.getInt32(48);
            Assert.assertEquals(3, securityId);
        });
    }

}