/*
 * Created on 01.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import kobold.client.plam.model.productline.Productline;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;


/**
 * @author Tammo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ModelStorage
{
    public static Productline loadModel(IProject project)
    {
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
}
