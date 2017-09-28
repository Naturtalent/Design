 
package it.naturtalent.design.ui.parts;

import java.io.File;
import java.util.Collection;
import java.util.EventObject;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPProjectContentChangedObserver;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.commands.EMFStoreBasicCommandStack;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.event.Event;

import it.naturtalent.application.IPreferenceAdapter;
import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.ui.Activator;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.actions.OpenDesignAction;
import it.naturtalent.design.ui.actions.SaveDesignAction;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class DesignsView
{
	
	/*
	 * Listener meldet Anderungen am Commandstack. Add und Delete Commands werden abgegriffen und die mit dem 
	 * Design korrespondierenden Drawfiles (.odg) synchron zu Erstellen und zu Löschen.
	 */
	private class DesingCommandStackListener implements CommandStackListener
	{
		@Override
		public void commandStackChanged(EventObject event)
		{
			File drawFile;
			EMFStoreBasicCommandStack commandStack = (EMFStoreBasicCommandStack) event.getSource();			
			Command command = commandStack.getMostRecentCommand();
			
			// nur 'Create- und DeleteCommands sind von Interesse
			if((command instanceof CreateChildCommand) || (command instanceof DeleteCommand))
			{		
				// wird ein Design geloescht, dan wird auch das DrawFile geloescht
				if (command instanceof DeleteCommand)
				{
					DeleteCommand delCommand = (DeleteCommand) command;										
					Collection<?> results = delCommand.getResult();
					Object obj = results.iterator().next();
					if (obj instanceof Design)
					{
						Design design = (Design) obj;
						if(StringUtils.isNotEmpty(design.getDesignURL()))
						{
							// versuchen, aus der URL (Annahme: relativer Pfad) eine NtProjektID zu extrahieren								
							Path path = new Path(design.getDesignURL());
							String projectID = path.segment(0);
							IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
								
							// existiert ein NtProjekt mit der extrahierten ID
							if (iProject.exists())
							{
								// Datei im NtProjektdataverzeichnis loeschen
								IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
								IPath iPath = folder.getLocation();
								String designFileName = FilenameUtils.getName(design.getDesignURL());
								iPath = iPath.append(designFileName);
								drawFile = iPath.toFile();
								drawFile.delete();
								DesignUtils.getDesignProject().saveContents();
								eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");
							}
							else
							{
								// aus der URL ist keine brauchbare ProjektID ableitbar - absoluter Pfad unterstellt
								drawFile = path.toFile();								
							}
							
							drawFile.delete();								
							DesignUtils.getDesignProject().saveContents();
							eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");								
						}
						
						log.error("kein DesignDrawFile definiert"); //$NON-NLS-N$
					}
				}
			
				// wurde ein Design erzeugt, wird auch dass dazugehoerige DesignFile erzeugt
				if (command instanceof CreateChildCommand)
				{
					CreateChildCommand addCommand = (CreateChildCommand) command;
					Collection<?>createResults = addCommand.getResult();
					Object createObj = createResults.iterator().next();
					if(createObj instanceof Design)
					{
						Design design = (Design) createObj;
						Object obj = design.eContainer();
						if(obj instanceof DesignGroup)
						{
							DesignGroup desingGroup = (DesignGroup) obj;
							String projectID = desingGroup.getIProjectID();
							if (StringUtils.isNotEmpty(projectID))
							{
								IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
								if (iProject.exists())
								{
									// DrawFile im NtProjectdata-Verzeichnis
									// anlegen - Aederung im DesignProjekt
									// speichern
									String pathDesignFile = DesignUtils
											.createProjectDesignFile(iProject);
									design.setDesignURL(pathDesignFile);
									DesignUtils.getDesignProject().saveContents();
									eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");
								}
								else
								{
									log.error("Ungueltige ProjektID - es wurde kein DrawFile angelgt");
								}
							}
							else
							{
								// kein NtProjekt zugeordnet - Drawfile manuell anlegen
								FileDialog dlg = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
								dlg.setText("neue Zeichnug erzeugen");
								dlg.setFilterExtensions(new String[]{ ".odg" });
								
								// temporaere Verzeichnis wird im Dialog vorauswaehlen
								IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(IPreferenceAdapter.ROOT_APPLICATION_PREFERENCES_NODE);
								String tempPath = instancePreferenceNode.get(IPreferenceAdapter.PREFERENCE_APPLICATION_TEMPDIR_KEY,null);
								dlg.setFilterPath(tempPath);

								// Filedialog oeffnen
								String drawFilePath = dlg.open();
								if (drawFilePath != null)
								{		
									drawFilePath = FilenameUtils.removeExtension(drawFilePath)+"."+"odg";
									design.setDesignURL(drawFilePath);				
									DesignUtils.createDesignFile(drawFilePath);	
									DesignUtils.getDesignProject().saveContents();
									eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");									
								}
							}
						}
					}
				}
			}
		}
	}
	private DesingCommandStackListener desingCommandStackListener = new DesingCommandStackListener();

	public final static String DESIGNSVIEW_ID = "it.naturtalent.design.ui.part.designs";
	
	// Name des ECP Projekts indem alle Zeichnungen gespeichert werden
	public final static String DESIGNPROJECTNAME = "DesignsEMFProject";
	
	// Eventkey zur Selektion einer DesignGroup (@see DesignMasterDetailRenderer)
	public final static String DESIGN_SELECTGROUP_EVENT = "designselectgroupevent";

	// der Reenderer (@DesignMasterDetailRenderer) hat einen TreeViewer angelegt und stellt diesen zur Veruegung 
	public final static String DESIGN_TREEVIEWER_EVENT = "designtreeviewerevent";
	
	// mit diesem Event wird eine Aenderung im DesignModel angezeigt
	public static final String DESIGNPROJECTCHANGED_MODELEVENT = "designprojectmodelevent"; //$NON-NLS-N$

	// mit diesem Event wird angezeigt, dass die Daten des Modell festgeschrieben wurden
	public static final String DESIGNPROJECTSAVED_MODELEVENT = "designprojectsavedmodelevent"; //$NON-NLS-N$

	
	private IProject selectedProject;
	
	private IEventBroker eventBroker;

	private Log log = LogFactory.getLog(this.getClass());
	
	@Inject private ESelectionService selectionService;
	
	/*
	 * 'ECPProjectContentChangedObserver' Überwacht Aenderungen im Modell
	 */
	private class ProjectModelChangeObserver implements ECPProjectContentChangedObserver
	{
		@Override
		public Collection<Object> objectsChanged(ECPProject project,Collection<Object> objects)
		{
			// Aenderungen am Modell werden vie Broker weitergemeldet	
			eventBroker.send(DESIGNPROJECTCHANGED_MODELEVENT, "designProjectModelData changed");			
			return null;
		}

	}
	private ProjectModelChangeObserver projectModelChangedObserver;
	
	/*
	 * Listener ueberwacht zusaetzlich (zum EMF-eigenen Listener) die Selektion im TreeViewer des MasterDetailsRenderer
	 * 
	 */
	private class TreeMasterViewSelectionListener implements ISelectionChangedListener
	{
		@Override
		public void selectionChanged(SelectionChangedEvent event)
		{
			final Object treeSelected = ((IStructuredSelection) event.getSelection()).getFirstElement();			
			if (treeSelected instanceof EObject) 
			{
				// das selektierte Object dem Service uebergeben
				selectionService.setSelection(treeSelected);
				
				// OpenToolItem disablen
				ToolItem openToolitem = DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID);				
				openToolitem.setEnabled(false);
				
				// CloseToolItem disablen
				ToolItem closeToolitem = DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID);
				closeToolitem.setEnabled(false);
				
				// SyncProjektItem disablen
				ToolItem syncToolitem = DesignUtils.getToolItem(DesignUtils.TOOLBAR_LINKPROJECT_ID);
				syncToolitem.setEnabled(false);

				
				if(treeSelected instanceof Design)
				{
					// das dem selektierten Design zugeordnete DrawObjekt in den Vordergrund bringen
					Design selectedDesign = (Design) treeSelected;
					
					// ein NtProject ist definiert > 'syncToolitem' enablen   
					DesignGroup group =  DesignUtils.findDesignGroup(selectedDesign);
					syncToolitem.setEnabled(StringUtils.isNotEmpty(group.getIProjectID()));

					
					Map<Design,DrawDocument> openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
					DrawDocument selectedDrawDocument = openDrawDocumentMap.get(selectedDesign);
					if(selectedDrawDocument != null)	
					{
						// das zugeordneter DrawFile ist geoeffnet, und wird in den Vordergrund gebracht
						selectedDrawDocument.setFocus();
						
						// CloseToolItem enablen
						closeToolitem.setEnabled(true);
						
						if (StringUtils.isNotEmpty(selectedDesign.getDesignURL()))
						{
							// OpenToolItem enablen, wenn ein DrawFile existiert und geoffnet werden koennte 
							File designFile = DesignUtils.getDesignFile(selectedDesign);
							openToolitem.setEnabled(designFile != null);
						}
					}

				}
				else
				{
					if(treeSelected instanceof DesignGroup)
					{
						// ein NtProject ist definiert > 'syncToolitem' enablen   
						DesignGroup group =  (DesignGroup) treeSelected;
						syncToolitem.setEnabled(StringUtils.isNotEmpty(group.getIProjectID()));
					}
				}
			}
		}
	}
	
	private TreeViewer treeViewer;

	@Inject
	public DesignsView()
	{

	}

	@PostConstruct
	public void postConstruct(Composite parent, EPartService partService, IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
		
		//Designs designs = getDesigns();
		Designs designs = DesignUtils.getDesigns();
		if (designs != null)
		{
			try
			{
				ECPSWTViewRenderer.INSTANCE.render(parent, designs);
			}

			catch (ECPRendererException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// einen Command-Listener einbinden
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(DesignUtils.getDesignProject());
		EMFStoreBasicCommandStack commandStack = (EMFStoreBasicCommandStack) domain.getCommandStack();					
		domain.getCommandStack().addCommandStackListener(desingCommandStackListener);
		
		// einen ModelChange-Listener
		projectModelChangedObserver = new ProjectModelChangeObserver();
		ECPUtil.getECPObserverBus().register(projectModelChangedObserver);
		
		// nach der Aktivierung dieses Parts muessen die Toolbaraktionen enablen
		MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);				
		partService.addPartListener(new IPartListener()
		{			
			@Override
			public void partVisible(MPart part)
			{
			}
			
			@Override
			public void partHidden(MPart part)
			{
			}
			
			@Override
			public void partDeactivated(MPart part)
			{
			}
			
			@Override
			public void partBroughtToTop(MPart part)
			{
			}
			
			@Override
			public void partActivated(MPart part)
			{
				if(StringUtils.equals(part.getElementId(), DESIGNSVIEW_ID))
				{
					DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(false);
					DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(false);		

					DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID).setEnabled(false);
					DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID).setEnabled(false);
				}				
			}
		});
		
	}
	
	@PreDestroy
	public void dispose()
	{
		Map<Design, DrawDocument>openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
		for(DrawDocument drawDocument : openDrawDocumentMap.values())
		{
			if(drawDocument != null)
				drawDocument.closeDocument();
		}
		
		if((openDrawDocumentMap != null) && (!openDrawDocumentMap.isEmpty()))
		{
			DrawDocument drawDocument = openDrawDocumentMap.values().iterator().next();
			{
				if(drawDocument != null)
					openDrawDocumentMap.values().iterator().next().closeDesktop();
			}
			
		}
		
		// Command-Listener entfernen 
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(DesignUtils.getDesignProject());
		EMFStoreBasicCommandStack commandStack = (EMFStoreBasicCommandStack) domain.getCommandStack();					
		domain.getCommandStack().removeCommandStackListener(desingCommandStackListener);
		
		// unregister ModeChangeListener
		ECPUtil.getECPObserverBus().unregister(projectModelChangedObserver);
	}
	
	/**
	 * Die selektierte Resource (ResourceNavigator) wird erkannt. Wurde ein Project selektiert wird dies 
	 * zwischengespeichert. Wurde ein IProject selektiert und existiert eine entsprechende proektbezogenen DesignGroup 
	 * im DesignMasterView, dann wird diese Gruppe selektiert. 
	 *  
	 * @param selectedResource
	 */
	@Inject
	public void setResourceSelection(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional IProject selectedProject)
	{
		if(selectedProject != null)
		{
			this.selectedProject = selectedProject;
			DesignGroup designGroup = DesignUtils.findDesignGroup(selectedProject.getName());
			if(designGroup != null)
			{
				// DesignGroup selektieren
				if(treeViewer != null)
					treeViewer.setSelection(new StructuredSelection(designGroup));
			}
		}
	}
	
	public Object getSelection ()
	{
		if(treeViewer != null)
		{
			IStructuredSelection selection = treeViewer.getStructuredSelection();
			return selection.getFirstElement();
		}
		return  null;
	}

	@Inject
	@Optional
	public void handleEvent(@UIEventTopic(DESIGN_TREEVIEWER_EVENT) TreeViewer treeViewer)
	{
		// den vom Reenderer installierten TreeViewer (DetailMasterView) zur Verfuegung stellen
		this.treeViewer = treeViewer;		
		this.treeViewer.addSelectionChangedListener(new TreeMasterViewSelectionListener());	
		
		if(selectedProject != null)
		{			
			DesignGroup designGroup = DesignUtils.findDesignGroup(selectedProject.getName());
			if(designGroup != null)				
				treeViewer.setSelection(new StructuredSelection(designGroup));
		}			
	}

	@Inject
	@Optional
	public void handleModelChangedEvent(@UIEventTopic(DESIGNPROJECTCHANGED_MODELEVENT) String info, MPart part)
	{		
		// Tooltip 'save' disablen - Tooltip 'undo' disablen 
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(true);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(true);		
	}
	
	@Inject
	@Optional
	public void handleModelSaveEvent(@UIEventTopic(DESIGNPROJECTSAVED_MODELEVENT) String info, MPart part)
	{		
		// Tooltip 'save' disablen - Tooltip 'undo' disablen 
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(false);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(false);		
	}


	@Inject
	@Optional
	public void handleDocumentOpneEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN) Object obj, MPart part)
	{
		// Event Dokument wurde geoffnet - Toolbar anpassen
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID, part).setEnabled(false);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID, part).setEnabled(true);	
	}
	
	@Inject
	@Optional
	public void handleDocumentCloseEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE) Object obj, MPart part)
	{
		// Event Dokument wurde geschlossen - Toolbar anpassen
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID, part).setEnabled(true);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID, part).setEnabled(false);	
	}
	
	/*
	 * Event (DesignsView.DESIGN_SELECTGROUP_EVENT) empfangen und die uebergebene DesignGroup "DESIGNGROUP" im 
	 * TreeViewer selektieren
	 */	
	@Inject
	@Optional
	public void handleSelectGroupEvent(@UIEventTopic(DesignsView.DESIGN_SELECTGROUP_EVENT) DesignGroup designGroup)
	{				
		treeViewer.setSelection(new StructuredSelection(designGroup));		
	}

	/*
	 * Das Modell, indem die Designdaten gespeichert werden. wird zurueckgegeben (ggf. neu erzeugt) 
	 */
	/*
	private Designs getDesigns()
	{
		Designs designs = null;
		
		ECPProject designsProject = ECPUtil.getECPProjectManager().getProject(DESIGNPROJECTNAME);	
		
		// ggf. Projekt 'DESIGNPROJECT' erzeugen
		if(designsProject == null)
			designsProject = Activator.createProject(DESIGNPROJECTNAME);
		
		// im ECPProject das Modell Archives suchen 
		EList<Object>projectContents = designsProject.getContents();
		if(!projectContents.isEmpty())
		{
			for(Object projectContent : projectContents)
			{
				if (projectContent instanceof Designs)
				{
					designs = (Designs) projectContent; 
					break;
				}
			}			
		}
		else
		{
			// das Modell Designs erzeugen und im ECPProject speichern
			EClass designsClass = DesignsPackage.eINSTANCE.getDesigns();
			designs = (Designs)EcoreUtil.create(designsClass);
			projectContents.add(designs);
			designsProject.saveContents();			
		}
		
		return designs;
	}
	*/


	
}