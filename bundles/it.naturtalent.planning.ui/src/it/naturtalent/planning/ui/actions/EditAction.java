package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.planning.ui.PlanningUtils;
import it.naturtalent.planning.ui.dialogs.AlternativeDialog;
import it.naturtalent.planning.ui.dialogs.DesignDialog;
import it.naturtalent.planning.ui.dialogs.PlanningItemDialog;
import it.naturtalent.planning.ui.wizards.PlanningWizard;
import it.naturtalent.planning.ui.wizards.PlanningWizardPage;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactory;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

public class EditAction extends AbstractPlanungAction
{
	private PlanningMaster planung;
	//private Alternative alternative;
	
	public EditAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_EDIT.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		//setToolTipText("Planung bearbeiten");
	}
	
	@Override
	public void run()
	{
		PlanningWizard wizard = new PlanningWizard();		
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
		
		StructuredSelection selection = (StructuredSelection) planungMasterComposite
				.getTreeViewer().getSelection();
		Object selObj = selection.getFirstElement();
		
		// Planung selektiert
		if(selObj instanceof PlanningMaster)
		{	
			dialog.create();
			PlanningWizardPage wizardPage = (PlanningWizardPage) wizard.getPage(PlanningWizardPage.PLANNING_WIZARD_PAGE);		
			wizardPage.setAlternativeFactoryRegistry(alternativeFactoryRepository);
			
			// selektierte Planung an Dialog uebergeben
			planung = (PlanningMaster) selObj;
			wizardPage.setPlanningMaster(planung);
			wizardPage.updateWidgets();
			
			if(dialog.open() == WizardDialog.OK)
			{
				IProject iProject = null;
				String projectKey = planung.getProjektKey();
				if(StringUtils.isNotEmpty(projectKey))
					iProject =  ResourcesPlugin.getWorkspace().getRoot().getProject(projectKey);
								
				Map<String,Alternative> existAlternatives = new HashMap<String, Alternative>();				
				Alternative [] planningAlternatives = null;
								
				PlanningWizardPage planningWizardPage = (PlanningWizardPage) wizard.getPages()[0];				
				String [] checkedAlternatives = planningWizardPage.getCheckedAlternatives();
								
				if(planung != null)
				{
					//die in Planung existierenden Alternativen aus checked entfernen				
					Alternative[] planungAlternatives = planung.getAlternatives();
					if(ArrayUtils.isNotEmpty(planungAlternatives))
					{
						for(Alternative alternative : planungAlternatives)
							existAlternatives.put(alternative.getName(), alternative);
						
						List<String>checkeds = Arrays.asList(checkedAlternatives);
						for(String checked : checkedAlternatives)
						{
							if(existAlternatives.containsKey(checked))
							{
								// eine bereits zugeordnete Alternative bleibt erhalten
								planningAlternatives = ArrayUtils.add(planningAlternatives, existAlternatives.get(checked));
								checkedAlternatives = ArrayUtils.removeElement(checkedAlternatives, checked);
							}
						}
					}										
				}
				
				if(ArrayUtils.isNotEmpty(checkedAlternatives))
				{
					for(String checkedAlternative : checkedAlternatives)
					{
						// existiert eine Factory, dann neue Alternative instanziieren
						IAlternativeFactory alternativeFactory = alternativeFactoryRepository
								.getAlternativeFactory(checkedAlternative);
						if (alternativeFactory != null)
						{
							// neue Alternative hinzufuegen
							Alternative alternative = alternativeFactory.createAlternative();
							planningAlternatives = ArrayUtils.add(planningAlternatives, alternative);
							
							// die Designs updaten
							Design [] designs = alternative.getDesigns();
							for(Design design : designs)
							{			
								// Key auf die Alternative
								design.setKey(alternative.getId());
								
								if (iProject != null)
								{
									File designFile = PlanningUtils.getProjectDesignFile(iProject,design);
									if (!designFile.exists())
									{
										// Template in die Zeichnungsdokument
										// kopieren
										designFile = PlanningUtils.copyDesignTemplate(designFile.getPath());
									}

									design.setDesignURL(designFile.toString());
								}
							}							
						}
					}
				}
				
				planung.setAlternatives(planningAlternatives);
				planningModel.update(planung);
				
			}		
			return;
		}

		// Alternative selektiert
		if(selObj instanceof Alternative)
		{
			Alternative alternative = (Alternative) selObj;
			AlternativeDialog alternativeDialog = new AlternativeDialog(Display.getDefault().getActiveShell());
			alternativeDialog.create();
			planung = planungMasterComposite.getSelectedPlanning();
			alternativeDialog.initDialog(planung, alternative.getName());			
			if(alternativeDialog.open() == AlternativeDialog.OK)
			{
				alternative.setName(alternativeDialog.getName());				
				planningModel.update(planung);				
			}
		}
		
		// Design selektiert
		if(selObj instanceof Design)
		{
			DesignDialog designDialog = new DesignDialog(Display.getDefault().getActiveShell());
			designDialog.create();
			designDialog.setLayerContentRepository(layerContentRepository);
			
			Design orgDesign = (Design) selObj;
			Design cloneDesign = orgDesign.clone();

			// die zugehoerige Alternative
			ITreeContentProvider cp = (ITreeContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider();				
			Alternative alternative = (Alternative) cp.getParent(orgDesign);
			
			// die bereits vorhandenen Desingnnamen zwecks Validierung uebergeben
			Design [] designs = alternative.getDesigns();
			if(ArrayUtils.isNotEmpty(designs))
			{
				List<String>existNames = new ArrayList<String>();
				for(Design design : designs)
					existNames.add(design.getName());
				existNames.remove(orgDesign.getName());
				designDialog.setAlreadyExistDesigns(existNames);				
			}
			
			designDialog.setDesign(cloneDesign);
			if(designDialog.open() == DesignDialog.OK)
			{
				// die Daten des bearbeiteten Design uebernehmen
				orgDesign.setName(cloneDesign.getName());
				orgDesign.setItems(cloneDesign.getItems());
				
				// die zugeoerige Planung speichern
				PlanningMaster planung = (PlanningMaster) cp.getParent(alternative);
				planningModel.update(planung);
			}
		}
		
		// PlanningItem selektiert
		if(selObj instanceof PlanningItem)
		{
			PlanningItem orgItem = (PlanningItem) selObj;			
			PlanningItemDialog planningDialog = new PlanningItemDialog(Display.getDefault().getActiveShell(), null);
			planningDialog.create();
			planningDialog.setLayerContentRepository(layerContentRepository);
			planningDialog.setPlanningItem(orgItem.clone());
			PlanningItemDialog planningItemDialog = new PlanningItemDialog(Display.getDefault().getActiveShell(), null);
			planningItemDialog.create();
			planningItemDialog.setLayerContentRepository(layerContentRepository);
			planningItemDialog.setPlanningItem(orgItem.clone());
			if(planningItemDialog.open() == PlanningItemDialog.OK)
			{
				Layer layer = null;
				PlanningItem editedItem = planningItemDialog.getPlanningItem();								
				if(!StringUtils.equals(orgItem.getLayer(), editedItem.getLayer()));
				{
					// evtl. Namensaenderung im DrawDokument realisieren
					if(drawDocument != null)
					{					
						layer = drawDocument.getLayer(orgItem.getLayer());
						layer.setName(editedItem.getLayer());						
					}					
					else
					{
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Ebene", "Änderung nur bei geöffneten Dokument möglich");
						return;
					}
				}

				// die editierten Daten uebernehmen
				orgItem.copyIn(editedItem);
				
				ITreeContentProvider cp = (ITreeContentProvider) planungMasterComposite
						.getTreeViewer().getContentProvider();				
				Design design = (Design) cp.getParent(orgItem);				
				Alternative alternative = (Alternative)cp.getParent(design);
				PlanningMaster planung = (PlanningMaster) cp.getParent(alternative);
				planningModel.update(planung);
				
				// persist speichern, bei gleichzeitiger Aenderungen im Dokument (geanderter Layername)
				if(layer != null)
					planningModel.saveModel();
			}
		}
	}
	
}
