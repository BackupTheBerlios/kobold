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
 * $Id: MessageManagerTest.java,v 1.2 2004/08/02 11:12:45 garbeam Exp $
 *
 */

package kobold.server.controller;

import java.util.Date;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;

import junit.framework.TestCase;
import kobold.common.data.KoboldMessage;
import kobold.server.controller.UserManager;
import kobold.server.data.User;

/**
 * @author garbeam
 *
 * TestCase for MessageManager.
 */
public class MessageManagerTest extends TestCase {

	/**
	 * Constructor for UserManagerTest.
	 * @param arg0
	 */
	public MessageManagerTest(String arg0) {
        super(arg0);
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());
	}
	
	public void testMesages() {

		System.setProperty("kobold.server.storePath", "");
		System.setProperty("kobold.server.userStore", "test-users.xml");

	    UserManager manager = UserManager.getInstance();
		
		manager.addUser(new User("pliesmn", "Martin", "pliesmn"));
		manager.addUser(new User("contan", "Armin", "contan"));
		manager.addUser(new User("schneipk", "Patrick", "schneipk"));
		manager.addUser(new User("vanto", "Tammo", "vanto"));
		manager.addUser(new User("garbeam", "Anselm", "garbeam"));
		
		MessageManager man = MessageManager.getInstance();
		
		KoboldMessage koboldMessage = new KoboldMessage();
		koboldMessage.setDate(new Date());
		koboldMessage.setSender("pliesmn");
		koboldMessage.setReceiver("contan");
		koboldMessage.setMessageText("Hallo");
		man.sendMessage(koboldMessage);
		System.getProperty("kobold.server.storePath");
		
		man.serialize();
		man.deserialize();
		
		KoboldMessage msg = (KoboldMessage) man.fetchMessage("contan");
	    assertTrue(msg.getMessageText().equals("Hallo"));
	}
	
}