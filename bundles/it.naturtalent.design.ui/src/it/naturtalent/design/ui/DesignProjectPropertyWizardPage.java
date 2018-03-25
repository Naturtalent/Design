package it.naturtalent.design.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.DesignsFactory;
import it.naturtalent.e4.project.ui.emf.ProjectModelEventKey;
import it.naturtalent.e4.project.ui.wizards.emf.ProjectPropertyWizardPage;

public class DesignProjectPropertyWizardPage extends WizardPage
{
	
	private String iProjectID = null;
	
	private DesignProjectProperty designProjectProperty;
	
	private DesignGroup wizardDesignGroup;
	
	private Text txtProjectName;

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
			// dem Wizard eine DesignGroup uebergeben
			wizardDesignGroup = (DesignGroup) designProjectProperty.getNtPropertyData();
			if(wizardDesignGroup == null)
			{
				// noch keine projektbezogenene DesignGroup vorhanden 
				wizardDesignGroup = DesignsFactory.eINSTANCE.createDesignGroup();
				wizardDesignGroup.setName(txtProjectName.getText());
			}
			
			
			ECPSWTViewRenderer.INSTANCE.render(container, (EObject) wizardDesignGroup);
			
			
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
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
	}

	public DesignGroup getWizardDesignGroup()
	{
		return wizardDesignGroup;
	}


	@Inject
	@Optional
	public void handleModelChangedEvent(@UIEventTopic(ProjectModelEventKey.PROJECTNAME_WIZARDTEXTFIELD) Text text)
	{
		txtProjectName = text;
	}



}
