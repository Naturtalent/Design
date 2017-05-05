package it.naturtalent.planning.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planning.ui.Messages;

import org.apache.commons.lang3.ArrayUtils;
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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.fieldassist.ControlDecoration;

public class DesignDialog extends TitleAreaDialog
{
	public class PlanningItemLabelProvider extends LabelProvider implements
	ITableLabelProvider
	{

		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof PlanningItem)
			{
				PlanningItem item = (PlanningItem) element;
				return item.getName();
			}
			
			return element.toString();
		}

	}
	
	private ILayerContentRepository layerContentRepository;
	private DataBindingContext m_bindingContext;
	private static Text txtName;
	private Table table;
	private Button btnEdit;
	private Button btnDelete;
	private TableViewer tableViewer;
	private static Button okButton;
	private ControlDecoration controlDecorationName;
	private ControlDecoration controlDecorationTableviewer;
	
	private Design design;

	private Map<Object,IStatus>statusMap = new HashMap<Object, IStatus>();
	
	private List<String>alreadyExistDesigns = new ArrayList<String>();
		
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
			String design = (String) value;
			IStatus status = Status.OK_STATUS;
			
			if (StringUtils.isEmpty(design))
			{
				// leeres Eingabefeld
				status = ValidationStatus.error("kein Name definiert"); //$NON-NLS-1$
			}
			else
			{
				if(alreadyExistDesigns.contains(design))
				{
					// ein Desing existiert bereits unter diesem Namen
					status = ValidationStatus.error("eine Zeichnung mit diesem Namen existiert bereits"); 									
				}	
			}
			
			statusMap.put(txtName, status);
			updateWidgets();					
			return status;
		}
	}
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DesignDialog(Shell parentShell)
	{
		super(parentShell);		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setTitle(Messages.DesignDialog_this_title);
		setMessage(Messages.DesignDialog_this_message);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(Messages.DesignDialog_lblName_text);
		
		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		controlDecorationName = new ControlDecoration(txtName, SWT.LEFT | SWT.TOP);		
		controlDecorationName.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));
		
		new Label(container, SWT.NONE);		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
				
		Label lblInhalte = new Label(container, SWT.NONE);
		lblInhalte.setText(Messages.DesignDialog_lblInhalte_text);
		
		//tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener()
				{
					public void selectionChanged(SelectionChangedEvent event)
					{
						updateWidgets();
					}
				});
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		controlDecorationTableviewer = new ControlDecoration(table, SWT.LEFT | SWT.TOP);		
		controlDecorationTableviewer.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));
		
		tableViewer.setLabelProvider(new PlanningItemLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Composite compositeButtons = new Composite(container, SWT.NONE);
		compositeButtons.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnNew = new Button(compositeButtons, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				PlanningItemDialog planningDialog = new PlanningItemDialog(getShell(), design);
				planningDialog.create();
				planningDialog.setLayerContentRepository(layerContentRepository);
				
				// ein neues Items erzeugen und editieren
				PlanningItem newItem = new PlanningItem();
				planningDialog.setPlanningItem(newItem);
				if(planningDialog.open() == planningDialog.OK)
				{
					// neues Item zum Design hinzufuegen
					PlanningItem [] items = design.getItems();
					items = ArrayUtils.add(items, newItem);
					design.setItems(items);
					tableViewer.setInput(items);
					statusMap.put(tableViewer, Status.OK_STATUS);
					updateWidgets();
				}
			}
		});
		btnNew.setText(Messages.DesignDialog_btnNewButton_text);
		
		btnEdit = new Button(compositeButtons, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object selObj = selection.getFirstElement();
				if (selObj instanceof PlanningItem)
				{
					// Itemdialog aufrufen
					PlanningItem orgItem = (PlanningItem) selObj;
					PlanningItemDialog planningDialog = new PlanningItemDialog(getShell(), design);
					planningDialog.create();
					planningDialog.setLayerContentRepository(layerContentRepository);
					
					// eine Kopie des Items an den Dialog uebergeben
					planningDialog.setPlanningItem(orgItem.clone());
					if(planningDialog.open() == PlanningItemDialog.OK)
					{
						// die editierten Itemdaten uebernehmnen
						PlanningItem planningItem = planningDialog.getPlanningItem();
						orgItem.setName(planningItem.getName());
						orgItem.setLayer(planningItem.getLayer());
						orgItem.setContentClass(planningItem.getContentClass());
					}
				}
			}
		});
		btnEdit.setEnabled(false);
		btnEdit.setText(Messages.DesignDialog_btnEdit_text);
		
		btnDelete = new Button(compositeButtons, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object selObj = selection.getFirstElement();
				if (selObj instanceof PlanningItem)
				{
					// Item entfernen
					PlanningItem delItem = (PlanningItem) selObj;
					PlanningItem [] items = design.getItems();
					items = ArrayUtils.removeElement(items, delItem);
					design.setItems(items);
					tableViewer.setInput(items);
					if(ArrayUtils.isEmpty(items))						
						statusMap.put(tableViewer, ValidationStatus.error("keine Zeichnung definiert"));
					updateWidgets();
				}
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setText(Messages.DesignDialog_btnDelete_text);
		new Label(container, SWT.NONE);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}
	
	public void setDesign(Design design)
	{
		if(m_bindingContext != null)
			m_bindingContext.dispose();
		
		this.design = design;
		
		tableViewer.setInput(design.getItems());

		if(ArrayUtils.isEmpty(design.getItems()))
			statusMap.put(tableViewer, ValidationStatus.error("keine Zeichnung definiert"));
		
		m_bindingContext = initDataBindings();
	}
	
	public void setAlreadyExistDesigns(List<String> alreadyExistDesigns)
	{
		this.alreadyExistDesigns = alreadyExistDesigns;
	}

	public void setLayerContentRepository(
			ILayerContentRepository layerContentRepository)
	{
		this.layerContentRepository = layerContentRepository;
	}
	
	private void updateWidgets()
	{
		IStatus status;
		
		controlDecorationName.hide();
		controlDecorationTableviewer.hide();
		okButton.setEnabled(true);
		
		Set<Object> keySet = statusMap.keySet();
		for(Iterator<Object> iterator = keySet.iterator(); iterator.hasNext();)
		{
			Object key = iterator.next();
			status = statusMap.get(key);
			if(!status.isOK())
			{
				okButton.setEnabled(false);
				
				if(key.equals(txtName))
				{
					controlDecorationName.show();
					controlDecorationName.setDescriptionText(status.getMessage());
				}
				else
					if(key.equals(tableViewer))
					{
						controlDecorationTableviewer.show();
						controlDecorationTableviewer.setDescriptionText(status.getMessage());
					}
			}
		};
		
		
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		Object selObj = selection.getFirstElement();
		boolean isItemSelected = selObj instanceof PlanningItem;
		
		btnEdit.setEnabled(isItemSelected);
		btnDelete.setEnabled(isItemSelected);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(483, 469);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue nameDesignObserveValue = BeanProperties.value("name").observe(design);
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterGetValidator(new EmptyStringValidator());
		bindingContext.bindValue(observeTextTxtNameObserveWidget, nameDesignObserveValue, strategy, null);
		//
		return bindingContext;
	}
}
