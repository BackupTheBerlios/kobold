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
 * $Id: ModelStorage.java,v 1.58 2004/11/22 16:12:36 garbeam Exp $
 *
 */
package kobold.client.plam.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
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
    public static final String PRODUCTLINE_META_FILE = ".productlinemetainfo.xml";
    public static final String PRODUCT_META_FILE = ".productmetainfo.xml";
    
    public static final Logger logger = Logger.getLogger(ModelStorage.class);
    
    public static Productline loadModel(KoboldProject kp, kobold.common.data.Productline spl)
    {
        logger.debug("Loading model...");
        Productline pl = null;
        
        //get the PL directory
        IProject project = kp.getProject();
        IFolder plmeta = project.getFolder(spl.getResource());
        if (plmeta.exists()) {
            IFile modelFile = plmeta.getFile(PRODUCTLINE_META_FILE);
            if (modelFile.exists()) {
                InputStream in;
                try {
                    in = modelFile.getContents();
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(in);
                    pl = new Productline(kp, document.getRootElement());                    
                } catch (CoreException e) {
 					logger.error( "loadModel(KoboldProject, kobold.common.data.Productline)", e);
                } catch (DocumentException e) {
					logger.error( "loadModel(KoboldProject, kobold.common.data.Productline)", e);
                }
            }
        } else {
            logger.debug("no model loaded.");
        }
        
        return pl;        
    }
    
    /**
     * Creates the PL directory
     * @param pl the pl for creating the dirs
     * @return monitor the PL-IFolder
     */
    private static void createPlDirectory (Productline pl, IProgressMonitor monitor) {
        //create directory for the PL
        monitor.beginTask("Creating directory structure", 150);
        //the PL directory
        IProject project = pl.getKoboldProject().getProject();
        
        try {
            IFolder plFolder = project.getFolder(pl.getResource());
            if (!plFolder.exists()) {
                plFolder.create(true, true, monitor);
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
			logger.error( "createFile(IFile, IProgressMonitor, ByteArrayOutputStream)", e);
        } finally {
            monitor.done();
        }
    }
    

    
    /**
     *	Stores the complete model of the pl to the metainfo-files 
     * @param pl the pl to store
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
                    IFolder plmeta = project.getFolder(pl.getResource());
                    
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
						logger.error("run(IProgressMonitor)", e);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
						logger.error("run(IProgressMonitor)", e);
                        
                    } finally {
                        monitor.done();
                    }
                    
                    //write the products metafile
                    serializeProduct(pl, monitor);
                    
                    //create CAS subdirs
                    serializeCoreassets(pl, monitor);
                    
                    ServerHelper.updateProductline(pl.getKoboldProject());
                }
            });
        } catch (InvocationTargetException e) {
			logger.error("storeModel(Productline)", e);
        } catch (InterruptedException e) {
			logger.error("storeModel(Productline)", e);
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
            modulePath = asset.getResource() + IPath.SEPARATOR + modulePath;
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
     * @param asset theAsset an abstract asset
     */
    public static IPath getPathForAsset(AbstractAsset asset) {
        AbstractRootAsset root = asset.getRoot();        
        return new Path(root.getKoboldProject().getProject().getLocation()
                + "" + IPath.SEPARATOR + getLocalPathForAsset(asset));
    }
    
    public static IPath getLocalPathForAsset (AbstractAsset asset) {
        String thePath = "";
        while (asset != null) {
        	
        	if (thePath != "")
        	{
        		thePath = asset.getResource() + IPath.SEPARATOR + thePath;
        		if (asset instanceof Product) {
        			break;
        		}
  
        	}
        	else
        	{
        		thePath = asset.getResource();
        	}
      		asset = asset.getParent();
        }
        return new Path(thePath);
    }
    

    public static IResource getResourceForFD(FileDescriptor fd) 
    {
        AbstractAsset asset = (AbstractAsset)fd.getParentAsset();
        IFolder folder = getFolderForAsset(asset);
        String relP = fd.getFilename();
        fd = (FileDescriptor)fd.getParent();
        while (fd != null) {
            relP = fd.getFilename() + IPath.SEPARATOR + relP ;
            fd = (FileDescriptor)fd.getParent();
        }
        return folder.findMember(relP);
    }
    
    public static IFolder getFolderForAsset(AbstractAsset asset) {
        AbstractRootAsset root = asset.getRoot();
        String thePath = "";
        while (asset != null) {
            
                thePath = asset.getResource() + IPath.SEPARATOR + thePath;
            //}
            
            asset = asset.getParent();
        }
        // @ FIXME when root = null --> nullpointerexception
        return root.getKoboldProject().getProject().getFolder(thePath);
    }
    
    public static void serializeProduct (Productline pl, IProgressMonitor monitor)
    {
        //get the PRODUCTS-directory
        //the PL directory
        KoboldProject kp = pl.getKoboldProject();
        IProject project = kp.getProject();
        //IFolder productsFolder = project.getFolder(); //pl.getResource());
        //PRODUCTS-dir
        //productsFolder = productsFolder.getFolder(PRODUCTS_FOLDER_NAME);
        
        //get all products
        List products = pl.getProducts();
        
        //for each product
        for (Iterator it = products.iterator(); it.hasNext();) {
            Product product = (Product) it.next();
            
            //create the dir
            IFolder specialProductFolder = project.getFolder(product.getResource());
            
            
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
				logger.error("serializeProduct(Productline, IProgressMonitor)",
						e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
				logger.error("serializeProduct(Productline, IProgressMonitor)",
						e);
            }
            //Part of generating all subfolders
            //List of all related components
            List relCompList = product.getRelatedComponents();
            Iterator it2 = relCompList.iterator();
            int count = 0;
            while(it2.hasNext()){
                count=count+1;
                RelatedComponent tmpRelComp = (RelatedComponent)it2.next();
                
                IFolder relCompFolder = specialProductFolder.getFolder(tmpRelComp.getResource());
                
                //createDirectory
                try {
                    if (!relCompFolder.exists()) {
                        relCompFolder.create(true, true, monitor);
                    }
                } catch (CoreException e) {
                    KoboldPLAMPlugin.log(e);
                }
            }
            
            List specCompList = product.getSpecificComponents();
            it2 = specCompList.iterator();
            while(it2.hasNext()){
                SpecificComponent tmpRelComp = (SpecificComponent)it2.next();
                
                IFolder relCompFolder = specialProductFolder.getFolder(tmpRelComp.getResource());
                
                //createDirectory
                try {
                    if (!relCompFolder.exists()) {
                        relCompFolder.create(true, true, monitor);
                    }
                } catch (CoreException e) {
                    KoboldPLAMPlugin.log(e);
                }
            }
            
            // commit the product
            kp.commitProduct(product);
        }
        
    }
    
    public static void serializeCoreassets (Productline pl, IProgressMonitor monitor)
    {
        //get the CAS-directory
        //the PL directory
        IProject project = pl.getKoboldProject().getProject();
        IFolder casFolder = project.getFolder(pl.getResource());
        //CAS-dir
        //casFolder = casFolder.getFolder(COREASSETS_FOLDER_NAME);
        
        //get all cas
        List cas = pl.getComponents();
        
        //for each component
        for (Iterator it = cas.iterator(); it.hasNext();) {
            Component component = (Component) it.next();
            
            if (component.getResource() == null) {
                // Resource of Component is null
                logger.error("Component \""+component.getName()+"\" has no resource!");
                continue;
            }
            //create the dir
            IFolder specialComponentFolder = casFolder.getFolder(component.getResource());
            
            
            //createDirectory
            try {
                
                if (!specialComponentFolder.exists()) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("serializeCoreassets(Productline, IProgressMonitor) - Now create comp-dir:"
										+ specialComponentFolder.toString());
					}
                    specialComponentFolder.create(true, true, monitor);
                }
            } catch (CoreException e) {
                KoboldPLAMPlugin.log(e);
            }
            finally {
                //create Variant dirs in each component
                serializeVariants(component, monitor);
                
                monitor.done();
            }	    	
            
        }
    }

    public static void serializeCoreassets (Variant var, IProgressMonitor monitor)
    {
        IPath path = getLocalPathForAsset(var);
        
        IProject project = ((Productline)var.getRoot()).getKoboldProject().getProject();
        //get all cas
        List cas = var.getComponents();
        
        //for each component
        for (Iterator it = cas.iterator(); it.hasNext();) {
            Component component = (Component) it.next();
            
            
            if (component.getResource() == null) {
                // Resource of Component is null
                logger.error("Component \""+component.getName()+"\" has no resource!");
                continue;
            }
            
            //create the dir            
            IFolder specialComponentFolder = project.getFolder(path.toString());
            specialComponentFolder = specialComponentFolder.getFolder(component.getResource());
            
            
            //createDirectory
            try {
                if (!specialComponentFolder.exists()) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("serializeCoreassets(Variant, IProgressMonitor) - Now create comp-dir:"
										+ specialComponentFolder.toString());
					}
                    specialComponentFolder.create(true, true, monitor);
                }
            } catch (CoreException e) {
                KoboldPLAMPlugin.log(e);
            }
            finally {
                //create Variant dirs in each component
                serializeVariants(component, monitor);
                
                monitor.done();
            }	    	
            
        }
    }

    
    public static void serializeVariants (Component co, IProgressMonitor monitor)
    {
        //get the CAS-directory
        //the PL directory
        //IProject project = co.getRoot().getKoboldProject().getProject();
        //IFolder casFolder = project.getFolder(co.getRoot().getResource());
        //CAS-dir
        //casFolder = casFolder.getFolder(COREASSETS_FOLDER_NAME);
        //casFolder = casFolder.getFolder(co.getResource());
        
        IPath path = getLocalPathForAsset(co);
        
        IProject project = ((Productline)co.getRoot()).getKoboldProject().getProject();

    	
        //get all vars
        List vars = co.getVariants();
        
        //for each variant
        for (Iterator it = vars.iterator(); it.hasNext();) {
            Variant variant = (Variant) it.next();
            if (variant.getResource() == null){
                logger.error("Variant \""+variant.getName()+"\" has no resource!");
                continue;
            }              
            //create the dir
            //IFolder specialVariantFolder = casFolder.getFolder(variant.getResource());
            IFolder specialVariantFolder = project.getFolder(path.toString());
            specialVariantFolder = specialVariantFolder.getFolder(variant.getResource());
            
            //createDirectory
            try {
                if (!specialVariantFolder.exists()) {
                    specialVariantFolder.create(true, true, monitor);
                }
            } catch (CoreException e) {
                KoboldPLAMPlugin.log(e);
            }
            finally {
                //create CAS dirs in each Variant
                serializeCoreassets(variant, monitor);

                monitor.done();
            }	    	
            
        }
    }
    
    public static Product retrieveProduct(Productline pl, String productId)
    {
        kobold.common.data.Product product = ServerHelper.fetchProduct(pl.getKoboldProject(), productId);
        
        if (product == null) {
            return null;
        }
        
        KoboldProject kp = pl.getKoboldProject();
        IProject project = kp.getProject();
        
        // VCM trigger
        kp.updateProduct(product, project);
        
        IFolder productFolder = project.getFolder(product.getResource());
        if (productFolder.exists()) {
            IFile modelFile = productFolder.getFile(PRODUCT_META_FILE);
            if (modelFile.exists()) {
                InputStream in;
                try {
                    in = modelFile.getContents();
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(in);
                    Product p = new Product(pl, document.getRootElement());
                    p.setRepositoryDescriptor(product.getRepositoryDescriptor());
                    return p;                    
                } catch (CoreException e) {
                    // TODO Auto-generated catch block
					logger.error("retrieveProduct(Productline, String)", e);
                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
					logger.error("retrieveProduct(Productline, String)", e);
                }
            }
        }
        
        return null;
    }
    
    public static void deleteVariantDirectory (Variant variant)
    {
    	String path = variant.getLocalPath().toOSString();
    	//deleteTree(path);
    	//
    	IProgressMonitor monitor = new NullProgressMonitor();
    	try {
            getFolderForAsset(variant).delete(true, monitor);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
			logger.error("deleteVariantDirectory(Variant)", e);
        }
    	
		if (logger.isDebugEnabled()) {
			logger.debug("deleteVariantDirectory(Variant) - Delete: " + path);
		}
    }

    public static void deleteComponentDirectory (Component comp)
    {
    	String path = comp.getLocalPath().toOSString();
    	//deleteTree(path);
       	IProgressMonitor monitor = new NullProgressMonitor();
    	try {
            getFolderForAsset(comp).delete(true, monitor);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
			logger.error("deleteComponentDirectory(Component)", e);
        }
		if (logger.isDebugEnabled()) {
			logger.debug("deleteComponentDirectory(Component) - Delete: "
					+ path);
		}
    }
    

    public static void deleteProductComponentDirectory(ProductComponent comp) {
        String path = comp.getLocalPath().toOSString();
       	IProgressMonitor monitor = new NullProgressMonitor();
    	try {
            getFolderForAsset(comp).delete(true, monitor);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
			logger
					.error("deleteProductComponentDirectory(ProductComponent)",
							e);
        }
    	//deleteTree(path);
    }

    
//    private static void deleteTree( File path )
//    {
//        File files[] = path.listFiles();
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
//                if (files[i].isDirectory()) deleteTree(files[i]);
//                files[i].delete();
//            }
//        }
//        path.delete();
//    }
//    private static void deleteTree( String path )
//    {
//      deleteTree( new File(path) );
//    }
    
    
}

