/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * 
 */
package kobold.client.vcm.preferences;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.dialog.PasswordFieldEditor;
import kobold.client.vcm.KoboldVCMPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */


public class VCMPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(VCMPreferencePage.class);

	public static final String KOBOLD_VCM_ASK_PWD = "kobold.vcm.askForPassword";
	public static final String KOBOLD_VCM_PWD_STR = "kobold.vcm.user.Password";
	public static final String KOBOLD_VCM_USER_STR = "kobold.vcm.userName";
	public static final String KOBOLD_VCM_SCRIPT_LOCATION = "kobold.vcm.scriptLocation";

	public VCMPreferencePage() {
		super(GRID);
		setPreferenceStore(KoboldVCMPlugin.getDefault().getPreferenceStore());
		setDescription("This page lets you modify the values for the VCM Configuration");
		initializeDefaults();
	}
	
    /**
     * Sets the default values of the preferences.
     */
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(KOBOLD_VCM_ASK_PWD, false);
		store.setDefault(KOBOLD_VCM_PWD_STR, "");
		store.setDefault(KOBOLD_VCM_USER_STR, "");
		store.setDefault(KOBOLD_VCM_SCRIPT_LOCATION, "");
	}
	
    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
	public void createFieldEditors() {
		addField(new BooleanFieldEditor( KOBOLD_VCM_ASK_PWD, "&Ask for Password everytime", getFieldEditorParent()));
		addField(new StringFieldEditor(KOBOLD_VCM_USER_STR, "User name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(KOBOLD_VCM_SCRIPT_LOCATION, "Script location:", getFieldEditorParent()));
		addField(new PasswordFieldEditor(KOBOLD_VCM_PWD_STR, "User Password:", getFieldEditorParent()));
	}
	
    /**
     * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
     */
    protected void adjustForNumColumns(int numColumns) {
    }

	public void init(IWorkbench workbench) {
	}
}