package it.naturtalent.planning.ui.wizards;

import it.naturtalent.e4.project.INtProject;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planning.ui.dialogs.SelectProjectsDialog;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.AlternativeRegistry;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.PlanningMaster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PlanningWizardPage extends WizardPage
{
	private class AlternativeTableLabelProvider extends LabelProvider implements
			ITableLabelProvider
	{
		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof Alternative)
			{
				Alternative alternative = (Alternative) element;
				return alternative.getName();
			}
			return element.toString();
		}
	}

	public static final String PLANNING_WIZARD_PAGE = "PlanningWizardPage";

	private DataBindingContext m_bindingContext;

	private Text textPlanningName;

	private Text textAssignedProject;

	private CheckboxTableViewer checkboxTableViewer;

	private PlanningMaster planningMaster;
	
	private Button btnResetProject;
	
	private Button btnSelectProject;

	//private Alternative[] alternatives;
	
	// Zugriff auf die gespeicherten Alternativen
	private IAlternativeFactoryRegistry alternativeFactoryRegistry;
	
	
	private Set<String>listedAlternatives; 
	private String [] checkedAlternatives;

	private Table tableAlternatives;
	
	private IEventBroker eventBroker;

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	/**
	 * EventHandler zur Ueberwachung von Aenderungen in ProjektAdapterDaten
	 */
	private EventHandler projectEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			// die Projekdaten wurden geandert
			if(StringUtils.equals(event.getTopic(), IProjectData.PROJECT_EVENT_MODIFY_PROJECTNAME))
			{
				String projectName = (String) event.getProperty(IEventBroker.DATA);
				if(textPlanningName != null)
				{
					textPlanningName.setText(projectName);
					textAssignedProject.setText(projectName);
				}
			}
		}
	};

	/**
	 * Interne Klasse zum ueberpruefen des Textfeldes 'applicationText'
	 * 
	 * @author dieter
	 * 
	 */
	public class EmptyStringValidator implements IValidator
	{
		public EmptyStringValidator()
		{
			super();
		}

		@Override
		public IStatus validate(Object value)
		{
			IStatus status = Status.OK_STATUS;
			//PlanningWizardPage.this.setPageComplete(true);

			if (StringUtils.isEmpty((String) value))
			{
				//PlanningWizardPage.this.setPageComplete(false);
				status = ValidationStatus.error("Empty"); //$NON-NLS-1$
			}

			updateWidgets();
			return status;
		}
	}

	/**
	 * Create the wizard.
	 */
	public PlanningWizardPage()
	{
		super(PLANNING_WIZARD_PAGE);
		setTitle(Messages.PlanningWizardPage_this_title);
		setDescription(Messages.PlanningWizardPage_this_description);
	}

	@PostConstruct
	public void postConstruct(IEventBroker eventBroker, IAlternativeFactoryRegistry alternativeFactoryRegistry)
	{
		this.eventBroker = eventBroker;
		eventBroker.subscribe(IProjectData.PROJECT_EVENT+"*", projectEventHandler);
		this.alternativeFactoryRegistry = alternativeFactoryRegistry;
		
		
		/*
		Composite parent = new Composite(Display.getDefault().getActiveShell(),
				SWT.NONE);
		createControl(parent);
		*/
		
		/*
			Object obj = context
				.get(PlanningProjectAdapter.PLANNINGPROJECTADAPTERNAME);
		if (obj instanceof PlanningMaster)
		{
			System.out.println(((PlanningMaster) obj).getName());
			//init((PlanningMaster) obj);
		}
	*/
		
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		Section sctnNewSection = formToolkit.createSection(container,
				Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 4, 1));
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setExpanded(true);

		Composite composite = formToolkit.createComposite(sctnNewSection,
				SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		composite.setLayout(new GridLayout(5, false));
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblPlanningName = new Label(composite, SWT.NONE);
		lblPlanningName
				.setToolTipText(Messages.PlanningWizardPage_lblPlanningName_toolTipText);
		lblPlanningName
				.setText(Messages.PlanningWizardPage_lblPlanningName_text);
		new Label(composite, SWT.NONE);

		textPlanningName = new Text(composite, SWT.BORDER);
		textPlanningName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblAssignedProject = new Label(composite, SWT.NONE);
		lblAssignedProject
				.setToolTipText(Messages.PlanningWizardPage_lblAssignedProject_toolTipText);
		lblAssignedProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblAssignedProject
				.setText(Messages.PlanningWizardPage_lblAssignedProject_text);
		new Label(composite, SWT.NONE);

		textAssignedProject = new Text(composite, SWT.BORDER);
		textAssignedProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		textAssignedProject.setEditable(false);

		// Button Projekt zuordnen
		btnSelectProject = new Button(composite, SWT.NONE);
		btnSelectProject.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				SelectProjectsDialog dialog = new SelectProjectsDialog(Display
						.getDefault().getActiveShell());
				if (dialog.open() == SelectProjectsDialog.OK)
				{
					String assignedProjectID = dialog.getSelectedProjectId();
					if (StringUtils.isNotEmpty(assignedProjectID) && (planningMaster != null))
					{
						planningMaster.setProjektKey(assignedProjectID);
						if(StringUtils.isEmpty(planningMaster.getName()))
						{
							IProject project = ResourcesPlugin.getWorkspace().getRoot()
									.getProject(assignedProjectID);							
							try
							{
								String projectName = project.getPersistentProperty(INtProject.projectNameQualifiedName);
								planningMaster.setName(projectName);
							} catch (CoreException e1)
							{								
							}
						}
						setPlanningMaster(planningMaster);
					}
					updateWidgets();
				}
			}
		});
		btnSelectProject
				.setText(Messages.PlanningWizardPage_btnSelectProject_text);

		// Button Projektzuordnung aufheben
		btnResetProject = new Button(composite, SWT.NONE);
		btnResetProject.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				planningMaster.setProjektKey(null);
				setPlanningMaster(planningMaster);
				updateWidgets();
			}
		});
		btnResetProject
				.setToolTipText(Messages.PlanningWizardPage_btnResetProject_toolTipText);
		btnResetProject.setText(Messages.PlanningWizardPage_btnNewButton_text);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		
		Label lblAlternative = new Label(composite, SWT.NONE);
		lblAlternative.setText(Messages.PlanningWizardPage_lblAlternative_text);
		new Label(composite, SWT.NONE);

		// Tabelle mit den auswaehlbaren und planungsbezogenen ausgewaehlten Alternativen
		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener()
		{
			public void checkStateChanged(CheckStateChangedEvent event)
			{
				checkedAlternatives = null;
				Object[] result = checkboxTableViewer.getCheckedElements();
				if(ArrayUtils.isNotEmpty(result))
				{
					checkedAlternatives = new String[result.length];
					System.arraycopy(result, 0, checkedAlternatives, 0,result.length);
					
					// Initialisierung PlanningMaster
					/*
					if(planningMaster != null)
					{
						// die ausgewaehlten Alternativen in PlanningMaster speichern
						List<Alternative>selectedAlternatives = new ArrayList<Alternative>();
						for(String alternativeName : checkedAlternatives)
						{
							Alternative alternative = alternativeFactoryRegistry.getAlternativeFactory(factoryName)
						}
						if (ArrayUtils.isEmpty(planningMaster.getAlternatives()))
							planningMaster.setAlternatives(checkedAlternatives);
					}
					*/
					
				}
				updateWidgets();
			}
		});
		checkboxTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener()
				{
					public void selectionChanged(SelectionChangedEvent event)
					{
						// eine Alternative selektiert
						StructuredSelection selection = (StructuredSelection) checkboxTableViewer
								.getSelection();
						Object selObj = selection.getFirstElement();
						if (selObj instanceof Alternative)
						{
							
						}

						updateWidgets();
					}
				});
		tableAlternatives = checkboxTableViewer.getTable();
		GridData gd_tableAlternatives = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_tableAlternatives.heightHint = 143;
		tableAlternatives.setLayoutData(gd_tableAlternatives);
		//checkboxTableViewer.setLabelProvider(new AlternativeTableLabelProvider());
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		
		// verfuegbare Alternativen in Tabelle eintragen
		
		if(listedAlternatives == null)		
			init(null);		
		checkboxTableViewer.setInput(listedAlternatives);
				
		if(ArrayUtils.isNotEmpty(checkedAlternatives))
			checkboxTableViewer.setCheckedElements(checkedAlternatives);
		
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		formToolkit.adapt(lblNewLabel, true, true);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		m_bindingContext = initDataBindings();
		
		updateWidgets();
	}
	
	public void updateWidgets()
	{
		btnResetProject.setEnabled(planningMaster != null);
		btnSelectProject.setEnabled(planningMaster != null);

		// NewProjekWizard blockiert die direkte Eingabe des Planungsnamen 
		textPlanningName.setEnabled(planningMaster != null);
				
		// Page Complete
		setPageComplete(true);		
		if(StringUtils.isEmpty(textPlanningName.getText()))
		{
			// fehlender Projektname
			setPageComplete(false);
			return;
		}

		// mind eine Alternative ausgewaehlt
		setPageComplete(ArrayUtils.isNotEmpty(checkedAlternatives));		
	}

	public PlanningMaster getPlanningMaster()
	{
		return planningMaster;
	}

	public void init(PlanningMaster planningMaster)
	{
		this.planningMaster = planningMaster;
		
		// TableInhalt zuruecksetzen und mit den registrierten Alternativen aufbauen
		listedAlternatives = new HashSet<String>();
		if(alternativeFactoryRegistry != null)
		{
			String[] registeredAlternatives = alternativeFactoryRegistry
					.getAlternativeFatoryNames();
			for (String alternative : registeredAlternatives)
				listedAlternatives.add(alternative);
		}

		// die bereits der Planung zugeordneten Alternativen hinzufuegen
		checkedAlternatives = null;
		if (planningMaster != null)
		{ 
			Alternative [] planningAlternatives = planningMaster.getAlternatives();
			if (ArrayUtils.isNotEmpty(planningAlternatives))
			{
				for (Alternative alternative : planningAlternatives)
				{
					String alternativeName = alternative.getName();
					listedAlternatives.add(alternativeName);
					checkedAlternatives = ArrayUtils.add(checkedAlternatives,
							alternativeName);
				}
			}
		}
			
		// die Alternativen in Table anzeigen		
		if(checkboxTableViewer != null)
		{
			checkboxTableViewer.setInput(listedAlternatives);
			
			// die bereits benutzten Alternative checken		
			if(!ArrayUtils.isEmpty(checkedAlternatives))
				checkboxTableViewer.setCheckedElements(checkedAlternatives);
		}
	}
	
	public void setPlanningMaster(PlanningMaster planningMaster)
	{
		if (m_bindingContext != null)
			m_bindingContext.dispose();

		init(planningMaster);

		m_bindingContext = initDataBindings();
	}
	
	
	
	public void setAlternativeFactoryRegistry(IAlternativeFactoryRegistry alternativeFactoryRegistry)
	{
		this.alternativeFactoryRegistry = alternativeFactoryRegistry;
	}

	public String [] getCheckedAlternatives()
	{
		/*
		Object[] result = checkboxTableViewer.getCheckedElements();
		if(ArrayUtils.isNotEmpty(result))
		{
			Alternative [] alternatives = new Alternative[result.length];
			System.arraycopy(result, 0, alternatives, 0,result.length);
			return alternatives;
		}
		*/
		
		return checkedAlternatives;
	}
	
	@Override
	public void dispose()
	{
		if(eventBroker != null)
			eventBroker.unsubscribe(projectEventHandler);
			
		super.dispose();
	}

	protected DataBindingContext initDataBindings()
	{
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextPlanningNameObserveWidget = WidgetProperties
				.text(SWT.Modify).observe(textPlanningName);
		IObservableValue namePlanningMasterObserveValue = BeanProperties.value(
				"name").observe(planningMaster);
		UpdateValueStrategy strategy_2 = new UpdateValueStrategy();
		strategy_2.setAfterGetValidator(new EmptyStringValidator());
		bindingContext.bindValue(observeTextTextPlanningNameObserveWidget,
				namePlanningMasterObserveValue, strategy_2, null);
		//
		IObservableValue observeTextTextAssignedProjectObserveWidget = WidgetProperties
				.text(SWT.Modify).observe(textAssignedProject);
		IObservableValue projektKeyPlanningMasterObserveValue = BeanProperties
				.value("projektKey").observe(planningMaster);
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		UpdateValueStrategy strategy_1 = new UpdateValueStrategy();
		strategy_1.setConverter(new ToProjectNameConverter());
		bindingContext.bindValue(observeTextTextAssignedProjectObserveWidget,
				projektKeyPlanningMasterObserveValue, strategy, strategy_1);
		//
		return bindingContext;
	}
}
