/*
 * Created on 18.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;
import java.util.*;
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowMessage extends KoboldMessage {
   private HashMap result;
   private String  xmlMessage;
   private List historyList  = new ArrayList();
   private String type;
   
   
	  public void setType(String str) {
		  type = str;
	  }
   
	  public String getType() {
		  return type;
	  }

}   
   
/*
public HashMap getResult() {
	return result;
}

public String getXmlMessage() {
	return xmlMessage;
}

public void setXmlMessage(String string) {
	xmlMessage = string;
}

*/
