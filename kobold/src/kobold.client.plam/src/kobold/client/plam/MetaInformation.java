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

import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
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


	public static void getMetaData(String[] args) {
		System.out.println("Metadaten");
        
        // creation of a document-object
        Document document = new Document();
        
        try {
            
            // create a writer that listens to the document
            // and directs a PDF-stream to a file
            
            PdfWriter.getInstance(document, new FileOutputStream("MetaInfo.pdf"));
            
            // open the document
            document.open();
            
            // add a paragraph to the document
            document.add(new Paragraph("Meta-Informations"));
            document.add(new Paragraph("Productline:"));	
            Productline pl = new Productline("PL");
            document.add(new Phrase(pl.getName()));
            document.add(new Phrase(pl.getDescription()));
            document.add(new Phrase("Maintainers"));
            document.add(new Paragraph("Products:"));	
                    	
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
    public void createMetaInfoForPL (Document document, Productline pLine) 
    {
	    Productline pl = new Productline("PL");
        //document.add(new Paragraph("Productline:"));	
        //document.add(new Phrase(pl.getName()));
        //document.add(new Phrase(pl.getDescription()));
        //document.add(new Phrase("Maintainers"));
        //document.add(new Paragraph("Products:"));	
                 		
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
    
    //MetaInformation for Products
    public void createMetaInfoForProduct(Document document, Product p1) 
    {
    	Product product = new Product("Product1");
        product.getName();
    	product.getDescription();
    	product.getMaintainers();
    	
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
    
    //MetaInformation for SpecificComponents	
	private void createMetaInfoForSpecificComponents(Document document, SpecificComponent spComp) 
	{
		spComp.getName();
		spComp.getDescription();
		spComp.getMaintainers();
	}
	
	private void createMetaInfoForComponent(Document document, Component comp)
	{
		comp.getName();
		comp.getDescription();
		comp.getMaintainers();	
	}
	
	private void createMetaInfoForRelatedComponent(Document document, RelatedComponent relComp) 
	{
		relComp.getName();
		relComp.getDescription();
		relComp.getMaintainers();
	}
	
	private void createMetaInfoForMaintainer(Document document, User user) 
	{
		user.getUsername();	
	}
}
