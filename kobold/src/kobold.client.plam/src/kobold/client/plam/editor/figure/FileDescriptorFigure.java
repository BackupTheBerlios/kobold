/*
 * Created on 20.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.editor.figure;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.KoboldColors;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;


/**
 * @author pliesmn
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileDescriptorFigure  extends ProductComponentFigure
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(FileDescriptorFigure.class);

    protected Dimension corner = new Dimension(8, 8);
    
    /**
     * @see kobold.client.plam.editor.figure.AbstractNodeFigure#getAssetColor()
     */
    protected Color getAssetColor()
    {
        return KoboldColors.orMeta;
    }

}
