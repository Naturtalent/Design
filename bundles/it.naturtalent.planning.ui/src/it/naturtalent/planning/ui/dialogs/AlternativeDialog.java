package it.naturtalent.planning.ui.dialogs;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.PlanningMaster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AlternativeDialog extends TitleAreaDialog
{
	private Text txtAlternative;
	
	private String name;
	
	private Button okButton;
	
	private ControlDecoration controlDecorationName;
	
	private List<String>availableNames;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AlternativeDialog(Shell parentShell)
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
		setMessage("Die Bezeichnung einer Alternativen´ändern");
		setTitle("Alternative");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		txtAlternative = new Text(container, SWT.BORDER);
		txtAlternative.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				name = ((Text)e.getSource()).getText(); 
				updateWidgets();
			}
		});
		
		txtAlternative.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		controlDecorationName = new ControlDecoration(txtAlternative, SWT.LEFT | SWT.TOP);
		controlDecorationName.setDescriptionText("Some description");
		controlDecorationName.setImage(Icon.OVERLAY_ERROR.getImage(IconSize._7x8_OverlayIconSize));

		updateWidgets();
		return area;
	}
	
	public void initDialog(PlanningMaster planningMaster, String alternativeName)
	{
		Alternative [] alternatives = planningMaster.getAlternatives();
		availableNames = new ArrayList<String>();
		for(Alternative alternative : alternatives)
			availableNames.add(alternative.getName());
		
		availableNames.remove(alternativeName);
		
		this.name = alternativeName;		
		if(StringUtils.isNotEmpty(alternativeName))
			txtAlternative.setText(alternativeName);
		
		updateWidgets();
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,false);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(430, 218);
	}
	
	private void updateWidgets()
	{
		controlDecorationName.hide();
		
		if(StringUtils.isEmpty(name))
		{
			controlDecorationName.show();
			controlDecorationName.setDescriptionText("leeres Eingabefeld");
		}
		else
			if(availableNames.contains(name))
			{
				controlDecorationName.show();
				controlDecorationName.setDescriptionText("Alternative mit diesen Namen existiert bereits");				
			}
					
		if(okButton != null)
			okButton.setEnabled(!controlDecorationName.isVisible());
	}

}
