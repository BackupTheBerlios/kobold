/*
 * Created on 14.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam;

import java.io.FileOutputStream;
import java.io.IOException;
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
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
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
            
            //add a paragraph to the document
            document.add(new Paragraph(36, "Meta-Informations"));
            
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
            
            // DEBUG stuff
            Productline pl = new Productline();
            pl.setName("PL");
            createMetaInfoForPL(document, pl);
                             	
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
        document.add(new Paragraph("Productline:"));
       
        document.add(new Phrase("Name: " + pl.getName() + "\n"));
        document.add(new Phrase("Discription: " + pl.getDescription()+"\n"));
        document.add(new Phrase("ID: " + pl.getId()+"\n"));
        
        
        document.add(new Paragraph("Products:"));
    	for (Iterator ite = pl.getProducts().iterator(); 
		ite.hasNext();)
        {
			Product product = (Product) ite.next();
			createMetaInfoForProduct (document, product);
		}
    
    	document.add(new Paragraph("Components"));
    	for (Iterator ite = pl.getComponents().iterator(); 
		ite.hasNext();)
		{
			Component comp = (Component) ite.next();
			createMetaInfoForComponent (document, comp);
		}	
		
    	document.add(new Paragraph("Maintainers"));
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
    	document.add(new Phrase(product.getName()));
        document.add(new Phrase(product.getDescription()));
        document.add(new Phrase(product.getId()));
        
       	//product.getMaintainers();
    	
        document.add(new Paragraph("RelatedComponents"));
    	for (Iterator ite = product.getRelatedComponents().iterator(); 
		ite.hasNext();)
		{
			RelatedComponent relComp = (RelatedComponent) ite.next();
			createMetaInfoForRelatedComponent(document, relComp);
		}
    	
    	document.add(new Paragraph("SpezificComponents"));
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
    	document.add(new Phrase(spComp.getName()));
        document.add(new Phrase(spComp.getDescription()));
        document.add(new Phrase(spComp.getId()));
		
        document.add(new Paragraph("Maintainers"));
		for (Iterator ite = spComp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
		
		document.add(new Paragraph("FileDescriptors"));
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
    	document.add(new Phrase(relComp.getName()));
        document.add(new Phrase(relComp.getDescription()));
        document.add(new Phrase(relComp.getId()));
		//relComp.getMaintainers();
		
        document.add(new Paragraph("Maintainers"));
		for (Iterator ite = relComp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
		
		document.add(new Paragraph("FileDescriptors"));
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
    	document.add(new Phrase(user.getUsername()));	
    		
	}
	
	// MetaInformation for Components
	private void createMetaInfoForComponent(Document document, Component comp)
		throws DocumentException
	{
    	document.add(new Phrase(comp.getName()));
        document.add(new Phrase(comp.getDescription()));
        document.add(new Phrase(comp.getId()));
		//comp.getMaintainers();
		
        document.add(new Paragraph("Maintainers"));
		for (Iterator ite = comp.getMaintainers().iterator(); 
		ite.hasNext();)
		{
			User user = (User) ite.next();
        	createMetaInfoForMaintainer (document, user);
        }
		
		document.add(new Paragraph("Variants"));
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
    	document.add(new Phrase(var.getName()));
        document.add(new Phrase(var.getDescription()));
        document.add(new Phrase(var.getId()));		
		
        document.add(new Paragraph("Components"));
		for (Iterator ite = var.getComponents().iterator();
		ite.hasNext();)
		{
			Component comp = (Component) ite.next();
			createMetaInfoForComponent(document, comp);
		}
		
		document.add(new Paragraph("Releases"));
		for (Iterator ite = var.getReleases().iterator();
		ite.hasNext();)
		{
			Release release = (Release) ite.next();
			createMetaInfoForReleases(document, release);
		}
		
		document.add(new Paragraph("FileDescriptors"));
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
		document.add(new Phrase(fileDes.getLocalPath().toString()));
        document.add(new Phrase(fileDes.getLastChange().toString()));
        document.add(new Phrase(fileDes.getRevision()));
				
	}
	
	// MetaInformation for Releases
	private void createMetaInfoForReleases(Document document, Release release)
		throws DocumentException
	{
		document.add(new Phrase(release.getName()));
		document.add(new Phrase(release.getDescription()));
		document.add(new Phrase(release.getId()));
				
	}
	
}
