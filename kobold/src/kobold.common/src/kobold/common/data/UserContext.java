/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * $Id: UserContext.java,v 1.12 2004/11/05 10:50:56 grosseml Exp $
 */

package kobold.common.data;

import org.apache.log4j.Logger;

import java.net.URL;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Base class for user context information (session info).
 * This class provides information about the current user context
 * which is the session info.
 *
 * @author garbeam
 */
public class UserContext implements ISerializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserContext.class);

	// members
	private String userName;
	private String sessionId;
	private URL serverUrl = null;
	
    /**
     * DOM constructor for user contexts.
	 * @param element the DOM element represendting this user context.
	 */
	public UserContext(Element element) {
		deserialize(element);
	}


    /**
     * Default constructor, which provides basic info
     * about the user context.
     * This class could be used to determine more information
     * about the specific user context from the Kobold server,
     * e.g. roles, repository access data, etc.
     *
     * @param userName username of the users (like a Unix username).
     * @param sessionId the current session Id of the user context.
     */
    public UserContext(String userName, String sessionId)
    {
        this.userName = userName;
        this.sessionId = sessionId;
    }

    /**
     * @return Returns the username of this user context.  
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @return Returns the session id of this user context.
     */
    public String getSessionId() {
        return this.sessionId;
    }

	/**
	 * Sets sessionId.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * Serializes this object.
	 * 
	 * @return DOM Element representing this object.
	 */
	public Element serialize() {
		Element userContext = DocumentHelper.createElement("usercontext");
		userContext.addElement("username").addText(this.userName);
		userContext.addElement("session-id").addText(this.sessionId);
		
		return userContext;
	}
	
	/**
	 * Deserializes this object.
	 * @param element the DOM element representing this object.
	 */
	public void deserialize(Element element) {
		this.userName = element.elementText("username");
		this.sessionId = element.elementText("session-id");
	}
	
    public URL getServerUrl() {
        return serverUrl;
    }
    
    public void setServerUrl(URL serverUrl) {
        this.serverUrl = serverUrl;
    }
}