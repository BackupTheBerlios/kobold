/*
 * Created on Apr 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;

/**
 * @author grosseml
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class History {
	private int id;
	private String name;
	private String performer;
	private String decision_made;
	private String decision_opportunities;
	private String choices;
	private int step;
	
	
	

	/**
	 * @return
	 */
	public String getChoices() {
		return choices;
	}

	/**
	 * @return
	 */
	public String getDecision_made() {
		return decision_made;
	}

	/**
	 * @return
	 */
	public String getDecision_opportunities() {
		return decision_opportunities;
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
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getPerformer() {
		return performer;
	}

	/**
	 * @return
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @param string
	 */
	public void setChoices(String string) {
		choices = string;
	}

	/**
	 * @param string
	 */
	public void setDecision_made(String string) {
		decision_made = string;
	}

	/**
	 * @param string
	 */
	public void setDecision_opportunities(String string) {
		decision_opportunities = string;
	}

	/**
	 * @param i
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setPerformer(String string) {
		performer = string;
	}

	/**
	 * @param i
	 */
	public void setStep(int step) {
		this.step = step;
	}

}
