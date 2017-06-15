package it.naturtalent.design.ui.actions;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.MasterDetailAction;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class CloseDesignAction  extends MasterDetailAction
{

	public CloseDesignAction()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Meuepunkt 'Zeichnung schliessen' zeigen/nicht zeigen
	 * 
	 */
	@Override
	public boolean shouldShow(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			Map<Design, DrawDocument>openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
			if(openDrawDocumentMap.containsKey(design))
				return true;
		}
		
		return false;
	}

	@Override
	public void execute(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			Map<Design, DrawDocument>openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
			DrawDocument drawDocument = openDrawDocumentMap.get(design);
			if(drawDocument != null)
				drawDocument.closeDocument();
		}
	}

}
