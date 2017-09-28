 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.parts.DesignsView;

public class CloseAction
{
	@Execute
	public void execute(@Optional MPart part, @Optional EPartService partService)
	{

		//MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
		DesignsView designView = (DesignsView) part.getObject();
		Object selObj = designView.getSelection();

		if(selObj instanceof Design)
		{			
			CloseDesignAction closeAction = new CloseDesignAction();
			closeAction.execute((Design)selObj);
		}

	}

}