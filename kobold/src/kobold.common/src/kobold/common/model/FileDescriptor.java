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
 * $Id: FileDescriptor.java,v 1.2 2004/06/22 11:29:15 vanto Exp $
 *
 */

package kobold.common.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

/**
 * @author garbeam
 */
public class FileDescriptor extends AbstractAsset {
    private DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSZ");
    
	private String path;
	private String revision;
	private Date lastChanged;
	private String lastAuthor;
	
	private List filedescs = new ArrayList();

	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public FileDescriptor (String name) {
		super(name);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public FileDescriptor (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = super.serialize();
		
		if (path != null) {
		    element.addAttribute("path", path);
		}

		if (revision != null) {
		    element.addAttribute("revision", revision);
		}

		if (lastAuthor != null) {
		    element.addAttribute("author", lastAuthor);
		}

		if (lastChanged != null) {
		    element.addAttribute("changed", dateFormat.format(lastChanged));
		}

		Element fds = element.addElement("filedescriptors");
		Iterator it = filedescs.iterator();
		while (it.hasNext()) {
            FileDescriptor fd = (FileDescriptor) it.next();
            fds.add(fd.serialize());
        }
		
		return element;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) 
	{
	    path = element.attributeValue("path");
	    revision = element.attributeValue("revision");
	    lastAuthor = element.attributeValue("author");
	    
	    String ch = element.attributeValue("changed");
	    if (ch != null) {
	        try {
                lastChanged = dateFormat.parse(ch);
            } catch (ParseException e) {
                lastChanged = null;
            }
	    }
	    
		Iterator it = element.element("filedescriptors").elementIterator(AbstractAsset.FILE_DESCRIPTOR);
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addFileDescriptor(new FileDescriptor(varEl));
		}
	}

	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return AbstractAsset.FILE_DESCRIPTOR;
	}

   
	/**
	 * Adds a new fileDescriptor.
	 *
	 * @param fileDescriptor contains the new fileDescriptor
	 */
	public void addFileDescriptor(FileDescriptor fileDescriptor) {
		filedescs.add(fileDescriptor);
		fileDescriptor.setParent(this);
	}

	public void removeFileDescriptor(FileDescriptor fd) {
	    filedescs.remove(fd);
	    fd.setParent(null);
	}
    
	public List getFileDescriptors()
	{
	    return Collections.unmodifiableList(filedescs);
	}
	
	/**
	 * @return Returns the lastAuthor.
	 */
	public String getLastAuthor() {
		return lastAuthor;
	}
	
	/**
	 * @param lastAuthor The lastAuthor to set.
	 */
	public void setLastAuthor(String lastAuthor) {
		this.lastAuthor = lastAuthor;
	}
	
	/**
	 * @return Returns the lastChangeDate.
	 */
	public Date getLastChanged() {
		return lastChanged;
	}
	
	/**
	 * Sets the date of the file revision
	 */
	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}
	
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return Returns the version.
	 */
	public String getRevision() {
		return revision;
	}
	
	/**
	 * @param version The version to set.
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}
	

}
