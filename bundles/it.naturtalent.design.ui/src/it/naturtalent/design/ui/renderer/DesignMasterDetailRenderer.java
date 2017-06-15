package it.naturtalent.design.ui.renderer;


import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.TreeMasterDetailSWTRenderer;
import org.eclipse.emf.ecp.view.treemasterdetail.model.VTreeMasterDetail;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.actions.OpenDesignAction;
import it.naturtalent.libreoffice.draw.DrawDocument;


/**
 * Dieser Renderer wird benoetigt, um ueber den Zugriff auf den TreeViewer ueber das im Master selektierte Object
 * zu informieren
 * 
 * @author dieter
 *
 */
public class DesignMasterDetailRenderer extends TreeMasterDetailSWTRenderer
{
	
	private class TreeMasterViewSelectionListener implements ISelectionChangedListener
	{
		@Override
		public void selectionChanged(SelectionChangedEvent event)
		{
			final Object treeSelected = ((IStructuredSelection) event.getSelection()).getFirstElement();			
			if (treeSelected instanceof EObject) 
			{
				System.out.println(
						"DesignMasterDetailRenderer.TreeMasterViewSelectionListener.selectionChanged()");	
				if(treeSelected instanceof Design)
				{
					// wenn das Design geoffnet wurde, dann in den Focus bringen
					Design selectedDesign = (Design) treeSelected; 
					Map<Design,DrawDocument> openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
					DrawDocument selectedDrawDocument = openDrawDocumentMap.get(selectedDesign);
					if(selectedDrawDocument != null)						
						selectedDrawDocument.setFocus();						
				}
				
			}
		}
	}

	private TreeViewer treeViewer;
	
	
	//private ESelectionService selectionService  = TechnikAddon.getSelectionService();
	
	@Inject
	public DesignMasterDetailRenderer(VTreeMasterDetail vElement,
			ViewModelContext viewContext, ReportService reportService)
	{
		super(vElement, viewContext, reportService);
	}
	
	@Override
	protected TreeViewer createMasterTree(Composite masterPanel)
	{
		treeViewer = super.createMasterTree(masterPanel);
		
		treeViewer.addSelectionChangedListener(new TreeMasterViewSelectionListener());
		
		return treeViewer;
	}

}
