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
 * $Id: ScriptChooserDialog.java,v 1.1 2004/08/27 16:28:03 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.common.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Dialog for choosing scripts for pre-/post-invocation of assets.
 * 
 * @author garbeam
 */
public class ScriptChooserDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(ScriptChooserDialog.class);
 
    private AbstractAsset asset;
    private CheckboxTableViewer beforeScriptsViewer; 
    private CheckboxTableViewer afterScriptsViewer; 
    private Map beforeScripts;
    private Map afterScripts;
    private Table beforeScriptsTable;
    private Table afterScriptsTable;
    
    /**
     * @param parentShell
     */
    public ScriptChooserDialog(Shell parentShell, AbstractAsset asset)
    {
        super(parentShell);
        this.asset = asset;
        beforeScripts = asset.getRoot().getKoboldProject().getBeforeScripts();
        afterScripts = asset.getRoot().getKoboldProject().getAfterScripts();
    }

    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Script Assignment");
        setTitle("Script Assignment");
        setMessage("Assign scripts which are invoked before and after VCM actions to this asset.");
        Composite composite = (Composite) super.createDialogArea(parent);
        
// TODO
        return composite;
    }
    
    protected void okPressed()
    {
        super.okPressed();
    }
    
}
