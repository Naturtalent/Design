package it.naturtalent.planning.ui;

import org.eclipse.swt.widgets.Composite;

public class AreaTableComposite extends LengthTableComposite
{

	public AreaTableComposite(Composite parent, int style)
	{		
		super(parent, style);		
		tblclmnLength.setText(Messages.AreaTableComposite_Label_Flaeche);
	}

}
