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
 * $Id: AbstractNodeFigure.java,v 1.1 2004/05/14 02:19:15 vanto Exp $
 *
 */
package kobold.client.plam.editor.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

/**
 * @author Tammo
 */
public abstract class AbstractNodeFigure extends Shape {

    
	public static Color classColor = new Color(null, 255, 255, 206);
	private IFigure pane;
	private TitleBarFigure titlebar;
	
	public AbstractNodeFigure() 
	{
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new MarginBorder(1));        
		setBackgroundColor(classColor);
		setOpaque(true);

		titlebar = new TitleBarFigure();
		add(titlebar);
		
		/*pane = new Figure();
		pane.setBorder(new MarginBorder(5));
		ToolbarLayout tl = new ToolbarLayout();
		tl.setSpacing(5);
		pane.setLayoutManager(tl);
		add(pane);*/

		init1();
	}

	public void init1() {
		setBorder(new MarginBorder(5));
		ScrollPane scrollpane = new ScrollPane();
		pane = new FreeformLayer();
		pane.setLayoutManager(new FreeformLayout());
		//setLayoutManager(new StackLayout());
		add(scrollpane);
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);

		
		//setBackgroundColor(ColorConstants.listBackground);
		setOpaque(true);
	}
	
	public void showScriptLabel(boolean s)
	{
		titlebar.showScriptLabel(s);
	}

	public void setTitle(String title)
	{
		titlebar.setTitle(title);
	}
	
	/**
	 * @return
	 */
	public IFigure getContentPane() {
		return pane;
	}
	
	public static class TitleBarFigure extends RectangleFigure
	{
		private Label titleLabel;
		private IFigure iconWidget;
		
		private Label scriptLabel;
		
		public TitleBarFigure()
		{
			super();
			ToolbarLayout tbl = new ToolbarLayout();
			tbl.setVertical(false);
			setLayoutManager(tbl);
			setBorder(new MarginBorder(3));
			
			titleLabel = new Label();
			titleLabel.setLabelAlignment(Label.LEFT);
			FontData[] fd = JFaceResources.getDefaultFont().getFontData(); //new Font(null, SWT."Arial, sans-serif", 12, SWT.BOLD);
			fd[0].setStyle(SWT.BOLD);
			
			titleLabel.setFont(new Font(null, fd[0]));
			titleLabel.setForegroundColor(ColorConstants.black);

			
			add(titleLabel);
			
			iconWidget = new Figure();
			//add(iconWidget);
			
			scriptLabel = new Label("x");
			iconWidget.add(scriptLabel);	
		}
		
		protected void fillShape(Graphics graphics) 
		{
			super.fillShape(graphics);
			Color bC = getBackgroundColor();
			Color fC = getForegroundColor();
			
			setForegroundColor(new Color(null, new RGB(0xE9,0xEE,0xF3)));
			setBackgroundColor(new Color(null, new RGB(0xA2,0xB3,0xD1)));
			graphics.fillGradient(getBounds(), true);
			//setBackgroundColor(bC);
			//setForegroundColor(fC);
		}

		public void setTitle(String title)
		{
			titleLabel.setText(title);
		}
		
		public void showScriptLabel(boolean b)
		{
			scriptLabel.setVisible(b);
		}
	}
}

