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
 * $Id: AbstractNodeFigure.java,v 1.10 2004/10/21 21:32:41 martinplies Exp $
 *
 */
package kobold.client.plam.editor.figure;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;

import kobold.client.plam.model.AbstractStatus;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * @author Tammo
 */
public abstract class AbstractNodeFigure extends ComposableFigure {

    
	public static Color classColor = new Color(null, 255, 255, 206);
	private IFigure pane;
	private TitleBarFigure titlebar;
	
	public AbstractNodeFigure() 
	{
		//ToolbarLayout layout = new ToolbarLayout();
	    BorderLayout layout = new BorderLayout();
		setLayoutManager(layout);
		setBorder(new MarginBorder(1));        
		setBackgroundColor(classColor);
		setOpaque(true);

		titlebar = new TitleBarFigure();
		add(titlebar);
		layout.setConstraint(titlebar, BorderLayout.TOP);
		
		setBorder(new MarginBorder(3));
		ScrollPane scrollpane = new ScrollPane();
		pane = new FreeformLayer();
		pane.setLayoutManager(new FreeformLayout());
		//setLayoutManager(new StackLayout());
		add(scrollpane);
		layout.setConstraint(scrollpane, BorderLayout.CENTER);
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);
		
		//setBackgroundColor(ColorConstants.listBackground);
		setOpaque(true);
	}
	
	public void setTitle(String title)
	{
		titlebar.setTitle(title);
	}

	public void setDesc(String desc)
	{
		titlebar.setDesc(desc);
	}

	public void setStatusSet(Set status) {
	    titlebar.setStatusSet(status);
	    
	    if (status.contains(AbstractStatus.DEPRECATED_STATUS)) {
	        setLineStyle(Graphics.LINE_DOT);
	        setBackgroundColor(ColorConstants.lightGray);
	    } else {
	        setLineStyle(Graphics.LINE_SOLID);
	        setBackgroundColor(getAssetColor());
	    }
	    invalidate();
	}
	
	protected abstract Color getAssetColor(); 
	
	
	public IFigure getContentPane() {
		return pane;
	}

	public static class TitleBarFigure extends Figure
	{
		private Label titleLabel;
		private Figure iconWidget;
		
		private Label scriptLabel;
		private Set statusSet;
		private String title;
		private Label descLabel;
		
		private static Font font;
		static {
			FontData[] fd = JFaceResources.getDefaultFont().getFontData(); //new Font(null, SWT."Arial, sans-serif", 12, SWT.BOLD);
			fd[0].setStyle(SWT.BOLD);
			font = new Font(null, fd[0]);
		}
		
		public TitleBarFigure()
		{
			super();
			
			IFigure container = new Figure();
			container.setLayoutManager(new FlowLayout(true));
			
			setLayoutManager(new FlowLayout(false));
			setBorder(new MarginBorder(3));
			
			titleLabel = new Label();
			titleLabel.setLabelAlignment(Label.LEFT);
			
			titleLabel.setFont(font);
			titleLabel.setForegroundColor(ColorConstants.black);

			container.add(titleLabel);
			
			iconWidget = new Figure();
			iconWidget.setLayoutManager(new FlowLayout(true));
			container.add(iconWidget);
			
			add(container);
			descLabel = new Label();
			descLabel.setLabelAlignment(Label.LEFT);
			descLabel.setFont(font);
			descLabel.setForegroundColor(ColorConstants.gray);
			descLabel.setVisible(false);
			add(descLabel);
//			scriptLabel = new Label(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK));
//			scriptLabel.setTextAlignment(Label.RIGHT);
//			scriptLabel.setToolTip(new Label("This Asset contains Scripts"));
//			iconWidget.add(scriptLabel);	
		}
		
		/* Eats too much cpu
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
		}*/

		public void setTitle(String title) {
		    this.title = title;
		    titleLabel.setText(title);
		}
		
		public void setDesc(String desc) {
		    if (desc != null) {
		        descLabel.setText(desc);
		        descLabel.setVisible(true);
		    } else {
		        descLabel.setVisible(false);
		    }
		}
		
		public void setStatusSet(Set statusSet)
		{
			this.statusSet = statusSet;
			iconWidget.removeAll();

			if (statusSet != null) {
			    Iterator it = statusSet.iterator();
			    while (it.hasNext()) {
			        AbstractStatus s = (AbstractStatus)it.next();
			        Label si = new Label(); 
			        if (s.getIcon() == null) {
			            si.setText(s.getId());
			        } else {
			            si.setIcon(s.getIcon().createImage());
			        }
			        si.setToolTip(new Label(MessageFormat.format(s.getDescription(), new String[] {title})));
			        iconWidget.add(si);
			    }
			}
		}
	}
}

