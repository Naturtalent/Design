 
package it.naturtalent.design.ui.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPProjectContentChangedObserver;
import org.eclipse.emf.ecp.internal.edit.ECPControlHelper;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
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
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;

import it.naturtalent.application.IPreferenceAdapter;
import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.Layer;
import it.naturtalent.design.model.design.LayerSet;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.PageHelper;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class DesignsView
{
	
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

	// Event zeigt an, dass ein EObject dem Modell hinzuefuegt wurde
	public static final String DESIGNPROJECTADD_MODELEVENT = "designprojectaddmodelevent"; //$NON-NLS-N$

	
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
			if ((command instanceof CreateChildCommand)
					|| (command instanceof DeleteCommand)
					|| (command instanceof AddCommand)
					|| (command instanceof SetCommand))
			{	

				if (command instanceof SetCommand)
				{
					eventBroker.send(DESIGNPROJECTCHANGED_MODELEVENT, "Model changed");	
					return;
				}
				
				// wird ein Design geloescht, dan wird auch das DrawFile geloescht
				if (command instanceof DeleteCommand)
				{
					DeleteCommand delCommand = (DeleteCommand) command;										
					Collection<?> results = delCommand.getResult();
					Object obj = results.iterator().next();

					if (obj instanceof DesignGroup)
					{
						// NtProject zuegordnete Groups koennen nicht geloescht werden
						DesignGroup group = (DesignGroup) obj;
						if(!StringUtils.isNotEmpty(group.getIProjectID()))
						{	
							// kein NtProject, alle DrawDocumente separat loeschen
							EList<Design>designs = (EList<Design>)group.getDesigns();								
							for(Design groupedDesign : designs)
							{
								String designFileName = groupedDesign.getDesignURL();
								new File(designFileName).delete(); 
							}
							
							// das geaenderte Modell festschreiben
							DesignUtils.getDesignProject().saveContents();
							eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");								
						}
						
						return;
					}
					
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
							return;
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
							// in DesignGroup nachschauen ob ein NtProjekt zugordnet ist (DrawFile wird dann im NtProjekt-Datenbereich estellt)
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
									DesignUtils.createDesignFile(drawFilePath);	
								}
								else
								{
									// Dialogabbruch - autocreate DrawFile
									drawFilePath = DesignUtils.autoCreateDrawFile();
								}
								
								design.setDesignURL(drawFilePath);	
								DesignUtils.getDesignProject().saveContents();
								eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");																	
							}
						}
						
						// das neue Design selektieren
						treeViewer.setSelection(new StructuredSelection(design));
					}
					else
					{
						// neue Page
						if(createObj instanceof Page)
						{
							Page page = (Page) createObj;

							// automatisch einen Pagenamen generieren
							Design design = (Design) page.eContainer();
							EList<Page>pages = design.getPages();
							String pageName = DesignUtils.getAutoPageName(pages);
							page.setName(pageName);
							
							// neue Seite im DrawDocument hinzufuegen
							DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
							drawDocument.addDrawPage(page.getName());
							
							// neue Seite im Modell(TreeViewer) selektieren
							treeViewer.refresh(activeDesign);
							treeViewer.setSelection(new StructuredSelection(page));
						}
						else
						{
							// neuer Layer
							System.out.println("neuer Layer");
						}
					}
				}
				
				if (command instanceof AddCommand)
				{
					AddCommand addCommand = (AddCommand) command;		
					Collection<?> results = addCommand.getResult();
					EObject newObject = (EObject) results.iterator().next();
					eventBroker.send(DESIGNPROJECTADD_MODELEVENT, newObject);
				}

			}
		}
	}
	private DesingCommandStackListener desingCommandStackListener = new DesingCommandStackListener();

	private IProject selectedProject;
	
	private IEventBroker eventBroker;

	private Log log = LogFactory.getLog(this.getClass());
	
	@Inject private ESelectionService selectionService;
	
	@Inject private EPartService partService;
	
	private Design activeDesign;
	private Page activePage;
	
	// bei der erstmaligen Aktivierung den Toolbar-Status aktivieren 
	private static boolean firstTimeActivated = false;
	private IPartListener iPartListener = new IPartListener()
	{
		
		@Override
		public void partVisible(MPart part)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void partHidden(MPart part)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void partDeactivated(MPart part)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void partBroughtToTop(MPart part)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void partActivated(MPart part)
		{
			// beim erstmaligen Aktivieren der View den Toolbarstatus aktualisieren 
			if(StringUtils.equals(part.getElementId(), DESIGNSVIEW_ID))
			{					
				if (!firstTimeActivated)
				{						
					updateToolBarStatus(part);
					firstTimeActivated = true;
				}					
			}				
		}
	};
	
	// Zuordnungstabelle Desing und geoffnetes DrawDocument
	public static Map<Design,DrawDocument> openDrawDocumentMap = new HashMap<Design,DrawDocument>();
	
	public static final String DEFAULT_LAYERSETNAME = "alle Layer";

	
	/*
	 * 'ECPProjectContentChangedObserver' Überwacht Aenderungen im Modell
	 */
	// Obsolet - @see DesignCommandStackListener()
	private class ProjectModelChangeObserver implements ECPProjectContentChangedObserver
	{
		@Override
		public Collection<Object> objectsChanged(ECPProject project,Collection<Object> objects)
		{
			// Aenderungen am Modell werden vie Broker weitergemeldet	
			//eventBroker.send(DESIGNPROJECTCHANGED_MODELEVENT, "designProjectModelData changed");			
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
				// DesignView aktivieren (verhindert No Active Window Error)
				MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
				partService.activate(part);
				
				// das selektierte Object dem SdelectinService uebergeben
				selectionService.setSelection(treeSelected);

				// Toolbar entsprechend der Selektion updaten
				updateToolBarStatus(part);
				
				// Design selektiert
				//EObject eObject = DesignUtils.rollUpToDesing((EObject) treeSelected);
				if(treeSelected instanceof Design)
				{
					// das dem selektierten Design zugeordnete DrawObjekt in den Vordergrund bringen
					Design selectedDesign = (Design) treeSelected;					
					DrawDocument selectedDrawDocument = openDrawDocumentMap.get(selectedDesign);
					if(selectedDrawDocument != null)	
					{
						// das zugeordneter DrawFile ist geoeffnet, und wird in den Vordergrund gebracht
						selectedDrawDocument.setFocus();
					}
				}
				else
				{
					// Page selektiert
					if (treeSelected instanceof Page)
					{
						Page page = (Page) treeSelected;
						Design parentDesign = (Design) page.eContainer();
						if (!parentDesign.equals(activeDesign))
						{
							// System.out.println("nicht synchron");

							// Pageselektion erfolgte in einer anderen als der
							// aktuellen Zeichnung
							treeViewer.removeSelectionChangedListener(treeMasterViewSelectionListener);

							// die 'andere' Zeichnung wird zur aktiven, deren
							// Pages werden neu eingelesen
							activeDesign = parentDesign;
							//readDesignPages();
							pullDrawDocumentData();
							treeViewer.refresh(activeDesign);

							// gibt es bei den neu eingelesenen Pages eines mit
							// dem urspruenglichen Namen,
							// wird dieses selektiert
							String pageName = page.getName();
							EList<Page> activePages = activeDesign.getPages();
							for (Page activePage : activePages)
							{
								if (StringUtils.equals(pageName,activePage.getName()))
								{
									treeViewer.setSelection(new StructuredSelection(activePage));
									break;
								}
							}

							treeViewer.addSelectionChangedListener(treeMasterViewSelectionListener);
						}
						
						// Seite im DrawDocument selektieren
						DrawDocument selectedDrawDocument = openDrawDocumentMap.get(activeDesign);
						if (selectedDrawDocument != null)
						{
							// Selektion der Page in DrawDocument
							// System.out.println("Select Page im Modell");							
							selectedDrawDocument.selectPage(page.getName()); 
							selectedDrawDocument.setFocus();
						}
					}
					
					// Layer selektiert
					if (treeSelected instanceof Layer)
					{
						Layer layer = (Layer) treeSelected;
						Design parentDesign = (Design) layer.eContainer();
						if (!parentDesign.equals(activeDesign))
						{
							// System.out.println("nicht synchron");

							// Layerselektion erfolgte in einer anderen als der
							// aktuellen Zeichnung
							treeViewer.removeSelectionChangedListener(treeMasterViewSelectionListener);

							// die 'andere' Zeichnung wird zur aktiven, deren
							// Daten werden neu eingelesen
							activeDesign = parentDesign;
							pullDrawDocumentData();
							treeViewer.refresh(activeDesign);

							// gibt es bei den neu eingelesenen Layer eines mit
							// dem urspruenglichen Namen,
							// wird dieses selektiert
							String layerName = layer.getName();
							EList<Layer> activeLayers = activeDesign.getLayers();
							for (Layer activeLayer : activeLayers)
							{
								if (StringUtils.equals(layerName,activeLayer.getName()))
								{
									treeViewer.setSelection(new StructuredSelection(activeLayer));
									break;
								}
							}

							treeViewer.addSelectionChangedListener(treeMasterViewSelectionListener);
						}
						
						// Layer im DrawDocument selektieren
						DrawDocument selectedDrawDocument = openDrawDocumentMap.get(activeDesign);
						if (selectedDrawDocument != null)
						{
							// Selektion des Layers in DrawDocument
							// System.out.println("Select Page im Modell");							
							selectedDrawDocument.selectLayer(layer.getName()); 
							selectedDrawDocument.setFocus();
						}
					}
				}
			}
		}
	}
	private TreeMasterViewSelectionListener treeMasterViewSelectionListener = new TreeMasterViewSelectionListener();
	
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
		partService.addPartListener(iPartListener);
	}
	
	@PreDestroy
	public void dispose(EPartService partService)
	{		
		for(DrawDocument drawDocument : openDrawDocumentMap.values())
		{
			if(drawDocument != null)
				drawDocument.closeDesktop();
		}
		
		/*
		 * An dieser Stelle verursacht die Funktion in bestimmten Faellen eine jpipe-bridge closed Excpetion.
		 * Ist die Funktion erforderlich ?
		 */	
		/*
		if((openDrawDocumentMap != null) && (!openDrawDocumentMap.isEmpty()))
		{
			DrawDocument drawDocument = openDrawDocumentMap.values().iterator().next();
			{
				if(drawDocument != null)
					openDrawDocumentMap.values().iterator().next().closeDesktop();
			}			
		}
		*/
	
		partService.removePartListener(iPartListener);
		
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
	 * Der SelectionChangeListener muss vor der Selektion deaktiviert werden, da das Event vom ResourceNavigator
	 * getriggert wurde und dieses ist somit auch das aktive Fenster. Somit ist sichergestellt dass DesignView 
	 * seinerseits von seinem SelectionChangeListener aktiviert wird.  
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
				{
					// TreeMasterViewSelectionListener deaktivieren
					treeViewer.removeSelectionChangedListener(treeMasterViewSelectionListener);
					treeViewer.setSelection(new StructuredSelection(designGroup));
					treeViewer.addSelectionChangedListener(treeMasterViewSelectionListener);
				}
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
	
	public void setSelection (EObject eObject)
	{
		if(treeViewer != null)
		{
			treeViewer.setSelection(new StructuredSelection(eObject));
		}
	}


	/*
	 * TreeViewer wurde generiert (vom Renderer)
	 */
	@Inject
	@Optional
	public void handleEvent(@UIEventTopic(DESIGN_TREEVIEWER_EVENT) TreeViewer treeViewer)
	{
		// den vom Reenderer installierten TreeViewer (DetailMasterView) zur Verfuegung stellen
		this.treeViewer = treeViewer;		
		this.treeViewer.addSelectionChangedListener(treeMasterViewSelectionListener);	
		
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
		// Aenderung im Model erkannt, Status Tooltip - Aktionen anpassen
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(true);
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(true);	
		updateToolBarStatus(part);
	}
	
	@Inject
	@Optional
	public void handleModelSaveEvent(@UIEventTopic(DESIGNPROJECTSAVED_MODELEVENT) String info, MPart part)
	{		
		// Modeldaten wurde gespeichert, Status Tooltip - Aktionen anpassen 
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(false);
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(false);
		updateToolBarStatus(part);
	}

	@Inject
	@Optional
	public void handleModelAddEvent(@UIEventTopic(DESIGNPROJECTADD_MODELEVENT) EObject newObject, MPart part)
	{		
		// Modeldaten wurde gespeichert, Status Tooltip - Aktionen anpassen 
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(true);
		//DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(true);	
		updateToolBarStatus(part);
	}

	/*
	 * Zeichnung wurde geoffnet
	 *  
	 * @param obj
	 * @param part
	 */
	@Inject
	@Optional
	public void handleDocumentOpenEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN) DrawDocument drawDocument, MPart part)
	{
		if(activeDesign != null)
		{
			if(!openDrawDocumentMap.containsKey(activeDesign))
			{
				// das geoffnete DrawDocument unter dem Key 'activeDesign" eintragen
				openDrawDocumentMap.put(activeDesign, drawDocument);
				
				// Toolbarstatus updaten
				updateToolBarStatus(part);
				
				// Modell auf den neuesten Stand bringen
				pullDrawDocumentData();
			
				// nach dem Oeffnen wird die aktuelle Page des DrawDocuments auch entsprechend im TreeViewer selektiert
				selectCurrentPage();	
				
				return;
			}			
		}
		
		log.error("Fehler beim Oeffnen des DrawDocuments");
	}
	
	/*
	 * Zeichnung wurde geschlossen
	 *  
	 * @param obj
	 * @param part
	 */
	@Inject
	@Optional
	public void handleDocumentCloseEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE) Object obj, MPart part)
	{
		// das geschlossene DrawDocument aus der Liste der geoffneten Dokumente entfernen
		if (obj instanceof DrawDocument)
		{
			DrawDocument closedDrawDocument = (DrawDocument) obj;
			for(Design design : openDrawDocumentMap.keySet())
			{
				if (design != null)
				{
					DrawDocument openDrawDocument = openDrawDocumentMap.get(design);
					if (openDrawDocument.equals(closedDrawDocument))
					{
						// das geschlossene DrawDocument aus 'openDrawDocumentMap'
						openDrawDocumentMap.remove(design);
						break;
					}
				}
			}
		}				

		// Toolbarstatus aktualisieren
		updateToolBarStatus(part);
	}
	
	/*
	 * Event kommt ueber Hyperlink-Action im NtProperty-Details und selektiert die dem NtProjekt zugeordnete
	 * DesignGroup.
	 * 
	 * Event (DesignsView.DESIGN_SELECTGROUP_EVENT) empfangen und die uebergebene DesignGroup "DESIGNGROUP" im 
	 * TreeViewer selektieren (@see DesignProjectProperty)
	 */	
	@Inject
	@Optional
	public void handleSelectGroupEvent(@UIEventTopic(DesignsView.DESIGN_SELECTGROUP_EVENT) DesignGroup designGroup)
	{				
		if(treeViewer != null)
			treeViewer.setSelection(new StructuredSelection(designGroup));		
	}
	
	/*
	 * Design selektiert
	 * 
	 * Das selektierte Design wird aktuelle Zeichnung 'activeDesign' gespeichert.
	 * 
	 */	
	@Inject
	@Optional
	public void handleSelectDesignEvent(@Named(IServiceConstants.ACTIVE_SELECTION) Design design)
	{		
		this.activeDesign = design;
		
		// ist die selektierte Seite auch geoffnet, werden alle pages des DrawDocuments lesen 
		if(design != null)
		{
			pullDrawDocumentData();
		}
	}
	
	/*
	 * Page selektiert
	 * 
	 * Das selektierte Design wird aktuelle Zeichnung 'activeDesign' gespeichert.
	 * 
	 */	
	/*
	@Inject
	@Optional
	public void handleSelectDesignEvent(@Named(IServiceConstants.ACTIVE_SELECTION) Page page)
	{		
		this.activePage = page;
		
		// ist die selektierte Seite auch geoffnet, werden alle pages des DrawDocuments lesen 
		if(page != null)
			readAllLayers();		
	}
	*/

	
	/*
	 * Wechsel des DrawDocuments
	 * 
	 * Eine anderes DrawDocument wurde selektiert - das entsprechende Design selektieren
	 * Der FrameActionListener meldet die Aktivierung eines Frames (DrawDocument). Das dazugehoerige Design wird ueber
	 * die 'openDrawDocumentMap' gesucht und im TreeViewer selektiert. Uber das aktivierte Frame kann das DrawDocument 
	 * mit dem gleichen Frame gefunden werden, das zugehoerige Design ergibt sich aus dem Map.
	 * 
	 */	
	@Inject
	@Optional
	public void handleSelectDesignEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_ACTIVATE) Object xFrame)
	{								
		for(Design design : openDrawDocumentMap.keySet())
		{
			DrawDocument drawDocument = openDrawDocumentMap.get(design);			
			Object openXFrame = drawDocument.getXframe();
			if(openXFrame.equals(xFrame))
			{
				// DrawDocument gefunden von dem das Event ausging - wird 'actives' Design
				treeViewer.setSelection(new StructuredSelection(design));				
				break;
			}
		}
	}
	
	/*
	 * DrawPage wurde selektiert.
	 * DrawPagePropertyListener meldete eine geanderte "CurrentPage" Eigenschaft. Dies wird als Selektionsaenderung der
	 * DrawPage interpretiert.
	 * 	  
	 * Diese Funktion sucht im Modell das zur selektierten XDrawPage gehoerende Design und macht diese Zeichnung zur
	 * 'activeDesign'. Zusaetzlich wird die Page innerhalb der Zeichnung selektiert. 
	 * 
	 * Die Modelldaten muessen syncronisiert werden, das LibreOffice offensichtlich Seiten loescht und neu erzeugt.
	 * (ist z.B. Page 2 leer und wird verschoben wird diese geloescht und eine neue Page 3 an der neuen Position erzeugt) 
	 * 
	 * @param obj
	 * @param part
	 */
	@Inject
	@Optional
	public void handleDrawPageChangeProperty(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_PAGECHANGE_PROPERTY) Object xDrawPage)
	{				
		// die Zeichnung mit der Seite 'xDrawPage' wird zur activen Zeichnung 
		Design design = findParentDesign(xDrawPage);
		
		if(design != null)
			treeViewer.setSelection(new StructuredSelection(design));
						
		// DrawDocumentDaten im Modell aktualisieren
		pullDrawDocumentData();

		// auch im Modell(TreeViewer) die aktive DrawDocument Seite selektieren  
		DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
		String pageName = drawDocument.readPageName(xDrawPage);		
		if(StringUtils.isNotEmpty(pageName))
		{
			EList<Page>designPages = activeDesign.getPages();
			for(Page page : designPages)
			{
				if(StringUtils.equals(pageName, page.getName()))
				{		
					treeViewer.removeSelectionChangedListener(treeMasterViewSelectionListener);
					treeViewer.refresh(activeDesign);					
					treeViewer.setSelection(new StructuredSelection(page));
					treeViewer.addSelectionChangedListener(treeMasterViewSelectionListener);
					return;
				}
			}
		}
	}
	
	/*
	 * Shape selektiert
	 * 
	 * Im DrawDocument wurde ein Shape selektiert. Es wird analysiert @see doShapeSelection() in welchem Layer der Shape 
	 * definiert ist. Dieser Layer wird selektiert.
	 * 
	 */	
	@Inject
	@Optional
	public void handleShapeSelectedEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_SHAPE_SELECTED) Object eventObject)
	{	
		// die Realisierung erfolgt in @see doShapeSelection() 
		DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
		if(drawDocument != null)
			drawDocument.doShapeSelection(eventObject);
	}
	
	/*
	 * Global Mousecklick
	 * 
	 * Ein Shape an der Mouseposition zeichnen.
	 * 
	 */	
	@Inject
	@Optional
	public void handleGlobalMouseClickEvent(@UIEventTopic(DrawDocumentEvent.DRAWDOCUMENT_EVENT_GLOBALMOUSEPRESSED) Object eventObject)
	{					
		if (eventObject instanceof NativeMouseEvent)
		{			
			DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
			if(drawDocument != null)
			{
				if (drawDocument.isStampMode())
				{
					// war der MouseClick im TopWindow
					NativeMouseEvent mouseEvent = (NativeMouseEvent) eventObject;
					Object mousePoint = drawDocument.containsPoint(
							mouseEvent.getX(), mouseEvent.getY());
					if (mousePoint != null)
					{

						System.out.println("ClickIntervall: "
								+ GlobalScreen.getMultiClickIterval());

						// die angepasste Postion wird weitergegeben
						drawDocument.doGlobalMouseEvent(mousePoint);
					}
				}
			}
		}
	}


	/**
	 * Den Status der Tooolbaraktionen aktualisieren.
	 * 
	 * @param part
	 */
	private void updateToolBarStatus(MPart part)
	{
		// Undo
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(DesignUtils.getDesignProject());	
		//domain.getCommandStack().canUndo();
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_UNDO_ID, part).setEnabled(domain.getCommandStack().canUndo());
		
		// Save Data
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_SAVE_ID, part).setEnabled(DesignUtils.getDesignProject().hasDirtyContents());
		
		// die restlichen zunaechst zuruecksetzen
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID, part).setEnabled(false);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID, part).setEnabled(false);
		DesignUtils.getToolItem(DesignUtils.TOOLBAR_LINKPROJECT_ID, part).setEnabled(false);
		
		// ist ein Design selektiert (auch indirekt ueber Childs)	
		Object selObj = getSelection();
		EObject eObject = DesignUtils.rollUpToDesing((EObject) selObj);
		if(eObject instanceof Design)
		{
			Design design = (Design) eObject; 			
			DesignUtils.getToolItem(DesignUtils.TOOLBAR_OPENDESIGN_ID, part)
					.setEnabled(!openDrawDocumentMap.containsKey(design));
			
			DesignUtils.getToolItem(DesignUtils.TOOLBAR_CLOSEDESIGN_ID, part)
			.setEnabled(openDrawDocumentMap.containsKey(design));	
						
			DesignGroup group = (DesignGroup) design.eContainer();
			if(group == null)
			{
				// passiert, wenn design geloescht wurde
				DesignUtils.getToolItem(DesignUtils.TOOLBAR_LINKPROJECT_ID, part)
				.setEnabled(false);
			}
			else
			{			
				DesignUtils.getToolItem(DesignUtils.TOOLBAR_LINKPROJECT_ID, part)
				.setEnabled(StringUtils.isNotEmpty(group.getIProjectID()));
			}
		}
		else
		{
			if(selObj instanceof DesignGroup)
			{
				DesignGroup group =  (DesignGroup) selObj;
				DesignUtils.getToolItem(DesignUtils.TOOLBAR_LINKPROJECT_ID, part)
				.setEnabled(StringUtils.isNotEmpty(group.getIProjectID()));
			}
		}
	}
	
	/*
	 * Selektiert die Page des DrawDocuments im Modell
	 */
	private void selectCurrentPage()
	{		
		DrawDocument openDrawDocument = openDrawDocumentMap.get(activeDesign);
		
		// macht nur Sinn bei geoffneten Dokumenten
		if (openDrawDocument != null)
		{
			String pageName = openDrawDocument.getCurrentPage();
			EList<Page> pages = activeDesign.getPages();
			for (Page page : pages)
			{
				if (StringUtils.equals(page.getName(), pageName))
				{
					treeViewer.refresh(activeDesign);
					treeViewer.setSelection(new StructuredSelection(page));
					break;
				}
			}
		}
	}
		
	public static Map<Design,DrawDocument> getDrawDocumentMap()
	{
		return openDrawDocumentMap;		
	}
	
	/*
	 * ermittelt diejenige Zeichnung, in der sich die Seite 'xDrawPage' befindet
	 * 
	 */
	private Design findParentDesign(Object xDrawPage)
	{		
		for(Design design : openDrawDocumentMap.keySet())
		{
			DrawDocument drawDocument = openDrawDocumentMap.get(design);
			if(drawDocument.isChildPage(xDrawPage))
				return design;
		}
		return null;
	}

	/*
	 * alle DrawDocumentDaten in das Modell einlesen (synchronisieren)
	 * 
	 */
	private void pullDrawDocumentData()
	{
		readDesignPages();
		readDesignLayers();
		
		if(activeDesign != null)
		{
			DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);	
			if(drawDocument != null)
				drawDocument.readScaleData();
		}
	}

	/*
	 * Modelldaten auf den neusten Stand bringen (mit dem DrawDocument synchronisieren)
	 * 
	 * Alle Pages der Zeichnung einlesen.
	 * Die Modelldaten entsprechen anpassen.
	 */
	private void readDesignPages()
	{
		if (activeDesign != null)
		{			
			DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
			if(drawDocument != null)
			{
				// Namen aller im DrawDocument enthaltenen Pages
				List<String> drawPageNames = drawDocument.getAllPages();
				
				// die momentan im Modell gespeicherten Pages
				EList<Page>modelPages = activeDesign.getPages();

				// Hilfsliste sammelt alle ModelPages ohne Entsprechung im DrawDocument
				List<Page>obsoletModelPages = new ArrayList<Page>();

				// Abgleich ModelPage zur DrawDocumentPage
				for(Page modelPage : modelPages)
				{
					String modelPageName = modelPage.getName();
					if(!drawPageNames.contains(modelPageName))
					{
						// ModellPage hat keine Entsprechung im DrawDocument 
						obsoletModelPages.add(modelPage);
					}
					else
					{
						// ModellPage hat eine Entsprechung im DrawDocument
						drawPageNames.remove(modelPageName);
					}
				}
				
				// Verbliebene DrawDocumentPages in das Modell uebernehme
				for(String drawPageName : drawPageNames)
				{
					Page page;
					
					// ein Obsoletes wiederverwenden 
					if (!obsoletModelPages.isEmpty())
					{
						page = obsoletModelPages.get(0);
						page.setName(drawPageName);
						obsoletModelPages.remove(0);
					}
					else
					{
						// neues ModelPage erzeugen
						EClass pageClass = DesignsPackage.eINSTANCE.getPage();
						page = (Page) EcoreUtil.create(pageClass);
						page.setName(drawPageName);

						// zur Pagereference hinzufuegen
						EReference eReference = DesignsPackage.eINSTANCE.getDesign_Pages();
						EditingDomain editingDomain = DesignUtils.getDesignProject().getEditingDomain();												
						ECPControlHelper.addModelElementInReference(
								activeDesign, page, eReference,editingDomain);
					}
				}
				
				// die uebriggebliebenen ModelPages loeschen
				modelPages.removeAll(obsoletModelPages);					
			}						
		}		
	}
	
	private void readDesignLayers()
	{
		if (activeDesign != null)
		{			
			DrawDocument drawDocument = openDrawDocumentMap.get(activeDesign);
			if(drawDocument != null)
			{
				// die momentanen ModellLayerNamen listen
				List<String>modelLayerNames = new ArrayList<String>();
				List<Layer>layers = activeDesign.getLayers();
				for(Layer layer : activeDesign.getLayers())
					modelLayerNames.add(layer.getName());
					
				// die DrawDocument Layernamen listen
				List<String>drawlayerNames = drawDocument.getAllLayers();
				
				// alle Layer die bereits im Modell gespeichert sind aus 'drawLayerNames' streichen
				List<String>delList = new ArrayList<String>();
				for(String modelLayerName : modelLayerNames)
				{
					if(drawlayerNames.contains(modelLayerName))
						delList.add(modelLayerName);
				}
				drawlayerNames.removeAll(delList);
				
				// alle noch nicht im Modell vorhandenen Layer erzeugen
				for(String drawLayerName : drawlayerNames)
				{
					// neues ModelPage erzeugen
					EClass pageClass = DesignsPackage.eINSTANCE.getLayer();
					Layer layer = (Layer) EcoreUtil.create(pageClass);
					layer.setName(drawLayerName);
	
					// zur Layerreference hinzufuegen
					EReference eReference = DesignsPackage.eINSTANCE.getDesign_Layers();
					EditingDomain editingDomain = DesignUtils.getDesignProject().getEditingDomain();												
					ECPControlHelper.addModelElementInReference(
							activeDesign, layer, eReference,editingDomain);
				}
				
				// erneut alle DrawDocument Layernamen lesen
				drawlayerNames = drawDocument.getAllLayers();
				
				// erneut die momentanen ModellLayerNamen listen
				modelLayerNames = new ArrayList<String>();
				layers = activeDesign.getLayers();
				for(Layer layer : activeDesign.getLayers())
					modelLayerNames.add(layer.getName());
				
				delList.clear();
				for(String drawLayerName : drawlayerNames)
				{
					if(modelLayerNames.contains(drawLayerName))
						delList.add(drawLayerName);
				}
				
				// Modellayer die keine Entsprechung im DrawDocument haben loeschen
				modelLayerNames.removeAll(delList);
				
				// alternativ koennte man auch die ModellLayer im DrawDocument generieren
				/*
				for(String modelLayerName : modelLayerNames)
				{
					System.out.println("zu Loeschen: "+modelLayerName);
				}
				*/

			}
		}
	}
	
	private void readAllLayers()
	{
		boolean defaultLayerSet = false;
				
		if (activePage != null)
		{
			// existiert ein DefaultLayerSet (Layerset zeigt alle Layers)
			EList<LayerSet>layersets = activePage.getLayersets();
			for(LayerSet layerset : layersets)
			{
				if(StringUtils.equals(layerset.getName(), DEFAULT_LAYERSETNAME))
				{
					defaultLayerSet = true;
					break;
				}
			}
			
			if(!defaultLayerSet)
			{
				// DefaultLayerset erstellen				
				EClass layerSetClass = DesignsPackage.eINSTANCE.getLayerSet();
				LayerSet layerSet = (LayerSet) EcoreUtil.create(layerSetClass);
				layerSet.setName(DEFAULT_LAYERSETNAME);

				// zur Pagereference hinzufuegen
				EReference eReference = DesignsPackage.eINSTANCE.getPage_Layersets();
				EditingDomain editingDomain = DesignUtils.getDesignProject().getEditingDomain();												
				ECPControlHelper.addModelElementInReference(
						activePage, layerSet, eReference,editingDomain);
			}
			
						
			DrawDocument openDrawDocument = openDrawDocumentMap.get(activeDesign);
			if (openDrawDocument != null)
			{
				openDrawDocument.pullLayer();

				List<String> layernames = openDrawDocument.getLayerNames();
				if (!layernames.isEmpty())
				{
					for (String layername : layernames)
					{
						System.out.println("layername");
					}
				}
			}
		}
	}
	
}