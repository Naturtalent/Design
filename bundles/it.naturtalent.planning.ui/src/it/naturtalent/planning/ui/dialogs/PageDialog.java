package it.naturtalent.planning.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningEvent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class PageDialog extends TitleAreaDialog
{
	private IEventBroker eventBroker;
	
	private Table table;
	
	private TableViewer tableViewer;
	
	private String name;
	
	private Page [] pages;
	
	private Button btnDelete;
	
	private Button btnRename;
	
		
	public class PageLabelProvider extends LabelProvider implements ITableLabelProvider
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
			if (element instanceof Page)
			{
				Page page = (Page) element;
				return page.getName();
			}
			
			return element.toString();
		}

	}
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PageDialog(Shell parentShell)
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
		setMessage("Seiten hinzufügen, löschen oder umbenennen");
		setTitle("Die Seiten einer Zeichnung");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				updateWidgets();
			}
		});
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setLabelProvider(new PageLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		Composite compositeButton = new Composite(container, SWT.NONE);
		compositeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeButton.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnNeu = new Button(compositeButton, SWT.NONE);
		btnNeu.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				InputDialog inputDialog = new InputDialog(getShell(), "Zeichnung", "Bezeichung der Seite", null, new IInputValidator()
				{					
					@Override
					public String isValid(String newText)
					{
						if (StringUtils.isEmpty(newText))	
							return "leees Eingabefeld";
						
						if(isExist(newText))
							return "der eingegebene Name existiert bereits";
									
						name = newText;
						return null;
					}
				});
				
				if(inputDialog.open() == InputDialog.OK)
				{
					Page page = new Page();
					page.setName(name);
					tableViewer.add(page);
					pages = ArrayUtils.add(pages, page);
					eventBroker.post(PlanningEvent.PLANNING_EVENT_ADD_PAGE, page);
				}
				
				updateWidgets();
			}
		});
		btnNeu.setToolTipText("neu Seite einfügen");
		btnNeu.setText("neu");
		
		btnRename = new Button(compositeButton, SWT.NONE);
		btnRename.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				Page page = (Page) selection.getFirstElement();
				if (page != null)
				{

					InputDialog inputDialog = new InputDialog(getShell(),
							"Zeichnung", "Seite umbennen", page.getName(),
							new IInputValidator()
							{
								@Override
								public String isValid(String newText)
								{
									if (StringUtils.isEmpty(newText))
										return "leees Eingabefeld";

									if (isExist(newText))
										return "der eingegebene Name existiert bereits";

									name = newText;
									return null;
								}
							});

					if (inputDialog.open() == InputDialog.OK)
					{
						String [] names = new String[]{page.getName(),name};
						page.setName(name);
						tableViewer.update(page, null);
						eventBroker.post(PlanningEvent.PLANNING_EVENT_RENAME_PAGE, names);						
					}

				}
				updateWidgets();

			}
				
		});
		btnRename.setBounds(0, 0, 75, 25);
		btnRename.setText("umbenennen");
		
		btnDelete = new Button(compositeButton, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Page page = (Page) selection.getFirstElement();
				
				if(MessageDialog.openQuestion(getShell(),"Zeichnung","Seite löschen"))
				{
					tableViewer.remove(page);
					pages = ArrayUtils.removeElement(pages, page);
					eventBroker.post(PlanningEvent.PLANNING_EVENT_REMOVE_PAGE, page);
				}
				
				updateWidgets();
			}
		});
		btnDelete.setText("löschen");

		updateWidgets();
		return area;
	}
	
	public void setPages(Page [] pages)
	{
		this.pages = pages;
		tableViewer.setInput(pages);
	}
	
	public Page[] getPages()
	{
		return pages;
	}

	public void	setTitle(String title)
	{
		super.setTitle("Die Seiten einer Zeichnung "+"\n"+title);
	}
	
	private boolean isExist(String name)
	{
		Page [] pages = (Page[]) tableViewer.getInput();
		for(Page page : pages)
		{
			if(StringUtils.equals(name, page.getName()))
				return true;
		}
		
		return false;
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
	
	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}

	private void updateWidgets()
	{
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		btnDelete.setEnabled(!selection.isEmpty());
		btnRename.setEnabled(!selection.isEmpty());
		
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(352, 348);
	}
}
