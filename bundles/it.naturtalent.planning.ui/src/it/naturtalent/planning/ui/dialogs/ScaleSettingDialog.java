package it.naturtalent.planning.ui.dialogs;

import it.naturtalent.planning.ui.Messages;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class ScaleSettingDialog extends TitleAreaDialog
{
	private Text txtDenominator;
	private Text textLength;

	private Button btnRadioIndirekt;
	private Button btnRadioLength;
	
	private BigDecimal lineLenght;
	private Integer denominator;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ScaleSettingDialog(Shell parentShell)
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
		setMessage(Messages.ScaleSettingDialog_this_message);
		setTitle(Messages.ScaleSettingDialog_this_title);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		btnRadioIndirekt = new Button(container, SWT.RADIO);
		btnRadioIndirekt.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				txtDenominator.setEnabled(btnRadioIndirekt.getSelection());
			}
		});
		btnRadioIndirekt.setText("direkt");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblMastab = new Label(container, SWT.NONE);
		lblMastab.setText(Messages.ScaleSettingDialog_lblMastab_text);
		
		Label labelNominator = new Label(container, SWT.NONE);
		labelNominator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelNominator.setText("1 :");
		
		txtDenominator = new Text(container, SWT.BORDER);
		txtDenominator.setEnabled(false);
		txtDenominator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		btnRadioLength = new Button(container, SWT.RADIO);
		btnRadioLength.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				textLength.setEnabled(btnRadioLength.getSelection());
			}
		});
		btnRadioLength.setToolTipText("Eine im Plan auf dem Layer 'Masslinien' gezeichnete Linie eine konkrete Länge zuordnen");
		btnRadioLength.setSelection(true);
		btnRadioLength.setText("indirekt");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblLength = new Label(container, SWT.NONE);
		lblLength.setText(Messages.ScaleSettingDialog_lblLength_text);
		new Label(container, SWT.NONE);
		
		textLength = new Text(container, SWT.BORDER);
		GridData gd_textLength = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textLength.widthHint = 44;
		textLength.setLayoutData(gd_textLength);
		textLength.setFocus();
		
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
		return new Point(363, 347);
	}

	@Override
	protected void okPressed()
	{		
		if(btnRadioLength.getSelection())
		{
			denominator = null;
			lineLenght = new BigDecimal(textLength.getText());
		}
		else
		{
			lineLenght = null;
			denominator = new Integer(txtDenominator.getText());
		}
		
		super.okPressed();
	}

	public BigDecimal getLineLenght()
	{
		return lineLenght;
	}

	public Integer getDenominator()
	{
		return denominator;
	}

	public void setDenominator(Integer denominator)
	{
		if (denominator != null)
		{
			this.denominator = denominator;
			if (txtDenominator != null)
				txtDenominator.setText(new Integer(denominator).toString());
		}
	}
	
	
}
