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
 * $Id: GlobalMessageContainer.java,v 1.3 2004/05/18 21:23:58 garbeam Exp $
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

import kobold.common.data.AbstractKoboldMessage;

/**
 * @author garbeam
 *
 * This singleton class provides a stateful message container for
 * KoboldMessages.
 */
public class GlobalMessageContainer {
	
	private Map messages = new HashMap();
	private String globalMessageStore = "global.xml";
	private static GlobalMessageContainer instance = null;
	private static final Log logger = LogFactory.getLog(GlobalMessageContainer.class);
	
	/**
	 * @return instance of this Singleton.
	 */
	public synchronized static GlobalMessageContainer getInstance() {
		if (instance == null) {
			instance = new GlobalMessageContainer();
		}
		return instance;
	}
	

	/**
	 * Deserializes the messageStore.
	 */
	public void deserialize() {
		deserialize(this.globalMessageStore);
	}
	
	/**
	 * Deserializes the messageStore.
	 * 
	 * @param path the global message store.
	 */
	public void deserialize(String path) {
	
		SAXReader reader = new SAXReader();
		Document document = null;
		
		try {
			Map newMessages = new HashMap();
			
			document = reader.read(path);
			if (document != null) {
				List list = document.selectNodes( "/kobold-server/messages/message" );
				for (Iterator iter = list.iterator(); iter.hasNext(); ) {
					Element element = (Element) iter.next();
					AbstractKoboldMessage koboldMessage = AbstractKoboldMessage.createMessage(element);
					newMessages.put(koboldMessage.getId(), koboldMessage);
				}
			}
		    // don't care about old messages
		    messages = newMessages;
		} catch (DocumentException e) {
				logger.error("Could not open global message container", e);
		}
	}

	/**
	 * Basic constructure.
	 */
	private GlobalMessageContainer() {
		this.globalMessageStore =
			System.getProperty("kobold.server.storePath") +
				System.getProperty("kobold.server.globalMessageStore");
		deserialize();			
	}
	
	/**
	 * Adds a new KoboldMessage to the message container.
	 * @param koboldMessage
	 */
	public void addMessage(AbstractKoboldMessage koboldMessage) {
		messages.put(koboldMessage.getId(), koboldMessage);
		serialize();
	}
	
	/**
	 * @return the KoboldMessage by the given id.
	 * @param id the id for the KoboldMessage.
	 */
	public AbstractKoboldMessage getMessage(String id) {
		return (AbstractKoboldMessage)messages.get(id);
	}
	
	/**
	 * Serializes this GlobalMessageContainer.
	 */
	public void serialize() {
		serialize(this.globalMessageStore);
	}
		
	/**
	 * Serializes this GlobalMessageContainer.
	 * 
	 * @param path the path to the store.
	 */
	public void serialize(String path) {
		
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold-server");

		Element messages = root.addElement("messages");

		for (Iterator it = this.messages.values().iterator(); it.hasNext();) {
			AbstractKoboldMessage koboldMessage = (AbstractKoboldMessage) it.next();
			messages.add(koboldMessage.serialize());
		}

        XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			logger.error("Could not serialize global message container", e);
		}
	}
}
