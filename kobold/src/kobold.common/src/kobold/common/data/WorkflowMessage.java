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
	private String comment;
	private List history;
	private WorkflowItemGroup itemGroup;
	private int step;

	public void addHistoryObject(WorkflowMessage newWflowMessage){
			history.add(newWflowMessage);
	}
	
	public void addItem(WorkflowItemGroup itemGroup){
		this.itemGroup = itemGroup;
	}
	
	public WorkflowMessage(int wfid, String ncomm, int nstep, List nhist,WorkflowItemGroup itemGroup){
		workflowID = wfid;
		comment = ncomm;
		step = nstep;
		history = nhist;
		this.itemGroup = itemGroup;
		
	}
	
	public WorkflowMessage(){
		workflowID = 0;
		comment = "";
		step = 0;
		history = new ArrayList();
		itemGroup = null;		
	}

	/**
	 * @return
	 */
	public String getComment() {
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
	public void setComment(String string) {
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
   

