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
 * $Id: StatusUpdater.java,v 1.12 2004/07/29 15:14:37 memyselfandi Exp $
 * 
 */
package kobold.client.vcm.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.FileDescriptorHelper;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.ScriptServerConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author rendgeor
 * 
 * The class updates the FD(s) included in the delivered FD-container.
 * It works as listener and acts when something in the model change or a refresh is executed.
 * 
 */

public class StatusUpdater {

    private DateFormat df = new SimpleDateFormat();
    private static final Log logger = LogFactory.getLog(StatusUpdater.class);
    
	/**
	 * Listener who acts on the delivered FD-container and updates all included FD(s)
	 * @param fileDescriptorContainer, the FD-container to update
	 */
	public void updateFileDescriptors(IFileDescriptorContainer fileDescriptorContainer)
	{

		//command line command with the stats script to the changed part of the meta-data containing FD(s)
		String[] command = {"perl", getScriptPath() + 
							"stats.pl", fileDescriptorContainer.getLocalPath().getFullPath().toOSString()};
		//process the connection (open and parse the input)
		processConnection(command, fileDescriptorContainer);
		

	}
	
	public void processConnection (String[] command,
									IFileDescriptorContainer fileDescriptorContainer)
	{
		ScriptServerConnection conn = new ScriptServerConnection("faceLoc");

		try 
		{
			conn.open(command);
			String iString = conn.getInputStream().toString();
			//parse the string
			parseInputString(fileDescriptorContainer, iString);
			
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
			String tmpLocation = plugin.getBundle().getLocation();

			Path skriptPath = new Path(tmpLocation.substring(8,tmpLocation.length()));
			return ((Path)skriptPath.append("scripts" + IPath.SEPARATOR)).toOSString();
		}
		else
		{
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
		FileDescriptorHelper.clear(fileDescriptorContainer);

		//for the complete output of stats
		java.util.StringTokenizer line = new java.util.StringTokenizer(inputString, "\n");
		while(line.hasMoreTokens()) 
		{ 
		    //for each line (divided by tabs)
		    java.util.StringTokenizer localLine = new java.util.StringTokenizer(inputString, "\t");
		    while(localLine.hasMoreTokens()) 
		    { 
		        //it's a directory
		        if (line.countTokens() == 1)
		        {
		        	FileDescriptorHelper.
							createDirectory(localLine.nextToken(), fileDescriptorContainer);
          
		        }
		        //it's a file
		        else
		        {
		        	/*1*/String filename = localLine.nextToken();
		        	/*2*/String revision = localLine.nextToken();
	            
		            Date date = null;
		            try {
		            	/*3*/date = df.parse(localLine.nextToken());
                    } catch (ParseException e) {
                    	logger.debug("could not parse date - using null instead", e);
                    }

                    /*4*/boolean isBinary = localLine.nextToken().equals("binary");                    
                    
                    //add the new FD
		            FileDescriptorHelper.createFile (filename, revision, date, isBinary, fileDescriptorContainer);
		            //System.out.println(line.nextToken());
		            
		        }
		    }
		    
		}	
	}
 
}