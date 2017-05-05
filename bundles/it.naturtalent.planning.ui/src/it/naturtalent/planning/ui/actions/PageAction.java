package it.naturtalent.planning.ui.actions;

import org.eclipse.swt.widgets.Display;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planning.ui.dialogs.PageDialog;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningMaster;
import it.naturtalent.planung.PlanningModel;

public class PageAction extends AbstractPlanungAction
{

	public PageAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_EDIT.getImageDescriptor(IconSize._16x16_DefaultIconSize));
	}

	@Override
	public void run()
	{
		if (planungMasterComposite != null)
		{
			Design curDesign = planungMasterComposite.getCurDesign();
			if (curDesign != null)
			{
				PageDialog dialog = new PageDialog(Display.getDefault().getActiveShell());
				dialog.create();
				dialog.setEventBroker(eventBroker);
				
				Page[] pages = curDesign.getPages().clone();				
				dialog.setPages(pages);				
				dialog.setTitle(curDesign.getName());
				
				if (dialog.open() == PageDialog.OK)
				{
					PlanningModel planningModel = planungMasterComposite.getModel();
					PlanningMaster planung = planungMasterComposite.getSelectedPlanning();					
					pages = dialog.getPages();
					curDesign.setPages(pages);
					planningModel.update(planung);
				}				
			}
		}
	}	
	

}
