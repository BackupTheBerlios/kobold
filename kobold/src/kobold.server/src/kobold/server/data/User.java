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
 * $Id: User.java,v 1.8 2004/11/05 10:50:56 grosseml Exp $
 *
 */

package kobold.server.data;

import org.apache.log4j.Logger;

import java.io.IOException;

import kobold.common.data.ISerializable;
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
 * on the Kobold server (password, roles...).
 *
 * @author Armin Cont
 */
public class User implements ISerializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(User.class);

	private static final Log log = LogFactory.getLog(User.class);

	private String userName;
	private String password;
	private String fullName;
	private boolean isValid;

	/**
	 * Basic constructor.
	 */
	public User(String userName, String fullName, String password) {
	    this.userName = userName;
	    this.fullName = fullName;
	    this.password = password;
	    this.isValid = true;
	}
	
	/**
	 * Basic DOM constructor.
	 *
	 * @param element DOM element which represents this user.
	 */
	public User(Element element) {
		deserialize(element);
	}

	/**
     * deserializes the data which is contained in the passed DOM element and
     * updates 'this accordingly
     * 
	 * @param element DOM element containing the data to be deserialized and
     *                applied 
	 */
	public void deserialize(Element element) {

	    this.userName = element.elementText("username");
	    this.fullName = element.elementText("fullname");
	    this.isValid = element.element("isvalid").equals("yes");
	    
		try {
			this.password =
				new String(
					new BASE64Decoder().decodeBuffer(
						element.elementText("password")));
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Serializes this User and returns the resulting DOM tree.
	 * @return DOM Element containing the serialized data of this User 
	 */

	public Element serialize() {
		Document document = DocumentHelper.createDocument();
		Element user = document.addElement("user");
		user.addElement("fullname").addText(this.fullName);
		user.addElement("username").addText(this.userName);
		user.addElement("password").addText(
				new BASE64Encoder().encode(this.password.getBytes()));
		user.addElement("isvalid").addText(isValid ? "yes" : "no");

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
	 * Returns the decoded password of this user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the full name of this user.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns the username of this user.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the password of this user.
	 * @param password the decoded password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the full name of this user
	 * @param fullName the real name
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Sets the username of this user.
	 * @param userName the username
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Sets the validation info of this user.
	 * @param valid the validation info.
	 */
	public void setValid(boolean valid) {
	    this.isValid = valid;
	}
	
	/**
	 * Returns the validation info of this user.
	 */
	public boolean isValid() {
	    return this.isValid;
	}

    /**
     * Creates and returns a kobold.common.data.User object that represents this
     * User in a secure fashion (-> without the User's password or server 
     * specific data).
     * 
     * @return kobold.common.data.User representation of this User
     */
    public kobold.common.data.User getSecureRepresentation(){
        return new kobold.common.data.User(getUserName(), getFullName());
    }
}