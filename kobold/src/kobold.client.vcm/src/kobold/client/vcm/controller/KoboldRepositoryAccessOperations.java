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
 */
package kobold.client.vcm.controller;



import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.common.data.UserContext;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.io.ScriptDescriptor;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.TeamException;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPlugin;
/**
 * @author schneipk
 *
 * This is the actual implementation of the repository Access Interface for use with
 * the Kobold PLAM Tool
 */
public class KoboldRepositoryAccessOperations implements KoboldRepositoryOperations {
	
	/* The Connection to the specified Repository used by the Repository Access Operations
	 */ 

	private static UserContext userContext = null;
	
	//Variable to store Release Tag
	
	//Variable to store 
	
	// RepositoryDescriptor of the current Project
	private RepositoryDescriptor currentVCMProvider = null;
	
	// The VCM Repository User, Password and Repository Path
	private String  repositoryRootPath = "";
	private String repositoryHost = "";
	private String  repositoryModulePath = "";
	
	// The commandline Argument String
	private String[] argString = null; 
	
	// The fields used to store the selected Types(productline, etc..)
	private Productline productLine = null;
	private Product product = null;
	private Variant variant = null;
	private Release release = null;
	private Component component = null;
	
	// skriptExtension
	private String skriptExtension = null;
	
	// Path of the current Skript Directory (installation Dir)
	private Path skriptPath = null;
	
	// Path of the selected Asset on the local filesystem
	private String localPath = null;

	// The prefix/name for the skripts
	
	private final String IMPORT = "import.";
	private final String ADD = "add.";
	private final String UPDATE = "update.";
	private final String COMMIT = "commit.";
	private final String CHECKOUT = "checkout.";
	private final String TAG = "tag.";
	private final String RM = "rm.";
	public static final char QUOTATION = 0x22;
	
	public KoboldRepositoryAccessOperations()
	{
		KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
		String tmpLocation = plugin.getBundle().getLocation();
		// The Location String contains "@update/" this needs to be removed
		
		
		String tmpString = System.getProperty("os.name");
		// This tests what OS is used and sets the skript extension accordingly
		if (tmpString.indexOf("Win",0) != -1 ) 
		{
			this.skriptPath = new Path(tmpLocation.substring(8,tmpLocation.length()));
			this.skriptPath = (Path)skriptPath.append("scripts" + IPath.SEPARATOR);
			skriptExtension = "bat";
		}
		else
			{
			this.skriptPath = new Path(tmpLocation.substring(7,tmpLocation.length()));
			this.skriptPath = (Path)skriptPath.append("scripts" + IPath.SEPARATOR);
				skriptExtension = "sh";
			}
			
		
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preCheckin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
		if (performOperation) {
			try {
			    for (int i = 0; i < resources.length; i++)
	            {
			        List tmpList = resources[i].getBeforeScripts();
                    if (tmpList != null)
                    {
                        for (int j = 0; j < tmpList.size(); j++)
                        {
                            progress = KoboldPolicy.monitorFor(progress);
                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_COMMIT))
                            {
                                progress.beginTask("precheckin working", 2);
                                ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
                                if (connection != null) {
                                    String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                    connection.setSkriptName(beforeSkriptPath);
                                    connection.open(progress);
                                    connection.close();
                                }
                                progress.done();
                            }
                        }
                    }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postCheckin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
		if (performOperation) {
			try {
			    for (int i = 0; i < resources.length; i++)
	            {
			        List tmpList = resources[i].getAfterScripts();
                    if (tmpList != null)
                    {
                        for (int j = 0; j < tmpList.size(); j++)
                        {
                            progress = KoboldPolicy.monitorFor(progress);
                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_COMMIT))
                            {
                                progress.beginTask("postcheckin working", 2);
                                ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                        repositoryRootPath);
                                if (connection != null) {
                                    String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                    connection.setSkriptName(beforeSkriptPath);
                                    connection.open(progress);
                                    connection.close();
                                }
                                progress.done();
                            }
                        }
                    }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 */
	private void initConnection(ScriptServerConnection connection, AbstractAsset[] assets)
	{
		if(assets != null)
		{
			for (int i = 0; i < assets.length; i++) {
				
				if (assets[i] instanceof Productline) {
					productLine = (Productline) assets[i];
					
//					repositoryPath
					currentVCMProvider = productLine.getRepositoryDescriptor();
					localPath = productLine.getLocalPath().toOSString();
					setCurrentVCMProvider(productLine.getRepositoryDescriptor());
					connection.setRepositoryDescriptor(productLine.getRepositoryDescriptor());

				}
				if (assets[i] instanceof Product) {
					product = (Product) assets[i];
					localPath = product.getLocalPath().toOSString();
					currentVCMProvider = product.getRepositoryDescriptor();
					setCurrentVCMProvider(product.getRepositoryDescriptor());
					connection.setRepositoryDescriptor(product.getRepositoryDescriptor());
				}
				if (assets[i] instanceof Variant) {
					variant = (Variant) assets[i];
					localPath = variant.getLocalPath().toOSString();
					setCurrentVCMProvider(variant.getRemoteRepository()); 					
					connection.setRepositoryDescriptor(variant.getRemoteRepository());
				}
				if (assets[i] instanceof Component) {
					component = (Component) assets[i];
					localPath = component.getLocalPath().toOSString();
					setCurrentVCMProvider(component.getRemoteRepository());
					connection.setRepositoryDescriptor(component.getRemoteRepository());
				}			
				
				if (currentVCMProvider != null) {
					repositoryRootPath = currentVCMProvider.getRoot();
					repositoryModulePath = currentVCMProvider.getPath();
					repositoryHost = currentVCMProvider.getHost();
				} else {
					MessageDialog.openError(new Shell(), "Error", "The Repository Provider has not been set, please check Configuration");

				}
			}
		}	
		
	}
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preCheckout(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation
			) throws TeamException {
		if (performOperation) {
			try {
			    for (int i = 0; i < resources.length; i++)
	            {
			        List tmpList = resources[i].getBeforeScripts();
                    if (tmpList != null)
                    {
                        for (int j = 0; j < tmpList.size(); j++)
                        {
                            progress = KoboldPolicy.monitorFor(progress);
                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_CHECKOUT))
                            {
                                progress.beginTask("precheckout working", 2);
                                ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                        repositoryRootPath);
                                if (connection != null) {
                                    String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                    connection.setSkriptName(beforeSkriptPath);
                                    connection.open(progress);
                                    connection.close();
                                }
                                progress.done();
                            }
                        }
                    }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	/**public void postCheckout(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation ) throws TeamException {
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("postcheckout working", 2);
				ScriptServerConnection connection = new ScriptServerConnection(repositoryRootPath);
				connection.setSkriptName(skriptPath.toOSString().concat(IMPORT).concat(skriptExtension));
				initConnection(connection,resources);
				connection.open(progress);
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}**/
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void importing(AbstractAsset[] resources, int depth, IProgressMonitor progress)
			throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("import working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null)
			connection.setSkriptName(skriptPath.toOSString().concat(IMPORT).concat(skriptExtension));
			initConnection(connection,resources);
			String tempString[] = new String[argString.length+1];
			for (int i = 0; i < argString.length; i++) {
				tempString[i] = argString[i];
			}
			InputDialog messageInput = new InputDialog(new Shell(),"Please enter a message for the repository","VCM Message :",null, null);
			messageInput.open();
			String message = String.valueOf(QUOTATION);
//			String message = "";
			message = message.concat(messageInput.getValue());
			message =  message.concat(String.valueOf(QUOTATION));
			System.out.println(message);
			tempString[tempString.length-1] = message; 
			argString = tempString;
			if (productLine != null)
            {
                String tmpString[] = new String[argString.length+1];
                for (int i = 0; i < argString.length; i++)
                {
                    tmpString[i] = argString[i];
                }
                java.io.File metainfo = productLine.getLocalPath().append(".productlinemetainfo.xml").toFile();
                java.io.File viewdata = productLine.getLocalPath().append(productLine.getName()+".vm").toFile();
                if (viewdata.exists())
                {
                    tmpString[tmpString.length-1] = productLine.getName().concat("/"+viewdata.getName());
                    connection.open(progress, tmpString);
                }
                argString = tmpString;
                if (metainfo.exists())
                {
                    tmpString[tmpString.length-1] = productLine.getName().concat("/"+metainfo.getName());
                }
                System.out.println("lalal");
            }
			connection.open(progress,argString);
			connection.close();	
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(AbstractAsset[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("checkout working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			connection.setSkriptName(skriptPath.toOSString().concat(CHECKOUT).concat(skriptExtension));
    			initConnection(connection,resources);
    			if (productLine != null)
                {
//                    String tmpString[] = new String[argString.length+1];
//                    for (int i = 0; i < argString.length; i++)
//                    {
//                        tmpString[i] = argString[i];
//                    }
                    java.io.File metainfo = productLine.getLocalPath().append(".productlinemetainfo.xml").toFile();
                    java.io.File viewdata = productLine.getLocalPath().append(productLine.getName()+".vm").toFile();
                    if (viewdata.exists())
                    {
                        argString[argString.length-1] = productLine.getName().concat("/"+viewdata.getName());
                        connection.open(progress, argString);
                    }
//                    argString = tmpString;
                    if (metainfo.exists())
                    {
                        argString[argString.length-1] = productLine.getName().concat("/"+metainfo.getName());
                    }
                    System.out.println("lalal");
                }
    			connection.open(progress, argString);
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("checkin/commit working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			connection.setSkriptName(skriptPath.toOSString().concat(COMMIT).concat(skriptExtension));
    			initConnection(connection,resources);
    			String tempString[] = new String[argString.length+1];
    			for (int i = 0; i < argString.length; i++) {
    				tempString[i] = argString[i];
    			}
    			InputDialog messageInput = new InputDialog(new Shell(),"Please enter a message for the repository","VCM Message :",null, null);
    			messageInput.open();
    			String message = String.valueOf(QUOTATION);
    //			String message = "";
    			message = message.concat(messageInput.getValue());
    			message =  message.concat(String.valueOf(QUOTATION));
    			System.out.println(message);
    			tempString[tempString.length-1] = message; 
    			argString = tempString;
    			connection.open(progress,argString);
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postGet(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postImport(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
			{
				if (performOperation)
		        {
		            try
		            {
					    for (int i = 0; i < resources.length; i++)
			            {
					        List tmpList = resources[i].getAfterScripts();
		                    if (tmpList != null)
		                    {
		                        for (int j = 0; j < tmpList.size(); j++)
		                        {
		                            progress = KoboldPolicy.monitorFor(progress);
		                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
		                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_IMPORT))
		                            {
		                                progress.beginTask("postimport working", 2);
		                                ScriptServerConnection connection = ScriptServerConnection.getConnection(
		                                        repositoryRootPath);
		                                if (connection != null) {
    		                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
    		                                connection.setSkriptName(beforeSkriptPath);
    		                                connection.open(progress);
    		                                connection.close();
		                                }
		                                progress.done();
		                            }
		                        }
		                    }
			            }
		            } catch (Exception e)
		            {
		                e.printStackTrace();
		            }
		        }
			}

	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preImport(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preImport(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
	{
		if (performOperation)
        {
            try
            {
			    for (int i = 0; i < resources.length; i++)
	            {
			        List tmpList = resources[i].getBeforeScripts();
                    if (tmpList != null)
                    {
                        for (int j = 0; j < tmpList.size(); j++)
                        {
                            progress = KoboldPolicy.monitorFor(progress);
                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_IMPORT))
                            {
                                progress.beginTask("preimport working", 2);
                                ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                        repositoryRootPath);
                                if (connection != null) {
                                    String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                    connection.setSkriptName(beforeSkriptPath);
                                    connection.open(progress);
                                    connection.close();
                                }
                                progress.done();
                            }
                        }
                    }
	            }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
	/**
	 * @param currentVCMProvider The currentVCMProvider to set.
	 */
	public void setCurrentVCMProvider(RepositoryDescriptor currentVCMProvider) {
		this.currentVCMProvider = currentVCMProvider;
		if(currentVCMProvider != null && currentVCMProvider instanceof RepositoryDescriptor)
		{
				argString = new String[4];
			if (localPath != null || repositoryHost != ""|| repositoryModulePath != "" || repositoryRootPath != ""  ) {
				argString[0] = localPath;
				argString[1] = currentVCMProvider.getHost();//repositoryHost;
				argString[2] = currentVCMProvider.getRoot();//repositoryRootPath;
				argString[3] = currentVCMProvider.getPath(); // @ FIXME repositoryModulePath;
			} else {
				MessageDialog.openError(new Shell(), "Hoooonk", "Du nix hast gesetzt den RepositoryProvider Alder");
			}
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void postcheckout(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation
	) throws TeamException {
		if (performOperation) {
			try {
			    for (int i = 0; i < resources.length; i++)
	            {
			        List tmpList = resources[i].getAfterScripts();
                    if (tmpList != null)
                    {
                        for (int j = 0; j < tmpList.size(); j++)
                        {
                            progress = KoboldPolicy.monitorFor(progress);
                            ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                            if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_CHECKOUT))
                            {
                                progress.beginTask("postcheckout working", 2);
                                ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                        repositoryRootPath);
                                if (connection != null) {
                                    String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                    connection.setSkriptName(beforeSkriptPath);
                                    connection.open(progress);
                                    connection.close();
                                }
                                progress.done();
                            }
                        }
                    }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param repositoryPath The repositoryPath to be set.
	 */
	public void setRepositoryRootPath(String repositoryPath) {
		this.repositoryRootPath = repositoryPath;
	}
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#add(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void add(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("checkout working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			initConnection(connection,assets);
    			connection.setSkriptName(skriptPath.toOSString().concat(ADD).concat(skriptExtension));
    			initConnection(connection,assets);
    			connection.open(progress, argString);
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preAdd(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void preAdd(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < assets.length; i++)
            {
		        List tmpList = assets[i].getBeforeScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_ADD))
                        {
                            progress.beginTask("preadd working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postAdd(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void postAdd(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < assets.length; i++)
            {
		        List tmpList = assets[i].getAfterScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_ADD))
                        {
                            progress.beginTask("postadd working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#update(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void update(AbstractAsset[] resources, int depth, IProgressMonitor progress) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("update working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			initConnection(connection,resources);
    			connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
    			initConnection(connection,resources);
    			connection.open(progress, argString);
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preUpdate(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preUpdate(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < resources.length; i++)
            {
		        List tmpList = resources[i].getBeforeScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_UPDATE))
                        {
                            progress.beginTask("preupdate working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postUpdate(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postUpdate(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < resources.length; i++)
            {
		        List tmpList = resources[i].getAfterScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_UPDATE))
                        {
                            progress.beginTask("postupdate working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
	

	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#Import(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
		//	Not needed with type IResource using AbstractAsset instead
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
		//  Not needed with type IResource using AbstractAsset instead
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
		// Not needed with type IResource using AbstractAsset instead
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#uncheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void uncheckout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// No requirement not needed
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#delete(org.eclipse.core.resources.IResource[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void delete(IResource[] resources, IProgressMonitor progress)
			throws TeamException {
		// NOT IN USE / NO REQUIREMENT
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#moved(org.eclipse.core.runtime.IPath, org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void moved(IPath source, IResource target, IProgressMonitor progress)
			throws TeamException {
		// NOT IN USE
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isCheckedOut(org.eclipse.core.resources.IResource)
	 */
	public boolean isCheckedOut(IResource resource) {
		// NOT IN USE
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#hasRemote(org.eclipse.core.resources.IResource)
	 */
	public boolean hasRemote(IResource resource) {
		// NOT IN USE
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isDirty(org.eclipse.core.resources.IResource)
	 */
	public boolean isDirty(IResource resource) {
		// NOT IN USE / IN ITERATION I
		return false;
	}
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#tag(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
     */
    public void tag(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
		try {
		
		    if (!(assets[0] instanceof Release)) {
		        return;
		    }
		    Release rel = (Release) assets[0];
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("tag working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			initConnection(connection,assets);
    			connection.setSkriptName(skriptPath.toOSString().concat(TAG).concat(skriptExtension));
    			String tempString[] = new String[argString.length+1];
    			for (int i = 0; i < argString.length; i++) {
    				tempString[i] = argString[i];
    			}
    			String message = rel.getName();
    			message =  message.concat(String.valueOf(QUOTATION));
    			System.out.println(message);
    			tempString[tempString.length-1] = message; 
    			argString = tempString;
    			initConnection(connection,assets);
    			for (Iterator iterator = rel.getFileRevisions().iterator(); iterator.hasNext();) {
    			    argString[argString.length - 2] = ((Release.FileRevision)iterator.next()).getPath(); 
        			connection.open(progress, argString);
    			}
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#add(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void rm(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("rm working", 2);
			ScriptServerConnection connection = ScriptServerConnection.getConnection(repositoryRootPath);
			if (connection != null) {
    			initConnection(connection,assets);
    			connection.setSkriptName(skriptPath.toOSString().concat(RM).concat(skriptExtension));
    			initConnection(connection,assets);
    			connection.open(progress, argString);
    			connection.close();	
			}
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preAdd(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void preRm(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < assets.length; i++)
            {
		        List tmpList = assets[i].getBeforeScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_DELETE))
                        {
                            progress.beginTask("prerm working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postAdd(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void postRm(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
	    try
        {
		    for (int i = 0; i < assets.length; i++)
            {
		        List tmpList = assets[i].getAfterScripts();
                if (tmpList != null)
                {
                    for (int j = 0; j < tmpList.size(); j++)
                    {
                        progress = KoboldPolicy.monitorFor(progress);
                        ScriptDescriptor tmpScriptDescriptor = ((ScriptDescriptor)tmpList.get(j));
                        if (tmpScriptDescriptor.getVcmActionType().equals(tmpScriptDescriptor.VCM_DELETE))
                        {
                            progress.beginTask("postrm working", 2);
                            ScriptServerConnection connection = ScriptServerConnection.getConnection(
                                    repositoryRootPath);
                            if (connection != null) {
                                String beforeSkriptPath = ((ScriptDescriptor)tmpList.get(j)).getPath();
                                connection.setSkriptName(beforeSkriptPath);
                                connection.open(progress);
                                connection.close();
                            }
                            progress.done();
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
