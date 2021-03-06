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
 * $Id: SetConstraintCommand.java,v 1.5 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.editor.command;

import org.apache.log4j.Logger;



import kobold.client.plam.editor.model.AssetView;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

/**
 * SetConstraintCommand
 * 
 * @author Tammo van Lessen
 * @version $Id: SetConstraintCommand.java,v 1.5 2004/11/05 10:32:31 grosseml Exp $
 */
public class SetConstraintCommand extends Command {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SetConstraintCommand.class);

    private AssetView model;
    private Point newPos;
    private Dimension newSize;
	private Point oldPos;
	private Dimension oldSize;


    //~ Methods ----------------------------------------------------------------

	public void setLocation(Rectangle r){
		setLocation(r.getLocation());
		setSize(r.getSize());
	}

	public void setLocation(Point p) {
		newPos = p;
	}

	public void setSize(Dimension p) {
		newSize = p;
	}

	public void setViewModel(AssetView model) {
		this.model = model;
	}

	public void execute() {
		oldSize = model.getSize();
		oldPos  = model.getLocation();
		model.setLocation(newPos);
		model.setSize(newSize);
	}

	public String getLabel(){
		if (oldSize.equals(newSize))
			return "Move";
		return "Resize";
	}

    public void redo()
    {
        model.setSize(newSize);
        model.setLocation(newPos);
    }

    public void undo()
    {
        model.setSize(oldSize);
        model.setLocation(oldPos);
    }

}
