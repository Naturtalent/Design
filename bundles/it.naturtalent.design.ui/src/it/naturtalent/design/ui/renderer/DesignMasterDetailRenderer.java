package it.naturtalent.design.ui.renderer;


import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.TreeMasterDetailSWTRenderer;
import org.eclipse.emf.ecp.view.treemasterdetail.model.VTreeMasterDetail;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.design.ui.parts.DesignsView;


/**
 * Dieser Renderer wird benoetigt, um ueber den Zugriff auf den TreeViewer ueber das im Master selektierte Object
 * zu informieren.
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
		
		return treeViewer;
	}
	
}
