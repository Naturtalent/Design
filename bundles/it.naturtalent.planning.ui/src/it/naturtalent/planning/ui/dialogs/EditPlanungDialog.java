package it.naturtalent.planning.ui.dialogs;

import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.PlanningMaster;



import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.wb.swt.SWTResourceManager;

public class EditPlanungDialog extends TitleAreaDialog
{
	private DataBindingContext m_bindingContext;
	private Text txtPlanung;
	private Text txtAlternative;
	
	private PlanningMaster planung;
	private Alternative alternative;	
	
	//private String planningName;
	//private String alternativeName;
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private static Button okButton;
	private Button btnNeuPlanung;
	private Button btnNeuAlternative;

	/**
	 * Interne Klasse zum ueberpruefen des Textfeldes 'applicationText'
	 * 
	 * @author dieter
	 * 
	 */
	public static class EmptyStringValidator implements IValidator
	{
		public EmptyStringValidator()
		{
			super();
		}

		@Override
		public IStatus validate(Object value)
		{
			IStatus status = Status.OK_STATUS;
			
			if (StringUtils.isEmpty((String) value))
				status = ValidationStatus.error("Empty"); //$NON-NLS-1$
			
			okButton.setEnabled(status.isOK());			
			return status;
		}
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EditPlanungDialog(Shell parentShell)
	{
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{	
		super.configureShell(newShell);
		newShell.setText(Messages.PlanungDialog_Title);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setTitle(Messages.PlanungDialog_this_title);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNamePlanung = new Label(container, SWT.NONE);
		lblNamePlanung.setText(Messages.PlanungDialog_lblNamePlanung_text);
		new Label(container, SWT.NONE);
		
		txtPlanung = new Text(container, SWT.BORDER);
		txtPlanung.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnNeuPlanung = new Button(container, SWT.NONE);
		btnNeuPlanung.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPlanung(null, null);
			}
		});
		btnNeuPlanung.setToolTipText(Messages.EditPlanungDialog_btnNeu_toolTipText);
		btnNeuPlanung.setText(Messages.PlanungDialog_btnNeu_text);
		
		// Projektzuordnung aendern
		ImageHyperlink mghprlnkAssignproject = formToolkit.createImageHyperlink(container, SWT.NONE);
		mghprlnkAssignproject.addHyperlinkListener(new HyperlinkAdapter()
		{
			public void linkActivated(HyperlinkEvent e)
			{
				// Projektauswahl
				SelectProjectsDialog dialog = new SelectProjectsDialog(Display.getDefault().getActiveShell());
				if(dialog.open() == SelectProjectsDialog.OK)
				{
					if(planung != null)
					{
						String assignedProjectID = dialog.getSelectedProjectId();
						planung.setProjektKey(assignedProjectID);
						if (StringUtils.isNotEmpty(assignedProjectID))
						{
							// bei 'leerem' Eingabefeld wird der Projektname uebernommen
							String projectName = dialog.getProjectName(assignedProjectID);
							if (StringUtils.isEmpty(txtPlanung.getText()))
								txtPlanung.setText(projectName);
						}
					}
				}
			}
		});
		mghprlnkAssignproject.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		formToolkit.paintBordersFor(mghprlnkAssignproject);
		mghprlnkAssignproject.setText(Messages.EditPlanungDialog_mghprlnkAssignproject_text);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblNameAlternative = new Label(container, SWT.NONE);
		lblNameAlternative.setText(Messages.PlanungDialog_lblNameAlternative_text);
		new Label(container, SWT.NONE);
		
		txtAlternative = new Text(container, SWT.BORDER);
		txtAlternative.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnNeuAlternative = new Button(container, SWT.NONE);
		btnNeuAlternative.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPlanung(planung, null);
			}
		});
		btnNeuAlternative.setToolTipText(Messages.EditPlanungDialog_btnNeu_1_toolTipText);
		btnNeuAlternative.setText(Messages.PlanungDialog_btnNeu_1_text);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 337);
	}

	public void setPlanung(PlanningMaster planung, Alternative alternative)
	{
		if(m_bindingContext != null)
			m_bindingContext.dispose();
				
		this.alternative = new Alternative();
		if(planung == null)
		{
			this.planung = new PlanningMaster();			
			btnNeuPlanung.dispose();
			btnNeuAlternative.dispose();
			setTitle(Messages.EditPlanungDialog_DefinePlanningTitle);
		}
		else
		{
			this.planung = planung;
			if(alternative != null)			
				this.alternative = alternative;			
			else btnNeuAlternative.dispose(); 
		}
				
		m_bindingContext = initDataBindings();
	}
	
	public PlanningMaster getPlanung()
	{
		return planung;
	}
			
	public Alternative getAlternative()
	{
		return alternative;
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtPlanungObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtPlanung);
		IObservableValue namePlanungObserveValue = BeanProperties.value("name").observe(planung); //$NON-NLS-1$
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterGetValidator(new EmptyStringValidator());
		bindingContext.bindValue(observeTextTxtPlanungObserveWidget, namePlanungObserveValue, strategy, null);
		//
		IObservableValue observeTextTxtAlternativeObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtAlternative);
		IObservableValue nameAlternativeObserveValue = BeanProperties.value("name").observe(alternative); //$NON-NLS-1$
		bindingContext.bindValue(observeTextTxtAlternativeObserveWidget, nameAlternativeObserveValue, null, null);
		//
		return bindingContext;
	}
}
