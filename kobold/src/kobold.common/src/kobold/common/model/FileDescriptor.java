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
 * $Id: FileDescriptor.java,v 1.1 2004/06/21 21:03:54 garbeam Exp $
 *
 */

package kobold.common.model;

import kobold.common.model.*;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author garbeam
 */
public class FileDescriptor extends AbstractAsset {


	//the config
	String path;
	String version;
	String lastChangeDate;
	String lastAuthor;
	
	
	//or another fileDescriptor for a directory
	private FileDescriptor fileDescriptor;

	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public FileDescriptor (String fileDescriptorName) {
		super();
		setName (fileDescriptorName);
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
		Element fdElement = DocumentHelper.createElement("fd");
		fdElement.addText(getName ());
		
		if (fileDescriptor != null)
		{
			//now all fd'S
			//Element fdElement2 = fdElement.addElement ("fds");
		
			//serialize the fd
			if  (this.path != null) fdElement.addElement("path").addText(this.path);
			fdElement.add (fileDescriptor.serialize ());
		}
		else
		{
			if (this.path != null) fdElement.addElement("path").addText(this.path);
			if (this.version != null) fdElement.addElement("version").addText(this.version);
			if (this.lastChangeDate != null) fdElement.addElement("lastChangeDate").addText(this.lastChangeDate);
			if (this.lastAuthor != null) fdElement.addElement("lastAuthor").addText(this.lastAuthor);
		}		
		return fdElement;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element product = element.element("product");
		setName (element.getText ());
		//this.productLineName = element.elementText("productline");
	}

	/**
	 * @return name of the dependent productline.

	public String getDependsName() {
		return productLineName;
	}
	 */


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
		this.fileDescriptor = fileDescriptor;
		//set parent
		fileDescriptor.setParent(this);

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
	public String getLastChangeDate() {
		return lastChangeDate;
	}
	/**
	 * @param lastChangeDate The lastChangeDate to set.
	 */
	public void setLastChangeDate(String lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
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
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	

}
