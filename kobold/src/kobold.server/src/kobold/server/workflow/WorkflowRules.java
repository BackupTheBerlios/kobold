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
		 * transfers the suggestion from p to his pe
		 */
		Rule no1 = new Rule("Core Group Suggestion - Step 1");
		Declaration dec1 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		no1.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 1)", new Declaration[] {dec1}));
		no1.addParameterDeclaration(dec1);
		no1.setConsequence(new BlockConsequence(""+	
			"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
			"answer.addParentId(msg.getId());"+	
			"answer.setSender(msg.getSender());"+	
			"answer.setSubject(\"New Request for a Core Group Commit\");" +
			"answer.setStep(msg.getStep() + 1);"+
			// gets the sender's inputs
			"java.util.HashMap map = msg.getWorkflowData();" +
			"answer.setReceiver(msg.getReceiver());" +
			"answer.setMessageText(\"Programmer \"+msg.getSender() + \" would like to suggest " +
					"committing the file \" + map.get(\"file\") + \" in component \" + map.get(\"component\") + \" "+
					"to a Core Group. He added the following comment: \"" +
					"msg.getComment());" +
			// adds decision possibilities for the pe
			"kobold.common.data.WorkflowItem rb1 = new kobold.common.data.WorkflowItem(\"true\",\"Assent and transfer to PLE\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem rb2 = new kobold.common.data.WorkflowItem(\"false\",\"Dismiss suggestion\", kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem cont = new kobold.common.data.WorkflowItem(\"decision\",\"Decision: \",kobold.common.data.WorkflowItem.CONTAINER);"+
			"cont.addChild(rb1);"+
			"cont.addChild(rb2);" +
			"answer.putWorkflowData(\"file\", map.get(\"file\"));"+
			"answer.putWorkflowData(\"component\", map.get(\"component\"));"+
			"answer.addWorkflowControl(cont);" +
			"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		

		
		
		/**
		 * Rule No.2
		 * Core Group Suggestion
		 * transfers the suggestion from pe to ple
		 * or sends a rejection to p
		 */
		Rule no2 = new Rule("Core Group Suggestion - Step 2");
		
		Declaration dec2 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		no2.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 2)", new Declaration[] {dec2}));
		no2.addParameterDeclaration(dec2);
		no2.setConsequence(new BlockConsequence(""+
		"kobold.common.data.WorkflowMessage answer = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
		"answer.setSender(msg.getSender());"+
		"answer.addParentId(msg.getId());"+
		"java.lang.String decision = \"\";" +
		// gets the sender's input
		"java.util.HashMap map = msg.getWorkflowData();" +
		"decision = map.get(\"decision\");" + 
		"answer.setStep(msg.getStep()+1);"+
		//if the pe supports the suggestion
		"if (decision.equals(\"true\")){"+
			"answer.setReceiver(\"\");"+ //TODO
			"answer.setSubject(\"New Request for a Core Group Commit\");"+
			"answer.setMessageText(\"Product Engineer \" + msg.getSender() + \" got a suggestion from one of his programmers to " +
					"commit the file \" + map.get(\"file\") + \" in component \" + map.get(\"component\") + \" "+
					"to a Core Group. He added the following comment: \"" +
					"msg.getComment());" +
			//add decision possibilities for the ple
			"kobold.common.data.WorkflowItem radio1 = new kobold.common.data.WorkflowItem(\"true\",\"Assent\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem radio2 = new kobold.common.data.WorkflowItem(\"false\",\"Dissent\",kobold.common.data.WorkflowItem.RADIO);"+
			"kobold.common.data.WorkflowItem container = new kobold.common.data.WorkflowItem(\"decision\",\"Decision: \",kobold.common.data.WorkflowItem.CONTAINER);"+
			"container.addChild(radio1);"+
			"container.addChild(radio2);"+
			"answer.addWorkflowControl(container);"+
		"}"+
		// if the pe rejects
		"else if (decision.equals(\"false\")){"+
			"kobold.server.controller.GlobalMessageContainer gmc = kobold.server.controller.GlobalMessageContainer.getInstance();"+
			"kobold.common.data.WorkflowMessage wfm = (kobold.common.data.WorkflowMessage)gmc.getMessage(msg.getParentIds()[0]);"+
			"answer.setReceiver(wfm.getSender());"+
			"answer.setSubject(\"Core Group suggestion rejected\");"+
			"answer.setMessageText(\"Your suggestion was rejected by \"+msg.getSender() +\" due to the following reasons: \" +"+
					 "msg.getComment());"+
		"}"+
		"kobold.server.controller.MessageManager.getInstance().sendMessage(null, answer);"));
		

		
		
		
		/**
		 * Rule No.3
		 * Core Group Suggestion
		 * lets the pe and p know about the ple's decision
		 */
		Rule no3 = new Rule("Core Group Suggestion - Step 3");
		
		Declaration dec3 = new Declaration(new ClassObjectType(WorkflowMessage.class), "msg");
		no3.addCondition(new ExprCondition("(msg.getWorkflowType().equals(\"Core Group Suggestion\")) && (msg.getStep() == 3)", new Declaration[] {dec3}));
		no3.addParameterDeclaration(dec1);
		no3.setConsequence(new BlockConsequence(""+
				// gets the sender's inputs
				"java.util.HashMap map = msg.getWorkflowData();" +
				// answer to p
				"kobold.common.data.WorkflowMessage answerP = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
				"answerP.setSender(msg.getSender());"+
				"answerP.addParentId(msg.getId());"+
				"answerP.setStep(msg.getStep()+1);"+
				// answer to pe
				"kobold.common.data.WorkflowMessage answerPE = new kobold.common.data.WorkflowMessage(msg.getWorkflowType());"+
				"answerPE.setSender(msg.getSender());"+
				"answerPE.addParentId(msg.getId());"+
				"answerPE.setStep(msg.getStep()+1);"+
				//analyzing the answer
				"String decision = \"\";"+
				"decision = map.get(\"decision\");"+
				//setting the recipients
				"kobold.server.controller.GlobalMessageContainer gmc = GlobalMessageContainer.getInstance();"+
				"kobold.common.data.WorkflowMessage wfm = (kobold.common.data.WorkflowMessage)gmc.getMessage(msg.getParentIds()[0]);"+
				"answerP.setReceiver(wfm.getSender());"+
				"answerPE.setReceiver(wfm.getSender());"+
				// if the ple assents
				"if (decision.equals(\"true\")){"+
					"answerP.setSubject(\"Core Group Suggestion accepted\");"+
					"answerP.setMessageText(\"Your suggestion was accepted by \"+msg.getSender() +\" with the following annotation: \" +"+
						"msg.getComment());"+
					"answerPE.setSubject(\"Core Group Suggestion accepted\");"+
					"answerPE.setMessageText(\"Your suggestion was accepted by \"+msg.getSender() +\" with the following annotation: \" +"+
						"msg.getComment());"+
				"}"+
				// if the ple dissents
				"else if (decision.equals(\"false\")){"+
					"answerP.setSubject(\"Core Group Suggestion rejected\");"+
					"answerP.setMessageText(\"Your suggestion was rejected by \"+msg.getSender() +\" due to the following reasons: \" +"+
						"msg.getComment());"+
					"answerPE.setSubject(\"Core Group Suggestion rejected\");"+
					"answerPE.setMessageText(\"Your suggestion was accepted by \"+msg.getSender() +\" due to the following reasons: \" +"+
						"msg.getComment());" +
				"}"+
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
