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
 * $Id: MessageManager.java,v 1.5 2004/05/19 15:20:25 grosseml Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.KoboldMessage;
import kobold.common.data.UserContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs). It's a
 * singleton class.
 *
 * @author garbeam
 */
public class MessageManager {

	private Map queues = null;
	private Map pendingMessages = null;
	private String messageStore = "messages.xml";
	
	static private MessageManager instance;
	 
	static public MessageManager getInstance() {
		 if (instance == null ) {
		 	 instance = new MessageManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
	 * @param path
	 */
	private MessageManager() {
		queues = new HashMap();
		pendingMessages = new HashMap();
		this.messageStore = System.getProperty("kobold.server.storePath") +
			System.getProperty("kobold.server.messageStore");
		deserialize();
		// DEBUG
		dummyMsg();
	}

	/**
	 * @return next message for the given userContext.
	 * @param userContext the user context.
	 */
	public synchronized AbstractKoboldMessage fetchMessage(UserContext userContext) {
		MessageQueue queue =
			(MessageQueue) queues.get(userContext.getSessionId());
		if (queue != null) {
			AbstractKoboldMessage koboldMessage =
				(AbstractKoboldMessage) queue.getMessage();
			if (koboldMessage != null) {
				koboldMessage.setState(AbstractKoboldMessage.STATE_FETCHED);
			}
			return koboldMessage;
		}
		return null;
	}

	/**
	 * Invalidates the given KoboldMessage for the given UserContext.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message to invalidate. 
	 */	
	public synchronized void invalidateMessage(UserContext userContext, 
												  AbstractKoboldMessage koboldMessage)
	{
		MessageQueue queue =
				(MessageQueue) queues.get(userContext.getSessionId());
		if (queue != null) {
			queue.removeMessage(koboldMessage);
			koboldMessage.setState(AbstractKoboldMessage.STATE_INVALID);
		}
	}
	
	/**
	 * Sends a message to the specified receipient.
	 * 
	 * @param userContext
	 * @param koboldMessage
	 */
	public synchronized void sendMessage(UserContext userContext,
											AbstractKoboldMessage koboldMessage)
	{
		UserContext recContext = SessionManager.getInstance().
					getUserContextForUserName(koboldMessage.getReceiver());
					
		MessageQueue queue =
				(MessageQueue) queues.get(recContext.getSessionId());
		if (queue != null) {
			queue.addMessage(koboldMessage);
			koboldMessage.setState(AbstractKoboldMessage.STATE_UN_FETCHED);
		}
		else {
			pendingMessages.put(koboldMessage.getReceiver(),
											  koboldMessage);
		}
	}
	
	
	/**
	 * Registers new queue for the given userContext.
	 * It's assumed that the given userContext has been logged in
	 * freshly. 
	 * @param userContext the user context.
	 */
	public void registerQueue(UserContext userContext) {
		MessageQueue queue =
				(MessageQueue) queues.get(userContext.getSessionId());
		if (queue == null) {
			queue = new MessageQueue(userContext);
			queues.put(userContext.getSessionId(),
							 queue);
		}
		
		for (Iterator it = pendingMessages.keySet().iterator(); it.hasNext(); )
		{
			String userName = (String) it.next();
			if (userName.equals(userContext.getUserName())) {
				queue.addMessage((AbstractKoboldMessage)pendingMessages.get(userName));
				//RTFM ;) causes ConcurrentModificationEx: pendingMessages.remove(userName);
				it.remove();
			}
		}
	}
	
	/**
	 * Unregisters queue of the given userContext.
	 * @param userContext
	 */
	public synchronized void unregisterQueue(UserContext userContext) {
		MessageQueue queue =
				(MessageQueue) queues.get(userContext.getSessionId());
		if (queue != null) {
			GlobalMessageContainer cont = GlobalMessageContainer.getInstance();
			
			// copy pending messages back to nologin receipients
			for (Iterator it = queue.getQueue().iterator(); it.hasNext(); ) {
				String id = (String) it.next();
				pendingMessages.put(userContext.getUserName(),
												  cont.getMessage(id));
			}
		}	
	}
	
	/**
	 * Serializes all queues and all pending messages.
	 */
	public synchronized void serialize() {
		serialize(this.messageStore);
	}
	
	/**
	 * Serializes all queues and all pending messages.
	 * 
	 * @param path the path to store.
	 */
	public synchronized void serialize(String path) {
		
		// first of all serialize all queues
		for (Iterator it = queues.values().iterator(); it.hasNext(); ) {
			MessageQueue queue = (MessageQueue) it.next();
			queue.serialize();
		}
		
		// next serialize pending messages
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold-server");

		Element pending = root.addElement("pending");

		for (Iterator it = this.pendingMessages.values().iterator(); it.hasNext();) {
			AbstractKoboldMessage koboldMessage = (AbstractKoboldMessage) it.next();
			pending.add(koboldMessage.serialize());
		}

		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.server.controller.MessageManager");
			log.error(e);
		}

	}

	/**
	 * Deserializes all pending messages.
	 */
	public synchronized void deserialize() {
		deserialize(this.messageStore);
	}
	
	/**
	 * Deserializes all pending messages.
	 * 
	 * @param path the store to read from.
	 */
	public synchronized void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
		} catch (DocumentException e) {
			Log log = LogFactory.getLog("kobold.server.controller.MessageManager");
			log.error(e);
		}
		
		if (document == null) {
			return;
		}
		
		List list = document.selectNodes( "/kobold-server/pending" );
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			AbstractKoboldMessage koboldMessage = AbstractKoboldMessage.createMessage(element);
			pendingMessages.put(koboldMessage.getReceiver(), koboldMessage);
		}
	}
	
	// DEBUG
	public void dummyMsg() {
		AbstractKoboldMessage koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("vato");
		koboldMessage.setSender("garbeam");
		koboldMessage.setMessageText("Kobold rulez");
		pendingMessages.put(koboldMessage.getReceiver(), koboldMessage);
		
		koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("schneipk");
		koboldMessage.setSender("vanto");
		koboldMessage.setMessageText("Kobold rulez");
		pendingMessages.put(koboldMessage.getReceiver(), koboldMessage);
		
		koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("garbeam");
		koboldMessage.setSender("schneipk");
		koboldMessage.setMessageText("Kobold rulez");
		pendingMessages.put(koboldMessage.getReceiver(), koboldMessage);
	}
	
}