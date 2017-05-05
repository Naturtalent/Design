package it.naturtalent.planning.ui;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;

public class DefaultLayerContentComposite extends Composite
{

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Table table;
	
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DefaultLayerContentComposite(Composite parent, int style)
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
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table.heightHint = 412;
		table.setLayoutData(gd_table);
		toolkit.paintBordersFor(table);
		
		TableViewerColumn tableViewerColumnIndex = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnIndex = tableViewerColumnIndex.getColumn();
		tblclmnIndex.setWidth(48);
		tblclmnIndex.setText("Index");
		
		TableViewerColumn tableViewerColumnLength = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnType = tableViewerColumnLength.getColumn();
		tblclmnType.setAlignment(SWT.CENTER);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Typ");
		tableViewer.setLabelProvider(new DefaultLayerContentLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());

	}
	
	public void initComposite(String [][] data)
	{
		tableViewer.setInput(data);
	}

}
