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
 * $Id: ProductComponent.java,v 1.4 2004/07/22 10:28:55 rendgeor Exp $
 *
 */
package kobold.client.plam.model.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IGXLExport;

import org.dom4j.Element;
import org.eclipse.core.resources.IFolder;

/**
 * @author rendgeor
 */
public abstract class ProductComponent extends AbstractMaintainedAsset 
									   implements IGXLExport,
									   			  IFileDescriptorContainer {

	private List prodComps = new ArrayList();
	
	public ProductComponent() {
		super("unnamed");
	}

	public ProductComponent(String name) {
		super(name);
	}
	
	public void addProductComponent(ProductComponent comp)
	{
		prodComps.add(comp);
		comp.setParent(this);
	}
	
	public void removeProductComponent(ProductComponent comp)
	{
		prodComps.remove(comp);
	}
	
	public List getProductComponents()
	{
		return Collections.unmodifiableList(prodComps);
	}
	
	
	/**
	 * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.elementIterator("product-components");
		while (it.hasNext()) {
			Element el = (Element)it.next();
			addProductComponent(createProductComponent(el));
		}
	}
	
	/**
	 * @see kobold.common.data.ISerializable#serialize()
	 */
	public Element serialize() {
		Element element = super.serialize();
		Element childrenEl = element.addElement("product-components");
		
		Iterator it = prodComps.iterator();
		while (it.hasNext()) {
			ProductComponent comp = (ProductComponent) it.next();
			childrenEl.add(comp.serialize());
		}
		
		return element;
	}
	
	public static ProductComponent createProductComponent(Element element)
	{
		if (element.getName().equals(AbstractAsset.RELATED_COMPONENT)) {
			return new RelatedComponent(element);
		} else if (element.getName().equals(AbstractAsset.SPECIFIC_COMPONENT)) {
			return new SpecificComponent(element);
		}
		throw new IllegalArgumentException("Unkown product component type");
	}

    private List filedescs = new ArrayList();

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#addFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void addFileDescriptor(FileDescriptor fd)
    {
        filedescs.add(fd);
        fd.setParentAsset(this);
    }

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#removeFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void removeFileDescriptor(FileDescriptor fd)
    {
        filedescs.remove(fd);
        fd.setParentAsset(null);
    }

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#getFileDescriptors()
     */
    public List getFileDescriptors()
    {
        return Collections.unmodifiableList(filedescs);
    }
    
    /**
     * returns the fd with the name
     */
    public FileDescriptor getFileDescriptor (String name)
	{
		for (Iterator it = filedescs.iterator(); it.hasNext();) {
			FileDescriptor fd = (FileDescriptor) it.next();
			if (fd.getFilename().equals(name)) {
				return fd;
			}
		}	
		System.out.println ("fd "+name + " not found!");
		return null;
	}

    
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	public IFolder getLocalPath() {
		//FIXME: calc localpath
		return null;
	}
}
