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
 * $Id: FileDescriptorHelper.java,v 1.12 2004/10/21 21:32:41 martinplies Exp $
 *
 */
package kobold.client.plam.model;

import java.util.Date;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IPath;


/**
 * Helper class for handling FileDescriptors.
 * It's used for creating fds for files and directories automatically
 * 
 * @author Tammo
 */
public class FileDescriptorHelper
{
	/**
	 * Creates a file FileDescriptor for a file and all directories in which the file is included.
	 * @param filename the filename to create
	 * @param revision the revision of the file
	 * @param lastChange the last changed date of the file
	 * @param isBinary if the file is a binary one
	 * @param root the root fd-container
	 */
    public static void createFile(String filename,  String revision, 
								  Date lastChange,  boolean isBinary, 
								  IFileDescriptorContainer root) 
    {
        if (filename.equals(".")) {
            return;
        }
        IFileDescriptorContainer fd = root;
        StringTokenizer tz = new StringTokenizer(filename, ""+IPath.SEPARATOR);
        int toks = tz.countTokens();
        toks--; // prevent last token from processing, cause it's the filename;-)
        for (int i = 0; i < toks; i++) {
             String resource = tz.nextToken();
             FileDescriptor tmp = fd.getFileDescriptor(resource);
             
             //create the prefix directories of the file
             if (tmp == null) {
                 tmp = new FileDescriptor();
                 tmp.setFilename(resource);
                 tmp.setDirectory(true);
                 fd.addFileDescriptor(tmp);
             }
             //set the fd to the new added fd
             fd = tmp;
        }
        FileDescriptor fileDescriptor = new FileDescriptor();
        if (tz.hasMoreTokens()) {
        	fileDescriptor.setFilename(tz.nextToken());
        }
        else {
            // special case, appears only if filename argument contained no delimeters
            fileDescriptor.setFilename(filename);
        }
    	fileDescriptor.setRevision(revision);
    	fileDescriptor.setLastChange(lastChange);
    	fileDescriptor.setBinary(isBinary);
    	fileDescriptor.setLastChange(lastChange);
    	fd.addFileDescriptor (fileDescriptor);
    }

	/**
	 * Creates a file FileDescriptor for a directory and all subdirectories in which the new file is included.
	 * @param dirName the directory name
	 * @param root the root filedescriptor
     * 
     */
    public static void createDirectory(String dirName,  IFileDescriptorContainer root) 
    {
        if (dirName.equals(".")) {
            return;
        }
        IFileDescriptorContainer fd = root;
        StringTokenizer tz = new StringTokenizer(dirName, ""+IPath.SEPARATOR);
        while (tz.hasMoreTokens()) {
             String resource = tz.nextToken();
             if (resource.equals(".") || resource.equals("")) {
                 continue;
             }
             FileDescriptor tmp = fd.getFileDescriptor(resource);
             if (tmp == null) {
                 tmp = new FileDescriptor();
                 tmp.setFilename(resource);
                 tmp.setDirectory(true);
                 fd.addFileDescriptor(tmp);
             }
             fd = tmp;
        }
    }
    
}
