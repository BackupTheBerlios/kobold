/*
 * Created on 25.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import kobold.server.workflow.*;
import kobold.common.data.*;
import java.util.*;

public class TestDrools {
	
	public TestDrools() {
	}

	public static void main(String[] args) {

		WorkflowMessage msg = new WorkflowMessage();
		WorkflowMessage msg2 = new WorkflowMessage();
		msg.setType("Core Group Suggestion");
		msg.setSender("Sender");
		History his = new History();
		his.setStep(1);
		his.setHashtable((Hashtable)his.getHashtable().put("file", "Dateiname"));
		msg.addHistory(his);
		WorkflowEngine.applWorkflow(msg);
		System.out.println("");
		
		System.out.println("Phase 1");
		System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		System.out.println("Empfänger: " + msg.getRecipient());
		his = (History) msg.getHistoryList().get(msg.getHistoryList().size() - 1);
		System.out.println("Schritt: " + his.getStep());
		System.out.println("Regel: " + his.getFiredRule());
		System.out.println("Absender (History): " + his.getSender());
		System.out.println("Button-Wert: " + his.getHashtable().get("radioButton"));
		System.out.println("");
		
		msg.setSender("Sender2");
		History his2 = new History();
		WorkflowEngine.applWorkflow(msg);
		
		System.out.println("Phase 2");
		System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		System.out.println("Empfänger: " + msg.getRecipient());
		his2 = (History) msg.getHistoryList().get(msg.getHistoryList().size() - 1);
		System.out.println("Schritt: " + his2.getStep());
		System.out.println("Regel: " + his2.getFiredRule());
		System.out.println("Absender (History): " + his2.getSender());
		System.out.println("Button-Wert: " + his2.getHashtable().get("radioButton2"));		
		System.out.println("");
		
		msg.setSender("Sender3");
		History his3 = new History();
		WorkflowEngine.applWorkflow(msg);
		
		System.out.println("Phase 3");
		System.out.println("Workflow-Typ: " + msg.getType());
		System.out.println("XML: " + msg.getMessageText());
		System.out.println("Empfänger: " + msg.getRecipient());
		his3 = (History) msg.getHistoryList().get(msg.getHistoryList().size() - 1);
		System.out.println("Schritt: " + his3.getStep());
		System.out.println("Regel: " + his3.getFiredRule());
		System.out.println("Absender (History): " + his3.getSender());
		System.out.println("Button-Wert: " + his3.getHashtable().get("radioButton2"));		
		System.out.println("");
		
		
	}
}
