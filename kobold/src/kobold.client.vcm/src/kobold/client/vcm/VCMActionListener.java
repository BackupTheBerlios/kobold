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
 * $Id: VCMActionListener.java,v 1.4 2004/09/22 13:58:12 garbeam Exp $
 *
 */
package kobold.client.vcm;

import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;
import kobold.client.vcm.controller.StatusUpdater;

/**
 * @author Tammo
 */
public class VCMActionListener implements IVCMActionListener
{

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#refreshFiledescriptors(kobold.client.plam.model.IFileDescriptorContainer)
     */
    public void refreshFiledescriptors(IFileDescriptorContainer container)
    {
		StatusUpdater st = new StatusUpdater();
		st.updateFileDescriptors(container);
		//System.out.println("TEST STATUS");
    }

    /* (non-Javadoc)
     * @see kobold.client.plam.listeners.IVCMActionListener#checkoutProductline(kobold.client.plam.model.AbstractRootAsset)
     */
    public void updateProductline(AbstractRootAsset rootAsset)
    {
		KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
		try
		{
			AbstractAsset tmpAsset[] = {rootAsset};
			repoAccess.checkout(tmpAsset, null, null ,true);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProductline(kobold.client.plam.model.AbstractRootAsset)
     */
    public void commitProductline(AbstractRootAsset rootAsset) {
        // TODO Auto-generated method stub
        
    }

}
