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
 * $Id: IVCMActionListener.java,v 1.11 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.listeners;

import org.apache.log4j.Logger;

import java.util.List;

import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Productline;

import org.eclipse.core.resources.IProject;


/**
 * Provides an interface to all vcm action. Due to information hiding issues,
 * the VCM Plugin should implement this listener and has to register itself to
 * all active productlines, which invoke action via this interface.
 * 
 * @author Tammo
 */
public interface IVCMActionListener
{
	/**
	 * Logger for this class
	
	private static final Logger logger = Logger
			.getLogger(IVCMActionListener.class);
     */
    /**
     * updates a Release of a RealatedComponent
     * @param rc   Release that should be updates
     * @param newRelase Release of the productline 
     */
    void updateRelease(RelatedComponent rc, Release newRelase);
    
    
    /**
     * Refreshes all filedescriptors in this container.
     * 
     * @param container
     */
    void refreshFiledescriptors(IFileDescriptorContainer container);
    
    /**
     * Update server asset.
     * Used by server side productline and products.
     * @param asset
     * @param p the project
     */
    void updateAsset(kobold.common.data.Asset asset, IProject p);
    
    /**
     * Commit productline.
     * 
     * @param pl
     */
    void commitProductline(Productline pl);
    
    /**
     * Commit productline.
     * 
     * @param product
     */
    void commitProduct(Product product);
    
    /**
     * Tags a release.
     * @param release the release containing all necessary info.
     */
    void tagRelease(Release release);
 
    /**
     * Adds a all FileDescriptors.
     * @param asset AbstractRootAsset
     * @param fds List of FileDescriptors to add.
     */
    void addFileDescriptors(AbstractRootAsset asset, List fds);
    
}