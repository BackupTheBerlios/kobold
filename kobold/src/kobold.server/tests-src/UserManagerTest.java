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
 * $Id: UserManagerTest.java,v 1.2 2004/05/06 23:30:23 garbeam Exp $
 *
 */

import junit.framework.TestCase;
import kobold.common.data.RoleP;
import kobold.common.data.RolePE;
import kobold.common.data.RolePLE;
import kobold.common.data.User;
import kobold.server.controller.UserManager;

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
		
		User user = new User();
		user.setUserName("garbeam");
		user.setRealName("Anselm Garbe");
		user.setPassword("halloballo");
		user.addRole(new RoleP());
		user.addRole(new RolePE());
		user.addRole(new RolePLE());
		
		UserManager manager = UserManager.getInstance();
		
		manager.addUser(user);
		manager.serialize("test.xml");
		
		manager.removeUser(user);
		manager.deserialize("test.xml");
		
		user = manager.getUser("garbeam");
		assertTrue(user.getPassword() == "halloballo");
		assertTrue(user.getRoles().size() == 3);
		
		
	}

}
