package it.naturtalent.design.ui.renderer;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.renderer.TextControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.MassstabShapeAdapter;
import it.naturtalent.design.ui.actions.SaveDesignAction;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.draw.DrawDocument;


public class PageMassstabRenderer extends TextControlSWTRenderer
{

	private DesignsView designsView;	
	private DrawDocument drawDocument;
	private Page page;
		
	@Inject
	public PageMassstabRenderer(VControl vElement,
			ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding,
			EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider,
			EMFFormsEditSupport emfFormsEditSupport)
	{
		super(vElement, viewContext, reportService, emfFormsDatabinding,
				emfFormsLabelProvider, vtViewTemplateProvider, emfFormsEditSupport);
				
		page = (Page) viewContext.getDomainModel();
		
		MApplication currentApplication = E4Workbench.getServiceContext()
				.get(IWorkbench.class).getApplication();
		EPartService partService = currentApplication.getContext()
				.get(EPartService.class);
				
		MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
		if (part != null)
		{
			Object object = part.getObject();
			if (object instanceof DesignsView)
				designsView = (DesignsView) object;
		}
	}
	
	@Override
	protected Control createSWTControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Text text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addVerifyListener(new VerifyListener()
		{			
			@Override
			public void verifyText(VerifyEvent e)
			{
				boolean isInt = true;
				
				String newValue = e.text;
				if (StringUtils.isNotEmpty(newValue))
				{
					try
					{
						new Integer(newValue);
					} catch (NumberFormatException e1)
					{
						isInt = false;
					}

					if (!isInt)
						e.doit = false;
				}			
			}
		});
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("zeichnerisch");
		btnNewButton.setToolTipText("Maßstab zeichnerich ermitteln");
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if(drawDocument != null)
				{
					// Mit dem Adapter den Massstab einstellen
					MassstabShapeAdapter adapter = new MassstabShapeAdapter();					
					adapter.push(drawDocument, null);
					
					// Massstab im ModelPage eintragen
					page.setScaleDenominator(adapter.getScaleDenominator());
				
					// Modeldaten speichern
					SaveDesignAction saveAction = new SaveDesignAction();					
					MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
					IEventBroker eventBroker = currentApplication.getContext().get(IEventBroker.class);
					saveAction.execute((EventBroker) eventBroker);					
				}
				
				text.setFocus();
			}
		});
		
		btnNewButton.setEnabled(false);		
		if (designsView != null)
		{
			Design activeDesign = designsView.getActiveDesign();
			if (activeDesign != null)
			{
				drawDocument = designsView.openDrawDocumentMap.get(activeDesign);
				if(drawDocument != null)
					btnNewButton.setEnabled(true);
			}
		}		
				
		return composite;
	}
	
	
}