package it.naturtalent.planning.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

public class PlanningWizard extends Wizard
{

	public PlanningWizard()
	{
		setWindowTitle("PlanningWizard");
	}

	@Override
	public void addPages()
	{
		addPage(new PlanningWizardPage());
	}

	@Override
	public boolean performFinish()
	{
		return true;
	}

}
