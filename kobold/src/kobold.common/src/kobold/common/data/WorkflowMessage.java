/*
 * Created on 18.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;
import java.util.*;

import org.dom4j.Element;
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowMessage extends KoboldMessage {
	
	protected static final String TYPE = "workflow";
	private String workflowId;
	private String comment;
	private Set parents = new HashSet();
	private List controlItems = new LinkedList();
	private HashMap answer; 


	public WorkflowMessage()
	{
		super("wmesg");
	}
	
	public WorkflowMessage(Element data)
	{
		super(data);
	}
	
	public void addParentId(String id)
	{
		parents.add(id);
	}
	
	public String[] getParentIds()
	{
		return (String[])parents.toArray(new String[0]);	
	}
	
	public void addWorkflowControl(WorkflowItem item)
	{
		controlItems.add(item);
	}
	
	public WorkflowItem[] getWorkflowControl()
	{
		return (WorkflowItem[])controlItems.toArray(new WorkflowItem[0]);
	}
	
	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * Sets the Workflow ID. 
	 * This ID describes, which Workflow should be applied by Drools

	 * @return workflow id
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
	}

	/**
	 * Sets the Workflow ID. 
	 * This ID describes, which Workflow should be applied by Drools
	 */
	public void setWorkflowId(String id) {
		workflowId = id;
	}

	/**
	 * @return
	 */
	public HashMap getAnswer() {
		return answer;
	}

	/**
	 * @param map
	 */
	public void setAnswer(HashMap map) {
		answer = map;
	}

	/**
	 * @see kobold.common.data.KoboldMessage#deserialize(org.dom4j.Element)
	 */
	protected void deserialize(Element data) 
	{
		super.deserialize(data);
	}

	/**
	 * @see kobold.common.data.KoboldMessage#serialize()
	 */
	public Element serialize() 
	{
		Element el = super.serialize();
		el.addElement("workflow-id").setText(workflowId);
		el.addElement("comment").addCDATA(comment);
		
		Element hist = el.addElement("history");
		Iterator it = parents.iterator();
		while (it.hasNext()) {
			String id = (String)it.next();
			hist.addElement("parent").setText(id);
		}
		
		Element controls = el.addElement("controls");
		it = controlItems.iterator();
		while (it.hasNext()) {
			WorkflowItem control = (WorkflowItem)it.next();
			controls.add(control.serialize());
		}
		
		// TODO store answers
		/*Element answers = el.addElement("results");
		it = answer.*/
		return el;
	}

}   
   

