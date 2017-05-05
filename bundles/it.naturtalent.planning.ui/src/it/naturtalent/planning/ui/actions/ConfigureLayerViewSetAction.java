package it.naturtalent.planning.ui.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.planning.ui.dialogs.ConfigureViewSetDialog;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;

public class ConfigureLayerViewSetAction extends AbstractPlanungAction
{

	@Override
	public void run()
	{
		Design design = planungMasterComposite.getCurDesign();
		if (design != null)
		{
			ConfigureViewSetDialog dialog = new ConfigureViewSetDialog(Display
					.getDefault().getActiveShell());
			dialog.create();
			dialog.setDesign(planungMasterComposite.getCurDesign(), planungMasterComposite.getVisibleLayers());
			if(dialog.open() == ConfigureViewSetDialog.OK)		
				 planungMasterComposite.setVisibleLayers(dialog.getVisibleLayers());
			
			// durch Selektion Aenderung sichtbar machen
			PlanningItem item = planungMasterComposite.getCurItem();
			if(item != null)
				planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(item));
		}
	}

	
}
