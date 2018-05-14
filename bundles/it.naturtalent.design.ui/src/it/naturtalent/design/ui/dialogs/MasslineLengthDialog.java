package it.naturtalent.design.ui.dialogs;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import it.naturtalent.design.ui.Messages;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

/**
 * Dialog zur Eingabe einer Referenzlaenge.
 * Der Referenzlinie im DrawDocument wird eine definierte Laenge zugeordnet und daraus wird dann der
 * Massstab berechnet.
 * 
 * @author dieter
 *
 */
public class MasslineLengthDialog extends Dialog
{
	private Text textLength;	
	private BigDecimal dialogLength;
	private Button okButton;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MasslineLengthDialog(Shell parentShell)
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
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblLength = new Label(container, SWT.NONE);
		lblLength.setText(Messages.MasslineLengthDialog_lblLength_text);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		textLength = new Text(container, SWT.BORDER);
		textLength.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(StringUtils.isEmpty(textLength.getText()))
					okButton.setEnabled(false);
			}
		});
		textLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
						okButton.setEnabled(true);
					} catch (NumberFormatException e1)
					{
						isInt = false;
					}

					if (!isInt)
					{
						e.doit = false;
						okButton.setEnabled(false);
					}
				}
			}
		});
		
		// momentan wird 'm' als Laengeneinheit vorgegeben 
		CCombo comboUnit = new CCombo(container, SWT.BORDER);
		comboUnit.setEnabled(false);	
		comboUnit.setEditable(false);
		comboUnit.setText(Messages.MasslineLengthDialog_comboUnit_text);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}
	
	@Override
	protected void okPressed()
	{
		dialogLength = new BigDecimal(textLength.getText());
		super.okPressed();
	}

	public BigDecimal getDialogLength()
	{
		return dialogLength;
	}

	
}
