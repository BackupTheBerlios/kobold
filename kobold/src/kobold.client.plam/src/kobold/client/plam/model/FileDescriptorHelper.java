/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * $Id: FileDescriptorHelper.java,v 1.1 2004/07/11 12:38:34 vanto Exp $
 *
 */
package kobold.client.plam.model;

import java.util.Date;


/**
 * Helper class for handling FileDescriptors.
 * 
 * @author Tammo
 */
public class FileDescriptorHelper
{
	/**
	 * Creates an implicit file.
	 *  
	 * @param filename
	 * @param directory
	 * @param revision
	 * @param lastChange
	 * @param isBinary
	 */
    public static FileDescriptor createFile(String filename, 
            	String revision, Date lastChange, boolean isBinary) 
    {
        FileDescriptor fd = new FileDescriptor();
    	fd.setFilename(filename);
    	//setDirectory(false);
    	fd.setRevision(revision);
    	fd.setLastChange(lastChange);
    	fd.setBinary(isBinary);
        return fd;
    }

    /**
     * Creates an implicit directory
     * @param filename
     */
    public static FileDescriptor createDirectory(String dirName) 
    {
        FileDescriptor fd = new FileDescriptor();
    	fd.setFilename (dirName);
    	fd.setDirectory(true);
        return fd;
    }

    /**
     * Removes recursivly all filedescriptors and its children from the given
     * Container and orphans them.
     * 
     * @param fdc
     */
    public static void clear(IFileDescriptorContainer fdc) 
    {
        for (int i = 0; i < fdc.getFileDescriptors().size(); i++) {
            FileDescriptor fd = (FileDescriptor)fdc.getFileDescriptors().get(i);
            fdc.removeFileDescriptor(fd);
            clear(fd);
        }
    }
}
