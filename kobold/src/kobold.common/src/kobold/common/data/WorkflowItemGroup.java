package kobold.common.data;

import java.util.*;

/*
 * Created on May 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * @author pliesmn
 *
 * A WorkflowItemGroup conatains all Items, that should displayed on one Group in 
 * a Workflow Window.
 * A WokflowItemGroup can contains WorkflowItems and other WorkflowGroups
 */
public class WorkflowItemGroup {
	private List workflowItems = new ArrayList();
	public WorkflowItemGroup(){
	  	
	}
	
	public void addWorkflowItem(WorkflowItem wi) {
		this.workflowItems.add(wi);
	}
	
	public List getWorkflowItems() {
		return this.workflowItems;
	}
}
