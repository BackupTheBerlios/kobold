/*
 * Created on 24.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.useractions;

import kobold.client.plam.controller.*;
import kobold.common.io.RepositoryDescriptor;
import kobold.client.plam.*;
import kobold.common.data.*;
/**
 * @author grosseml
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class productactions {
	
	public productactions(){
		
	}
	
	public void newProduct(UserContext userContext, String productName, String productLineName){
        SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
        
        Product prod1 = new Product(productName, productLineName,
        							new RepositoryDescriptor("cvs", "blafasel.org",
        									"/root/blubber"));
        
        client.addProduct(currUser, prod1);        
	}
	
	public void changeProduct(UserContext userContext, String productName) {
	
	}
}
