package it.naturtalent.planning.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.impl.AvalonLogger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class ConfigureViewSetDialog extends TitleAreaDialog
{
	private Text textName;
	private Table table;
	private Table table_1;

	
	private TableViewer tableViewer;
	private Button btnAdd;
	private Button btnAddAll;
	private Button btnRemove;
	private Button btnRemoveAll;
	private TableViewer tableViewerSet;
	
	private Design design;
	private List<String>availableItems;
	private List<String> visibleLayers;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfigureViewSetDialog(Shell parentShell)
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
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name des Sets");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblAvailable = new Label(container, SWT.NONE);
		lblAvailable.setText("verfügbare Ebenen");
		new Label(container, SWT.NONE);
		
		Label lblSet = new Label(container, SWT.NONE);
		lblSet.setText("ViewSet");
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				updateWidgets();
			}
		});
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		
		btnAdd = new Button(composite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				
				List<String>items = selection.toList();
				for(String item : items)
				{
					tableViewer.remove(item);
					tableViewerSet.add(item);
				}
				updateWidgets();
			}
		});
		btnAdd.setText("hinzufügen");
		
		btnAddAll = new Button(composite, SWT.NONE);
		btnAddAll.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tableViewer.setInput(null);
				tableViewerSet.setInput(null);
				PlanningItem [] items = design.getItems();
				for(PlanningItem item : items)
					tableViewerSet.add(item.getLayer());
				updateWidgets();				
			}
		});
		btnAddAll.setText("alle hinzufügen");
		
		btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewerSet.getSelection();
				
				List<String>items = selection.toList();
				for(String item : items)
				{
					tableViewerSet.remove(item);
					tableViewer.add(item);
				}
				updateWidgets();			
			}
		});
		btnRemove.setText("entfernen");
		
		btnRemoveAll = new Button(composite, SWT.NONE);
		btnRemoveAll.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tableViewer.setInput(null);
				tableViewerSet.setInput(null);
				PlanningItem [] items = design.getItems();
				for(PlanningItem item : items)
					tableViewer.add(item.getLayer());
				updateWidgets();				
			}
		});
		btnRemoveAll.setText("alle entfernen");
		
		tableViewerSet = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewerSet
				.addSelectionChangedListener(new ISelectionChangedListener()
				{
					public void selectionChanged(SelectionChangedEvent event)
					{
						updateWidgets();
					}
				});
		table_1 = tableViewerSet.getTable();
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewerSet.setContentProvider(new ArrayContentProvider());

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(665, 497);
	}
	
	public void setDesign(Design design, List<String> visibleLayers)
	{
		this.design = design;
		this.visibleLayers = (visibleLayers != null) ? visibleLayers : new ArrayList<String>();
		
		availableItems = new ArrayList<String>();
		List<String>designItems = new ArrayList<String>();
		
		PlanningItem [] items = design.getItems();
		if(ArrayUtils.isNotEmpty(items))
		{
			for(PlanningItem item : items)
			{
				String itemName = item.getLayer();
				designItems.add(itemName);
				if(!visibleLayers.contains(itemName))				
					availableItems.add(itemName);
			}

			// enthaelt der 'visibleLayer' Namen, die im Desing nicht definiert sind
			for(String itemName : visibleLayers)
			{				
				if(!designItems.contains(itemName))				
					visibleLayers.remove(itemName);				
			}
		}
		
		
		tableViewerSet.setInput(visibleLayers);
		tableViewer.setInput(availableItems);	
		
		updateWidgets();
	}
	
	

	@Override
	protected void okPressed()
	{		
		visibleLayers = (List<String>) new ArrayList<String>();
		TableItem [] items = tableViewerSet.getTable().getItems();
		
		for(TableItem item : items)
			visibleLayers.add((String)item.getData());
		
		super.okPressed();
	}

	public List<String> getVisibleLayers()
	{
		return visibleLayers;
	}
	
	private void updateWidgets()
	{
		btnAdd.setEnabled(!(tableViewer.getSelection().isEmpty()));
		btnAddAll.setEnabled((tableViewer.getTable().getItemCount() > 0));
		
		btnRemove.setEnabled(!(tableViewerSet.getSelection().isEmpty()));
		btnRemoveAll.setEnabled((tableViewerSet.getTable().getItemCount() > 0));
	}

}
