package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.Shape;

import java.util.List;

public class PullAction extends AbstractPlanungAction
{

	public PullAction()
	{
		super();
		setImageDescriptor(Icon.ICON_PULL.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("Daten vom Design lesen");
	}

	@Override
	public void run()
	{
		if (drawDocument != null)
		{
			// den im Design selektierten Layer ermitteln
			Layer layer = drawDocument.getLastSelectedLayer();
			if (layer != null)
			{
				List<Shape>shapes = layer.pullLayerShapes();
				eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_SHAPE_PULLED,shapes);
			}
		}
	}
	
	
	
}
