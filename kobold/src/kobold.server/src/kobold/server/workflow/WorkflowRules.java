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
		
		/**
		 * Rule No.1
		 */
		Rule no1 = new Rule("Core Group Suggestion - Step 1");
		
		Declaration dec1 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no1.addCondition(new ExprCondition("(msg.getType().equals(\"Core Group Suggestion\")) && (msg.getHistoryList().get(msg.getHistoryList().size() - 1).getStep() == 1)", new Declaration[] {dec1}));
		no1.addParameterDeclaration(dec1);
		no1.setConsequence(new BlockConsequence(
			"msg.addHistory(msg.newHistory());" +			"if (msg.getHistoryList().size() > 1) {" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(msg.getHistoryList().get(msg.getHistoryList().size() - 2).getStep() + 1);" +			"}" +			"else {" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setStep(1);" +			"}" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setFiredRule(\"Core Group Suggestion - Step 1\");" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setSender(msg.getSender());" +			"msg.setRecipient(\"Recipient\");" +			"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
				"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
				"<workflow>" + 
				"<line><header>Vorschlag f�r eine neue Core Group Datei</header></line>" + 
				"<line></line>" + 
				"<line><text>W�rde gerne eine Datei in eine Core Group hinzuf�gen.</text></line>" + 
				"<line><interaction name=\\\"radioButton\\\" value=\\\"yes\\\" type=\\\"radio\\\">weiterleiten</interaction></line>" +
				"<line><interaction name=\\\"radioButton\\\" value=\\\"no\\\" type=\\\"radio\\\">ablehnen</interaction></line>" +
				"<line><text>Ihr Kommentar: </text><textfield name=\\\"comment\\\"/></line>" +
				"<line><interaction type=\\\"submit\\\">abschicken</interaction></line>" +
				"</workflow>\");" +			"java.util.Hashtable hash = new Hashtable();" +			"hash = msg.getHistoryList().get(msg.getHistoryList().size() - 1).getHashtable();" +			"hash.put(\"radioButton\", \"yes\");" +			"msg.getHistoryList().get(msg.getHistoryList().size() - 1).setHashtable(hash);"));
		
		
		
		/**
		 * Rule No.2
		 */
		Rule no2 = new Rule("Core Group Suggestion - Step 2");
		
		Declaration dec2 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no2.addCondition(new ExprCondition("(msg.getType().equals(\"Core Group Suggestion\")) && (msg.getHistoryList().get(msg.getHistoryList().size() - 1).getStep() == 2)", new Declaration[] {dec2}));
		no2.addParameterDeclaration(dec1);
		no2.setConsequence(new BlockConsequence(
			"msg.addHistory(msg.newHistory());" +
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
					"<line><header>Vorschlag f�r eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Eine Datei wurde f�r eine Core Group vorgeschlagen.</text></line>" + 
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
					"<line><header>Ihr Vorschlag f�r eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Die Datei wurde leider abgelehnt.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">best�tigen</interaction></line>" +
					"</workflow>\");" +			"}}" +			"else {" +			"	msg.setRecipient(\"Recipient3\");" +			"	msg.setMessageText(\"Der Button-Wert ist null.\");" +			"}"));
		
		
		
		
		
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
					"<line><header>Vorschlag f�r eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Der Vorschlag wurde angenommen.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">best�tigen</interaction></line>" +
					"</workflow>\");" +
			"}" +
			"else {" +
				"msg.setRecipient(\"Recipient5+4\");" +
				"msg.setMessageText(\"<?xml version = \\\"1.0\\\"?>" + 
					"<?xml-stylesheet type = \\\"text/xsl\\\" href = \\\"styleSheets/workflow.xsl\\\" ?>" +
					"<workflow>" + 
					"<line><header>Ihr Vorschlag f�r eine neue Core Group Datei</header></line>" + 
					"<line></line>" + 
					"<line><text>Die Datei wurde leider abgelehnt.</text></line>" + 
					"<line><interaction type=\\\"submit\\\">best�tigen</interaction></line>" +
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
