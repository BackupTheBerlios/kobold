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
 * $Id: IFileDescriptorContainer.java,v 1.10 2004/08/05 18:17:07 garbeam Exp $
 *
 */
package kobold.client.plam.model;

import java.util.List;

import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.runtime.IPath;


/**
 * @author Tammo
 */
public interface IFileDescriptorContainer
{
	/**
	 * Adds a filedescriptor.
	 *
	 * @param filedescriptor to add
	 */
    void addFileDescriptor(FileDescriptor fd);

    /**
	 * Removes a filedescriptor.
	 *
	 * @param filedescriptor to remove
	 */
    void removeFileDescriptor(FileDescriptor fd);
    
    /**
     * Returns an unmodifiable list of filedescriptors.
     * 
     * @return list of filedescriptors
     */
    List getFileDescriptors();
    
    /**
     * 
     * @param name, the name of the fd
     * @return the fd
     */
    FileDescriptor getFileDescriptor (String name);
   
    
    /**
     * Returns the remote repository descriptor.
     * @return
     */
    RepositoryDescriptor getRemoteRepository();
    
    /**
     * Returns the local path of this.
     * @return
     */
    IPath getLocalPath();
    
    /**
     * Retruns the root Asset.
     * @return
     */
    AbstractRootAsset getRoot();

    void clear();
}
