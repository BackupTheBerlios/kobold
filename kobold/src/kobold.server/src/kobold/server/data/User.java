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
 * $Id: User.java,v 1.1 2004/06/27 16:48:02 vanto Exp $
 *
 */

package kobold.server.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import kobold.common.data.ISerializable;
import kobold.common.data.Role;
import kobold.common.data.UserContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This class contains all information about a user that has to be stored
 * on the Kobold Server (password, roles...).
 *
 * @author Armin Cont
 */
public class User implements ISerializable {

	private Vector roles;
	private String userName;
	private String password;
	private String realName;

	/**
	 * Basic constructor.
	 */
	public User() {
		roles = new Vector();
	}
	
	/**
	 * Basic DOM constructor.
	 * 
	 * @param element DOM element which represents
	 * this user.
	 */
	public User(Element element) {
		roles = new Vector();
		deserialize(element);
	}

	/**
	 * @param element
	 */
	public void deserialize(Element element) {

		this.userName = element.selectSingleNode("//user/username").getText();

		this.realName = element.selectSingleNode("//user/realname").getText();

		try {
			this.password =
				new String(
					new BASE64Decoder().decodeBuffer(
						element.selectSingleNode("//user/password").getText()));
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.common.data.User");
			log.error(e);
		}

		List roles = element.selectNodes("//user/roles");
		for (ListIterator it = (ListIterator) this.roles.iterator();
			it.hasNext();
			)
		{
			Element roleElem = (Element) it.next();
			Role role = Role.createRole(roleElem);
			addRole(role);
		}
	}

	/**
	 * Serializes this User and returns the resulting DOM tree.
	 * @return
	 */

	public Element serialize() {
		Document document = DocumentHelper.createDocument();
		Element user = document.addElement("user");
		user.addElement("realname").addText(this.realName);
		user.addElement("username").addText(this.userName);
		user.addElement("password").addText(
				new BASE64Encoder().encode(this.password.getBytes()));

		Element roles = user.addElement("roles");
		for (Iterator it = (Iterator) this.roles.iterator();	it.hasNext(); ) {
			Role role = (Role) it.next();
			roles.add(role.serialize());
		}
        return user;
	}

	/**
	 * @return initial user context.
	 *
	 * @param sessionId the session id to use for initial
	 * user context.
	 */
	public UserContext getInitialUserContext(String sessionId) {
		return new UserContext(this.userName, sessionId);
	}

	/**
	 * Adds a new Role to this user's role list.
	 * 
	 * @param role the Role to add
	 */
	public void addRole(Role role) {
		roles.add(role);
	}

	/**
	 * Removes the passed Role form the user's role list. if the passed
	 * role is not part of the user's role list this method has no effect.
	 *
	 * @param r the role to remove
	 */
	public void removeRole(Role role) {
		roles.remove(role);
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @return
	 */
	public Vector getRoles() {
		return roles;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setRealName(String string) {
		realName = string;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string;
	}
}
