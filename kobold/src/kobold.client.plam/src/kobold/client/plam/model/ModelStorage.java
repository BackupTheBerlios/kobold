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
 * $Id: ModelStorage.java,v 1.3 2004/07/29 15:17:49 garbeam Exp $
 *
 */
package kobold.client.plam.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import kobold.client.plam.model.product.Product;
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
    public static final String PRODUCTLINE_META_FILE = ".productlinemetainfo";
    public static final String PRODUCT_META_FILE = ".productmetainfo";
    public static final String COREASSET_META_FILE = ".coreassetmetainfo";
    
    public static final Logger logger = Logger.getLogger(ModelStorage.class);
    
    public static Productline loadModel(IProject project)
    {
        logger.debug("Loading model...");
        Productline pl = null;
        IFolder plmeta = project.getFolder(".plmeta");
        if (plmeta.exists()) {
            IFile modelFile = plmeta.getFile("model.pl.xml");
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
            System.out.println("plmeta nonexistent");

        }
        return pl;        
    }
    
    public static void storeModel(final Productline pl, final IProject project)
    {
        logger.debug("Storing model...");
        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        try {
            progressService.busyCursorWhile(new IRunnableWithProgress(){
                public void run(IProgressMonitor monitor) {
                    IFolder plmeta = project.getFolder(".plmeta");
                    
                    if (!plmeta.exists()) {
                        try {
                            plmeta.create(true, true, monitor);
                        } catch (CoreException e1) {
                            e1.printStackTrace();
                        }    
                    }
                    IFile modelFile = plmeta.getFile("model.pl.xml");
                    
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    XMLWriter writer;
                    try {
                        writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
                        writer.write(pl.serialize());
                        writer.close();
                        
                        if (modelFile.exists()) {
                            modelFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
                        } else {
                            modelFile.create(new ByteArrayInputStream(out.toByteArray()), 
                                true, monitor);
                        }
                        out.close();   
                        
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (CoreException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
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
            modulePath = asset.getName() + File.separator + modulePath;
            asset = asset.getParent();
        }
        
        AbstractRootAsset root = (AbstractRootAsset)asset;
        RepositoryDescriptor repositoryDescriptor = root.getRepositoryDescriptor();
        
        return
            new RepositoryDescriptor(repositoryDescriptor.getType(),
                    				 repositoryDescriptor.getProtocol(),
                    				 repositoryDescriptor.getHost(),
                    				 repositoryDescriptor.getRoot(),
                    				 repositoryDescriptor.getPath() + File.separator +
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
                    thePath = COREASSETS_FOLDER_NAME + File.separator +
                    		  asset.getName() + File.separator + thePath;
                }
                else {
                    thePath = asset.getName() + File.separator + thePath;
                }
            }
            else if ((asset.getType() == AbstractAsset.SPECIFIC_COMPONENT) ||
                     (asset.getType() == AbstractAsset.RELATED_COMPONENT)) {
                thePath = PRODUCTS_FOLDER_NAME + File.separator +
                		  asset.getName() + File.separator + thePath;
            }
            else {
                thePath = asset.getName() + File.separator + thePath;
            }
            
            asset = asset.getParent();
        }
        
        return new Path(root.getProject().getIProject().getLocation()
                        + File.separator + thePath);
    }

}
