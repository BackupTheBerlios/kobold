/*
 * Created on 28.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package drools;
import org.drools.*;

/**
 * This class mediates between Kobold and drools. 
 * @author Bettina Druckenmueller
 */
public class Drools {

	/**
	 * Since drools is going to be of the Singleton concept, theInstance 
	 * will be the only available instance of the class.
	 */
	private Drools theInstance;

	/**
	 * The rulebase for the Kobold rules
	 */
	private RuleBase ruleBase;

	/**
	 * The corresponing working memory of the rulebase
	 */
	private WorkingMemory workingMemory;

	/**
	 * The interface that allows Kobold to create and work with the 
	 * drools instance.
	 * @return the current instance of drools
	 */
	public Drools getTheInstance() {
		return theInstance;
	}

	/**
	 * The get-method for rulebase
	 * @return the rulebase
	 */
	private RuleBase getRuleBase() {
		return ruleBase;
	}

	/**
	 * The set-method for rulebase
	 * @param ruleBase
	 */
	private void setRuleBase(RuleBase ruleBase) {
		this.ruleBase = ruleBase;
	}

	/**
	 * The get-method for workingmemory
	 * @return the workingmemory
	 */
	private WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

	/**
	 * The set-method for workingmemory
	 * @param workingMemory
	 */
	private void setWorkingMemory(WorkingMemory workingMemory) {
		this.workingMemory = workingMemory;
	}

	/**
	 * Loads the Kobold rulebase from an external file.
	 */
	private void loadRuleBase() {
	}

	/**
	 * Adds a workflowmessage object to the workingmemory.
	 * @param msg a workflowmessage object
	 */
	private void assertObject(WorkflowMessage msg) {
	}

	/**
	 * Deletes a workflowmessage object from the workingmemory.
	 * @param msg a workflowmessage object
	 */
	private void retractObject(WorkflowMessage msg) {
	}

	/**
	 * Changes a workflowmessage object inside the workingmemory.
	 * @param msg a workflowmessage object
	 */
	private void modifyObject(WorkflowMessage msg) {
	}

	/**
	 * Used to refresh the rulebase whenever it's changed during
	 * the runtime of drools. This method is especially for
	 * testing purposes.  
	 */
	public void refreshRuleBase() {
	}

	/**
	 * The interface that allows Kobold to transfer a 
	 * workflowmessage object. It adds the object to the 
	 * workingmemory and fires all rules. Afterwards the method
	 * determines what will happen to the object. It will either
	 * be deleted or transfered to the messagequeues of the
	 * recipients.
	 * @param msg a workflowmessage object
	 */
	public void applWorkflow(WorkflowMessage msg) {
	}

}
