package kobold.common.data;
/*
 * Created on May 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * @author pliesmn
 *
 * A WorkflowItem is a Checkbutton, a Radiobutton or a  single-Line-TextWindow
 * The data, to display a WorkFlowItem is stored here.  
 * 
 */
public class WorkflowItem {
	public final static int TEXT = 0; 
	public final static int CHECK = 1;
	public final static int RADIO = 2;
	
	String description;
	String nameValue; // The Name of the Checkbutton or Textwindow or the value of the Radiobutton 
	int type;
	
	public WorkflowItem(String nameValue,String description, int type) {
		this.nameValue = nameValue;
		this.description = description;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public String getName(){
		return nameValue;
	}
	
	public String getValue(){
		return nameValue;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
