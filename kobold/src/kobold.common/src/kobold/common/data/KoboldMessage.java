/*
 * Created on 16.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;

import java.util.*;
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KoboldMessage {
          private String sender;
	      private String receiver;
	      private String messageText;
	      private String date;
	      private String priority;
	      private String subject;
	      private  int id;
	      private Date sendDate;
	      
	  
	      
	      
	      	      
		/**
		 * @return
		 */
		public String getDate() {
			return date;
		}

		/**
		 * @return
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return
		 */
		public String getMessageText() {
			return messageText;
		}

		/**
		 * @return
		 */
		public String getPriority() {
			return priority;
		}

		/**
		 * @return
		 */
		public String getReciever() {
			return receiver;
		}

		/**
		 * @return
		 */
		public String getSender() {
			return sender;
		}

		/**
		 * @return
		 */
		public String getSubject() {
			return subject;
		}

		/**
		 * @param string
		 */
		public void setDate(String string) {
			date = string;
		}

		/**
		 * @param i
		 */
		public void setId(int i) {
			id = i;
		}

		/**
		 * @param string
		 */
		public void setMessageText(String string) {
			messageText = string;
		}

		/**
		 * @param string
		 */
		public void setPriority(String string) {
			priority = string;
		}

		/**
		 * @param string
		 */
		public void setReciever(String string) {
			receiver = string;
		}

		/**
		 * @param string
		 */
		public void setSender(String string) {
			sender = string;
		}

		/**
		 * @param string
		 */
		public void setSubject(String string) {
			subject = string;
		}

}
