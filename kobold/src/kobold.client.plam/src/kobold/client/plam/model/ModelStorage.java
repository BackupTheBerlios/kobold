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
 * $Id: ModelStorage.java,v 1.17 2004/08/05 09:13:01 vanto Exp $
 *
 */
package kobold.client.plam.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.common.io.RepositoryDescriptor;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;


/**
 * @author Tammo
 */
public class ModelStorage
{
    public static final String COREASSETS_FOLDER_NAME = "CAS";
    public static final String PRODUCTS_FOLDER_NAME = "PRODUCTS";
    public static final String PRODUCTLINE_META_FILE = ".productlinemetainfo.xml";
    public static final String PRODUCT_META_FILE = ".productmetainfo.xml";
    
    public static final Logger logger = Logger.getLogger(ModelStorage.class);
    
    public static Productline loadModel(IProject project, kobold.common.data.Productline spl)
    {
        logger.debug("Loading model...");
        Productline pl = null;
        
		//get the PL directory
     	IFolder plmeta = project.getFolder(spl.getResource());
        if (plmeta.exists()) {
            IFile modelFile = plmeta.getFile(PRODUCTLINE_META_FILE);
            if (modelFile.exists()) {
                InputStream in;
                try {
                    in = modelFile.getContents();
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(in);
                    pl = new Productline(document.getRootElement());
                } catch (CoreException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            logger.debug("no model loaded.");
        }

        return pl;        
    }
    
    /**
     * Creates the PL directory
     * @param pl, the pl for creating the dirs
     * @return the PL-IFolder
     */
	private static void createPlDirectory (Productline pl, IProgressMonitor monitor) {
		//create directory for the PL
		monitor.beginTask("Creating directory structure", 150);
		//the PL directory
        IProject project = pl.getKoboldProject().getProject();
        
        try {
	        IFolder plFolder = project.getFolder(pl.getName());
	        if (!plFolder.exists()) {
	            plFolder.create(true, true, monitor);
	        }
	
	        IFolder plFolder2 = plFolder.getFolder(COREASSETS_FOLDER_NAME);
	        if (!plFolder2.exists()) {
	            plFolder2.create(true, true, monitor);
	        }
	        
	        plFolder2 = plFolder.getFolder(PRODUCTS_FOLDER_NAME);
	        if (!plFolder2.exists()) {
	            plFolder2.create(true, true, monitor);
	        }
        } catch (CoreException e) {
            KoboldPLAMPlugin.log(e);
        } finally {
            monitor.done();
        }
	}	

    private static void createFile (IFile modelFile, IProgressMonitor monitor, ByteArrayOutputStream out)
	{
		try{
		    if (modelFile.exists()) {
		        modelFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
		    } else {
		        modelFile.create(new ByteArrayInputStream(out.toByteArray()), 
		            true, monitor);
		    }
		    
	    } catch (CoreException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } finally {
	        monitor.done();
	    }
	}
    
    
    /**
     *	Stores the complete model of the pl to the metainfo-files 
     * @param pl, the pl to store
     */
    public static void storeModel(final Productline pl)
    {
        logger.debug("Storing model...");
        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        try {
            progressService.run(false, true, new IRunnableWithProgress(){
                public void run(IProgressMonitor monitor) {
                	monitor.beginTask("Storing PLAM Model", 1000);
            		//get the PL directory
                    IProject project = pl.getKoboldProject().getProject();
                 	IFolder plmeta = project.getFolder(pl.getName());
                	
                	//create the PL,PRODUCTS,CAS directories
                	createPlDirectory(pl, new SubProgressMonitor(monitor, 500));
                	
                	//create the metafiles
                    IFile modelFile = plmeta.getFile(PRODUCTLINE_META_FILE);
                    
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    XMLWriter writer;
                    try {
                        writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
                        writer.write(pl.serialize());
                        writer.close();
                        
                        //write the metafile
                        createFile(modelFile, new SubProgressMonitor(monitor, 500), out);
                        
                        out.close();   
                        
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
   
                    } finally {
                        monitor.done();
                    }
                    
                    //write the products metafile
                    serializeProduct(pl, monitor);
                    serializeCoreassets(pl, monitor);
                    
                    ServerHelper.updateProductline(pl.getKoboldProject());
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the repository descriptor of the asset. If a product(line)
     * is given, it'll return a repository descriptor to the product(line).
     *
     * Note: If the asset is a product(line) or core-asset this method will just return
     *       the repository descriptor of it.
     * @param asset the asset you're gaining the repository descriptor for.
     */
    
    public static RepositoryDescriptor getRepositoryDescriptorForAsset(AbstractAsset asset) {

        // checks special cases first
        if (asset.getType() == AbstractAsset.PRODUCT_LINE) {
            return ((Productline)asset).getRepositoryDescriptor();
        }
        else if (asset.getType() == AbstractAsset.PRODUCT) {
            return ((Product)asset).getRepositoryDescriptor();
        }
        
        String modulePath = "";
        while (! (asset instanceof AbstractRootAsset)) {
            modulePath = asset.getName() + IPath.SEPARATOR + modulePath;
            asset = asset.getParent();
        }
        
        AbstractRootAsset root = (AbstractRootAsset)asset;
        RepositoryDescriptor repositoryDescriptor = root.getRepositoryDescriptor();
        
        return
            new RepositoryDescriptor(repositoryDescriptor.getType(),
                    				 repositoryDescriptor.getProtocol(),
                    				 repositoryDescriptor.getHost(),
                    				 repositoryDescriptor.getRoot(),
                    				 repositoryDescriptor.getPath() + IPath.SEPARATOR +
                    				 modulePath);
    }
	
    
    /**
     * Returns full path of given abstract assets. The full path will
     * be calculated.
     * @param theAsset an abstract asset
     */

    public static IPath getPathForAsset(AbstractAsset asset) {
        
        AbstractRootAsset root = asset.getRoot();
        String thePath = "";
        while (asset != null) {
             
            if (asset.getType() == AbstractAsset.COMPONENT) {
                if (asset.getParent().getType() != AbstractAsset.VARIANT) {
                    thePath = COREASSETS_FOLDER_NAME + IPath.SEPARATOR +
                    		  asset.getName() + IPath.SEPARATOR + thePath;
                }
                else {
                    thePath = asset.getName() + IPath.SEPARATOR + thePath;
                }
            }
            else if ((asset.getType() == AbstractAsset.SPECIFIC_COMPONENT) ||
                     (asset.getType() == AbstractAsset.RELATED_COMPONENT)) {
                thePath = PRODUCTS_FOLDER_NAME + IPath.SEPARATOR +
                		  asset.getName() + IPath.SEPARATOR + thePath;
            }
            else {
                thePath = asset.getName() + IPath.SEPARATOR + thePath;
            }
            
            asset = asset.getParent();
        }
        
        return new Path(root.getKoboldProject().getProject().getLocation()
                        + "" + IPath.SEPARATOR + thePath);
    }
    
	
	
	public static void serializeProduct (Productline pl, IProgressMonitor monitor)
	{
		//get the PRODUCTS-directory
		//the PL directory
        IProject project = pl.getKoboldProject().getProject();
       	IFolder productsFolder = project.getFolder(pl.getName());
        //PRODUCTS-dir
        productsFolder = productsFolder.getFolder(PRODUCTS_FOLDER_NAME);
        
		//get all products
    	List products = pl.getProducts();
    	
        //for each product
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();
		       
	        //create the dir
	        IFolder specialProductFolder = productsFolder.getFolder(product.getName());

			
			//createDirectory
	        try {
		        if (!specialProductFolder.exists()) {
		            specialProductFolder.create(true, true, monitor);
		        }
	        } catch (CoreException e) {
	            KoboldPLAMPlugin.log(e);
	        }
	        /**finally {
	            monitor.done();
	        }**/
	            
	    	
	    	//create the metafiles
	        IFile modelFile = specialProductFolder.getFile(PRODUCT_META_FILE);
	        
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        XMLWriter writer;
			
	        try {
	            writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
	            writer.write(product.serialize());
	            writer.close();
	            
	            //write the metafile
	            createFile(modelFile, monitor, out);
	            
	            out.close();
	            
	            
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();



	        }
		}

	}
	
	public static void serializeCoreassets (Productline pl, IProgressMonitor monitor)
	{
		//get the CAS-directory
		//the PL directory
        IProject project = pl.getKoboldProject().getProject();
       	IFolder casFolder = project.getFolder(pl.getName());
        //CAS-dir
        casFolder = casFolder.getFolder(COREASSETS_FOLDER_NAME);
        
		//get all cas
    	List cas = pl.getComponents();
    	
        //for each product
		for (Iterator it = cas.iterator(); it.hasNext();) {
			Component component = (Component) it.next();
		       
	        //create the dir
	        IFolder specialComponentFolder = casFolder.getFolder(component.getName());

			
			//createDirectory
	        try {
		        if (!specialComponentFolder.exists()) {
		            specialComponentFolder.create(true, true, monitor);
		        }
	        } catch (CoreException e) {
	            KoboldPLAMPlugin.log(e);
	        }
	        finally {
	            monitor.done();
	        }	    	

	        }
		}

	}

