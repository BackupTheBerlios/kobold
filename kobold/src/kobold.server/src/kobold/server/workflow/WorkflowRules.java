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
		kobold.common.data.WorkflowMessage msg = new kobold.common.data.WorkflowMessage("Core Group Suggestion");
		/**
		 * Rule No.1
		 */
		Rule no1 = new Rule("Core Group Suggestion - Step 1");
		Declaration dec1 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no1.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 1)", new Declaration[] {dec1}));
		no1.addParameterDeclaration(dec1);
		no1.setConsequence(new BlockConsequence(""+
			"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
			"answer.addParentId(msg.getId());"+	//add the actual parent as history
			"answer.setSender(msg.getSender());"+	
			//"answer.setReceiver(msg.getReceiver());"+
			"answer.setSubject(\"Antrag auf Commit eines Coregroup Items\");"+
			
			"kobold.common.data.WorkflowItem[] controls = msg.getWorkflowControls();"+
			"answer.setReceiver(controls[2].getValue());" +
				
			
			"answer.setMessageText(\"Der Mitarbeiter \"+msg.getSender() + \" würde gerne " +
					"ein Commit der Datei \" + controls[0].getValue() + \" unter \" + controls[1].getValue() + \" "+
					"in die Coregroup machen. Bitte wählen Sie daher in " +
					"untenstehenden Optionen.\");"+
			
			//creating necessary workflow items
			//containing 2 radio buttons (forward, deny)
			"kobold.common.data.WorkflowItem rb1 = new kobold.common.data.WorkflowItem(\"true\",\"An zuständigen PLE weiterleiten\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem rb2 = new kobold.common.data.WorkflowItem(\"false\",\"Ablehnen\", kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem cont = new kobold.common.data.WorkflowItem(\"bla\",\"Entscheidung\",kobold.common.data.WorkflowItem.CONTAINER);"+
			"kobold.common.data.WorkflowItem recip = new kobold.common.data.WorkflowItem(\"\",\"Recipient (PLE):\",kobold.common.data.WorkflowItem.TEXT);"+
			"cont.addChild(rb1);"+
			"cont.addChild(rb2);"+
			"answer.addWorkflowControl(control[0]);" +
			"answer.addWorkflowControl(control[1]);" +
			"answer.addWorkflowControl(recip);" +
			"answer.addWorkflowControl(cont);"+
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
		
		//analyzing the answer
		"java.lang.String decision = \"\";"+
		"kobold.common.data.WorkflowItem[] controls = msg.getWorkflowControls();"+

		"decision = controls[3].getValue();" + 
		"answer.setStep(msg.getStep()+1);"+

		
		"if (decision.equals(\"An zuständigen PLE weiterleiten\")){"+
			"answer.setReceiver(controls[2].getValue());"+
			"answer.setSubject(\"Ein Vorschlag für ein neues Core Group Item ist eingegangen\");"+
			"answer.setMessageText(\"Es ist ein neuer Vorschlag für das Core Group Item \" + controls[0].getValue() + \" unter \" + controls[1].getValue() + \" eingegangen, \" +"+
					"\"der zuständige PE schrieb dazu: \" + msg.getComment());"+

			//add decisions for core group PE
			"kobold.common.data.WorkflowItem radio1 = new kobold.common.data.WorkflowItem(\"false\",\"Vorschlag zustimmen\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem radio2 = new kobold.common.data.WorkflowItem(\"true\",\"Vorschlag ablehnen\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem container = new kobold.common.data.WorkflowItem(null,\"Entscheidung\",kobold.common.data.WorkflowItem.CONTAINER);"+
			"container.addChild(radio1);"+
			"container.addChild(radio2);"+
			"answer.addWorkflowControl(container);"+

		"}"+
		
		"else if (decision.equals(\"Ablehnen\")){"+
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
				"kobold.common.data.WorkflowItem controls[] = msg.getWorkflowControls();"+
				"decision = controls[0].getValue();"+

			"if (decision.equals(\"Vorschlag zustimmen\")){"+
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
			"else if (decision.equals(\"Vorschlag ablehnen\")){"+
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
		 */
		Rule no4 = new Rule("Text Mail");
		
		Declaration dec4 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		
		no4.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"TextMail\"))", new Declaration[] {dec4}));
		no4.addParameterDeclaration(dec4);
		no4.setConsequence(new BlockConsequence(""+

		"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
		"answer.setSender(msg.getSender());"+
		"answer.addParentId(msg.getId());"+
		
		"kobold.common.data.WorkflowItem[] controls = msg.getWorkflowControls();"+

		"answer.setStep(msg.getStep()+1);"+
		"answer.setReceiver(controls[0].getValue());"+
		"answer.setSubject(controls[1].getValue());"+
		"answer.setMessageText(msg.getComment());"+

		//add reply
		"kobold.common.data.WorkflowItem recip = new kobold.common.data.WorkflowItem(\"\",\"Recipient: \",kobold.common.data.WorkflowItem.TEXT);"+
		"kobold.common.data.WorkflowItem subj = new kobold.common.data.WorkflowItem(\"true\",\"Subject: \",kobold.common.data.WorkflowItem.TEXT);"+
		"answer.addWorkflowControl(recip);"+
		"answer.addWorkflowControl(subj);" +
			
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		
		
		
		
		
		set.addRule(no1);
		set.addRule(no2);
		set.addRule(no3);
		set.addRule(no4);
		return set;
	}

}
