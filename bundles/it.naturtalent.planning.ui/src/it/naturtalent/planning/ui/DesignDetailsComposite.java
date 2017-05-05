package it.naturtalent.planning.ui;

import java.util.HashMap;
import java.util.Map;

import it.naturtalent.planning.ui.actions.DesignHyperlinkAction;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.Page;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

public class DesignDetailsComposite extends Composite
{

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
	
	private Map<Page, ImageHyperlink> designLinkMap = new HashMap<Page, ImageHyperlink>();

	/**
	 * Erzeugt ein Composite mit Links zu den einzelnen Seiten des Designs 
	 * 
	 * @param parent
	 * @param style
	 */
	public DesignDetailsComposite(Composite parent, int style, final DesignHyperlinkAction hyperlinkAction)
	{
		super(parent, style);
		addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));

		ImageHyperlink [] designHyperlinks = null;
		
		
		// fuer jede Seite des Designs einen Link anlegen
		Page [] pages = hyperlinkAction.getPages();
		if(ArrayUtils.isNotEmpty(pages))
		{
			for(final Page page : pages)
			{
				ImageHyperlink mghprlnkNewImagehyperlink = toolkit.createImageHyperlink(this, SWT.NONE);
				mghprlnkNewImagehyperlink.addHyperlinkListener(new HyperlinkAdapter() 
				{
					public void linkActivated(HyperlinkEvent e)
					{					
						// die Seite aus dem Datenbereich auslesen
						Object obj = e.getSource();
						if (obj instanceof Widget)
						{
							Widget widget = (Widget) obj;
							obj = widget.getData();
							if (obj instanceof Page)
							{
								Page page = (Page) obj;
								
								// diese Aktion wird ausgefuehrt, wenn der Link aktiviert wird
								hyperlinkAction.runPageAction(page);
							}
						}
					}
				});
				toolkit.paintBordersFor(mghprlnkNewImagehyperlink);
				mghprlnkNewImagehyperlink.setText(page.getName());
				mghprlnkNewImagehyperlink.setToolTipText(Messages.DesignDetailsComposite_ToolTipOpenDocument);
				
				// die Seite wird im Datenbereich von ImageHyperlink gespeichert
				mghprlnkNewImagehyperlink.setData(page);
				
				designHyperlinks = ArrayUtils.add(designHyperlinks, mghprlnkNewImagehyperlink);
				
				designLinkMap.put(page, mghprlnkNewImagehyperlink);
			}
			
			// die Designhyperlinks dem Composite mitgeben
			setData(designLinkMap);
		}
	}

	
	/**
	 * Erzeugt ein Composite mit Links zu den einzelnen Seiten des Designs 
	 * @param parent
	 * @param style
	 */	
	/*
	public DesignDetailsComposite(Composite parent, int style, final DesignHyperlinkAction hyperlinkAction)
	{
		super(parent, style);
		addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));

		ImageHyperlink [] designHyperlinks = null;
		Design [] designs = hyperlinkAction.getDesigns();
		if(ArrayUtils.isNotEmpty(designs))
		{
			for(final Design design : designs)
			{
				ImageHyperlink mghprlnkNewImagehyperlink = toolkit.createImageHyperlink(this, SWT.NONE);
				mghprlnkNewImagehyperlink.addHyperlinkListener(new HyperlinkAdapter() 
				{
					public void linkActivated(HyperlinkEvent e)
					{
						hyperlinkAction.runDesíng(design);
					}
				});
				toolkit.paintBordersFor(mghprlnkNewImagehyperlink);
				mghprlnkNewImagehyperlink.setText(design.getName());
				mghprlnkNewImagehyperlink.setToolTipText(Messages.DesignDetailsComposite_ToolTipOpenDocument);
				designHyperlinks = ArrayUtils.add(designHyperlinks, mghprlnkNewImagehyperlink);
				designLinkMap.put(design, mghprlnkNewImagehyperlink);
			}
			
			// die Designhyperlinks dem Composite mitgeben
			setData(designLinkMap);
		}
	}
*/

	
}
