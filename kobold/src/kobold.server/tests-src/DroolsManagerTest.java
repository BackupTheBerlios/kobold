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
 * $Id: DroolsManagerTest.java,v 1.3 2004/05/15 14:45:22 garbeam Exp $
 *
 */

import junit.framework.TestCase;

import kobold.server.workflow.*;
import kobold.common.data.*;

/**
	 * @author garbeam
	 *
	 * TestCase for DroolsManager.
	 */
	
public class DroolsManagerTest extends TestCase {

	/**
	 * Constructor for DroolsManagerTest.
	 * @param arg0
	 */
	

	
	public DroolsManagerTest(String arg0) {
		
		super(arg0);
	}
	
	public void testSerialize() {

		WorkflowMessage msg = new WorkflowMessage();
		WorkflowMessage msg2 = new WorkflowMessage();
		// 	FIX: msg.setType("Core Group Suggestion");
		msg.setSender("Sender");
		WorkflowEngine.applWorkflow(msg);
		System.out.println("");
		
		System.out.println("Phase 1");
		// 	FIX: System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		// 	FIX: System.out.println("Empfänger: " + msg.getReciever());
		System.out.println("");
		
		msg.setSender("Sender2");
		WorkflowEngine.applWorkflow(msg);
		
		System.out.println("Phase 2");
		// 	FIX: System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		// 	FIX: System.out.println("Empfänger: " + msg.getReciever());
		System.out.println("");
		
		msg.setSender("Sender3");
		WorkflowEngine.applWorkflow(msg);
		
		System.out.println("Phase 3");
		// 	FIX: System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		// 	FIX: System.out.println("Empfänger: " + msg.getReciever());
		System.out.println("");
	}
}
