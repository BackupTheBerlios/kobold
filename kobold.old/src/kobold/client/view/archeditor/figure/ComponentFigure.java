/*
 * gef-talk
 * TODO
 * Created on 20.02.2004
 *
 */
package kobold.client.view.archeditor.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * ComponentFigure
 * 
 * @author Tammo van Lessen
 * @version $Id: ComponentFigure.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 */
public class ComponentFigure extends RectangleFigure {
    
    public static Color classColor = new Color(null, 255, 255, 206);
    private IFigure pane;
    private Label label;
    
    public ComponentFigure(String name) 
    {
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        setBorder(new MarginBorder(5));        
        //setBorder(new LineBorder(ColorConstants.black, 1));
        setBackgroundColor(classColor);
        setOpaque(true);

        Display d = Display.getCurrent();

        Font classFont = JFaceResources.getHeaderFont(); //new Font(null, SWT."Arial, sans-serif", 12, SWT.BOLD);
        label = new Label(name);
        label.setFont(classFont);
        label.setLabelAlignment(PositionConstants.CENTER);
        add(label);
        
/*        pane = new FreeformLayer();
        //pane.setLayoutManager(new FreeformLayout());
        pane.setBorder(new MarginBorder(5));
        ToolbarLayout tl = new ToolbarLayout();
        tl.setSpacing(5);
        pane.setLayoutManager(tl);
        ScrollPane scrollpane = new ScrollPane();
        
        scrollpane.setViewport(new FreeformViewport());
        scrollpane.setContents(pane);
        
        add(scrollpane);*/

        pane = new Figure();
        pane.setBorder(new MarginBorder(5));
        ToolbarLayout tl = new ToolbarLayout();
        tl.setSpacing(5);
        pane.setLayoutManager(tl);
        add(pane);
    }
    
    /**
     * @return
     */
    public IFigure getContentPane() {
        return pane;
    }
}
