/*
 * Created on 14.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

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

	public void getMetaData(AbstractAsset asset) {
		System.out.println("Metadaten");
        
        // creation of a document-object
        Document document = new Document();
        
        try {
            
            // create a writer that listens to the document
            // and directs a PDF-stream to a file
            
            PdfWriter.getInstance(document, new FileOutputStream("MetaInfo.pdf"));
            
            // open the document
            document.open();
            
            // titlepage
            document.add(new Paragraph("Meta-Informations",
                	FontFactory.getFont(FontFactory.HELVETICA, 40, Font.ITALIC)));
            document.add(new Paragraph(new Date().toString()));
            
           /* try 
            {
                Watermark watermark = new Watermark(Image.getInstance("icons/kobold_persp.gif"), 200, 420);
                document.add(watermark);
            }
            catch(Exception e) {
                System.err.println("Are you sure you have the file 'icons/kobold_persp.gif' in the right path?");
            }
           */ 
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
            
           /* // DEBUG stuff
            Productline pl = new Productline();
            pl.setName("PL");
            createMetaInfoForPL(document, pl);
           */                  	
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
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
         
        document.add(new Phrase("    Discription: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
        Table table = new Table(1,1);  
        table.setBorderWidth(0);
        table.setPadding(4);
        table.setSpacing(4);
        table.addCell(pl.getDescription());
        document.add(table);
        document.add(new Phrase("\n\n"));
                
    	for (Iterator ite = pl.getProducts().iterator(); 
		ite.hasNext();)
        {
			Product product = (Product) ite.next();
			createMetaInfoForProduct (document, product);
		}
    
    	for (Iterator ite = pl.getComponents().iterator(); 
		ite.hasNext();)
		{
			Component comp = (Component) ite.next();
			createMetaInfoForComponent (document, comp);
		}	
		
    	for (Iterator ite = pl.getMaintainers().iterator();
        ite.hasNext();)
        {
        	User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
	
    }
    
    // MetaInformation for Products
    private void createMetaInfoForProduct(Document document, Product product) 
    	throws DocumentException
    {
    	document.add(new Phrase("Product:  ",
                FontFactory.getFont(FontFactory.HELVETICA, 18)));
    	document.add(new Phrase(product.getName(),
		    FontFactory.getFont(FontFactory.HELVETICA, 18, Font.ITALIC)));
		document.add(new Phrase("\n\n"));
		document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(product.getId() + "\n"));
	     
	    document.add(new Phrase("    Discription: ",
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
    	document.add(new Phrase("SpecificComponent: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(spComp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(spComp.getId()+"\n"));
	     
	    document.add(new Phrase("    Discription: ",
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
    	document.add(new Phrase("RelatedComponent: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(relComp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(relComp.getId()+"\n"));
	     
	    document.add(new Phrase("    Discription: ",
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
    	document.add(new Phrase("Maintainer: ",
                FontFactory.getFont(FontFactory.HELVETICA, 14)));
    	document.add(new Phrase(user.getUsername(),
	        FontFactory.getFont(FontFactory.HELVETICA, 14, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
    	
	}
	
	// MetaInformation for Components
	private void createMetaInfoForComponent(Document document, Component comp)
		throws DocumentException
	{
		document.add(new Phrase("Component: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(comp.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(comp.getId()+"\n"));
	     
	    document.add(new Phrase("    Discription: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(comp.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n\n"));
	   
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
		document.add(new Phrase("Variant: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(var.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(var.getId()+"\n"));
	     
	    document.add(new Phrase("    Discription: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(var.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n\n"));
	          
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
		document.add(new Phrase("FileDescriptor: ",
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
		document.add(new Phrase("Release: ",
                FontFactory.getFont(FontFactory.HELVETICA, 16)));
    	document.add(new Phrase(release.getName(),
	        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.ITALIC))); 
	    document.add(new Phrase("\n"));
	    document.add(new Phrase("    ID: ",
            FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    document.add(new Phrase(release.getId()+"\n"));
	     
	    document.add(new Phrase("    Discription: ",
	        FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC)));
	    Table table = new Table(1,1);  
	    table.setBorderWidth(0);
	    table.setPadding(4);
	    table.setSpacing(4);
	    table.addCell(release.getDescription());
	    document.add(table);
	    document.add(new Phrase("\n\n"));
	    
	}
	
}
