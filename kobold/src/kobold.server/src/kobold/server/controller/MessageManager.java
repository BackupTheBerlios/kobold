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
 * $Id: MessageManager.java,v 1.10 2004/11/05 10:50:56 grosseml Exp $
 *
 */
package kobold.server.controller;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.KoboldMessage;
import kobold.common.data.User;

/**
 * This singelton class serializes and manages all the Kobold messages on the
 * server. Messages are oranized in message queues which are linked to their
 * respective users by this class.
 *
 * @see kobold.server.controller.MessageQueue
 *
 * @author garbeam
 */
public class MessageManager {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MessageManager.class);

	private Map queues = null;
	
	static private MessageManager instance;
	 
	static public synchronized MessageManager getInstance() {
		 if (instance == null ) {
		 	 instance = new MessageManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
	 * TODO: remove the dummy-call before delivery 
	 */
	private MessageManager() {
		queues = new HashMap();
		deserialize();
		// DEBUG
		dummyMsg();
	}

	/**
	 * @return next message for the given username.
	 * @param username
	 */
	public synchronized AbstractKoboldMessage fetchMessage(String username) {
		MessageQueue queue =
			(MessageQueue) queues.get(username);
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
	 * Invalidates the given KoboldMessage for the given username.
	 * 
	 * @param username the username.
	 * @param koboldMessage the message to invalidate. 
	 */	
	public synchronized void invalidateMessage(String username, 
											   AbstractKoboldMessage koboldMessage)
	{
	    if (username.equals(koboldMessage.getReceiver())) {
	        MessageQueue queue = (MessageQueue) queues.get(username);
		    if (queue != null) {
		        queue.removeMessage(koboldMessage);
		        koboldMessage.setState(AbstractKoboldMessage.STATE_INVALID);
		    }
		}
	}
	
	/**
	 * Sends a message to the specified receipient.
	 * 
	 * @param koboldMessage
	 */
	public synchronized void sendMessage(AbstractKoboldMessage koboldMessage)
	{
		MessageQueue queue =
				(MessageQueue) queues.get(koboldMessage.getReceiver());
		if (queue != null) {
			queue.addMessage(koboldMessage);
			koboldMessage.setState(AbstractKoboldMessage.STATE_UN_FETCHED);
		}
		serialize();
	}
	
	/**
	 * Serializes all queues and all pending messages.
	 */
	public synchronized void serialize() {
		
		// first of all serialize all queues
		for (Iterator it = queues.values().iterator(); it.hasNext(); ) {
			MessageQueue queue = (MessageQueue) it.next();
			queue.serialize();
		}
	}

	/**
	 * Deserializes all pending messages.
	 */
	public synchronized void deserialize() {
	    
	    UserManager manager = UserManager.getInstance();
	    List users = manager.getAllUsers();
	    
	    queues.clear();
	    for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
	        User user = (User) iterator.next();
	        queues.put(user.getUsername(), new MessageQueue(user.getUsername()));
	    }
	}
	
	// DEBUG
	public void dummyMsg() {
		AbstractKoboldMessage koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("vanto");
		koboldMessage.setSender("garbeam");
		koboldMessage.setSubject("About Kobold");
		koboldMessage.setMessageText("Kobold rulez");
		sendMessage(koboldMessage);
		
		koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("schneipk");
		koboldMessage.setSender("vanto");
		koboldMessage.setSubject("About Kobold");
		koboldMessage.setMessageText("Kobold rulez");
		sendMessage(koboldMessage);
		
		koboldMessage = new KoboldMessage();
		koboldMessage.setReceiver("garbeam");
		koboldMessage.setSender("schneipk");
		koboldMessage.setSubject("About Kobold");
		koboldMessage.setMessageText("Kobold rulez");
		sendMessage(koboldMessage);
	}
	
}