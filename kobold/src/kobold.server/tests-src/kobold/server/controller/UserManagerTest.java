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
 * $Id: UserManagerTest.java,v 1.3 2004/09/23 13:43:14 vanto Exp $
 *
 */

package kobold.server.controller;

import junit.framework.TestCase;
import kobold.server.data.User;

/**
 * @author garbeam
 *
 * TestCase for UserManager.
 */
public class UserManagerTest extends TestCase {

	/**
	 * Constructor for UserManagerTest.
	 * @param arg0
	 */
	public UserManagerTest(String arg0) {
		
		super(arg0);
	}
	
	public void testSerialize() {
	
		System.setProperty("kobold.server.storePath", "");
		System.setProperty("kobold.server.userStore", "test-users.xml");
		
	    UserManager manager = UserManager.getInstance();
		
	    User martin = new User("pliesmn", "Martin", "pliesmn");
		manager.addUser(martin);
		User armin = new User("contan", "Armin", "contan");
		manager.addUser(armin);
		
		manager.removeUser(martin);
		manager.removeUser(armin);
	}

	public void testDeserialize() {
		System.setProperty("kobold.server.storePath", "");
		System.setProperty("kobold.server.userStore", "test-users.xml");

	    UserManager manager = UserManager.getInstance();
	    
	    assertTrue(manager.getUser("pliesmn").getPassword().equals("pliesmn"));
	    assertTrue(manager.getUser("contan").getPassword().equals("contan"));
	}
}
