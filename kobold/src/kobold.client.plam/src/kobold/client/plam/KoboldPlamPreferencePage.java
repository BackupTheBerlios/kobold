/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 *
 */
package kobold.client.plam;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Create koblod preference page. 
 * 
 * @author pliesmn
 */

public class KoboldPlamPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	public static final String PROTOKOL_HANDLER = "java.protocol.handler.pkgs";
	public static final String JAVA_DEBUG = "javax.net.debug";
	public static final String JAVA_SSL  = "javax.net.ssl.keyStore";
	public static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
	public static final String TRUST_STORE = "javax.net.ssl.trustStore";
    public static final String TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

	public KoboldPlamPreferencePage() {
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
				new StringFieldEditor(JAVA_DEBUG, "Debug:", getFieldEditorParent()));
	    addField(new FileFieldEditor(JAVA_SSL, "Keystore path:", getFieldEditorParent()));
	    
	    addField(
				new StringFieldEditor(KEY_STORE_PASSWORD, "Keystore password:", getFieldEditorParent()));
	    addField(new FileFieldEditor(TRUST_STORE, "Truststore path:", getFieldEditorParent()));
	    
	    addField(
				new StringFieldEditor(TRUST_STORE_PASSWORD, "Truststore password:" , getFieldEditorParent()));
		}
	
	public void init(IWorkbench workbench) {
	}
}