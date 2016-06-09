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

package com.epam.cme.mdp3;

import com.epam.cme.mdp3.sbe.message.meta.MdpMessageType;

/**
 * Interface to MDP Message.
 */
public interface MdpMessage extends FieldSet {
    /**
     * Gets Id of the messages in MDP/SBE schema
     *
     * @return id number
     */
    @Override
    int getSchemaId();

    /**
     * Gets semantic type of the Message (e.g. IncrementalRefresh)
     *
     * @return a SemanticMsgType value
     */
    SemanticMsgType getSemanticMsgType();

    /**
     * Sets metadata to this instance for later usage in processing flows
     * @param messageType related metadata instance
     */
    void setMessageType(MdpMessageType messageType);

    /**
     * Creates copy of the current Mdp Message
     *
     * @return a copy instance of the of the current Mdp Message
     */
    @Override
    MdpMessage copy();
}
