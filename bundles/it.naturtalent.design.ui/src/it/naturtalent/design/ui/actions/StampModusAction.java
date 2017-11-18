package it.naturtalent.design.ui.actions;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.MasterDetailAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class StampModusAction extends MasterDetailAction
{

	public StampModusAction()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldShow(EObject eObject)
	{
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
			Design design = (Design) eObject;
			Map<Design, DrawDocument>openDrawDocumentMap = DesignsView.getDrawDocumentMap();
			if(openDrawDocumentMap.containsKey(design))
			{
				DrawDocument drawDocument = openDrawDocumentMap.get(design);
				drawDocument.setStampMode(!drawDocument.isStampMode());
			}				
		}
	}

}
