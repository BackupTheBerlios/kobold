/*
 * Created on 14.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common;

import junit.framework.Test;
import junit.framework.TestSuite;
import kobold.common.data.KoboldMessageTest;
import kobold.common.data.WorkflowItemTest;

/**
 * @author Tammo
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommonTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for kobold.common.data");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(KoboldMessageTest.class));
		suite.addTest(new TestSuite(WorkflowItemTest.class));
		//$JUnit-END$
		return suite;
	}
}
