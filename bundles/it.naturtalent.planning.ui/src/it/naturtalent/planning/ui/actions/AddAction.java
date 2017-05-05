package it.naturtalent.planning.ui.actions;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.naturtalent.e4.project.NtProject;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planning.ui.PlanningUtils;
import it.naturtalent.planning.ui.dialogs.DesignDialog;
import it.naturtalent.planning.ui.dialogs.EditPlanungDialog;
import it.naturtalent.planning.ui.dialogs.PlanningItemDialog;
import it.naturtalent.planning.ui.dialogs.SelectAlternativeDialog;
import it.naturtalent.planning.ui.wizards.PlanningWizard;
import it.naturtalent.planning.ui.wizards.PlanningWizardPage;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.DefaultAlternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactory;
import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

public class AddAction extends AbstractPlanungAction
{

	public AddAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_ADD.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		//setToolTipText(Messages.AddAction_NewPlanningLabel);
	}
	
	@Override
	public void run()
	{			
		
		StructuredSelection selection = (StructuredSelection) planungMasterComposite
				.getTreeViewer().getSelection();
		Object selObj = selection.getFirstElement();
		
	
		// eine neue Planung
		if (selObj instanceof PlanningMaster)
		{
			PlanningWizard wizard = new PlanningWizard();
			WizardDialog dialog = new WizardDialog(Display.getDefault()
					.getActiveShell(), wizard);

			// eine neue Planung initialisieren
			dialog.create();
			PlanningWizardPage planningWizardPage = (PlanningWizardPage) wizard
					.getPage(PlanningWizardPage.PLANNING_WIZARD_PAGE);
			planningWizardPage.setAlternativeFactoryRegistry(alternativeFactoryRepository);
			
			PlanningMaster planung = new PlanningMaster();
			planningWizardPage.setPlanningMaster(planung);

			if (dialog.open() == WizardDialog.OK)
			{
				String [] checkedAlternatives = planningWizardPage.getCheckedAlternatives();

				/*
				Alternative[] alternatives = planningWizardPage
						.getCheckedAlternatives();
						

				for (Alternative alternative : alternatives)
					PlanningUtils.updateAlternativeProperties(planung,
							alternative);

				planningModel.addPlanningMaster(planung);
				planningModel.saveModel();
				
				*/
			}
			
			
			return;
		}
	
		// eine neue Alternative
		if (selObj instanceof Alternative)
		{
			Alternative alternative = (Alternative) selObj;
			
			ITreeContentProvider cp = (ITreeContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider();	
			PlanningMaster planung = (PlanningMaster) cp.getParent(alternative);
			String projectKey = planung.getProjektKey();
			if(StringUtils.isNotEmpty(projectKey))
			{
				IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectKey);
				if(iProject != null)
				{
					
					Set<String>existAlternativeNames = new HashSet<String>();
					Alternative [] existAlternatives = planung.getAlternatives();
					for(Alternative existAlternative : existAlternatives)
						existAlternativeNames.add(existAlternative.getName());
						
					String [] availableNames = alternativeFactoryRepository.getAlternativeFatoryNames();
					for(String availableName : availableNames)
						existAlternativeNames.add(availableName);
					
					
					SelectAlternativeDialog alternativeDialog = new SelectAlternativeDialog(
							Display.getDefault().getActiveShell());
					alternativeDialog.create();
					alternativeDialog.setAlternativeNames(alternativeFactoryRepository.getAlternativeFatoryNames());
					if(alternativeDialog.open() == SelectAlternativeDialog.OK)
					{
						// die ausgewaehlte Alternative intanziieren
						String selectedAlternativeName = alternativeDialog.getSelectedAlternative();
						IAlternativeFactory alternativeFactory = alternativeFactoryRepository.getAlternativeFactory(selectedAlternativeName);						
						Alternative newAlternative = alternativeFactory.createAlternative();
						newAlternative.setKey(planung.getId());

						// Zeichnungsdokumente im Projektverzeichnis erzeugen 
						Design [] desings = newAlternative.getDesigns();
						if(ArrayUtils.isNotEmpty(desings))
						{
							for(Design design : desings)
							{
								// Name des DrawDokuments wird von DesignID abgeleitet
								File designFile = PlanningUtils.getProjectDesignFile(iProject, design);
								if (!designFile.exists())
								{
									// Template (leeres DrawDokument) in das Zieldokument kopieren
									designFile = PlanningUtils.copyDesignTemplate(designFile.getPath());														
								}
								
								// Pfad zum Dokument im Design speichern
								design.setDesignURL(designFile.toString());
							}					
						}
						
						// neue Alternative muss Klasse 'Alternative' sein
						Alternative copyNewAlternative = new Alternative();
						copyNewAlternative.copyIn(newAlternative);
						Alternative [] alternatives = planung.getAlternatives();
						alternatives = ArrayUtils.add(alternatives, copyNewAlternative);
						planung.setAlternatives(alternatives);	
						
						planungMasterComposite.getTreeViewer().add(planung, newAlternative);						
						planningModel.update(planung);	
						planningModel.saveModel();
						
						// die neue Alternative selektieren
						planungMasterComposite.getTreeViewer().setSelection(
								new StructuredSelection(copyNewAlternative),true);
					}					
				}
			}
			
			return;			
		}
		
		// ein neues Design
		if (selObj instanceof Design)
		{
			ITreeContentProvider cp = (ITreeContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider();
			
			//ein neues Design erzeugen
			Design newDesign = new Design();
			
			DesignDialog designDialog = new DesignDialog(Display.getDefault().getActiveShell());
			designDialog.create();
			designDialog.setLayerContentRepository(layerContentRepository);
			designDialog.setDesign(newDesign);
			
			// die uebergeordnete Alternative
			Alternative alternative = (Alternative) cp.getParent(selObj);
			
			// die bereits vorhandenen Designnamen zwecks Validierung uebergeben
			Design [] existDesigns = alternative.getDesigns();
			if(ArrayUtils.isNotEmpty(existDesigns))
			{
				List<String>existNames = new ArrayList<String>();
				for(Design design : existDesigns)
					existNames.add(design.getName());				
				designDialog.setAlreadyExistDesigns(existNames);				
			}

			if(designDialog.open() == designDialog.OK)
			{
				if(alternative != null)
				{
					PlanningMaster planningMaster = (PlanningMaster) cp.getParent(alternative);
					
					// update 'DesignURL' nur wenn ein Projekt zugordnet ist					
					String drawDocumentPath = getDesignURL(planningMaster, newDesign);
					
					if(StringUtils.isNotEmpty(drawDocumentPath))
					{
						newDesign.setDesignURL(drawDocumentPath);
					
						Page page = new Page();
						page.setName("Seite 1");
						Page [] pages = {page};
						newDesign.setPages(pages);

						Design [] designs = alternative.getDesigns();
						designs = ArrayUtils.add(designs, newDesign);
						alternative.setDesigns(designs);
						planningModel.update(planningMaster);	
						planningModel.saveModel();
						
						// das neue Design selektieren
						planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(newDesign));
					}
				}
			}
			return;
		}
		
		// PlanningItem - ein neue Ebene
		if (selObj instanceof PlanningItem)
		{
			PlanningItem selectedItem = (PlanningItem) selObj;
			ITreeContentProvider cp = (ITreeContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider();				
			Design design = (Design) cp.getParent(selectedItem);
			
			PlanningItemDialog planningDialog = new PlanningItemDialog(
					Display.getDefault().getActiveShell(), design);
			planningDialog.create();
			planningDialog.setLayerContentRepository(layerContentRepository);
			
			// ein neues Items erzeugen und editieren
			PlanningItem newItem = new PlanningItem();
			newItem.setKey(design.getId());
			planningDialog.setPlanningItem(newItem);
			if(planningDialog.open() == planningDialog.OK)
			{
				// neues Item zum Design hinzufuegen
				PlanningItem [] items = design.getItems();
				items = ArrayUtils.add(items, newItem);
				design.setItems(items);
				
				Alternative alternative = (Alternative)cp.getParent(design);
				PlanningMaster planung = (PlanningMaster) cp.getParent(alternative);
				planningModel.update(planung);	
				planungMasterComposite.initDesign();
				
				// das neue Item selektieren
				planungMasterComposite.getTreeViewer().setSelection(new StructuredSelection(newItem));
			}
			return;
		}		
	}
	
	private String getDesignURL(PlanningMaster planningMaster, Design design)
	{
		if ((design != null) && (planningModel != null))
		{			
			if (planningMaster != null)
			{
				String projectID = planningMaster.getProjektKey();
				if(StringUtils.isNotEmpty(projectID))
				{
					IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
					if(project != null)
					{
						File designFile = PlanningUtils.getProjectDesignFile(project, design);
						if (!designFile.exists())
						{
							// Template in die Zeichnungsdokument kopieren
							designFile = PlanningUtils.copyDesignTemplate(designFile.getPath());
							if (designFile == null)
								return null;
						}
						
						return designFile.getPath();
					}
				}
			}
			else
			{

			}
		}

		return null;
	}
	
}
