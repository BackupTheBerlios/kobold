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

		addField(
			new BooleanFieldEditor(
				KOBOLD_VCM_ASK_PWD,
				"&Ask for Password everytime",
				getFieldEditorParent()));
		addField(new StringFieldEditor(KOBOLD_VCM_USER_STR, "User name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(KOBOLD_VCM_SCRIPT_LOCATION, "Script location:", getFieldEditorParent()));
		addField(new PasswordFieldEditor(KOBOLD_VCM_PWD_STR, "User Password:", getFieldEditorParent()));

		//		new (KOBOLD_VCM_PWD_STR, "Password:", getFieldEditorParent());
//		addField();
//		class PasswordFieldEditor extends FieldEditor{

	}
            /* (non-Javadoc)
             * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
             */
            protected void adjustForNumColumns(int numColumns)
            {
                
            }

            /* (non-Javadoc)
             * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
             */
//            protected void doFillIntoGrid(Composite parent, int numColumns)
//            {
//        		GridLayout layout = new GridLayout(2, false);
//        		layout.marginHeight = convertVerticalDLUsToPixels(parent,IDialogConstants.VERTICAL_MARGIN);
//        		layout.marginWidth = convertHorizontalDLUsToPixels(parent,IDialogConstants.HORIZONTAL_MARGIN);
//        		layout.verticalSpacing = convertVerticalDLUsToPixels(parent,IDialogConstants.VERTICAL_SPACING);
//        		layout.horizontalSpacing = convertHorizontalDLUsToPixels(parent,IDialogConstants.HORIZONTAL_SPACING);
//        		Label passwordLabel = new Label(parent,SWT.DEFAULT  );
//        		passwordLabel.setText("VCM Password");
//        		Text passwordField = new Text(parent,SWT.BORDER | SWT.LEAD);
//        		passwordField.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
//        				| GridData.FILL_HORIZONTAL));
//        		passwordField.setEchoChar('*');
//                
//            }
//
//            /* (non-Javadoc)
//             * @see org.eclipse.jface.preference.FieldEditor#doLoad()
//             */
//            protected void doLoad()
//            {
//
////        		addField(new StringFieldEditor())
//        	                    // TODO Auto-generated method stub
//                
//            }
//
//            /* (non-Javadoc)
//             * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
//             */
//            protected void doLoadDefault()
//            {
//                getPreferenceStore().getString(KOBOLD_VCM_PWD_STR);
//                
//            }
//
//            /* (non-Javadoc)
//             * @see org.eclipse.jface.preference.FieldEditor#doStore()
//             */
//            protected void doStore()
//            {
//               getPreferenceStore().setValue(KOBOLD_VCM_PWD_STR,"test");
//                
//            }
//
//            /* (non-Javadoc)
//             * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
//             */
//            public int getNumberOfControls()
//            {
//                // TODO Auto-generated method stub
//                return 2;
//            }
//		    
//		}
//		addField(new PasswordFieldEditor());
	
	
	
	public void init(IWorkbench workbench) {
	}
}