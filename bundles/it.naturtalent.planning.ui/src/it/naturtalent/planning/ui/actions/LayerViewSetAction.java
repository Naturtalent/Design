package it.naturtalent.planning.ui.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.planning.ui.dialogs.ConfigureViewSetDialog;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;

public class LayerViewSetAction extends AbstractPlanungAction
{

	@Override
	public void run()
	{
		planungMasterComposite.setViewerSet(!planungMasterComposite.isViewerSet());
		
		// durch Selektion Aenderung sichtbar machen
		PlanningItem item = planungMasterComposite.getCurItem();
		if(item != null)
			planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(item));
	}

	
}
