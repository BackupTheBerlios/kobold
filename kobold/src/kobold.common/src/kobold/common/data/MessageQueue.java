package kobold.common.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class implements a queue for KoboldMessage-(and derived) Objects
 *
 * @author Armin Cont
 */
public class MessageQueue {

	List queue;

	public MessageQueue() {
		queue = new ArrayList();
	}
/**
 * this method adds 'msg' to the queue
 *
 * @param msg the message to add
 */
public void addMessage(KoboldMessage msg) {
	
  }

/**
 * this method returns the oldest message in the queue and removes it
 *
 * @return the oldest message in the queue 
 */
public KoboldMessage getMessage() {
  return (KoboldMessage)queue.get(queue.size() - 1);
  }
  
  public void removeMessage(KoboldMessage koboldMessage) {
  	
  	queue.remove(koboldMessage);
  }
}
