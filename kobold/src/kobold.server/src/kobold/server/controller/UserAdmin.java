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
 * $Id: UserAdmin.java,v 1.1 2004/05/03 22:57:07 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import kobold.server.model.User;

/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs).
 *
 * @author Armin Cont
 */
public class UserAdmin {

	HashMap users;

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

	public Document serialize() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold");

		Element users = root.addElement("users");

		for (Iterator it = this.users.values().iterator(); it.hasNext();) {
			User user = (User) it.next();
			user.serialize(users);
		}

		return document;
	}

}
