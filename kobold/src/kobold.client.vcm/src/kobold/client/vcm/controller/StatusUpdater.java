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
 * $Id: StatusUpdater.java,v 1.22 2004/08/06 09:53:06 memyselfandi Exp $
 * 
 */
package kobold.client.vcm.controller;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.FileDescriptorHelper;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.*;
import kobold.client.vcm.preferences.VCMPreferencePage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author rendgeor
 * 
 * The class updates the FD(s) included in the delivered FD-container.
 * It works as listener and acts when something in the model change or a refresh is executed.
 * 
 */

public class StatusUpdater {

    //private DateFormat df = new SimpleDateFormat();
    private static final Log logger = LogFactory.getLog(StatusUpdater.class);
    
	/**
	 * Listener who acts on the delivered FD-container and updates all included FD(s)
	 * @param fileDescriptorContainer, the FD-container to update
	 */
	public void updateFileDescriptors(IFileDescriptorContainer fileDescriptorContainer)
	{

		//command line command with the stats script to the changed part of the meta-data containing FD(s)
		String[] command = {"perl", getScriptPath() + 
							"stats.pl", fileDescriptorContainer.getLocalPath().toOSString()};
	    logger.debug("updateFileDescriptors: " + command[0] + command[1] + command[2]);
		//process the connection (open and parse the input)
		processConnection(command, fileDescriptorContainer);
	}
	
	public void processConnection (String[] command,
									IFileDescriptorContainer fileDescriptorContainer)
	{
		ScriptServerConnection conn = new ScriptServerConnection("noUser");


		try 
		{
			String iString="";
			iString = conn.open(command,"");
			//conn.open(new NullProgressMonitor(), command);
			System.out.println(iString);
			conn.close();
			parseInputString(fileDescriptorContainer,iString);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Gets the path of the script(s)
	 * @return the script-path of the commandLine scripts
	 */
	private String getScriptPath ()
	{
		KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
		if (plugin != null)
		{
		    return new Path(plugin.getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_SCRIPT_LOCATION)
		                    + IPath.SEPARATOR).toOSString();
		}
		else {
			logger.debug("returned plugin still NULL");
			return null;
		}
	}
	
	/**
	 * Parses the inputString
	 * @param inputString, the inputString(all files and directories) to parse
	 * @param fileDescriptorContainer, a reference to the part who has FD(s) to update
	 */
	private void parseInputString (IFileDescriptorContainer fileDescriptorContainer, 
									String inputString)
	{
		System.out.println (inputString);
		
		//clears all FD(s)
        fileDescriptorContainer.clear();

		//for the complete output of stats
		java.util.StringTokenizer line = new java.util.StringTokenizer(inputString, "\n");
		while(line.hasMoreTokens()) 
		{ 
		    //for each line (divided by tabs)
		    java.util.StringTokenizer localLine = new java.util.StringTokenizer(line.nextToken(), "\t");
		    while(localLine.hasMoreTokens()) 
		    { 
		    
		        //it's a directory
		        if (localLine.countTokens() == 1)
		        {
		        	FileDescriptorHelper.
							createDirectory(localLine.nextToken(), fileDescriptorContainer);
          
		        }

		        else
		        {
		        	/*1*/String filename = localLine.nextToken();
		        	/*2*/String revision = localLine.nextToken();
		        	
		        	//not checked in directory or file
		            if (revision.endsWith("*")) {
		                // not managed yet by version control system
		                if (revision.equals("*")) {
        		             FileDescriptorHelper.createFile(filename, "", null, true,
        		                    						 fileDescriptorContainer);
		                }
		                else if (revision.equals("T*")) {
                             FileDescriptorHelper.createFile(filename, "", null, false,
        		                    						 fileDescriptorContainer);
		                }
		                else {
		                     FileDescriptorHelper.createDirectory(filename, fileDescriptorContainer);
		                }
		                
		            }
			        //it's a file
		            else {
		            	Date date = null;
		            	if(localLine.hasMoreTokens())
		            	{   		            
		            		date = new Date(Long.parseLong(localLine.nextToken()));
    		            	/*3*/
    		            	//date = df.parse(localLine.nextToken());
		            	}
    		  
    		            boolean isBinary = false;
    		            if (localLine.hasMoreTokens()) {
                        /*4*/isBinary = localLine.nextToken().equals("binary");                    
    		            }
    		            
		                //add the new FD
    		            FileDescriptorHelper.createFile (filename, revision, date, isBinary, fileDescriptorContainer);
    		            //System.out.println(line.nextToken());
		            }
		        }
		    }
		}	
	}
}