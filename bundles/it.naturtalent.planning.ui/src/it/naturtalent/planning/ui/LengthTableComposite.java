package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.Shape;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LengthTableComposite extends Composite
{

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Table table;
	private TableViewer tableViewer;
	
	protected TableColumn tblclmnLength;
	
	private IEventBroker eventBroker; 

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LengthTableComposite(Composite parent, int style)
	{
		super(parent, style);
		addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object selObj = selection.getFirstElement();
				if (selObj instanceof String [])
				{
					String [] stgData = (String []) selObj;
					Shape shape = (Shape) getData(stgData[0]);
					
					if(eventBroker != null)
						eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_SHAPE_SELECTED, shape);
				}
			}
		});
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table.heightHint = 417;
		table.setLayoutData(gd_table);
		toolkit.paintBordersFor(table);
		
		TableViewerColumn tableViewerColumnIndex = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnIndex = tableViewerColumnIndex.getColumn();
		tblclmnIndex.setWidth(52);
		tblclmnIndex.setText(Messages.LengthTableComposite_tblclmnIndex_text);
		
		TableViewerColumn tableViewerColumnLength = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnLength = tableViewerColumnLength.getColumn();
		tblclmnLength.setWidth(100);
		tblclmnLength.setText(Messages.LengthTableComposite_tblclmnLength_text);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnShapeType = tableViewerColumn.getColumn();
		tblclmnShapeType.setWidth(100);
		tblclmnShapeType.setText(Messages.LengthTableComposite_tblclmnShapeType_text);
		tableViewer.setLabelProvider(new DefaultLayerContentLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
	}

	public void initComposite(String [][] data)
	{
		tableViewer.setInput(data);
	}
	
	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}
	
}
