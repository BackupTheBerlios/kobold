/*
 * Created on 19.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.server.workflow;

import junit.framework.TestCase;
import kobold.common.data.*;

/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class WorkflowRulesTest extends TestCase {
	public static void main(String[] args) {
		WorkflowMessage msg = new WorkflowMessage("blubb");
		msg.setSender("sänder");
		msg.setReceiver("Räsiewer");
		msg.setStep(1);
		msg.setWorkflowType("Core Group Suggestion");
		WorkflowItem cont = new WorkflowItem("Ablehnen","bla",WorkflowItem.CONTAINER);
		WorkflowItem comment = new WorkflowItem("bla","bla", WorkflowItem.TEXT);
		msg.addWorkflowControl(comment);
		msg.addWorkflowControl(cont);
		
		
		WorkflowEngine.applWorkflow(msg);
		System.out.println("buh");
	}
}
