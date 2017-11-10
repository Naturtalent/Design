package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.Layer.DefaultLayerNames;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.LayerContent;
import it.naturtalent.planning.ui.parts.PlanungDetailsComposite;

public class MessenAction extends AbstractPlanungAction
{

	public MessenAction()
	{
		super();
		setImageDescriptor(Icon.ICON_MESSEN.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("messen");
	}

	@Override
	public void run()
	{		
		if(drawDocument != null)
		{
			// Layer 'Masslinien' aktivieren
			Layer layer = drawDocument.getLayer(DefaultLayerNames.Masslinien.getType());			
			drawDocument.selectDrawLayer(layer);
		
			PlanungDetailsComposite detailPage = planungMasterComposite.getDetailPage();
			ILayerContentRepository layerContentRepository = detailPage.getLayerContentRepository();			
			detailPage.setLayerContent(LayerContent.DefaultContentNames.LineLength.name());

			
		}		
	}
	
	
	
}
