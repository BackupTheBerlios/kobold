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
 * $Id: MessageQueue.java,v 1.5 2004/05/19 22:50:50 vanto Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.UserContext;

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

	private LinkedList queue = null;
	private UserContext userContext = null;
	private String messageStore = null;

	/**
	 * Basic constructor.
	 */
	public MessageQueue(UserContext userContext) {
		queue = new LinkedList();
		this.userContext = userContext;
		this.messageStore = System.getProperty("kobold.server.storePath") +
								userContext.getUserName() + ".xml";
		deserialize();
	}
	
	/**
	 * @return real list of this queue.
	 */
	public List getQueue() {
		return queue;
	}
	
	/**
	 * @return UserContext of this MessageQueue.
	 */
	public UserContext getUserContext() {
		return userContext;
	}
	
	/**
	 * This method adds a new message to the queue.
	 * @param koboldMessage the message to add.
	 */
	public void addMessage(AbstractKoboldMessage koboldMessage) {
		System.out.println(GlobalMessageContainer.getInstance());
		System.out.println(koboldMessage);
		GlobalMessageContainer.getInstance().addMessage(koboldMessage);
		queue.add(0, koboldMessage.getId());
	}

	/**
	 * @return the oldest message in the queue, does not remove it! 
	 */
	public AbstractKoboldMessage getMessage() {
	    try {
	        return (AbstractKoboldMessage) GlobalMessageContainer.getInstance().
				        getMessage((String)queue.getLast());
	    } catch (NoSuchElementException e) {
	    	return null;
	    }
	}

	/**
	 * Removes the given koboldMessage.
	 * @param koboldMessage the message.
	 */
	public void removeMessage(AbstractKoboldMessage koboldMessage) {
		queue.remove(koboldMessage.getId());
	}
	
	/**
	 * Serializes this message queue into messageStore.
	 */
	public void serialize() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold-server");

		Element queue = root.addElement("queue");

		for (Iterator it = this.queue.iterator(); it.hasNext();) {
			String id = (String) it.next();
			Element entry = queue.addElement("entry");
			entry.addText(id);
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
			document = reader.read(messageStore);
			
			LinkedList newQueue = new LinkedList();
			List list = document.selectNodes( "/kobold-server/queue" );
			for (Iterator iter = list.iterator(); iter.hasNext(); ) {
				Element element = (Element) iter.next();
				newQueue.add(element.getText());
			}
			// don't care about old queues
			queue = newQueue;
		} catch (DocumentException e) {
				LogFactory.getLog("kobold.server.controller.MessageQueue").error(e);
		}
	}
}
