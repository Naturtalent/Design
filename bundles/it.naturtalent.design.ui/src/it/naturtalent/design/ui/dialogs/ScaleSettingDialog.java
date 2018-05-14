package it.naturtalent.design.ui.dialogs;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ScaleSettingDialog extends TitleAreaDialog
{
	private Text txtDenominator;
	private Text textLength;

	private Button btnRadioDirect;
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
		//setMessage(Messages.ScaleSettingDialog_this_message);
		//setTitle(Messages.ScaleSettingDialog_this_title);
		
		setTitle("Maßtab definieren");
		setMessage("Maßtab direkt eingeben oder mit einer Linie in der Zeichnung");
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		btnRadioDirect = new Button(container, SWT.RADIO);
		btnRadioDirect.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				txtDenominator.setEnabled(btnRadioDirect.getSelection());
				if(btnRadioDirect.getEnabled())
					txtDenominator.setFocus();
			}
		});
		btnRadioDirect.setText("direkt");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblMastab = new Label(container, SWT.NONE);
		lblMastab.setText("Massstab");
		
		Label labelNominator = new Label(container, SWT.NONE);
		labelNominator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelNominator.setText("1 :");
		
		txtDenominator = new Text(container, SWT.BORDER);
		txtDenominator.setEnabled(false);
		txtDenominator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		txtDenominator.addVerifyListener(new VerifyListener()
		{			
			@Override
			public void verifyText(VerifyEvent e)
			{
				boolean isInt = true;
				
				String newValue = e.text;
				if (StringUtils.isNotEmpty(newValue))
				{
					try
					{
						new Integer(newValue);
					} catch (NumberFormatException e1)
					{
						isInt = false;
					}

					if (!isInt)
						e.doit = false;
				}			
			}
		});
		
		btnRadioLength = new Button(container, SWT.RADIO);
		btnRadioLength.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				textLength.setEnabled(btnRadioLength.getSelection());
				if(btnRadioLength.getEnabled())
					textLength.setFocus();
			}
		});
		btnRadioLength.setToolTipText("Eine im Plan auf dem Layer 'Masslinien' gezeichnete Linie eine konkrete Länge zuordnen");
		btnRadioLength.setSelection(true);
		btnRadioLength.setText("indirekt");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblLength = new Label(container, SWT.NONE);
		lblLength.setText("Länge");
		new Label(container, SWT.NONE);
		
		textLength = new Text(container, SWT.BORDER);
		GridData gd_textLength = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textLength.widthHint = 44;
		textLength.setLayoutData(gd_textLength);
		textLength.setFocus();
		textLength.addVerifyListener(new VerifyListener()
		{			
			@Override
			public void verifyText(VerifyEvent e)
			{
				boolean isInt = true;
				
				String newValue = e.text;
				if (StringUtils.isNotEmpty(newValue))
				{
					try
					{
						new Integer(newValue);
					} catch (NumberFormatException e1)
					{
						isInt = false;
					}

					if (!isInt)
						e.doit = false;
				}			
			}
		});
		
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
			// direkte Eingabe des Massstabs
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
