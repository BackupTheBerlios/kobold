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
	private int workflowID;
	private string comment;
	private List history;
	private List items;
	private int step;

	public void addHistoryObject(WorkflowMessage newWflowMessage){
			history.add(newWflowMessage);
	}
	
	public void addItem(WorkflowItem nWflowItem){
		items.add(nWflowItem);
	}
	
	public WorkflowMessage(int wfid, string ncomm, int nstep, List nhist, List nitems){
		workflowID = wfid;
		comment = ncomm;
		step = nstep;
		history = nhist;
		items = nitems;
		
	}
	
	public WorkflowMessage(){
		workflowID = 0;
		comment = "";
		step = 0;
		history = new ArrayList();
		items = new ArrayList();		
	}

	/**
	 * @return
	 */
	public string getComment() {
		return comment;
	}

	/**
	 * @return
	 */
	public List getHistory() {
		return history;
	}

	/**
	 * @return
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @return
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @return
	 */
	public int getWorkflowID() {
		return workflowID;
	}

	/**
	 * @param string
	 */
	public void setComment(string string) {
		comment = string;
	}

	/**
	 * @param list
	 */
	public void setHistory(List list) {
		history = list;
	}

	/**
	 * @param list
	 */
	public void setItems(List list) {
		items = list;
	}

	/**
	 * @param i
	 */
	public void setStep(int i) {
		step = i;
	}

	/**
	 * @param i
	 */
	public void setWorkflowID(int i) {
		workflowID = i;
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
