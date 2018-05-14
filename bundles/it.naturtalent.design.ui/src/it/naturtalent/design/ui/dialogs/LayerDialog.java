package it.naturtalent.design.ui.dialogs;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.design.ui.ILayerShapeFactory;
import it.naturtalent.design.ui.ILayerShapeFactoryRepository;
import it.naturtalent.design.ui.Messages;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class LayerDialog extends TitleAreaDialog
{
	private class TableLabelProvider extends LabelProvider
			implements ITableLabelProvider
	{
		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof ILayerShapeFactory)
			{
				ILayerShapeFactory layerShapeFactory  = (ILayerShapeFactory) element;
				return layerShapeFactory.getLabel();				
			}
			return element.toString();
		}
	}

	private static Shell shell = Display.getDefault().getActiveShell();
	
	private Layer layer;
	private Text text;
	private Table shapetypetable;
	private ILayerShapeFactoryRepository layerShapeRepository;
	private TableViewer tableViewer;
	

	public void setLayer(Layer layer)
	{
		this.layer = layer;		
	}

	/**
	 * @wbp.parser.constructor
	 */
	public LayerDialog()
	{
		this(shell);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LayerDialog(Shell parentShell)
	{
		super(parentShell);
	}
	
	@PostConstruct
	public void postConstruct(@Optional ILayerShapeFactoryRepository layerShapeRepository)
	{
		this.layerShapeRepository = layerShapeRepository;
	}
		
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setTitle(Messages.LayerDialog_this_title);		
		setTitleImage(Icon.WIZBAN_SMILEY.getImage(IconSize._75x66_TitleDialogIconSize));
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblabel = new Label(container, SWT.NONE);
		lblabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblabel.setBounds(0, 0, 58, 17);
		lblabel.setText(Messages.LayerDialog_lblNewLabel_text);
		
		text = new Text(container, SWT.BORDER);
		text.setText("");
		if(layer != null)
			text.setText( StringUtils.isNotEmpty(layer.getName()) ? layer.getName() : "");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblShapetype = new Label(container, SWT.NONE);
		lblShapetype.setText(Messages.LayerDialog_lblNewLabel_text_1);
		new Label(container, SWT.NONE);
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		shapetypetable = tableViewer.getTable();
		shapetypetable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		
		if(layerShapeRepository != null)
			tableViewer.setInput(layerShapeRepository.getLayerShapeFactories());

		return area;
	}
	
	@Override
	protected void okPressed()
	{
		if(layer != null)
			layer.setName(text.getText());
		
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		Object selObject = selection.getFirstElement();
		if(selObject != null)
		{
			ILayerShapeFactory layerShapeFactory  = (ILayerShapeFactory) selObject;
			layer.setShapeFactoryName(layerShapeFactory.getLabel());
		}
				
		super.okPressed();
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
		return new Point(450, 452);
	}
}
