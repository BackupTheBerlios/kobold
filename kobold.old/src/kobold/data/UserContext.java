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
 * $Id: UserContext.java,v 1.4 2004/03/30 23:49:09 vanto Exp $
 */

package kobold.data;

import java.lang.String;

/**
 * Base class for user context information (session info).
 * This class provides information about the current user context
 * which is the session info.
 *
 * @author garbeam
 */
public class UserContext{

    /**
     * Default constructor, which provides basic info
     * about the user context.
     * This class could be used to determine more information
     * about the specific user context from the Kobold server,
     * e.g. roles, repository access data, etc.
     *
     * @param username username of the users (like a Unix username).
     * @param sessionId the current session Id of the user context.
     */
    public UserContext(String username,
                       String sessionId)
    {
        this.username = username;
        this.sessionId = sessionId;
    }

    /**
     * @returns the username of this user context.  
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @returns the session id of this user context.
     */
    public String getSessionId() {
        return this.sessionId;
    }

    // members

    private String username;
    private String sessionId;
}
