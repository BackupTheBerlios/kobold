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
 * $Id: AbstractMaintainedAsset.java,v 1.12 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.common.data.User;
import kobold.common.exception.GXLException;
import net.sourceforge.gxl.GXLBag;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;


/**
 * Extends AbstractAsset with maintainer management. 
 * 
 * @author Tammo
 */
public abstract class AbstractMaintainedAsset extends AbstractAsset
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AbstractMaintainedAsset.class);

	private List maintainers = new ArrayList();
	
    public AbstractMaintainedAsset() 
    {
        super();        
    }
    
    public AbstractMaintainedAsset(String name) 
    {
        super(name);
    }


	public void addMaintainer(User user)
	{
	    AbstractRootAsset root = getRoot();
	    User listedUser = (User)root.getKoboldProject().getUserPool().get(user.getUsername());
	    if (listedUser == null) {
	        logger.warn("User not in UserPool -> User has not been added.");
	    } else {
	        maintainers.add(listedUser);
	        firePropertyChange(ID_DATA, null, user);
	    }
	}
	
	public void removeMaintainer(User user)
	{
	    maintainers.remove(user);
	    firePropertyChange(ID_DATA, null, user);
	}
	
	public List getMaintainers()
	{
	    return maintainers;
	}
	
	public GXLNode createGXLGraph(Map nodes) throws GXLException {
        GXLNode node = super.createGXLGraph(nodes);
        GXLBag bag = new GXLBag();
        for (Iterator ite = maintainers.iterator(); ite.hasNext();){
            User user = (User) ite.next();
            bag.add(new GXLString(user.getUsername())); 
        }
        node.setAttr("maintainer" ,bag);
	    return node;	    	
	}
	
}
