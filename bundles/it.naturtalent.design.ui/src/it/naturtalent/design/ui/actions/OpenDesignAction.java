package it.naturtalent.design.ui.actions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.MasterDetailAction;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;



/**
 * Wird ueber KontextMenue aufgerufen
 * 
 * Diese Aktion wird mit dem ExtensionPoint "org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"
 * dem EMF TreeMasterRednerer Menue zugeordnet und wird auch von diesem aufgerufen.
 * 
 * @author dieter
 *
 */
public class OpenDesignAction extends MasterDetailAction
{
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{		
		return null;
	}


	@Override
	public void execute(EObject eObject)
	{		
		new OpenAction((Design) eObject).run();
	}
	
	@Override
	public void dispose()
	{
	}
	
	@Override
	public boolean shouldShow(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			
			// nicht sichtbar, wenn Design bereits geoffnet
			if(DesignsView.openDrawDocumentMap.containsKey(design))
				return false;
			
			// sichtbar, wenn ein zuoeffnender DrawFile existiert  
			String designFilePath = design.getDesignURL();
			
			if(DesignUtils.existProjectDesignFile(designFilePath))
				return true;
			
			if(StringUtils.isEmpty(design.getDesignURL()))
			{
				log.error("kein DrawDateiVerzeichnis definiert");
				return false;
			}
			
			return true;
		}

		return false;
	}
	
}
