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
 * $Id: MessageQueue.java,v 1.7 2004/08/02 11:12:45 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import kobold.common.data.AbstractKoboldMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * This class implements a queue for KoboldMessage-(and derived) Objects
 *
 * @author garbeam
 */
public class MessageQueue {
    
    private static Log log = LogFactory.getLog(MessageQueue.class);
    private LinkedList queue = null;
	private String messageStore = null;

	/**
	 * Basic constructor.
	 */
	public MessageQueue(String username) {
		queue = new LinkedList();
		this.messageStore = System.getProperty("kobold.server.storePath") +
							username + ".xml";
		deserialize();
	}
	
	/**
	 * This method adds a new message to the queue.
	 * @param koboldMessage the message to add.
	 */
	public void addMessage(AbstractKoboldMessage koboldMessage) {
		queue.add(0, koboldMessage);
	}

	/**
	 * @return the oldest message in the queue, does not remove it! 
	 */
	public AbstractKoboldMessage getMessage() {
	    Object result = queue.getLast();
	    
	    return (result == null) ? null : (AbstractKoboldMessage) result;
	}

	/**
	 * Removes the given koboldMessage.
	 * @param koboldMessage the message.
	 */
	public void removeMessage(AbstractKoboldMessage koboldMessage) {
		queue.remove(koboldMessage);
	}
	
	/**
	 * Serializes this message queue into messageStore.
	 */
	public void serialize() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("message-queue");


		for (Iterator it = this.queue.iterator(); it.hasNext();) {
		    AbstractKoboldMessage koboldMessage = (AbstractKoboldMessage) it.next();
			root.add(koboldMessage.serialize());
		}

		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(messageStore));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			LogFactory.getLog("kobold.server.controller.MessageQueue").error(e);
		}
	}
	
	/**
	 * Deserializes this message queue.
	 */
	public void deserialize() {
		SAXReader reader = new SAXReader();
		Document document = null;
		
		try {
			queue.clear();
		    document = reader.read(messageStore);
		    Element root = document.getRootElement();
		    if (root != null) {
		        Iterator iter = root.elementIterator("message");
		        for (; iter.hasNext(); ) {
		            Element element = (Element) iter.next();
		            queue.add(0, AbstractKoboldMessage.createMessage(element));
		        }
		    }
		} catch (DocumentException e) {
			log.error(e);
		}
	}
}
