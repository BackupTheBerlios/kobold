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
 * $Id: StatusUpdater.java,v 1.41 2004/09/22 23:44:41 martinplies Exp $
 * 
 */
package kobold.client.vcm.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.FileDescriptorHelper;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.preferences.VCMPreferencePage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.progress.ProgressManager;

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
	public void updateFileDescriptors(final IFileDescriptorContainer fdCon)
	{
			    String tmpString = fdCon.getLocalPath().toOSString();
		final String[] command = {"perl", getScriptPath() + 
        					      "stats.pl", tmpString.substring(0,tmpString.length()-1)};
	    logger.debug("running: " + command[0] + " " + command[1] + " " + command[2]);
	    
	    try {
	        ProgressManager.getInstance().runInUI(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), new WorkspaceModifyDelegatingOperation(new IRunnableWithProgress() {	            
	            public void run(IProgressMonitor monitor)
	            throws InvocationTargetException, InterruptedException
	            {
	                monitor.beginTask("Regenerating file descriptors...", 2);
	                ScriptExecuter se = new ScriptExecuter(command);
	                String res;
	                try {
	                    res = se.open(monitor);
	                    monitor.worked(1);
	                    se.close();
	                    if (res != null) {
	                        parseInputString(fdCon, res);
	                        monitor.worked(2);
	                    } else {
	                        logger.warn("script has returned null");
	                    }
	                } catch (IOException e) {
	                    MessageDialog.openError(Display.getDefault().getActiveShell(),
	                            "VCM Error", e.getLocalizedMessage());
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                finally {
	                    monitor.done();
	                }
	                
	            }
	        }),IDEWorkbenchPlugin.getPluginWorkspace().getRoot());
	        
	        if (fdCon instanceof AbstractAsset){
	           // remove Components/ProductComponents  directories from FilDescriptorConatainer 
	           HashMap fdMap = new HashMap ();
	           for ( Iterator ite = fdCon.getFileDescriptors().iterator(); ite.hasNext();){
		             FileDescriptor fd = (FileDescriptor) ite.next();
		             fdMap.put(fd.getFilename(), fd);
		       }
	           for (Iterator ite = ((AbstractAsset) fdCon).getChildren().iterator(); ite.hasNext();) {
	               AbstractAsset asset = (AbstractAsset)ite.next();
	               if(fdMap.containsKey(asset.getResource())){
	                   fdCon.removeFileDescriptor((FileDescriptor)fdMap.get(asset.getResource()));
	               }
	           }
	           
	           
	        }
	    } catch (InvocationTargetException e) {
	    } catch (InterruptedException e) {
	        logger.info("FD Update cancelled.");
	    }
	}

	
	/**
	 * Gets the path of the script(s)
	 * @return the script-path of the commandLine scripts
	 */
	public String getScriptPath ()
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
	public void parseInputString (IFileDescriptorContainer fileDescriptorContainer, 
									String inputString)
	{
		System.out.println ("STRING TO PARSE: \n"+  inputString);
		
		//clears all FD(s)
        fileDescriptorContainer.clear();

		//for the complete output of stats
		java.util.StringTokenizer line = new java.util.StringTokenizer(inputString, "\n");
		while(line.hasMoreTokens()) 
		{ 
		    //for each line (all elements divided by tabs)
		    java.util.StringTokenizer localLine = new java.util.StringTokenizer(line.nextToken(), "\t");
		    while(localLine.hasMoreTokens()) 
		    { 
		    
	        	/*1*/String filename = localLine.nextToken();
	        	/*2*/String revision = localLine.nextToken();
		    	
//		        //it's a directory
//		        if (localLine.countTokens() == 1)
//		        {
	
//		        	FileDescriptorHelper.
//							createDirectory(localLine.nextToken(), fileDescriptorContainer);
//          
//		        }
//
//		        else
//		        {

	        		//erase the "/t" in windows!!
	        		if (revision.endsWith("\r"))
	        		{
	        			revision = revision.replaceAll("\r","");
	        		}
		        	
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
		                     //System.out.println("create dir started");
		                     FileDescriptorHelper.createDirectory(filename, fileDescriptorContainer);
	
		                }
		                
		            }
			        //it's a file under cvs control
		            else {
		            	Date date = null;
		            	if(localLine.hasMoreTokens())
		            	{   		            
		            		try {
	            				StringTokenizer localDate = new StringTokenizer(localLine.nextToken(), ",");
	            				Calendar cal = Calendar.getInstance();
	            				cal.set(Integer.parseInt(localDate.nextToken()), Integer.parseInt(localDate.nextToken()), 
	            				    Integer.parseInt(localDate.nextToken()), Integer.parseInt(localDate.nextToken()), 
	            				    Integer.parseInt(localDate.nextToken()), Integer.parseInt(localDate.nextToken()));
	            				date = cal.getTime();
		            		}
		            		catch (NumberFormatException ex) {
		            			System.out.println("Date parse exception");
		            			date = null;
		            		}
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
    
//	/**
//     * deletes the variant directory
//     * @param variant
//     */
//    public static void deleteVariantVCMDirectory (Variant variant)
//    {
//    	
//    	ScriptServerConnection sc = ScriptServerConnection.getConnection("noUser");
//    	
//    	if (sc == null) {
//    	    return;
//    	}
//    	StatusUpdater su = new StatusUpdater();
//		//command line command with the stats script to the changed part of the meta-data containing FD(s)
//		String[] command = {"perl", su.getScriptPath() + 
//							"cleanvcmdata.pl", variant.getLocalPath().toOSString()};
//		try 
//		{
//			String iString="";
//			iString = sc.open(command,"");
//			System.out.println(iString);
//			sc.close();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    	
// TODO: Logger
//    	//System.out.println ("deleted variant "+variant.getName()+" directory!");
//    }
    
}