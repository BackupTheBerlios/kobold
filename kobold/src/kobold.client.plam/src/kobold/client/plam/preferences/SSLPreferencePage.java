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
package kobold.client.plam.preferences;

import org.apache.log4j.Logger;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.SSLHelper;
import kobold.client.plam.editor.dialog.PasswordFieldEditor;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Create kobold preference page.
 * 
 * @author pliesmn
 */

public class SSLPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SSLPreferencePage.class);

    public SSLPreferencePage() {
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
        store.setDefault(SSLHelper.PROTOCOL_HANDLER,
                "com.sun.net.ssl.internal.www.protocol");
        store.setDefault(SSLHelper.JAVA_DEBUG, "all");
        store.setDefault(SSLHelper.KEY_STORE, "[your keystore path]");
        store.setDefault(SSLHelper.KEY_STORE_PASSWORD, "");
//        store.setDefault(SSLHelper.KEY_STORE_PASSWORD, "[your keystore passphrase]");
        store.setDefault(SSLHelper.TRUST_STORE,"[your truststore path]");
        store.setDefault(SSLHelper.TRUST_STORE_PASSWORD,"");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */

    public void createFieldEditors() {
        addField(new StringFieldEditor(SSLHelper.PROTOCOL_HANDLER,
                "Java protocol handler:", getFieldEditorParent()));
        addField(new StringFieldEditor(SSLHelper.JAVA_DEBUG, "Debug:",
                getFieldEditorParent()));
        addField(new FileFieldEditor(SSLHelper.KEY_STORE, "Keystore path:",
                getFieldEditorParent()));
//        addField(new PasswordFieldEditor(KOBOLD_VCM_PWD_STR, "User Password:", getFieldEditorParent()));
        addField(new PasswordFieldEditor(SSLHelper.KEY_STORE_PASSWORD,
                "Keystore password:", getFieldEditorParent()));
        	    addField(new FileFieldEditor(SSLHelper.TRUST_STORE, "Truststore path:",
        	        getFieldEditorParent()));

        addField(new PasswordFieldEditor(SSLHelper.TRUST_STORE_PASSWORD, "Truststore password:" ,
            	getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        KoboldPLAMPlugin.getDefault().initSSLProperties();
        return super.performOk();
    }
}