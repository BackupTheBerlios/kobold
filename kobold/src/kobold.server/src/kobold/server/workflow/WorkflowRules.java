/*
 * Created on 10.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.server.workflow;


import org.drools.rule.*;
import kobold.common.data.*;
import org.drools.semantics.java.*;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowRules {
	
	protected WorkflowRules () {
	}
	
	protected static RuleSet createRuleSet() throws Exception {
		RuleSet set = new RuleSet("Kobold");
		WorkflowMessage msg = new WorkflowMessage("bla");
		/**
		 * Rule No.1
		 */
		Rule no1 = new Rule("Core Group Suggestion - Step 1");
		
		Declaration dec1 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no1.addCondition(new ExprCondition("(msg.getWorkflowId().equals(\"Core Group Suggestion\")) && (msg.getStep == 1)", new Declaration[] {dec1}));
		no1.addParameterDeclaration(dec1);
		no1.setConsequence(new BlockConsequence(""+
			"WorkflowMessage answer = new WorkflowMessage(msg.getWorkflowId());"+
			"answer.addParentId(msg.getId());"+	//add the actual parent as history
			"answer.setSender(msg.getSender());"+	
			"answer.setReceiver(msg.getReceiver());"+
			"answer.setSubject(\"Antrag auf Commit eines Coregroup Items\");"+
			"answer.setMessageText(\"Der Mitarbeiter \"+msg.getSender() + \" würde gerne" +
					"ein Commit in die Coregroup machen. Bitte wählen Sie daher in " +
					"untenstehenden Optionen.\");"+
			
			//creating necessary workflow items
			//containing 2 radio buttons (forward, deny)
			//and a textbox for comments
			"WorkflowItem text = new WorkflowItem(\"\",\"Ihr Kommentar:\",\"TEXT\");"+
			"WorkflowItem rb1 = new WorkflowItem(\"true\",\"An zuständigen PE weiterleiten\", \"RADIO\");"+
			"WorkflowItem rb2 = new WorkflowItem(\"false\",\"Ablehnen\", \"RADIO\");"+
			"WorkflowItem cont = new WorkflowItem(null,\"Entscheidung\",\"CONTAINER\");"+
			"cont.addChild(rb1);"+
			"cont.addChild(rb2);"+
			
			"answer.addWorkflowControl(cont);"+
			"answer.addWorkflowControl(text);"));
/*			"msg.addHistory(msg.newHistory());" +			"if (msg.getHistoryList().size() > 1) {" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(msg.getHistoryList().get(msg.getHistoryList().size() - 2).getStep() + 1);" +			"}" +			"else {" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(1);" +			"}" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setFiredRule(\"Core Group Suggestion - Step 1\");" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setSender(msg.getSender());" +			"msg.setRecipient(\"Recipient\");" +			"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
				"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
				"<workflow>" + 
				"<line><header>Vorschlag für eine neue Core Group Datei</header></line>" + 
				"<line></line>" + 
				"<line><text>Würde gerne eine Datei in eine Core Group hinzufügen.</text></line>" + 
				"<line><interaction name=\\\"radioButton\\\" value=\\\"yes\\\" type=\\\"radio\\\">weiterleiten</interaction></line>" +
				"<line><interaction name=\\\"radioButton\\\" value=\\\"no\\\" type=\\\"radio\\\">ablehnen</interaction></line>" +
				"<line><text>Ihr Kommentar: </text><textfield name=\\\"comment\\\"/></line>" +
				"<line><interaction type=\\\"submit\\\">abschicken</interaction></line>" +
				"</workflow>\");" +			"java.util.Hashtable hash = new Hashtable();" +			"hash = msg.getHistoryList().get(msg.getHistoryList().size() - 1).getHashtable();" +			"hash.put(\"radioButton\", \"yes\");" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setHashtable(hash);"));
*/		
		
		
		/**
		 * Rule No.2
		 */
		Rule no2 = new Rule("Core Group Suggestion - Step 2");
		
		Declaration dec2 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no2.addCondition(new ExprCondition("(msg.getWorkflowId().equals(\"Core Group Suggestion\")) && (msg.getStep == 2)", new Declaration[] {dec2}));
		no2.addParameterDeclaration(dec1);
		//no2.setConsequence(new BlockConsequence(
		WorkflowMessage answer = new WorkflowMessage(msg.getWorkflowType());
		answer.setSender(msg.getSender());
		answer.addParentId(msg.getId());
		
		//analyzing the answer
		String decision = "";
		String comment = "";
		WorkflowItem controls[] = msg.getWorkflowControls();
		for(int i = 0; i < controls.length;i++){
			if (controls[i].getType().equals("TEXT")){
				comment = controls[i].getValue();
			}
			if (controls[i].getType().equals("CONTAINER")){
				WorkflowItem container[] = controls[i].getChildren();
				for (int j = 0; j < container.length;j++){
					if (container[j].getValue().equals("true")){
						decision = container[j].getDescription();
					}
				}
			}
		}
		
		if (decision.equals("An zuständigen PE weiterleiten")){
			answer.setReceiver("garbeam");
			answer.setSubject("Ein Vorschlag für ein neues Core Group Item ist eingegangen");
			answer.setMessageText("Es ist ein neuer Vorschlag für ein Core Group Item eingegangen," +
					"der zuständige PE schrieb dazu: " + comment);
			
			//add decisions for core group PE
			WorkflowItem radio1 = new WorkflowItem("false","Vorschlag zustimmen","RADIO");
			WorkflowItem radio2 = new WorkflowItem("true","Vorschlag ablehnen","RADIO");
			WorkflowItem comm = new WorkflowItem("","Bitte hier ein Kommentar einfügen:","TEXT");
			WorkflowItem container = new WorkflowItem(null,"Entscheidung","CONTAINER");
			container.addChild(radio1);
			container.addChild(radio2);
			answer.addWorkflowControl(comm);
			answer.addWorkflowControl(container);
		}
		
		/*	"msg.addHistory(msg.newHistory());" +
			"if (msg.getHistoryList().size() > 1) {" +
				"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(msg.getHistoryList().get(msg.getHistoryList().size() - 2).getStep() + 1);" +
			"}" +
			"else {" +
				"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(1);" +
			"}" +
			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setFiredRule(\"Core Group Suggestion - Step 2\");" +
			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setSender(msg.getSender());" +			"java.util.Hashtable hash = msg.getHistoryList().get(msg.getHistoryList().size() - 2).getHashtable();" +			"java.lang.String str = (String) hash.get(\"radioButton\");" +			"if (str != null) {" +			"if (str.equals(\"yes\")) {" +				"msg.setRecipient(\"Recipient2\");" +
				"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
					"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
					"<workflow>" + 
					"<line><header>Vorschlag für eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Eine Datei wurde für eine Core Group vorgeschlagen.</text></line>" + 
					"<line><interaction name=\\\"radioButton2\\\" value=\\\"yes\\\" type=\\\"radio\\\">zustimmen</interaction></line>" +
					"<line><interaction name=\\\"radioButton2\\\" value=\\\"no\\\" type=\\\"radio\\\">ablehnen</interaction></line>" +
					"<line><text>Ihr Kommentar: </text><textfield name=\\\"comment\\\"/></line>" +
					"<line><interaction type=\\\"submit\\\">abschicken</interaction></line>" +
					"</workflow>\");" +
				"java.util.Hashtable hash = new Hashtable();" +
				"hash = msg.getHistoryList().get(msg.getHistoryList().size() - 1).getHashtable();" +
				"hash.put(\"radioButton2\", \"yes\");" +
				"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setHashtable(hash);" +			"}" +			"else {" +				"msg.setRecipient(\"Recipient3\");" +
				"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
					"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
					"<workflow>" + 
					"<line><header>Ihr Vorschlag für eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Die Datei wurde leider abgelehnt.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">bestätigen</interaction></line>" +
					"</workflow>\");" +			"}}" +			"else {" +			"	msg.setRecipient(\"Recipient3\");" +			"	msg.setMessageText(\"Der Button-Wert ist null.\");" +			"}"));
		*/
		
		
		
		
		/**
		 * Rule No.3
		 */
		Rule no3 = new Rule("Core Group Suggestion - Step 3");
		
		Declaration dec3 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no3.addCondition(new ExprCondition("(msg.getType().equals(\"Core Group Suggestion\")) && (msg.getHistoryList().get(msg.getHistoryList().size() - 1).getStep() == 3)", new Declaration[] {dec3}));
		no3.addParameterDeclaration(dec1);
		no3.setConsequence(new BlockConsequence(
			"msg.addHistory(msg.newHistory());" +
			"if (msg.getHistoryList().size() > 1) {" +
				"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(msg.getHistoryList().get(msg.getHistoryList().size() - 2).getStep() + 1);" +
			"}" +
			"else {" +
				"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(1);" +
			"}" +
			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setFiredRule(\"Core Group Suggestion - Step 3\");" +
			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setSender(msg.getSender());" +
			"java.util.Hashtable hash = msg.getHistoryList().get(msg.getHistoryList().size() - 2).getHashtable();" +
			"java.lang.String str = (String) hash.get(\"radioButton2\");" +
			"if (str != null) {" +
			"if (str.equals(\"yes\")) {" +
				"msg.setRecipient(\"Recipient4+5\");" +
				"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
					"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
					"<workflow>" + 
					"<line><header>Vorschlag für eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Der Vorschlag wurde angenommen.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">bestätigen</interaction></line>" +
					"</workflow>\");" +
			"}" +
			"else {" +
				"msg.setRecipient(\"Recipient5+4\");" +
				"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
					"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
					"<workflow>" + 
					"<line><header>Ihr Vorschlag für eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Die Datei wurde leider abgelehnt.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">bestätigen</interaction></line>" +
					"</workflow>\");" +
			"}}" +
			"else {" +
			"	msg.setRecipient(\"Recipient5+4\");" +
			"	msg.setMessageText(\"Der Button-Wert ist null.\");" +
			"}"));
				
		
		set.addRule(no1);
		set.addRule(no2);
		set.addRule(no3);
		return set;
	}

}
