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

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import kobold.client.vcm.KoboldVCMPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

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
//	public static final String P_PATH = "pathPreference";
	public static final String ASK_PWD = "Ask for Password";
	public static final String PWD_STR = "User Password";
	public static final String USER_STR = "User Name";

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
		store.setDefault(ASK_PWD, true);
		store.setDefault(USER_STR, "");
		store.setDefault(PWD_STR, "");
	}
	
/**
 * Creates the field editors. Field editors are abstractions of
 * the common GUI blocks needed to manipulate various types
 * of preferences. Each field editor knows how to save and
 * restore itself.
 */

	public void createFieldEditors() {
//		addField(new DirectoryFieldEditor(P_PATH, 
//				"&Directory preference:", getFieldEditorParent()));
		addField(
			new BooleanFieldEditor(
				ASK_PWD,
				"&Ask for Password everytime",
				getFieldEditorParent()));

//		addField(new RadioGroupFieldEditor(
//			P_CHOICE,
//			"An example of a multiple-choice preference",
//			1,
//			new String[][] { { "&Choice 1", "choice1" }, {
//				"C&hoice 2", "choice2" }
//		}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(USER_STR, "&VCM User Name:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PWD_STR, "&VCM User Password:", getFieldEditorParent()));	
	}
	
	
	public void init(IWorkbench workbench) {
	}
}