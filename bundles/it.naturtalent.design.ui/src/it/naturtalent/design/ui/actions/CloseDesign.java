 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;

/**
 * Wird ueber die Toolbar aufgerufen
 * 
 * @author dieter
 *
 */

public class CloseDesign
{
	@Execute
	public void execute(@Optional MPart part, @Optional EPartService partService)
	{
		EObject eObject = null;
		
		//MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
		DesignsView designView = (DesignsView) part.getObject();		
		Object selObj = designView.getSelection();
		
		eObject = DesignUtils.rollUpToDesing((EObject) selObj);
		if (eObject instanceof Design)
		{	
			// Design im Treeviewer selektieren (wird 'active Design')
			designView.setSelection(eObject);
			
			CloseDesignAction closeAction = new CloseDesignAction();
			closeAction.execute((Design)eObject);			
		}
	}

}