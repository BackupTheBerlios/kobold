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
 * @author Bettina
 *
 * collection of Workflow rules
 */
public class WorkflowRules {
	
	protected WorkflowRules () {
	}
	
	protected static RuleSet createRuleSet() throws Exception {
		RuleSet set = new RuleSet("Kobold");
		
		/**
		 * Rule No.1
		 * Core Group Suggestion
		 * transfers the rule from p to his pe
		 */
		Rule no1 = new Rule("Core Group Suggestion - Step 1");
		Declaration dec1 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		no1.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 1)", new Declaration[] {dec1}));
		no1.addParameterDeclaration(dec1);
		no1.setConsequence(new BlockConsequence(""+	
			"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
			"answer.addParentId(msg.getId());"+	
			"answer.setSender(msg.getSender());"+	
			"answer.setSubject(\"Antrag auf Commit eines Coregroup Items\");" +
			"answer.setStep(msg.getStep() + 1);"+
			// gets the sender's inputs
			"java.util.HashMap map = msg.getWorkflowData();" +
			"answer.setReceiver(msg.getReceiver());" +
			"answer.setMessageText(\"Programmer \"+msg.getSender() + \" would like to suggest " +
					"committing the file \" + map.get(\"file\") + \" in component \" + map.get(\"path\") + \" "+
					"to a Core Group. He added the following comment: \"" +
					"msg.getComment());" +
			"kobold.common.data.WorkflowItem rb1 = new kobold.common.data.WorkflowItem(\"true\",\"Consent and transfer to PLE\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem rb2 = new kobold.common.data.WorkflowItem(\"false\",\"Ablehnen\", kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem cont = new kobold.common.data.WorkflowItem(\"bla\",\"Entscheidung\",kobold.common.data.WorkflowItem.CONTAINER);"+
			"kobold.common.data.WorkflowItem recip = new kobold.common.data.WorkflowItem(\"recipient\",\"Recipient (PLE):\",kobold.common.data.WorkflowItem.TEXT);"+
			"cont.addChild(rb1);"+
			"cont.addChild(rb2);" +
			"answer.putWorkflowData(\"file\", map.get(\"file\"));"+
			"answer.putWorkflowData(\"path\", map.get(\"path\"));"+
			"answer.addWorkflowControl(recip);" +
			"answer.addWorkflowControl(cont);" +
			"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		

		
		
		/**
		 * Rule No.2
		 */
		Rule no2 = new Rule("Core Group Suggestion - Step 2");
		
		Declaration dec2 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no2.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 2)", new Declaration[] {dec2}));
		no2.addParameterDeclaration(dec2);
		no2.setConsequence(new BlockConsequence(""+

		"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
		"answer.setSender(msg.getSender());"+
		"answer.addParentId(msg.getId());"+
		

		"java.lang.String decision = \"\";"+

		"java.util.HashMap map = msg.getWorkflowData();" +


		"decision = map.get(\"bla\");" + 
		"answer.setStep(msg.getStep()+1);"+

		
		"if (decision.equals(\"true\")){"+
			"answer.setReceiver(map.get(\"recipient\"));"+
			"answer.setSubject(\"Ein Vorschlag für ein neues Core Group Item ist eingegangen\");"+
			"answer.setMessageText(\"Es ist ein neuer Vorschlag für das Core Group Item \" + map.get(\"file\") + \" unter \" + map.get(\"path\") + \" eingegangen, \" +"+
					"\"der zuständige PE schrieb dazu: \" + msg.getComment());"+

			//add decisions for core group PE
			"kobold.common.data.WorkflowItem radio1 = new kobold.common.data.WorkflowItem(\"true\",\"Vorschlag zustimmen\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem radio2 = new kobold.common.data.WorkflowItem(\"false\",\"Vorschlag ablehnen\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem container = new kobold.common.data.WorkflowItem(\"bla\",\"Entscheidung\",kobold.common.data.WorkflowItem.CONTAINER);"+
			"container.addChild(radio1);"+
			"container.addChild(radio2);"+
			"answer.addWorkflowControl(container);"+

		"}"+
		
		"else if (decision.equals(\"false\")){"+
			"kobold.server.controller.GlobalMessageContainer gmc = kobold.server.controller.GlobalMessageContainer.getInstance();"+
			"kobold.common.data.WorkflowMessage wfm = (kobold.common.data.WorkflowMessage)gmc.getMessage(msg.getParentIds()[0]);"+
			"answer.setReceiver(wfm.getSender());"+
			"answer.setSubject(\"Ihre Anfrage wurde abgelehnt.\");"+
			"answer.setMessageText(\"Ihre Anfrage wurde von \"+msg.getSender() +\" mit der Begründung '\" +"+
					 "msg.getComment() +\"' abgelehnt\");"+
			
		"}"+
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		

		
		
		
		/**
		 * Rule No.3
		 */
		Rule no3 = new Rule("Core Group Suggestion - Step 3");
		
		Declaration dec3 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no3.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 3)", new Declaration[] {dec3}));
		no3.addParameterDeclaration(dec1);
		no3.setConsequence(new BlockConsequence(""+
				"java.util.HashMap map = msg.getWorkflowData();" +
				"kobold.common.data.WorkflowMessage answer1 = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
				"answer1.setSender(msg.getSender());"+
				"answer1.addParentId(msg.getId());"+
				"answer1.setStep(msg.getStep()+1);"+
				
				"kobold.common.data.WorkflowMessage answer2 = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
				"answer2.setSender(msg.getSender());"+
				"answer2.addParentId(msg.getId());"+
				"answer2.setStep(msg.getStep()+1);"+
				
				//analyzing the answer
				"String decision = \"\";"+
				"decision = map.get(\"bla\");"+

			"if (decision.equals(\"true\")){"+
				"kobold.server.controller.GlobalMessageContainer gmc = GlobalMessageContainer.getInstance();"+
				"kobold.common.data.WorkflowMessage wfm = (kobold.common.data.WorkflowMessage)gmc.getMessage(msg.getParentIds()[0]);"+
				"answer1.setReceiver(wfm.getSender());"+
				"answer1.setSubject(\"Ihr Vorschlag wurde akzeptiert.\");"+
				"answer1.setMessageText(\"Ihre Anfrage wurde von \"+msg.getSender() +\" akzeptiert. Weitere Anweisungen lauten: '\" +"+
						 "msg.getComment() +\"'\");"+
				
				"answer2.setReceiver(wfm.getSender());"+
				"answer2.setSubject(\"Ihr Vorschlag wurde akzeptiert.\");"+
				"answer2.setMessageText(\"Ihre Anfrage wurde von \"+msg.getSender() +\" akzeptiert. Weitere Anweisungen lauten: '\" +"+
						"msg.getComment() +\"'\");"+
						 
			"}"+
			"else if (decision.equals(\"false\")){"+
				"kobold.server.controller.GlobalMessageContainer gmc = GlobalMessageContainer.getInstance();"+
				"kobold.common.data.WorkflowMessage wfm = (kobold.common.data.WorkflowMessage)gmc.getMessage(msg.getParentIds()[0]);"+
				"answer1.setReceiver(wfm.getSender());"+
				"answer1.setSubject(\"Ihre Anfrage wurde abgelehnt.\");"+
				"answer1.setMessageText(\"Ihre Anfrage wurde von \"+msg.getSender() +\" mit der Begründung '\" +"+
					 "msg.getComment() +\"' abgelehnt\");"+
				
				"answer2.setReceiver(wfm.getSender());"+
				"answer2.setSubject(\"Ihre Anfrage wurde abgelehnt.\");"+
				"answer2.setMessageText(\"Ihre Anfrage wurde von \"+msg.getSender() +\" mit der Begründung '\" +"+
					"msg.getComment() +\"' abgelehnt\");}"+
				
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer1);" + 
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer2);"));


				

		
		
		
		
		
		
		
		
		
		/**
		 * Rule No.4
		 * implements a text mail
		 */
		Rule no4 = new Rule("Text Mail");
		
		Declaration dec4 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no4.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"TextMail\"))", new Declaration[] {dec4}));
		no4.addParameterDeclaration(dec4);
		no4.setConsequence(new BlockConsequence(""+

		"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(\"TextMail\");"+
		"answer.setSender(msg.getSender());"+
		// reference to msg
		"answer.addParentId(msg.getId());"+
		"answer.setStep(msg.getStep()+1);" +
		// gets the sender's inputs
		"java.util.HashMap map = msg.getWorkflowData();" +
		"answer.setReceiver(map.get(\"recipient\"));"+
		"answer.setSubject(map.get(\"subject\"));"+
		"answer.setMessageText(msg.getComment());"+
		//add the possibility to reply
		"kobold.common.data.WorkflowItem subj = new kobold.common.data.WorkflowItem(\"subject\",\"Subject: \",kobold.common.data.WorkflowItem.TEXT);"+
		"answer.addWorkflowControl(subj);" +
		//set recipient of the reply
		"answer.putWorkflowData(\"recipient\", msg.getSender());" +
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		
		
		
		
		
		set.addRule(no1);
		set.addRule(no2);
		set.addRule(no3);
		set.addRule(no4);
		return set;
	}

}
