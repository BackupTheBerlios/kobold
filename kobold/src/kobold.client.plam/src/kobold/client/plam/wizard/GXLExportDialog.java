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
 * $Id: GXLExportDialog.java,v 1.23 2004/11/11 19:20:30 neco Exp $
 *
 */
package kobold.client.plam.wizard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;
import kobold.common.exception.GXLException;
import net.sourceforge.gxl.GXLBool;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author pliesmn
 * 
 * 
 * Display Export Dialog and export the model as GXL.
 */
public class GXLExportDialog extends TitleAreaDialog {

    private Text textJarFile;
    private Text textGxlFile;
    private Button buttonJarExport;
    private Button searchJARFileButton;
    private static final Log logger = LogFactory.getLog(GXLExportDialog.class);
    private String messageCreateFile = "Create a File for Export";

    private AbstractAsset exportAsset; // asset that should be exportet
    private Group jarExportGroup;
    /**
     * @param parentShell
     */
    public GXLExportDialog(Shell parentShell, AbstractAsset asset) 
    {
        super(parentShell);
        exportAsset = asset;        
    }

    protected void createButtonsForButtonBar(Composite parent) 
    {
        createButton(parent, IDialogConstants.PROCEED_ID, "Export", true);
        createButton(parent, IDialogConstants.CANCEL_ID, Messages
                .getString("GXLExportDialog.Close"), false); //$NON-NLS-1$
    }
    protected Control createDialogArea(Composite parent)
    {
    	//this.setTitle("GXL EXPORT");
	    
	    this.setTitle("GXL Export");
	    this.getShell().setText("GXL Export");
	    this.setMessage(" Select a File for Export", 1);
    
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createContent(composite);
        return composite;
    }
    
    private void createContent(Composite parent)
    {
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setFont(parent.getFont());
		
		textGxlFile = new Text(panel, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 350;
		textGxlFile.setLayoutData(gd);
		
		Button GXLFileButton = new Button(panel, SWT.PUSH);
		GXLFileButton.setText(Messages
				.getString("GXLExportDialog.Browse")); 
		GXLFileButton.setFocus();
		GXLFileButton.addListener(SWT.Selection, new Listener() 
		{
			public void handleEvent(Event event) 
			{
		        GXLExportDialog.this.setMessage(null, IMessageProvider.NONE);//delete old error Messages
		        GXLExportDialog.this.textGxlFile.setText("");
		        FileDialog fd = new FileDialog(GXLExportDialog.this
		    		.getShell(), SWT.SAVE);
		        String path = fd.open();
		        if (path != null)
		        {
		        	GXLExportDialog.this.textGxlFile.setText(path);
		        	GXLExportDialog.this.setMessage(" File selected", 1);
		        	GXLExportDialog.this.getButton(10).setFocus();
		        }
		        else
		        {
		        	GXLExportDialog.this.setMessage(" Select a File for Export", 1);
		        	
		        }
		        GXLExportDialog.this.textGxlFile.redraw();
		        
			}
		});
    }
   
//    protected Control createDialogArea(Composite parent) {
//        //this.setTitle("GXL EXPORT");
//        this.setMessage(" Select a File for Export", 1);
//        this.getShell().setText("GXL EXPORT");
//        Composite exportGroup = new Composite(parent, SWT.NONE);
//            GridLayout layout = new GridLayout();
//            layout.numColumns = 1;
//            exportGroup.setLayout(layout);
//            exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//            // jar export.  export not implemented, widgets are invisible
//            buttonJarExport = new Button(exportGroup, SWT.CHECK);
//            buttonJarExport.setVisible(false); // is not yet implemented
//            buttonJarExport.setText(Messages
//                    .getString("GXLExportDialog.CheckBNameJarExport")); 
//
//            jarExportGroup = new Group(exportGroup, SWT.NONE);
//            jarExportGroup.setText("GXLExportDialog.GroupNameJarExport");
//            jarExportGroup.setVisible(false);
//            layout = new GridLayout();
//            layout.numColumns = 2;
//            jarExportGroup.setLayout(layout);
//            jarExportGroup
//                    .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//            textJarFile = new Text(jarExportGroup, SWT.SINGLE);
//            GridData gd = new GridData();
//            gd.widthHint = 300;
//            textJarFile.setLayoutData(gd);
//            searchJARFileButton = new Button(jarExportGroup, SWT.PUSH);
//            searchJARFileButton.setText(Messages
//                    .getString("GXLExportDialog.Browse")); 
//            searchJARFileButton.addListener(SWT.Selection, new Listener() {
//                public void handleEvent(Event event) {
//                    GXLExportDialog.this.setErrorMessage(null);
//                    FileDialog fd = new FileDialog(GXLExportDialog.this
//                            .getShell(), SWT.SAVE);
//                    GXLExportDialog.this.textJarFile.setText(fd.open());
//                    GXLExportDialog.this.textJarFile.redraw();
//                }
//            });
//
//            GXLExportDialog.this.textJarFile.setEnabled(false);
//            GXLExportDialog.this.searchJARFileButton.setEnabled(false);
//            buttonJarExport.addSelectionListener(new SelectionListener() {
//                public void widgetSelected(SelectionEvent e) {
//                    if (GXLExportDialog.this.buttonJarExport.getSelection()) {
//                        GXLExportDialog.this.textJarFile.setEnabled(true);
//                        GXLExportDialog.this.searchJARFileButton
//                                .setEnabled(true);
//                    } else {
//                        GXLExportDialog.this.textJarFile.setEnabled(false);
//                        GXLExportDialog.this.searchJARFileButton
//                                .setEnabled(false);
//                    }
//                }
//
//                public void widgetDefaultSelected(SelectionEvent e) {
//                    widgetSelected(e);
//                }
//            });
//
//
//            // gxl Export
//            Group gxlExportGroup = new Group(exportGroup, SWT.SHADOW_NONE);
//            layout = new GridLayout();
//            layout.numColumns = 2;
//            gxlExportGroup.setLayout(layout);
//            gxlExportGroup
//                    .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//            gxlExportGroup.setText(Messages
//                    .getString("GXLExportDialog.GroupNameGXLExport"));
//            textGxlFile = new Text(gxlExportGroup, SWT.SINGLE | SWT.BORDER);
//            GridData gd1 = new GridData();
//            gd1.widthHint = 300;
//            textGxlFile.setLayoutData(gd1);
//       
//            Button GXLFileButton = new Button(gxlExportGroup, SWT.PUSH);
//            GXLFileButton.setText(Messages
//                    .getString("GXLExportDialog.Browse")); 
//            GXLFileButton.addListener(SWT.Selection, new Listener() {
//                public void handleEvent(Event event) {
//                    GXLExportDialog.this.setMessage(null, IMessageProvider.NONE);//delete old error Messages
//                    FileDialog fd = new FileDialog(GXLExportDialog.this
//                            .getShell(), SWT.SAVE);
//                    String path = fd.open();
//                    if (path != null){
//                       GXLExportDialog.this.textGxlFile.setText(path);
//                    }
//                    GXLExportDialog.this.textGxlFile.redraw();
//                }
//            });
//        return exportGroup;
//    }


   public void buttonPressed(int button){
       switch (button){
         case IDialogConstants.PROCEED_ID: proceedPressed(); break;
         case IDialogConstants.CANCEL_ID: cancelPressed(); break;   
       }
   }


  private void proceedPressed() {
       this.setMessage(null, IMessageProvider.NONE);//delete old error Messages
       boolean noFault = true;      
       noFault = exportGraph(exportAsset, new File(textGxlFile.getText()));
       if(noFault){this.close();}
   }

    public void cancelPressed() {
        this.close();
    }

    public void exportFiles(AbstractAsset asset, File jarFile) {
        System.out.println(Messages.getString("GXLExportDialog.exportFiles")); //$NON-NLS-1$
    }

    /**
     * Generate GXL export. Export <code>asset</code> to <code>GXLFile</code>.
     * @param asset 
     * @param GXLFile 
     */
    public boolean exportGraph(AbstractAsset asset, File GXLFile) {
        GXLGraph graph = new GXLGraph("koboldgraph"); //$NON-NLS-1$
        GXLDocument doc = new GXLDocument();
        try {
            
            // create nodes
            HashMap nodeMap = new HashMap();
            graph.add(asset.createGXLGraph(nodeMap));
            // add edges and MetaNodes        
            Set addedEdges = new HashSet();
            // Now nodeMap contains all gxlnodes of Variants, Releases, Components and Product/Producline
            // as Value and as key the Variants, Releases,... .
            // For all assets the edges and MetaNodes must be added.            
            ArrayList nodes = new ArrayList();
            // MetaNodes are added => nodeMap.keySet will changed and must copied.
            for (Iterator ite = nodeMap.keySet().iterator(); ite.hasNext();){
                nodes.add(ite.next());
            }
            for (Iterator ite = nodes.iterator(); ite.hasNext();){
                createEdges((INode) ite.next(), graph, nodeMap, new HashSet(),
                        addedEdges, asset.getRoot().getEdgeContainer());
            }           
            doc.getDocumentElement().add(graph);            
            doc.write(GXLFile);
        } catch (GXLException e) {
            logger.debug(e.getLocalizedMessage(), e);
            this.setMessage(e.getLocalizedMessage(), IMessageProvider.ERROR);
            return false;
        } catch (IOException e) {
            logger.warn(e.getLocalizedMessage(), e);
            this.setMessage(e.getLocalizedMessage(), IMessageProvider.ERROR);
            return false;
        }
        catch (Exception e) {            
            String message;
            logger.error(e.getLocalizedMessage(), e);
            if(e.getLocalizedMessage() == null) {
                this.setMessage("An unexpected error has been occured.", IMessageProvider.ERROR);
            } else {
                this.setMessage(e.getLocalizedMessage(), IMessageProvider.ERROR);
            } 
            return false;
        }
        
        this.setMessage("Graph is exported", IMessageProvider.NONE);//delete old error Messages
        return true;
    }
    
    private boolean createEdges(INode node, GXLGraph graph, Map nodeMap,
            Set visited, Set addedEdges, EdgeContainer cont)
            throws GXLException {
        // collect all exported to asset
        boolean edgesAdd = false;
        for (Iterator ite = cont.getEdgesFrom(node).iterator(); ite.hasNext();) {
            Edge edge = (Edge) ite.next();
            if (!visited.contains(edge.getTargetNode())) {
                visited.add(node);
                if (edge.getTargetNode() instanceof MetaNode) {
                    boolean b = createEdges(edge.getTargetNode(), graph,
                            nodeMap, visited, addedEdges, cont);
                    if (b) {
                        edgesAdd = true;
                    }
                } else if (nodeMap.containsKey(edge.getTargetNode())) {
                    edgesAdd = true;
                }
                // create MetaNode if not exists
                if (node.getClass().equals(MetaNode.class)
                        && !nodeMap.containsKey(node)) {
                    GXLNode gxlNode = ((MetaNode) node).createGXLGraph(nodeMap);
                    graph.add(gxlNode);
                }
                if (edgesAdd) {
                    addEdge(edge, graph, nodeMap, addedEdges);
                }
            }
        }
        if (edgesAdd) {
            // => node is in gxlgraph
            // add the missing edges of loops
            for (Iterator ite = cont.getEdges(node).iterator(); ite.hasNext();) {
                Edge edge = (Edge) ite.next();
                if (nodeMap.containsKey(edge.getTargetNode())
                        && nodeMap.containsKey(edge.getStartNode())) {
                    addEdge(edge, graph, nodeMap, addedEdges);
                }
            }
        }
        return edgesAdd;
    }
    
    private void addEdge(Edge edge, GXLGraph graph, Map nodeMap, Set addedEdges) {
        if (!addedEdges.contains(edge)) {
            GXLGraphElement form = (GXLGraphElement) nodeMap.get(edge
                    .getStartNode());
            GXLGraphElement to = (GXLGraphElement) nodeMap.get(edge
                    .getTargetNode());
            GXLEdge gxlEdge = new GXLEdge(form, to);
            gxlEdge.setType(URI.create(edge.getGXLType()));
            gxlEdge.setAttr("isdirected", new GXLBool(true));
            graph.add(gxlEdge);
            addedEdges.add(edge); 
        }
    }  
    

}

   