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
 * $Id: UserManager.java,v 1.11 2004/07/07 15:40:52 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import kobold.server.data.User;

/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs). It's a
 * singleton class.
 */
public class UserManager {

    private static final Log log = LogFactory.getLog(UserManager.class);

	private HashMap users = null;
	private String userStore = "users.xml";
	
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
		this.userStore =
			System.getProperty("kobold.server.storePath") +
			System.getProperty("kobold.server.userStore");
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
	 */
	public void serialize() {
		serialize(this.userStore);
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
            users.add(user.serialize());
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
	 */
	public void deserialize() {
		deserialize(this.userStore);
	}
	
	/**
	 * Deserializes all users from the specified file.
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
			Element users = document.getRootElement().element("users");
			
			for (Iterator iterator = users.elementIterator("user"); iterator.hasNext(); ) {
			    Element element = (Element) iterator.next();
			    User user = new User(element);
			    this.users.put(user.getUserName(), user);
			}
		} catch (DocumentException e) {
			log.error(e);
		}
	}
	
	/**
	 * Returns list of {@see kobold.common.data.User} users.
	 */
	public List getAllUsers() {
	    List result = new ArrayList();
	    
	    for (Iterator iterator = users.values().iterator(); iterator.hasNext(); ) {
	        result.add((User) iterator.next());
	    }
	    
	    return result;
	}
	
	// DEBUG
	private void dummyUsers() {
	    addUser(new User("garbeam", "Anselm", "garbeam"));
	    addUser(new User("vanto", "Tammo", "vanto"));
	    addUser(new User("schneipk", "Patrick", "schneipk"));
	}
}
