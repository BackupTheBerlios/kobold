/*
 * Created on 13.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.wizard;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * @author Tammo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Messages {
	private static final String BUNDLE_NAME = "kobold.client.plam.wizard.wizard";//$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);
	/**
	 * 
	 */
	private Messages() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}