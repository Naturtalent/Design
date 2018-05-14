 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.dialogs.ScaleSettingDialog;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.draw.DrawDocument;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class MassstabAction
{
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, MPart part)
	{
		Object object = part.getObject();
		
		if (object instanceof DesignsView)
		{
			DesignsView designsView = (DesignsView) object;
			Design activeDesign = designsView.getActiveDesign();

			if(activeDesign != null)
			{
				DrawDocument drawDocument = designsView.openDrawDocumentMap.get(activeDesign);

				ScaleSettingDialog scaleSettingDialog = new ScaleSettingDialog(shell);
				if(scaleSettingDialog.open() == ScaleSettingDialog.OK)
				{
					Integer denominator = scaleSettingDialog.getDenominator();
					if(denominator != null)
					{
						activeDesign.setScaleDenominator(denominator);
					}
					else
					{
						
					}
				}
			}

		}
		
	}

	@CanExecute
	public boolean canExecute(MPart part, ESelectionService selectionService)
	{		
		Object selObject = selectionService.getSelection();
		//System.out.println(selObject);
		
	/*	
		Object object = part.getObject();		
		if (object instanceof DesignsView)
		{
			DesignsView designsView = (DesignsView) object;
			//designsView.get
			Design activeDesign = designsView.getActiveDesign();
			return (activeDesign != null);
		}
		*/
		
		return false;
	}
		
}