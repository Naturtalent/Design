package it.naturtalent.planning.ui.actions;

import it.naturtalent.planung.Design;


public class CloseDesignAction extends AbstractPlanungAction
{

	@Override
	public void run()
	{
		if(drawDocument != null)
		{			
			drawDocument.closeDocument();		
		}
	}
}
