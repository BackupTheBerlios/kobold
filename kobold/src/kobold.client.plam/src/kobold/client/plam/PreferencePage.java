/*
 * Created on 21.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.preference.IPreferenceStore;


/*
 * 
 * 
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	public static final String P_PATH = "pathPreference";
	public static final String P_BOOLEAN = "booleanPreference";
	public static final String P_CHOICE = "choicePreference";
	public static final String P_STRING = "stringPreference";
	public static final String PROTOKOL_HANDLER = "java.protocol.handler.pkgs";
	public static final String JAVA_DEBUG = "javax.net.debug";
	public static final String JAVA_SSL  = "javax.net.ssl.keyStore";
	public static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
	public static final String TRUST_STORE = "javax.net.ssl.trustStore";
    public static final String TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(KoboldPLAMPlugin.getDefault().getPreferenceStore());
		setDescription("Kobold Client preference page");
		initializeDefaults();
	}
/**
 * Sets the default values of the preferences.
 */
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(P_BOOLEAN, true);
		store.setDefault(P_CHOICE, "choice2");
		store.setDefault(P_STRING, "Default value");
		store.setDefault(PROTOKOL_HANDLER, "com.sun.net.ssl.internal.www.protocol");
		store.setDefault(JAVA_DEBUG,"all");
		store.setDefault(JAVA_SSL ,"[your keystore path]");
		store.setDefault(KEY_STORE_PASSWORD,"[your keystore passphrase]");
		store.setDefault(TRUST_STORE,"[your truststore path]");
	    store.setDefault(TRUST_STORE_PASSWORD,"[your truststore passphrase]");
	}
	
/**
 * Creates the field editors. Field editors are abstractions of
 * the common GUI blocks needed to manipulate various types
 * of preferences. Each field editor knows how to save and
 * restore itself.
 */

	public void createFieldEditors() {
	    addField(
				new StringFieldEditor(PROTOKOL_HANDLER, "Java protokol handler:", getFieldEditorParent()));
	    addField(
				new StringFieldEditor(JAVA_DEBUG, JAVA_DEBUG +":", getFieldEditorParent()));
	    addField(new DirectoryFieldEditor(JAVA_SSL, "keystore path:", getFieldEditorParent()));
	    
	    addField(
				new StringFieldEditor(KEY_STORE_PASSWORD, "Keystore passphrase:", getFieldEditorParent()));
	    addField(new DirectoryFieldEditor(TRUST_STORE, "Truststore path:", getFieldEditorParent()));
	    
	    addField(
				new StringFieldEditor(TRUST_STORE_PASSWORD, "Trust store password" , getFieldEditorParent()));
		}
	
	public void init(IWorkbench workbench) {
	}
}