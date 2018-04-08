package it.naturtalent.design.ui.renderer;


import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.TreeMasterDetailSWTRenderer;
import org.eclipse.emf.ecp.view.treemasterdetail.model.VTreeMasterDetail;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.provider.DesignEditPlugin;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.actions.OpenDesignAction;
import it.naturtalent.design.ui.parts.DesignsView;


/**
 * Diese Renderererweiterung wird benoetigt, um 
 * 
 * 1. Zugriff auf den TreeViewer zu bekommen (DesignsView.DESIGN_TREEVIEWER_EVENT)
 * 2. Design - KontextMenue 'page' en-/disablen (enable wenn DrawDocument geoffnet ist) 
 * 
 * Eingebunden wird dieser Renderer ueber @see DesignMasterDetailRendererService.
 * 
 * @author dieter
 *
 */
public class DesignMasterDetailRenderer extends TreeMasterDetailSWTRenderer
{
	@Inject
	public DesignMasterDetailRenderer(VTreeMasterDetail vElement,
			ViewModelContext viewContext, ReportService reportService)
	{
		super(vElement, viewContext, reportService);
	}
	
	/*
	 * Die Methode wird ueberschrieben, damit ein Zugriff auf den TreeViewer moeglich ist.
	 * Der TreeViewer wird als event (key: DesignsView.DESIGN_TREEVIEWER_EVENT) gepostet.
	 * Gleichzeitig wird ein TreeMasterViewSelectionListener (s.o.) in den Viewer eingebunden.
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.TreeMasterDetailSWTRenderer#createMasterTree(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TreeViewer createMasterTree(Composite masterPanel)
	{
		TreeViewer treeViewer = super.createMasterTree(masterPanel);
		
		// EventBroker ermitteln und TreeViewer posten
		MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
		IEventBroker eventBroker = currentApplication.getContext().get(IEventBroker.class);
		eventBroker.post(DesignsView.DESIGN_TREEVIEWER_EVENT, treeViewer);
			
		// MenuListener zur Ueberwachung des Design-Kontext-Menues
		treeViewer.getControl().getMenu().addMenuListener(new MenuListener()
		{			
			@Override
			public void menuShown(MenuEvent e)
			{
				Object source = e.getSource();
				if (source instanceof Menu)
				{
					Menu menu = (Menu) source;
					MenuItem [] items = menu.getItems();
					if(ArrayUtils.isNotEmpty(items))
					{						
						for(MenuItem item : items)
						{
							String text = item.getText();
						
							// Text ist definiert in 'it.naturtalent.design.model.design.provider' - plugin.properties
							String pageTypeText = DesignEditPlugin.INSTANCE.getString("_UI_Page_type");	//$NON-NLS-N$
							String layerTypeText = DesignEditPlugin.INSTANCE.getString("_UI_Layer_type");	//$NON-NLS-N$
							if(StringUtils.equals(text, pageTypeText) ||  StringUtils.equals(text, layerTypeText))
							{
								IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
								Object selObj = selection.getFirstElement();
								if (selObj instanceof Design)
								{
									// enablen, wenn das zugeordnete DrawDocument geoeffnet ist
									Design design = (Design) selObj;
									item.setEnabled(DesignsView.openDrawDocumentMap.get(design) != null);								
								}
							}						
						}						
					}					
				}
			}
			
			@Override
			public void menuHidden(MenuEvent e)
			{
				// TODO Auto-generated method stub				
			}
		});
		
		return treeViewer;
	}

}
