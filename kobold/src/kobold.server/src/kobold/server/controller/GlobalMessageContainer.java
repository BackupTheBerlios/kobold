/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: GlobalMessageContainer.java,v 1.1 2004/05/15 01:24:17 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import kobold.common.data.KoboldMessage;

/**
 * @author garbeam
 *
 * This singleton class provides a stateful message container for
 * KoboldMessages.
 */
public class GlobalMessageContainer {
	
	private Map messages = null;
	private String messageStore = null;
	private static GlobalMessageContainer instance = null;
	private Log log = null;
	
	/**
	 * @return instance of this Singleton.
	 */
	public static GlobalMessageContainer getInstance() {
		if (instance != null) {
			instance = new GlobalMessageContainer(
					System.getProperty("kobold.server.messageStore"));
		}
		return instance;
	}
	
	/**
	 * Deserializes the messageStore.
	 */
	public void deserialize() {
	
		SAXReader reader = new SAXReader();
		Document document = null;
		
		try {
			document = reader.read(messageStore);
			
			Map newMessages = new HashMap();
			List list = document.selectNodes( "/kobold-server/messages" );
		    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
				Element element = (Element) iter.next();
				KoboldMessage koboldMessage = new KoboldMessage(element);
			    newMessages.put(koboldMessage.getId(), koboldMessage);
		    }
		    // don't care about old messages
		    messages = newMessages;
		} catch (DocumentException e) {
				log.error(e);
		}
	}

	/**
	 * Basic constructure.
	 * @param messageStore the path to the message store file,
	 * e.g. <code>/var/kobold/messages.xml</code>.
	 */
	private GlobalMessageContainer(String messageStore) {
		this.messageStore = messageStore;
		
		LogFactory.getLog("kobold.server.controller.GlobalMessageContainer");
		deserialize();			
	}
	
	/**
	 * Adds a new KoboldMessage to the message container.
	 * @param koboldMessage
	 */
	public void addMessage(KoboldMessage koboldMessage) {
		messages.put(koboldMessage.getId(), koboldMessage);
		serialize();
	}
	
	/**
	 * @return the KoboldMessage by the given id.
	 * @param id the id for the KoboldMessage.
	 */
	public KoboldMessage getMessage(String id) {
		return (KoboldMessage)messages.get(id);
	}
	
	/**
	 * Serializes this GlobalMessageContainer.
	 */
	public void serialize() {
		
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold-server");

		Element messages = root.addElement("messages");

		for (Iterator it = this.messages.values().iterator(); it.hasNext();) {
			KoboldMessage koboldMessage = (KoboldMessage) it.next();
			messages.add(koboldMessage.serialize());
		}

        XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(messageStore));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
}
