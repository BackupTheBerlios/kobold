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
 * $Id: UserManager.java,v 1.6 2004/05/15 14:45:22 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import kobold.common.data.Role;
import kobold.common.data.RoleP;
import kobold.common.data.RolePE;
import kobold.common.data.RolePLE;
import kobold.common.data.User;

/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs). It's a
 * singleton class.
 *
 * @author garbeam
 */
public class UserManager {

	HashMap users;
	
	static private UserManager instance;
	 
	static public UserManager getInstance() {
		 if (instance == null ) {
		 	 instance = new UserManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
	 * @param path
	 */
	private UserManager() {
		users = new HashMap();
		// DEBUG
		dummyUsers();
	}
	
	/**
	 * Adds a new user.
	 *
	 * @param username String containing the new user's username
	 */
	public void addUser(User user) {
		users.put(user.getUserName(), user);
	}


	/**
	 * @return <code>true</code> if the userContext is PLE.
	 * @param userName the user name.
	 */
	public boolean isPLE(String userName) {
		 User user = getUser(userName);
		 if (user != null) {
		 	List roles = user.getRoles();
			for (Iterator it = roles.iterator(); it.hasNext(); ) {
				Role role = (Role) it.next();
				if (role instanceof RolePLE) {
					return true;
				}
			}
		 }
		return false;
	}

	/**
		 * @return <code>true</code> if the userContext is PE.
		 * @param userName the user name.
		 */
	public boolean isPE(String userName) {
		User user = getUser(userName);
		if (user != null) {
			List roles = user.getRoles();
			for (Iterator it = roles.iterator(); it.hasNext();) {
				Role role = (Role) it.next();
				if (role instanceof RolePE) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Changes the stored information for the user specified in info.
	 *
	 * @param username the name of the User.
	 */
	public User getUser(String username) {
		return (User) users.get(username);
	}

	/**
	 * Removes the specified user.
	 */
	public void removeUser(User user) {
		users.remove(user);
	}

	/**
	 * Serializes all users with its roles to the file specified
	 * by path.
	 * 
	 * @param path the file to serialize all users.
	 */
	public void serialize(String path) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold");

		Element users = root.addElement("users");

		for (Iterator it = this.users.values().iterator(); it.hasNext();) {
			User user = (User) it.next();
			user.serialize(users);
		}

		 XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.server.controller.UserAdmin");
			log.error(e);
		}

	}

	/**
	 * Deserializes all users from the specified file.
	 * 
	 * @param path - file where to read from.
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
		} catch (DocumentException e) {
			Log log = LogFactory.getLog("kobold.server.controller.UserAdmin");
			log.error(e);
		}
		
		List list = document.selectNodes( "/users/user" );
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			User user = new User(element);
			users.put(user.getUserName(), user);
		}
	}
	
	// DEBUG
	private void dummyUsers() {
		User anselm = new User();
		anselm.setUserName("garbeam");
		anselm.setRealName("Anselm R. Garbe");
		anselm.setPassword("garbeam");
		anselm.addRole(new RolePLE("kobold2"));
		anselm.addRole(new RolePE("kobold server"));
		anselm.addRole(new RoleP("kobold server"));
		
		User tammo = new User();
		tammo.setUserName("vanto");
		tammo.setRealName("Tammo van Lessen");
		tammo.setPassword("vanto");
		tammo.addRole(new RolePLE("kobold3"));
		tammo.addRole(new RolePE("kobold client"));
		tammo.addRole(new RoleP("kobold client"));
		
		User patrick = new User();
		patrick.setUserName("schneipk");
		patrick.setRealName("Patrick Schneider");
		patrick.setPassword("schneipk");
		patrick.addRole(new RolePLE("kobold4"));
		patrick.addRole(new RolePE("kobold vcm"));
		patrick.addRole(new RoleP("kobold vcm"));
		
		addUser(anselm);
		addUser(tammo);
		addUser(patrick);
	}
}