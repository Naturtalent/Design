package it.naturtalent.design.ui;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class DesignProjectPropertyWizardPage extends WizardPage
{
	
	private String iProjectID = null;
	
	private DesignProjectProperty designProjectProperty;

	/**
	 * Create the wizard.
	 */
	public DesignProjectPropertyWizardPage()
	{
		super("wizardPage");
		setTitle("Zeichnungsgruppe");
		setDescription("eine Zeichnungsgruppe bearbeiten");
	}
	
	@PostConstruct
	private void postConstruct(EPartService partService)
	{
		
	}


	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
				
		try
		{		
			ECPSWTViewRenderer.INSTANCE.render(container, (EObject) designProjectProperty.getNtPropertyData());
		} catch (ECPRendererException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setDesignProjectProperty(DesignProjectProperty designProjectProperty)
	{
		this.designProjectProperty = designProjectProperty;
	}
	
	

}
