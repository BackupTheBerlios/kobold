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
 * $Id: AbstractMaintainedAsset.java,v 1.1 2004/06/27 23:52:28 vanto Exp $
 *
 */
package kobold.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import kobold.common.data.User;


/**
 * Extends AbstractAsset with maintainer management. 
 * 
 * @author Tammo
 */
public abstract class AbstractMaintainedAsset extends AbstractAsset
{

	private List maintainers = new ArrayList();
	
    public AbstractMaintainedAsset() 
    {
        super();
    }
    
	/**
     * @param name
     */
    public AbstractMaintainedAsset(String name)
    {
        super(name);
    }
    
    /**
	 * @see kobold.common.model.IMaintainerContainer#addMaintainer(kobold.common.data.User)
	 */
	public void addMaintainer(User user)
	{
	    AbstractRootAsset root = getRoot();
	    User listedUser = (User)root.getUserPool().get(user.getUsername());
	    if (listedUser == null) {
	        root.getUserPool().put(user.getUsername(), user);
	        listedUser = user;
	    }
	    
	    maintainers.add(listedUser);
	}
	
	/**
	 * @see kobold.common.model.IMaintainerContainer#removeMaintainer(kobold.common.data.User)
	 */
	public void removeMaintainer(User user)
	{
	    maintainers.remove(user);
	}
	
	/**
	 * @see kobold.common.model.IMaintainerContainer#getMaintainers()
	 */
	public List getMaintainers()
	{
	    return Collections.unmodifiableList(maintainers);
	}

    public void deserialize(Element element)
    {
        super.deserialize(element);
        
    	if (element.element("maintainers") != null) {
    	    Iterator it = element.element("maintainers").elementIterator("maintainer");
    	    while (it.hasNext()) {
    	        Element maintEl = (Element)it.next();
    	        addMaintainer(new User(maintEl.attributeValue("username")));
    	    }
    	}
    }
    
    public Element serialize()
    {
        Element element = super.serialize();
        Element maintsEl = element.addElement("maintainers");
        Iterator it = maintainers.iterator();
        while (it.hasNext()) {
            maintsEl.addElement("maintainer").addAttribute("username", 
                	((User)it.next()).getUsername());
        }

        return element;
    }
}
