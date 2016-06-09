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
package com.epam.cme.mdp3.sbe.schema;

import com.epam.cme.mdp3.sbe.schema.vo.MessageSchema;
import com.epam.cme.mdp3.sbe.schema.vo.ObjectFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class MessageSchemaUnmarshaller {
    private static final String NAMESPACE = "http://www.fixprotocol.org/ns/simple/1.0";

    private MessageSchemaUnmarshaller() {
    }

    /*
     * Schema has broken namespaces so namespace filter is used to map namespaces
     */
    public static MessageSchema unmarshall(URI uri) throws SchemaUnmarshallingException {
        try {
            return unmarshall(uri.toURL().openStream());
        } catch (Exception e) {
            throw new SchemaUnmarshallingException("Failed to parse MDP Schema: " + e.getMessage(), e);
        }
    }

    public static MessageSchema unmarshall(InputStream inputStream) throws SchemaUnmarshallingException {
        try {
            final JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            final Unmarshaller unmarshaller = jc.createUnmarshaller();
            final InputSource is = new InputSource(new InputStreamReader(inputStream));
            final XMLReader reader = XMLReaderFactory.createXMLReader();
            final NamespaceFilter filter = new NamespaceFilter(NAMESPACE, false);
            filter.setParent(reader);
            final SAXSource source = new SAXSource(filter, is);
            unmarshaller.setEventHandler(event -> false);
            return (MessageSchema) unmarshaller.unmarshal(source);
        } catch (Exception e) {
            throw new SchemaUnmarshallingException("Failed to parse MDP Schema: " + e.getMessage(), e);
        }
    }
}
