 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;

public class UndoDesignAction
{
	@Execute
	public void execute(EventBroker eventBroker)
	{
		EditingDomain domain = AdapterFactoryEditingDomain
				.getEditingDomainFor(DesignUtils.getDesignProject());

		if (domain != null)
		{
			// undo
			domain.getCommandStack().undo();
						
			//setEnabled(domain.getCommandStack().canUndo());
			if(!domain.getCommandStack().canUndo())
			{
				DesignUtils.getDesignProject().saveContents();
				eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model last undo");
			}
		}
	}
	
}