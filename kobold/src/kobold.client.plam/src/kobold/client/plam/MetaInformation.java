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
 * $Id: MetaInformation.java,v 1.18 2004/08/25 09:52:00 neco Exp $
 *
 */
package kobold.client.plam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.common.data.User;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Necati Aydin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MetaInformation {

	private IPath filePath;
	public static final Log logger = LogFactory.getLog(MetaInformation.class);	
	
	public MetaInformation(IPath filePath) {
		this.filePath = filePath;
	}

	public void getMetaData(AbstractAsset asset) {
		logger.debug("Metadaten");
        
        // creation of a document-object
        Document document = new Document();
        
        try {
            
            // create a writer that listens to the document
            // and directs a PDF-stream to a file
            
            PdfWriter.getInstance(document, new FileOutputStream(filePath.toOSString()));
            
            // open the document
            document.open();
            
            // titlepage
            document.add(new Paragraph("Asset-Informations",
                	FontFactory.getFont(FontFactory.HELVETICA, 40, Font.ITALIC)));
            document.add(new Paragraph(new Date().toString()));
            document.add(new Phrase("\n\n\n\n\n\n"));
//            Image logo = Image.getInstance(
//                    KoboldPLAMPlugin.getImageDescriptor("icons/Kobold_logo.gif").getImageData());
//            logo.scaleAbsolute(500, 300);
//            logo.setAlignment(Image.MIDDLE);
//            document.add(logo);
 
            HeaderFooter footer = new HeaderFooter(new Phrase("Page: "), true);
            	document.setFooter(footer);
            // new page	
            document.newPage();
            
            // determine which asset we create metainfo for
            if (asset instanceof Productline) {
            	createMetaInfoForPL(document, (Productline)asset);
            }
            else if (asset instanceof Product){
            	createMetaInfoForProduct(document, (Product)asset);
            }
            else if (asset instanceof Component){
            	createMetaInfoForComponent(document, (Component)asset);
            }
            else if (asset instanceof RelatedComponent){
            	createMetaInfoForRelatedComponent(document, (RelatedComponent)asset);
            }
            else if (asset instanceof SpecificComponent){
            	createMetaInfoForSpecificComponents(document, (SpecificComponent)asset);
            }
            else if (asset instanceof Variant){
            	createMetaInfoForVariants(document, (Variant)asset);
            }
            else {
                logger.debug("unknown asset Type");
            }
            
           /* // DEBUG stuff
            Productline pl = new Productline();
            pl.setName("PL");
            createMetaInfoForPL(document, pl);
           */                  	
        }
        catch(DocumentException de) {
    		logger.error("Metadaten", de);
        }
        catch(IOException ioe) {
    		logger.error("Metadaten", ioe);
            System.err.println(ioe.getMessage());
        }
        
        // close the document
        document.close();
    }
 	//MetaInformation for Productlines
    private void createMetaInfoForPL (Document document, Productline pl) 
    	throws DocumentException
    {
    	document.add(new Phrase("Productline: ",
    		FontFactory.getFont(FontFactory.HELVETICA, 20)));
        document.add(new Phrase(pl.getName(),
                FontFactory.getFont(FontFactory.HELVETICA, 20, Font.ITALIC))); 
        document.add(new Phrase("\n\n"));
        document.add(new Phrase("    ID: ",
                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
        document.add(new Phrase(pl.getId()+"\n"));
         
        document.add(new Phrase("    Description: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
        Table table = new Table(1,1);  
        table.setBorderWidth(0);
        table.setPadding(4);
        table.setSpacing(4);
        table.addCell(pl.getDescription());
        document.add(table);
        document.add(new Phrase("\n\n"));
                
        for (Iterator ite = pl.getMaintainers().iterator();
        ite.hasNext();)
        {
        	User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
        
    	for (Iterator ite = pl.getComponents().iterator(); 
		ite.hasNext();)
		{
			Component comp = (Component) ite.next();
			createMetaInfoForComponent (document, comp);
		}
    	    	    
    	for (Iterator ite = pl.getProducts().iterator(); 
		ite.hasNext();)
        {
			Product product = (Product) ite.next();
			createMetaInfoForProduct (document, product);
		}
    	
    }
    
    // MetaInformation for Products
    private void createMetaInfoForProduct(Document document, Product product) 
    	throws DocumentException
    {
    	document.add(new Phrase("\n"));
    	document.add(new Phrase("Product:  ",
                FontFactory.getFont(FontFactory.HELVETICA, 18)));
    	document.add(new Phrase(product.getName(),
		    FontFactory.getFont(FontFactory.HELVETICA, 18, Font.ITALIC)));
		document.add(new Phrase("\n\n"));
		document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(product.getId() + "\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(product.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n\n"));
	  	  
       	//product.getMaintainers();
    	
    	for (Iterator ite = product.getRelatedComponents().iterator(); 
		ite.hasNext();)
		{
			RelatedComponent relComp = (RelatedComponent) ite.next();
			createMetaInfoForRelatedComponent(document, relComp);
		}
    	    	
    	for (Iterator ite = product.getSpecificComponents().iterator(); 
		ite.hasNext();)
		{
    		SpecificComponent spComp = (SpecificComponent) ite.next();
			createMetaInfoForSpecificComponents(document, spComp);
		}	
	
    }
    
    // MetaInformation for SpecificComponents	
    private void createMetaInfoForSpecificComponents(Document document, SpecificComponent spComp) 
    	throws DocumentException
    {	
    	document.add(new Phrase("\n"));
    	document.add(new Phrase("Specific Component: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(spComp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(spComp.getId()+"\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(spComp.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n\n"));
	    
	    for (Iterator ite = spComp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
				
		for (Iterator ite = spComp.getFileDescriptors().iterator(); 
		ite.hasNext();)
		{
			FileDescriptor fileDes = (FileDescriptor) ite.next();
        	createMetaInfoForFileDescriptors (document, fileDes);
        }
	}
			
	// MetaInformation for RelatedComponents
    private void createMetaInfoForRelatedComponent(Document document, RelatedComponent relComp) 
    	throws DocumentException
    {
    	document.add(new Phrase("\n"));
    	document.add(new Phrase("Related Component: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(relComp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(relComp.getId()+"\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(relComp.getDescription());
	    document.add(table);
	    	          
        for (Iterator ite = relComp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
		
		for (Iterator ite = relComp.getFileDescriptors().iterator(); 
		ite.hasNext();)
		{
			FileDescriptor fileDes = (FileDescriptor) ite.next();
        	createMetaInfoForFileDescriptors (document, fileDes);
        }
	}
	
	// MetaInformation for Maintainer
    private void createMetaInfoForMaintainer(Document document, User user) 
    	throws DocumentException
    {
    	document.add(new Phrase("    Maintainer: ",
                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
    	document.add(new Phrase(user.getUsername())); 
	    document.add(new Phrase("\n"));
    	
	}
	
	// MetaInformation for Components
	private void createMetaInfoForComponent(Document document, Component comp)
		throws DocumentException
	{
		document.add(new Phrase("\n"));
		document.add(new Phrase("Core Asset: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(comp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(comp.getId()+"\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(comp.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n"));
	    
	    document.add(new Phrase("    Status: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(comp.getStatusSet().toString() + "\n"));
		     
		for (Iterator ite = comp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
		
    	for (Iterator ite = comp.getVariants().iterator(); 
		ite.hasNext();)
		{
			Variant var = (Variant) ite.next();
			createMetaInfoForVariants(document, var);
		}
	}
	
	// MetaInformation for Variants
	private void createMetaInfoForVariants(Document document, Variant var) 
		throws DocumentException
	{
		document.add(new Phrase("\n"));
		document.add(new Phrase("Variant: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(var.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(var.getId()+"\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(var.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    Status: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(var.getStatusSet().toString() + "\n"));
		
	          
		for (Iterator ite = var.getComponents().iterator();
		ite.hasNext();)
		{
			Component comp = (Component) ite.next();
			createMetaInfoForComponent(document, comp);
		}
		
		for (Iterator ite = var.getReleases().iterator();
		ite.hasNext();)
		{
			Release release = (Release) ite.next();
			createMetaInfoForReleases(document, release);
		}
		
		for (Iterator ite = var.getFileDescriptors().iterator(); 
		ite.hasNext();)
		{
			FileDescriptor fileDes = (FileDescriptor) ite.next();
        	createMetaInfoForFileDescriptors (document, fileDes);
        }
	}
	
	// MetaInformation for FileDescriptors
	private void createMetaInfoForFileDescriptors(Document document, FileDescriptor fileDes)
		throws DocumentException
	{
		document.add(new Phrase("File Descriptor: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(fileDes.getFilename(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
    	document.add(new Phrase("    Local path: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC))); 
	    document.add(new Phrase(fileDes.getLocalPath().toString() + "\n"));
	    document.add(new Phrase("    Date of last change: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(fileDes.getLastChange().toString() + "\n"));   
	    document.add(new Phrase("    Revision: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(fileDes.getRevision()+"\n\n"));
	     
	}
	
	// MetaInformation for Releases
	private void createMetaInfoForReleases(Document document, Release release)
		throws DocumentException
	{
		document.add(new Phrase("\n"));
		document.add(new Phrase("Release: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(release.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(release.getId()+"\n"));
	     
	    document.add(new Phrase("    Description: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(release.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    Status: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(release.getStatusSet().toString() + "\n"));
		
	    
	}
	
}
