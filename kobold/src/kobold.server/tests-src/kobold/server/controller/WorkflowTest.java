/*
 * Created on 06.07.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.server.controller;
import kobold.common.data.WorkflowMessage;
import kobold.server.workflow.WorkflowEngine;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowTest {
	
	public static void main(String[] args) {
		
		WorkflowMessage msg = new WorkflowMessage("");
		WorkflowEngine.applWorkflow(msg);
	}
}
