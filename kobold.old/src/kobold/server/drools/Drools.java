/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: Drools.java,v 1.3 2004/03/30 23:52:26 vanto Exp $
 *
 */
package kobold.server.drools;
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
