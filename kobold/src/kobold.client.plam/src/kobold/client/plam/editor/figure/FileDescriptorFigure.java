/*
 * Created on 20.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.editor.figure;

import java.util.Set;

import kobold.client.plam.editor.KoboldColors;
import kobold.client.plam.model.AbstractStatus;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;


/**
 * @author pliesmn
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileDescriptorFigure  extends ProductComponentFigure
{
    protected Dimension corner = new Dimension(8, 8);
    
    /**
     * @see kobold.client.plam.editor.figure.AbstractNodeFigure#getAssetColor()
     */
    protected Color getAssetColor()
    {
        return KoboldColors.orMeta;
    }

}
