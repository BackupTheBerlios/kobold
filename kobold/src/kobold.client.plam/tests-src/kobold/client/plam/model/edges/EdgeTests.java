/*
 * Created on 14.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.model.edges;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EdgeTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for kobold.client.plam.model.edges");
        //$JUnit-BEGIN$
        suite.addTestSuite(EdgeContainerTest.class);
        //$JUnit-END$
        return suite;
    }
}
