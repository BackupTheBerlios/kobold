/*
 * Created on 04.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.preferences;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.SSLHelper;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * @author pliesmn
 * Preferences must be added.
 */
public class MainPreferencePage extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

    public MainPreferencePage() {
        super(GRID);
        setPreferenceStore(KoboldPLAMPlugin.getDefault().getPreferenceStore());
        setDescription("Please look at the child pages.");
        initializeDefaults();
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        IPreferenceStore store = getPreferenceStore();
        //store.setDefault(..., "...");       
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {        
        //addField(...);
    }

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        return super.performOk();
    }
}

