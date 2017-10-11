package it.naturtalent.design.ui.actions;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.MasterDetailAction;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.draw.DrawDocument;


/**
 * Wird ueber KontextMenue aufgerufen
 * 
 * Diese Aktion wird mit dem ExtensionPoint "org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"
 * dem EMF TreeMasterRednerer Menue zugeordnet und wird auch von diesem aufgerufen.
 * 
 * @author dieter
 *
 */
public class CloseDesignAction  extends MasterDetailAction
{

	public CloseDesignAction()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{		
		return null;
	}

	/*
	 * Meuepunkt 'Zeichnung schliessen' zeigen/nicht zeigen
	 * 
	 */
	@Override
	public boolean shouldShow(EObject eObject)
	{
		/*
		while (!(eObject instanceof Design))
			eObject = eObject.eContainer();
			*/
		
		eObject = DesignUtils.rollUpToDesing(eObject);
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			Map<Design, DrawDocument>openDrawDocumentMap = DesignsView.getDrawDocumentMap();
			if(openDrawDocumentMap.containsKey(design))
				return true;
		}
		
		return false;
	}

	@Override
	public void execute(EObject eObject)
	{
		eObject = DesignUtils.rollUpToDesing(eObject);
		if (eObject instanceof Design)
		{	
			// Design im Treeviewer selektieren (wird 'active Design')
			MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
			EPartService partService = currentApplication.getContext().get(EPartService.class);
			MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
			((DesignsView) part.getObject()).setSelection(eObject);

			new CloseAction((Design) eObject).run();
		}
	}

}
