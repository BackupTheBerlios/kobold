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
 * $Id: WorkflowEngine.java,v 1.7 2004/06/25 18:32:52 bettina Exp $
 *
 */
package kobold.server.workflow;

import kobold.common.data.RPCSpy;
import kobold.common.data.WorkflowMessage;
import org.drools.*;
import org.drools.rule.*;
import java.util.*;


/**
 * This class mediates between Kobold and drools.
 * Method applWorkflow implements the interface for WorkflowMessages
 * Method applRPCSpy implements the interface for rules written by the user 
 * @author Bettina
 */
public class WorkflowEngine {

	/**
	 * Since drools is going to be of the Singleton concept, theInstance 
	 * will be the only available instance of the class.
	 */
	private static WorkflowEngine theInstance;

	/**
	 * The rulebase for the Kobold rules
	 */
	private RuleBase ruleBase;

	/**
	 * The corresponing working memory of the rulebase
	 */
	private WorkingMemory workingMemory;

	/**
	 * the constructer of the class; declared protected so that no other 
	 * class can create a workflowengine object
	 */
	protected WorkflowEngine() {		
	}
	
	/**
	 * Gets the current Instance of WorkflowEngine
	 * @return the current instance of the WorkflowEngine
	 */
	public static WorkflowEngine getInstance() {
		if (theInstance == null) {
			theInstance = new WorkflowEngine();
			theInstance.loadRuleBase();
			RuleBase base = theInstance.getRuleBase();
			if (base == null) {
				System.out.println("Huhu");
			}
			theInstance.setWorkingMemory(base.newWorkingMemory());
		}
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
		try {
			RuleSet ruleSet = WorkflowRules.createRuleSet();
			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.addRuleSet(ruleSet);
			this.setRuleBase(builder.build());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Adds a workflowmessage object to the workingmemory.
	 * @param msg a workflowmessage object
	 */
	private void assertObject(Object obj) {
		WorkingMemory memory = this.getWorkingMemory();
		try {
			memory.assertObject(obj);
		} catch (FactException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a workflowmessage object from the workingmemory.
	 * @param obj a workflowmessage object
	 */
	private void retractObject(Object obj) {
		WorkingMemory memory = this.getWorkingMemory();
		List list = memory.getObjects();
		if (list.contains(obj)) {
			list.remove(obj);
		}
	}

	/**
	 * The interface that allows Kobold to transfer a 
	 * RPCSpy object. It adds the object to the 
	 * workingmemory and fires all rules. Afterwards the method
	 * determines what will happen to the object. It will *always*
	 * be deleted. You've to apply new Workflows within the rule
	 * which may match this RPCSpy object.
	 * @param rpcSpy the sniffed RPC call object.
	 */
	public static void applRPCSpy(RPCSpy rpcSpy) {
		WorkflowEngine engine = getInstance();
		engine.assertObject(rpcSpy);
		WorkingMemory memory = engine.getWorkingMemory();
		try {
			memory.fireAllRules();
		} catch (FactException e) {
			e.printStackTrace();
		}
		engine.retractObject(rpcSpy);
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
	public static void applWorkflow(WorkflowMessage msg) {
		WorkflowEngine engine = getInstance();
		engine.assertObject(msg);
		WorkingMemory memory = engine.getWorkingMemory();
		try {
			memory.fireAllRules();
		} catch (FactException e) {
			e.printStackTrace();
		}
		engine.retractObject(msg);
	}
}
