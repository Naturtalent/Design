package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;

public class SaveAction extends AbstractPlanungAction
{

	public SaveAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_SAVE.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("Planung speichern");
	}

	@Override
	public void run()
	{
		if(planningModel.isModified())
			planningModel.saveModel();
	}
	
	
	
}
