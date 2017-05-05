package it.naturtalent.planning.ui.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.libreoffice.draw.Scale;
import it.naturtalent.planung.Page;

public class SelectPageAction extends AbstractPlanungAction
{

	private Page page;
		
	@Override
	public void run()
	{
		if((drawDocument != null) && (page != null))
		{
			// Seite im Dokument selektieren
			String pageName = page.getName();
			drawDocument.selectDrawPage(pageName);
			drawDocument.setPageName(pageName);
						
			// den Massstab an das Dokument uebergeben
			Integer pageDenominator = page.getScaleDenominator();		
			if(pageDenominator == null)
			{
				// in Page ist noch kein Massstab definiert
				MessageDialog.openWarning(Display.getDefault().getActiveShell(),"Zeichnung","noch kein Maßstab definiert");
				
				// Massstab aus dem Dokument holen ...
				pageDenominator = drawDocument.pullScaleDenominator();
				
				// ... und in Page speichern
				page.setScaleDenominator(pageDenominator);
			}
			
			Integer documentDenominator = drawDocument.pullScaleDenominator();				
			if(!pageDenominator.equals(documentDenominator))
			{
				// den im Dokument verwendeten Massstab aktualisieren 
				Scale scale = drawDocument.getScale();
				scale.setScaleDenominator(pageDenominator);
				scale.setMeasureUnit(Scale.MEASUREUNIT_M);
				scale.pushScaleProperties();
			}
		}
	}

	public void setPage(Page page)
	{
		this.page = page;
	}

	@Override
	public void setDrawDocument(DrawDocument drawDocument)
	{
		// TODO Auto-generated method stub
		super.setDrawDocument(drawDocument);
	}

	
	
}
