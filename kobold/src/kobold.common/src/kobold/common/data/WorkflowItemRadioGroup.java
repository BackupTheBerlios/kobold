/*
 * Created on May 14, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;

/**
 * @author pliesmn
 *
 * The WorkflowRadioGroup contains the Radiobuttons, that should be 
 * displayed together. It can also contain the other WorkflowItems. 
 * Additional to the WorkflowItemGroup the WorkflowItemRadioGroup
 * contains the name, that are needed to store the value of the 
 * selected Radiobutton.
 * 
 */
public class WorkflowItemRadioGroup extends WorkflowItemGroup {
	private String name;	
	
	public WorkflowItemRadioGroup(String name){
	  	this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
}
