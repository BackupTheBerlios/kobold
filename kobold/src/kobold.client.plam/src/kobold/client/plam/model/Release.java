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
 * $Id: Release.java,v 1.8 2004/10/21 21:32:41 martinplies Exp $
 *
 */

package kobold.client.plam.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.common.data.ISerializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * TODO: Kopfkommentar
 * @author garbeam
 */
public class Release extends AbstractAsset{

    private List revisions = new ArrayList();
    private boolean released = false;
    public static final String GXL_TYPE = "http://kobold.berlios.de/types#Release";  
    

    public Release()
    {
        super();
    }
	
    public Release(String name)
    {
        super(name);
    }

    /**
	 * DOM constructor.
	 */
	public Release (AbstractAsset parent, Element element) {
		super();
		setParent(parent);
	    deserialize(element);
	}
	
	/**
	 * Adds a new fileDescriptor.
	 *
	 * @param filerev fileDescriptor contains the new fileDescriptor
	 */
	public void addFileRevision(FileRevision filerev) 
	{
		revisions.add(filerev);
	}
	
	public void removeFileRevision(FileRevision filerev) 
	{
	    revisions.remove(filerev);
	}
	
	public List getFileRevisions() 
	{
	    return Collections.unmodifiableList(revisions);
	}

    /**
     * @param released The released to set.
     */
    public void setReleased(boolean released)
    {
        this.released = released;
    }
    
    /**
     * Returns if the release is released or is in preparation mode.
     * @return Returns the released.
     */
    public boolean isReleased()
    {
        return released;
    }
    
	/**
	 * Serializes the product.
	 * @see kobold.client.plam.model.product.Product#serialize()
	 */
	public Element serialize() {
	    Element element = super.serialize();
	    element.addAttribute("released", "" + released);
	    
	    Element fdsEl = element.addElement("filerevisions");
	    
	    for (Iterator it = revisions.iterator(); it.hasNext();) {
            FileRevision fd = (FileRevision) it.next();
            fdsEl.add(fd.serialize());
        } 

		return element;
	}

	/**
	 * Deserializes this product.
	 */
	public void deserialize(Element element) {
	    super.deserialize(element);
	    Iterator it = element.element("filerevisions").elementIterator(FileRevision.TYPE);
	    released = element.attributeValue("released").equals(""+true);
	    
		while (it.hasNext()) {
		    Element fdEl = (Element)it.next();
		    addFileRevision(new FileRevision(fdEl));
		}
	}


	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() 
	{
		return AbstractAsset.RELEASE;
	}

	/**
	 * Provides a 2-tupel to represent a file revision.
	 * Contains path and revision of a file. 
	 */
	public static class FileRevision implements ISerializable 
	{
	    public static final String TYPE = "filerevision";
	    private String path, revision;
	    
	    public FileRevision(String path, String revision)
	    {
	        this.path = path;
	        this.revision = revision;
	    }
	    
        private FileRevision(Element element) 
        {
            deserialize(element);
        }
        
	    /**
         * @param path The path to set.
         */
        public void setPath(String path)
        {
            this.path = path;
        }
        
	    /**
         * @return Returns the path.
         */
        public String getPath()
        {
            return path;
        }
        
        /**
         * @param revision The revision to set.
         */
        public void setRevision(String revision)
        {
            this.revision = revision;
        }
        
        /**
         * @return Returns the revision.
         */
        public String getRevision()
        {
            return revision;
        }

        /**
         * @see kobold.common.data.ISerializable#serialize()
         */
        public Element serialize()
        {
            Element element = DocumentHelper.createElement(TYPE);
            if (path != null) {
                element.addElement("path").setText(path);
            }

            if (revision != null) {
                element.addElement("revision").setText(revision);
            }
            
            return element;
        }

        /**
         * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
         */
        public void deserialize(Element element)
        {
            path = element.elementTextTrim("path");
            revision = element.elementTextTrim("revision");
        }
	}

    /* (non-Javadoc)
     * @see kobold.client.plam.model.AbstractAsset#getGXLChildren()
     */
    public List getGXLChildren() {        
        return revisions;
    }

    /* (non-Javadoc)
     * @see kobold.client.plam.model.AbstractAsset#getGXLType()
     */
    public String getGXLType() {
        return GXL_TYPE;
    }
}
