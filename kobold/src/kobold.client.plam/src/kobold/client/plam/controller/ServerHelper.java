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
 * $Id: ServerHelper.java,v 1.2 2004/08/02 14:36:03 vanto Exp $
 *
 */
package kobold.client.plam.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kobold.client.plam.KoboldProject;
import kobold.client.plam.PLAMProject;
import kobold.common.data.Productline;
import kobold.common.data.UserContext;


/**
 * Provides static helper functions to communicate with the server.
 * @author Tammo
 */
public class ServerHelper
{
    public static final Log logger = LogFactory.getLog(ServerHelper.class);
    
    public static UserContext getUserContext(KoboldProject proj) {
        return SecureKoboldClient.getInstance().login(proj.getServerURL(), proj.getUserName(), proj.getPassword());
    }
    
    /**
     * Retrieves the Server-Productline instance for the {@link PLAMProject}
     * Returns null if PL doesnt exist or login fails.
     * 
     * @param p
     * @param plName
     * @return
     */
    public static Productline fetchProductline(KoboldProject p) 
    {
    	Productline pl = null;

		UserContext context = getUserContext(p);
		if (context != null) {
		    pl = SecureKoboldClient.getInstance().getProductline(context, p.getProductlineId());
		} else {
		    logger.warn("Could not fetch Productline due to a login failure");
		}
		
        return pl;
    }

    /**
     * @param project
     */
    public static List fetchAllUsers(KoboldProject p)
    {
        List users = null;

		UserContext context = getUserContext(p);
		if (context != null) {
		    users = SecureKoboldClient.getInstance().getAllUsers(context);
		} else {
		    logger.warn("Could not fetch user list due to a login failure");
		}

		return users;
    }
}
