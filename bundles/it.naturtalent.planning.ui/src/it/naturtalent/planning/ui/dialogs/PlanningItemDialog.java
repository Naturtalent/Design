package it.naturtalent.planning.ui.dialogs;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.libreoffice.draw.Shape.ShapeType;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.PlanningItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class PlanningItemDialog extends TitleAreaDialog
{
	private DataBindingContext m_bindingContext;
	
	private PlanningItem planningItem;
	//private String [] checkedClassNames;
	private static CheckboxTableViewer tableViewer;
	private static Text txtName;
	private static Text textLayer;
	private Table table;
	private static Button okButton;
	private static ControlDecoration controlDecorationName;
	private static ControlDecoration controlDecorationLayer;
	private static ControlDecoration controlDecorationClass;

	private ILayerContentRepository layerContentRepository;

	private Design design;
	private static List<String>existingItemNames;
	
	private static Map<Object,IStatus>statusMap = new HashMap<Object, IStatus>();
	
	private static List<String>alreadyExistLayer = new ArrayList<String>();
	private Composite compositeButton;
	private static Button btnStyle;
	
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
				status = ValidationStatus.error("kein Name definiert"); 
			else
				if(existingItemNames.contains(value))
					status = ValidationStatus.error("Name existiert bereits");
			
			statusMap.put(txtName, status);
			
			updateWidgets();	
			return status;
		}
	}

	/**
	 * Interne Klasse zum ueberpruefen des Textfeldes 'applicationText'
	 * 
	 * @author dieter
	 * 
	 */
	public static class EmptyTextLayer implements IValidator
	{
		public EmptyTextLayer()
		{
			super();
		}

		@Override
		public IStatus validate(Object value)
		{
			String layer = (String) value;
			IStatus status = Status.OK_STATUS;
			
			if (StringUtils.isEmpty(layer))
			{
				// leeres Eingabefeld
				status = ValidationStatus.error("keine Ebene definiert"); 			
			}
			else
			{
				if(alreadyExistLayer.contains(layer))
				{
					// Layername existiert bereits
					status = ValidationStatus.error("Layer existiert bereits"); 									
				}
			}
						
			statusMap.put(textLayer, status);
			updateWidgets();	
			return status;
		}
	}

	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PlanningItemDialog(Shell parentShell, Design design)
	{
		super(parentShell);
		this.design = design;	
		
		existingItemNames = new ArrayList<String>();
		if(design != null)
		{
			PlanningItem[] items = design.getItems();
			if(ArrayUtils.isNotEmpty(items))
			{
				for (PlanningItem item : items)
					existingItemNames.add(item.getName());
			}
		}
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setTitle(Messages.PlanningItemDialog_this_title);
		setMessage(Messages.PlanningItemDialog_this_message);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setToolTipText(Messages.PlanningItemDialog_lblName_toolTipText);
		lblName.setText(Messages.PlanningItemDialog_lblName_text);
		
		txtName = new Text(container, SWT.BORDER);
		
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		controlDecorationName = new ControlDecoration(txtName, SWT.LEFT | SWT.TOP);
		controlDecorationName.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));
		controlDecorationName.setDescriptionText("Some description");
		new Label(container, SWT.NONE);		
		Label lblEbene = new Label(container, SWT.NONE);
		lblEbene.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEbene.setToolTipText(Messages.PlanningItemDialog_lblEbene_toolTipText);
		lblEbene.setText(Messages.PlanningItemDialog_lblEbene_text);
		
		textLayer = new Text(container, SWT.BORDER);
		
		textLayer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		controlDecorationLayer = new ControlDecoration(textLayer, SWT.LEFT | SWT.TOP);
		controlDecorationLayer.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));
		
		
		new Label(container, SWT.NONE);		
		Label lblClass = new Label(container, SWT.NONE);
		lblClass.setToolTipText(Messages.PlanningItemDialog_lblClass_toolTipText);
		lblClass.setText(Messages.PlanningItemDialog_lblClass_text);
		
		tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addCheckStateListener(new ICheckStateListener()
		{
			public void checkStateChanged(CheckStateChangedEvent event)
			{
				String className = (String) event.getElement();
				planningItem.setContentClass(className);
				String [] checkedClassNames = new String[] {className};
				tableViewer.setCheckedElements(checkedClassNames);
				statusMap.put(tableViewer, Status.OK_STATUS);
				updateWidgets();
			}
		});
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		controlDecorationClass = new ControlDecoration(table, SWT.LEFT | SWT.TOP);
		controlDecorationClass.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		compositeButton = new Composite(container, SWT.NONE);
		compositeButton.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		btnStyle = new Button(compositeButton, SWT.NONE);
		btnStyle.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
			}
		});
		btnStyle.setToolTipText(Messages.PlanningItemDialog_btnStyle_toolTipText);
		btnStyle.setText(Messages.PlanningItemDialog_btnNewButton_text);
		tableViewer.setContentProvider(new ArrayContentProvider());

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
	
	public PlanningItem getPlanningItem()
	{
		return planningItem;
	}

	public void setPlanningItem(PlanningItem planningItem)
	{
		String [] checkedClassNames = null;
		statusMap.clear();
		if(m_bindingContext != null)
			m_bindingContext.dispose();
		
		this.planningItem = planningItem;

		// bereits im Design vorhandene Layernamen in 'alreadyExistLayer' auflisten 
		alreadyExistLayer.clear();
		if(design != null)
		{
			PlanningItem [] items = design.getItems();
			if(ArrayUtils.isNotEmpty(items))
			{
				for(PlanningItem item : items)
					alreadyExistLayer.add(item.getLayer());
			}
			
			// 'planningItem'selbst aus dieser Auflistung wieder entfernen
			alreadyExistLayer.remove(planningItem.getLayer());			
		}
		
		if(layerContentRepository != null)
		{
			// die verfuegbaren Klassennamen auflisten
			List<String>lAvailableNames = new ArrayList<String>();
			String [] availableNames = layerContentRepository.getLayerContentFactoryNames();
			if(ArrayUtils.isNotEmpty(availableNames))
			{				
				for(String name : availableNames)
				{
					lAvailableNames.add(name);				
					if(StringUtils.equals(name, planningItem.getContentClass()))
						checkedClassNames = new String[] {name};
				}
					
			}
			tableViewer.setInput(lAvailableNames);
			
			if(ArrayUtils.isNotEmpty(checkedClassNames))
				tableViewer.setCheckedElements(checkedClassNames);
			else statusMap.put(tableViewer, ValidationStatus.error("keine Klasse selekiert"));
		}
		
		m_bindingContext = initDataBindings();			
	}
	
	public void setLayerContentRepository(
			ILayerContentRepository layerContentRepository)
	{
		this.layerContentRepository = layerContentRepository;
	}
	
	
	private static void updateWidgets()
	{
		IStatus status;
		
		controlDecorationName.hide();
		controlDecorationLayer.hide();
		controlDecorationClass.hide();
		okButton.setEnabled(true);
		
		// Verifystatus abfragen
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
					if(key.equals(textLayer))
					{
						controlDecorationLayer.show();
						controlDecorationLayer.setDescriptionText(status.getMessage());
					}
					else
						if(key.equals(tableViewer))
						{
							controlDecorationClass.show();
							controlDecorationClass.setDescriptionText(status.getMessage());
						}
			}
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 617);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue namePlanningItemObserveValue = BeanProperties.value("name").observe(planningItem);
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterGetValidator(new EmptyStringValidator());
		bindingContext.bindValue(observeTextTxtNameObserveWidget, namePlanningItemObserveValue, strategy, null);
		//
		IObservableValue observeTextTextLayerObserveWidget = WidgetProperties.text(SWT.Modify).observe(textLayer);
		IObservableValue layerPlanningItemObserveValue = BeanProperties.value("layer").observe(planningItem);
		UpdateValueStrategy strategy_1 = new UpdateValueStrategy();
		strategy_1.setAfterGetValidator(new EmptyTextLayer());
		bindingContext.bindValue(observeTextTextLayerObserveWidget, layerPlanningItemObserveValue, strategy_1, null);
		//
		return bindingContext;
	}
}
