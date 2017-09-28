 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.internal.events.EventBroker;

import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;

public class SaveDesignAction
{
	@Execute
	public void execute(EventBroker eventBroker)
	{
		DesignUtils.getDesignProject().saveContents();
		eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");
	}
	
}