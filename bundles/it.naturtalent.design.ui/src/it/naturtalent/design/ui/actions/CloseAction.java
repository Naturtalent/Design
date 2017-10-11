package it.naturtalent.design.ui.actions;

import java.util.Map;

import org.eclipse.jface.action.Action;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class CloseAction extends Action
{
	private Design design;

	public CloseAction(Design design)
	{
		super();
		this.design = design;
	}

	@Override
	public void run()
	{		
		Map<Design, DrawDocument>openDrawDocumentMap = DesignsView.getDrawDocumentMap();
		DrawDocument drawDocument = openDrawDocumentMap.get(design);
		if(drawDocument != null)
		{
			// vor dem Schliessen den ShapeListener entfernen
			drawDocument.deActivateShapeListener();
			drawDocument.closeDocument();
		}

	}
	
	
}
