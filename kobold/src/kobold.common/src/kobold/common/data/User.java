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
 * $Id: User.java,v 1.11 2004/06/27 18:52:10 vanto Exp $
 *
 */
package kobold.common.data;


import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * Provides a user representation on the client side.
 * 
 * Implements ISerializable to make the tranport through XMLRPC possible.
 * 
 * @author Tammo
 */
public class User implements ISerializable
{
    private String username;
    private String fullname;
    
    
    public User(String username, String fullname)
    {
        this.username = username;
        this.fullname = fullname;
    }
    
    /**
     * @param fullname The fullname to set.
     */
    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }
    
    /**
     * @return Returns the fullname.
     */
    public String getFullname()
    {
        return fullname;
    }
    
    /**
     * @param username The username to set.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @see kobold.common.data.ISerializable#serialize()
     */
    public Element serialize()
    {
        Element user = DocumentHelper.createElement("user");
        if (username != null) {
            user.addAttribute("username", username);   
        }
        
        if (fullname != null) {
            user.addAttribute("fullname", fullname);
        }
        
        return user;
    }

    /**
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element)
    {
       username = element.attributeValue("username");
       fullname = element.attributeValue("fullname");
    }
}
