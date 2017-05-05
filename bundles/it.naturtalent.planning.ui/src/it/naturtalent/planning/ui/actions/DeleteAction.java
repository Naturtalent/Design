package it.naturtalent.planning.ui.actions;

import java.io.File;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;
import it.naturtalent.planning.ui.Messages;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

public class DeleteAction extends AbstractPlanungAction
{

	private PlanningMaster planung;
	private Alternative alternative;
	
	public DeleteAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_DELETE.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		//setToolTipText(Messages.DeleteAction_ToolTipDelete);
	}
	
	@Override
	public void run()
	{
		StructuredSelection selection = (StructuredSelection) planungMasterComposite
				.getTreeViewer().getSelection();
		Object selObj = selection.getFirstElement();

		// Planung selektiert
		if(selObj instanceof PlanningMaster)
		{
			planung = (PlanningMaster) selObj;			
			if (MessageDialog.openQuestion(Display.getDefault()
					.getActiveShell(),
					Messages.DeleteAction_MessageTitlePlanung,
					Messages.bind(Messages.DeleteAction_DeleteMessage, planung.getName())))
			{
				planningModel.deletePlanningMaster(planung.getId());
				planningModel.saveModel();
			}
		}
		
		if (selObj instanceof Alternative)
		{
			alternative = (Alternative) selObj;
			if (MessageDialog.openQuestion(Display.getDefault()
					.getActiveShell(),
					Messages.DeleteAlternativeAction_MessageTitle,
					Messages.bind(Messages.DeleteAlternativeAction_DeleteMessage, alternative.getName())))
			{
				ITreeContentProvider provider = (ITreeContentProvider) planungMasterComposite
						.getTreeViewer().getContentProvider();
				planung = (PlanningMaster) provider.getParent(alternative);
				Alternative [] alternatives = planung.getAlternatives();
				alternatives = ArrayUtils.removeElement(alternatives, alternative);
				planung.setAlternatives(alternatives.length == 0 ? null : alternatives);				
				planningModel.update(planung);
				planningModel.saveModel();
				
				// parent Planung selektieren
				planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(planung));				

			}
			
			return;
		}
		
		// Design - (DrawDocument)
		if (selObj instanceof Design)
		{
			Design design = (Design) selObj;
			if (MessageDialog.openQuestion(Display.getDefault()
					.getActiveShell(),
					Messages.DeleteAlternativeAction_MessageTitle,
					Messages.bind(Messages.DeleteDesignAction_DeleteMessage, design.getName())))
			{				
				String designPath = design.getDesignURL();
				if(StringUtils.isNotEmpty(designPath))
				{
					File designFile = new File(designPath);
					designFile.delete();
				}
				
				ITreeContentProvider provider = (ITreeContentProvider) planungMasterComposite
						.getTreeViewer().getContentProvider();
				
				Alternative alternative = (Alternative) provider.getParent(design);
				Design [] designs = alternative.getDesigns();
				designs = ArrayUtils.removeElements(designs, design);
				alternative.setDesigns(designs);
				
				planung = (PlanningMaster) provider.getParent(alternative);
				planningModel.update(planung);
				planningModel.saveModel();
				
				// parent Alternative selektieren
				planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(alternative));				
			}
				
			return;
		}
		
		// PlanningItem selektiert
		if(selObj instanceof PlanningItem)
		{
			PlanningItem selectedItem = (PlanningItem) selObj;
			if (MessageDialog.openQuestion(Display.getDefault()
					.getActiveShell(),
					Messages.DeletePlanningItemAction_MessageTitle,
					Messages.bind(Messages.DeletePlanningItemAction_DeleteMessage, selectedItem.getName())))
			{
				ITreeContentProvider provider = (ITreeContentProvider) planungMasterComposite
						.getTreeViewer().getContentProvider();				
				Design design = (Design) provider.getParent(selectedItem);
				PlanningItem [] items = design.getItems();
				items = ArrayUtils.removeElement(items, selectedItem);
				design.setItems(items);
				
				Alternative alternative = (Alternative) provider.getParent(design);
				PlanningMaster planung = (PlanningMaster) provider.getParent(alternative);				
				planningModel.update(planung);
				planningModel.saveModel();
				
				// parent Design selektieren
				planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(design));				
			}
		}			
	}

	
	
}
