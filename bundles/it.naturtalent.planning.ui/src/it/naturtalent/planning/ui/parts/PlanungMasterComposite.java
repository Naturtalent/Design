package it.naturtalent.planning.ui.parts;




import it.naturtalent.e4.office.odf.shapes.IOdfElementFactoryRegistry;
import it.naturtalent.e4.project.IResourceNavigator;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.libreoffice.draw.IItemStyle;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.Scale;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.libreoffice.draw.Style;
import it.naturtalent.planning.ui.DefaultItemStyle;
import it.naturtalent.planning.ui.DesignDetailsComposite;
import it.naturtalent.planning.ui.DesignDrawDocumentRegistry;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planning.ui.actions.AbstractPlanungAction;
import it.naturtalent.planning.ui.actions.AddAction;
import it.naturtalent.planning.ui.actions.CloseDesignAction;
import it.naturtalent.planning.ui.actions.ConfigureLayerViewSetAction;
import it.naturtalent.planning.ui.actions.DeleteAction;
import it.naturtalent.planning.ui.actions.DesignHyperlinkAction;
import it.naturtalent.planning.ui.actions.EditAction;
import it.naturtalent.planning.ui.actions.LayerViewSetAction;
import it.naturtalent.planning.ui.actions.OpenDesignAction;
import it.naturtalent.planning.ui.actions.SaveAction;
import it.naturtalent.planning.ui.actions.SelectPageAction;
import it.naturtalent.planning.ui.actions.TestAction;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.IItemStyleFactory;
import it.naturtalent.planung.IItemStyleRepository;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningEvent;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;
import it.naturtalent.planung.PlanningModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;


public class PlanungMasterComposite extends Composite
{
	
	private MDirtyable dirtyable;
	
	private IItemStyleRepository itemStyleRepository;
	
	private IEventBroker eventBroker;
	
	private DrawDocument drawDocument;
	
	// dieser Designknoten ist aktive (das zugordnete Dokument ist geoffnet) s. OpenAction()
	private Design curDesign;	
	
	private PlanningItem curItem;
	
	private DesignDetailsComposite designDetailsComposite;
	
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
	private PlanningModel planningModel = new PlanningModel();
	
	// Zugriff auf die Detailseite
	private PlanungDetailsComposite detailPage;
	
	// viewerSet Layernamen
	private boolean viewerSet = false;
	private List<String>visibleLayers = new ArrayList<String>();
	
	//public static final String DESIGN_URL_SETTING = "desingurlsetting"; //$NON-NLS-N$
	//private String designPath;
	
	// Ereignisse im Planungsmodell
	private EventHandler planningEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			Object eventObj = event.getProperty(IEventBroker.DATA);
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_ADDED))
			{
				return;
			}

			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_SAVED))
			{
				planningModel.loadModel();
				treeViewer.refresh();
				updateWidgets();
				return;
			}

			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_MODIFIED))
			{
				PlanningMaster modifiedData = (PlanningMaster) eventObj;
				treeViewer.refresh(modifiedData, true);
				
				if(curDesign != null)
				{
					// durch erneutes Selektieren Details Hyperlink aktualisieren
					// (ein geanderter Desingname wird jetzt auch auf der Detailseite angezeigt)
					treeViewer.setSelection(new StructuredSelection(curDesign));
				}
				
				updateWidgets();
				return;
			}
			
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_SELECT_PLANNINGMASTER))
			{
				if (eventObj instanceof PlanningMaster)
				{
					PlanningMaster planningMaster = (PlanningMaster) eventObj;
					Object obj  = treeViewer.getInput();
					if (obj instanceof PlanningModel)
					{
						PlanningModel planningModel = (PlanningModel) obj;
						PlanningMaster viewPlanningMaster = planningModel.getData(planningMaster.getId());
						if(viewPlanningMaster != null)
						{
							IEventBroker saveBroker = eventBroker;
							eventBroker = null;
							treeViewer.setSelection(new StructuredSelection(viewPlanningMaster));
							treeViewer.expandToLevel(viewPlanningMaster, 1);
							eventBroker = saveBroker;
						}						
					}
				}
				return;
			}

		}
	};
	
	// Ereignisse im DrawDokument 
	private EventHandler draWEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			Object eventObj = event.getProperty(IEventBroker.DATA);

			// ein DesignDokument wurde geschlossen
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE))
			{
				// aus dem Registry entfernen
				DesignDrawDocumentRegistry.removeDrawDocument(curDesign);
				

				// die Actionen aktualisieren
				/*
				 for (Iterator<AbstractPlanungAction> actions = actionRegistry.values().iterator(); actions.hasNext();)
				 {
					 AbstractPlanungAction action = actions.next(); 
					 action.setDrawDocument(null);						 
				 }
				 */

				// DrawPage wurde geschlossen
				DrawDocument openDocument = drawDocument;
				setDrawDocument(null);
				curDesign = null;	
				
				if(openDocument != null)
				{
					Design documentDesign = seekDesignByDocumentPath(openDocument.getDocumentPath());
					if(documentDesign != null)
						treeViewer.update(documentDesign, null);
				}
				
				updateWidgets();				
				return;
			}
			
			// ein DesignDokument wurde geoffnet
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN))
			{
				if(eventObj instanceof DrawDocument)
				{
					drawDocument = (DrawDocument) eventObj; 
					initDrawDocument();
					initDesign();					
					//initPage();

					// Dokument im Registry aufnehmen
					DesignDrawDocumentRegistry.addDrawDocument(curDesign, drawDocument);
					
					// Dokument an die Actionen weitergeben
					for (Iterator<AbstractPlanungAction> actions = actionRegistry.values().iterator(); actions.hasNext();)
					{
						AbstractPlanungAction action = actions.next(); 
						action.setDrawDocument(drawDocument);						 
					}

					// nach dem Oeffnen des Dokuments Seite[0] auswaehlen
					Page page = curDesign.getPages()[0];
					SelectPageAction openPageAction = (SelectPageAction) getRegisteredAction(PlanungActionID.SELECT_PAGE);
					openPageAction.setPage(page);
					openPageAction.run();
					
					 updateWidgets();
				}
				return;
			}
			
			// Dokumentwechsel
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CHANGEREQUEST))
			{
				if((drawDocument == null) && (designDetailsComposite != null))
				{
					Page page = (Page) eventObj; 					
					DesignHyperlinkAction linkAction = new DesignHyperlinkAction(curDesign);
					linkAction.setEventBroker(eventBroker);
					//linkAction.runPageAction(page);
					
					//System.out.println(designLinkMap.size());
				}
				else
				{
					MessageDialog
							.openWarning(getShell(), "Zeichnung",
									"das gewählte Zeichnungsdokument konnte nicht geöffnet werden");
				}
				
				return;
			}
			
			// eine Shape wurde in der DetailPage selektiert
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_SHAPE_SELECTED))
			{			
				if(drawDocument != null)
				{
					// Shape im Design selektieren
					Shape shape = (Shape) eventObj;
					if(shape != null)
						shape.select(drawDocument);
				}
			}
			
		}
	};
	
	
	public enum PlanungActionID
	{		
		ADD_PLANUNG,
		DELETE_PLANUNG,
		EDIT_PLANUNG,
		SAVE_ACTION,		
		CLOSE_DESIGN,
		OPEN_DESIGN,
		SELECT_PAGE,
		LAYER_VIEWSET,
		CONFIGURE_LAYERVIEWSET,
		TEST_ACTION,
	}
	private Map<PlanungActionID, AbstractPlanungAction> actionRegistry = new HashMap<PlanungActionID, AbstractPlanungAction>();
	private TreeViewer treeViewer;

	// KontextMenues
	private Map<PlanungActionID, MenuItem> menuRegistry = new HashMap<PlanungActionID, MenuItem>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PlanungMasterComposite(Composite parent, int style)
	{
		super(parent, style);		
		//this.parentPart = parentPart;
		
		addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				if(eventBroker != null)
				{
					eventBroker.unsubscribe(draWEventHandler);
					eventBroker.unsubscribe(planningEventHandler);
				}
				
				if(drawDocument != null)
					drawDocument.closeDocument();

				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));
		
		Section sctnPlanung = toolkit.createSection(this, Section.TWISTIE | Section.TITLE_BAR);
		sctnPlanung.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(sctnPlanung);
		sctnPlanung.setText("Planung");
		sctnPlanung.setExpanded(true);
		createSectionToolbar(sctnPlanung);
		
		Composite composite = toolkit.createComposite(sctnPlanung, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnPlanung.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		
		treeViewer = new TreeViewer(composite, SWT.BORDER);		
		treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				StructuredSelection selection = (StructuredSelection) treeViewer.getSelection();
				Object selObj = selection.getFirstElement();
				
				if(selObj instanceof PlanningMaster)
				{
					if (eventBroker != null)
						eventBroker
								.post(IResourceNavigator.NAVIGATOR_EVENT_SELECT_PROJECT,
										((PlanningMaster)selObj).getProjektKey());
					return;
				}

				if ((selObj instanceof PlanningMaster)
						|| (selObj instanceof Alternative)
						|| (selObj instanceof Design
						|| (selObj instanceof PlanningItem)))
				{
					actionRegistry.get(PlanungActionID.EDIT_PLANUNG).run(); 
					return;
				}
			}
		});
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				AddAction addAction = (AddAction) actionRegistry.get(PlanungActionID.ADD_PLANUNG);
				EditAction editAction = (EditAction) actionRegistry.get(PlanungActionID.EDIT_PLANUNG);
				DeleteAction deleteAction = (DeleteAction) actionRegistry.get(PlanungActionID.DELETE_PLANUNG);
				
				StructuredSelection selection = (StructuredSelection) treeViewer.getSelection();
				Object selObj = selection.getFirstElement();

				PlanungContentProvider contentProvider = (PlanungContentProvider) treeViewer
						.getContentProvider();

				// eine Alternative wurde selektiert
				if(selObj instanceof Alternative) 
				{					
					curDesign = null;
					curItem = null;	
					addAction.setToolTipText("eine neue Alternative hinzufuegen");
					editAction.setToolTipText("eine Alternative bearbeiten");
					deleteAction.setToolTipText("eine Alternative loeschen");
					
					detailPage.setDynamicComposite(null);
					detailPage.setDetailTitle("Alternative");
					updateWidgets();
					return;
				}
				
				// ein Design wurde selektiert
				if(selObj instanceof Design) 
				{
					curDesign = null;
					curItem = null;		

					// Action Tooltips zuruecksetzen					
					addAction.setToolTipText("");
					editAction.setToolTipText("");
					deleteAction.setToolTipText("");

					// Design-Detailseite loeschen
					detailPage.setDynamicComposite(null);
					
					// Abbruch, wenn kein Dokument geoffnet ist
					if(drawDocument == null)
					{
						updateWidgets();
						return;
					}
					
					// entspricht das geoffnete Dokument dem des selektierten Designs
					Design design = (Design) selObj;						
					String docPath = drawDocument.getDocumentPath();
					String designPath = design.getDesignURL();
					if(!StringUtils.equals(docPath, designPath))
					{
						updateWidgets();
						return;
					}
					
					// selektiertes Design ist aktulles Design
					curDesign = (Design) selObj;
					
					// Action Tooltips aktualisiern					
					addAction.setToolTipText("eine neue Zeichnung hinzufuegen");
					editAction.setToolTipText("eine Zeichnung bearbeiten");
					deleteAction.setToolTipText("eine Zeichnung loeschen");
					
					// Design Hyperlink (Seiten einer Zeichnung) in Detailpage generieren 
					Composite detailsComposite = detailPage.getDetailsComposite();
					//DesignHyperlinkAction designHyperlinkAction = new DesignHyperlinkAction(curAlternativ);
					
					DesignHyperlinkAction designHyperlinkAction = new DesignHyperlinkAction(curDesign);					
					designHyperlinkAction.setEventBroker(eventBroker);
					designHyperlinkAction.setPlanungMasterComposite(PlanungMasterComposite.this);
					
					designDetailsComposite = new DesignDetailsComposite(detailsComposite, SWT.NONE,designHyperlinkAction);
					detailPage.setDynamicComposite(designDetailsComposite);
					detailPage.setDetailTitle("Zeichnung");
					
					updateWidgets();
					
					
					// alle Layer sichtbar
					if (drawDocument != null)
					{												
						PlanningItem [] items = curDesign.getItems();
						for(PlanningItem planningItem : items)
						{
							Layer itemLayer = drawDocument.getLayer(planningItem.getLayer());
							if(itemLayer != null)
								itemLayer.setVisible(true);
						}
					}
					
					return;
				}
				
				// ein Item (Ebene) wurde selektiert
				if(selObj instanceof PlanningItem)
				{
					addAction.setToolTipText("eine Ebene hinzufügen");
					editAction.setToolTipText("eine Ebene bearbeiten");
					deleteAction.setToolTipText("eine Ebene löschen");
					
					// den ausgewaehlten ContentComposite zeigen
					//PlanningItem item = (PlanningItem) selObj;
					curItem = (PlanningItem) selObj;
					detailPage.setLayerContent(curItem.getContentClass());
					
					
					if(curDesign == null)
					{						
						if(drawDocument != null)
						{	
							// ist Parent von PlanningItem (Design) Inhaber des DrawDokuments
							String docPath = drawDocument.getDocumentPath();							
							contentProvider = (PlanungContentProvider) treeViewer.getContentProvider();
							Design selectedDesign = (Design) contentProvider.getParent(selObj);
							String designPath = selectedDesign.getDesignURL();
							if(StringUtils.equals(docPath, designPath))
								curDesign = selectedDesign;
						}
						else
						{
							// Design und DrawDokument nicht definiert
							updateWidgets();
							detailPage.updateWidgets();
							return;
						}
					}
					
					// ist Parent von PlanningItem (Design) Inhaber des DrawDokuments 
					String docPath = drawDocument.getDocumentPath();
					contentProvider = (PlanungContentProvider) treeViewer.getContentProvider();
					Design selectedDesign = (Design) contentProvider.getParent(selObj);
					String designPath = selectedDesign.getDesignURL();
					if(!StringUtils.equals(docPath, designPath))
					{
						// das selektierte PlanningItem gehoert zu einem 'fremden' Dokument
						updateWidgets();
						detailPage.updateWidgets();
						return;
					}

					// PlanningItem gehoert zum geoffneten DrawDokument
					Layer layer = drawDocument.getLayer(curItem.getLayer());
					if (layer != null)
					{
						// PlanningItem Layer (Ebene) im Dokument selektieren
						drawDocument.selectDrawLayer(layer);

						// alle anderen Ebenen unsichtbar
						PlanningItem[] items = curDesign.getItems();
						for (PlanningItem planningItem : items)
						{
							Layer itemLayer = drawDocument
									.getLayer(planningItem.getLayer());
							if (itemLayer != null)
								itemLayer.setVisible(false);
						}
						layer.setVisible(true);

						// ViewerSet-Layer ebenfalls sichtbar machen
						if (viewerSet)
						{
							for (String visibleLayer : visibleLayers)
							{
								Layer setLayer = drawDocument
										.getLayer(visibleLayer);
								if (setLayer != null)
									setLayer.setVisible(true);
							}
						}

						initItem(curItem);
					}

					// Titel der Planungsdetailseite 
					detailPage.setDetailTitle("Inhalt");
					if (drawDocument != null)
					{
						String pageName = drawDocument.getPageName();
						if (StringUtils.isNotEmpty(pageName))
							detailPage.setDetailMessage("Seite: " + pageName);
					}
					
					updateWidgets();
					detailPage.updateWidgets();
					
					return;
				}
				
				// PlanningMaster wurder selektiert
				if (selObj instanceof PlanningMaster)
				{					
					PlanningMaster planningMaster = (PlanningMaster) selObj;
										
					addAction
							.setToolTipText(Messages.AddAction_NewPlanningLabel);
					editAction.setToolTipText("eine Planung bearbeiten");
					deleteAction
							.setToolTipText(Messages.DeleteAction_ToolTipDelete);

					curDesign = null;
					detailPage.setDynamicComposite(null);
					detailPage.setDetailTitle("Planung");
					updateWidgets();

					// Projekt im Projektnavigator selektieren		
					/*
					if (eventBroker != null)
						eventBroker
								.post(IResourceNavigator.NAVIGATOR_EVENT_SELECT_PROJECT,
										planningMaster.getProjektKey());
										*/
				}
			}
		});
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 82, 82);
		toolkit.paintBordersFor(tree);
		
		// Kontextmenue
		Menu menu = new Menu(tree);
		tree.setMenu(menu);

		// ADD
		MenuItem mntmADD = new MenuItem(menu, SWT.NONE);
		mntmADD.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.ADD_PLANUNG).run();
			}
		});
		mntmADD.setText("hinzufügen");
		mntmADD.setImage(Icon.COMMAND_ADD.getImage(IconSize._16x16_DefaultIconSize));
		menuRegistry.put(PlanungActionID.ADD_PLANUNG, mntmADD);

		// Delete
		MenuItem mntmDelete = new MenuItem(menu, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.DELETE_PLANUNG).run();
			}
		});
		mntmDelete.setText("löschen");
		mntmDelete.setImage(Icon.COMMAND_DELETE.getImage(IconSize._16x16_DefaultIconSize));
		menuRegistry.put(PlanungActionID.DELETE_PLANUNG, mntmDelete);

		new MenuItem(menu, SWT.SEPARATOR);

		// Open
		MenuItem mntmOpen = new MenuItem(menu, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.OPEN_DESIGN).run();
			}
		});
		mntmOpen.setText("Zeichnung öffnen");
		menuRegistry.put(PlanungActionID.OPEN_DESIGN, mntmOpen);
		
		MenuItem mntmClose = new MenuItem(menu, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.CLOSE_DESIGN).run();				
			}
		});
		mntmClose.setText("Zeichnung schließen");
		menuRegistry.put(PlanungActionID.CLOSE_DESIGN, mntmClose);
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmLayerVisible = new MenuItem(menu, SWT.CHECK);
		mntmLayerVisible.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.LAYER_VIEWSET).run(); 
			}
		});
		mntmLayerVisible.setText("ViewSet an");
		menuRegistry.put(PlanungActionID.LAYER_VIEWSET, mntmLayerVisible);
		
		MenuItem mntmLayersVisible = new MenuItem(menu, SWT.NONE);
		mntmLayersVisible.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.CONFIGURE_LAYERVIEWSET).run();			
			}
		});
		mntmLayersVisible.setText("Ebenen ViewSet");
		menuRegistry.put(PlanungActionID.CONFIGURE_LAYERVIEWSET, mntmLayersVisible);
				
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmTest = new MenuItem(menu, SWT.NONE);
		mntmTest.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(PlanungActionID.TEST_ACTION).run();
			}
		});
		mntmTest.setText("test");
		menuRegistry.put(PlanungActionID.TEST_ACTION, mntmTest);
		
		PlanungLabelProvider planungLabelProvider = new PlanungLabelProvider();
		planungLabelProvider.setPlanungMasterComposite(this);
		treeViewer.setLabelProvider(planungLabelProvider);		
		treeViewer.setContentProvider(new PlanungContentProvider());
		
		updateWidgets();
	}
	
	private void createSectionToolbar(Section section)
	{		
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(section);		
		section.setTextClient(toolbar);
		
		final Cursor handCursor = new Cursor(Display.getCurrent(),SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				if (handCursor.isDisposed() == false)				
					handCursor.dispose();				
			}
		});
		
		AbstractPlanungAction action;

		// Add Action
		action = new AddAction();
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.ADD_PLANUNG, action);
		action.setPlanungMasterComposite(this);
		
		// Delete Action
		action = new DeleteAction();
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.DELETE_PLANUNG, action);
		action.setPlanungMasterComposite(this);
		
		// Edit Action
		action = new EditAction();
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.EDIT_PLANUNG, action);
		action.setPlanungMasterComposite(this);


		// Settings Action
/*
		action = new SettingsAction(); 
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.SETTINGS, action);
		action.setPlanungMasterComposite(this);
		*/

		
		// Save Action
		action = new SaveAction(); 
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.SAVE_ACTION, action);
		action.setPlanungMasterComposite(this);

		
		action = new CloseDesignAction(); 		
		actionRegistry.put(PlanungActionID.CLOSE_DESIGN, action);
		action.setPlanungMasterComposite(this);

		action = new OpenDesignAction(); 		
		actionRegistry.put(PlanungActionID.OPEN_DESIGN, action);
		action.setPlanungMasterComposite(this);
		
		// eine bestimmte Seite (Folie) selektieren
		action = new SelectPageAction(); 		
		actionRegistry.put(PlanungActionID.SELECT_PAGE, action);
		action.setPlanungMasterComposite(this);

		// LayerViewSet ein-/ausschalten
		action = new LayerViewSetAction();
		actionRegistry.put(PlanungActionID.LAYER_VIEWSET, action);
		action.setPlanungMasterComposite(this);

		// LayerViewSet konfigurieren
		action = new ConfigureLayerViewSetAction();
		actionRegistry.put(PlanungActionID.CONFIGURE_LAYERVIEWSET, action);
		action.setPlanungMasterComposite(this);

		// Test
		action = new TestAction();
		actionRegistry.put(PlanungActionID.TEST_ACTION, action);
		action.setPlanungMasterComposite(this);


		
		toolBarManager.update(true);
	}
	
	private void initItem(PlanningItem item)
	{
		IItemStyle itemStyle = null;
		Layer layer = drawDocument.getLayer(curItem.getLayer());
		Style style = layer.getStyle();
		
		String styleName = item.getStyleName();		
		if(StringUtils.isNotEmpty(styleName))
		{
			IItemStyleFactory styleFactory = itemStyleRepository.getItemStyleFactory(styleName);
			if(styleFactory != null)		
				itemStyle = styleFactory.createItemStyle();
		}
		else
			itemStyle = itemStyleRepository.getItemStyleFactory(
					DefaultItemStyle.DEFAULTITEMSTYLE).createItemStyle();
		
		
		if(itemStyle != null)
			style.setItemStyle(itemStyle);
		
		style.pushItemStyle(drawDocument);
		
		
		
		/*
		Style style = layer.getStyle();
		
		
		style.setStyleObject(Style.STYLEOBJECT_WITHOUTFILL);
		style.setLineColor(new Integer( 0xff420e ) );
		style.pushLineColor(drawDocument);
		
		style.setLineWidth(new Integer(50));
		style.pushLineWidth(drawDocument);

		
		style.printProperties(drawDocument);
		
		
		System.out.println(style);
		*/
	}
	
	public PlanningMaster getSelectedPlanning()
	{
		StructuredSelection selection = (StructuredSelection) treeViewer.getSelection();
		Object selObj = selection.getFirstElement();
		
		if (selObj instanceof PlanningMaster)
			return (PlanningMaster) selObj;
		
		PlanungContentProvider contentProvider = (PlanungContentProvider) treeViewer.getContentProvider();
		if (selObj instanceof Alternative)
		{
			Alternative alternative = (Alternative) selObj;
			return (PlanningMaster) contentProvider.getParent(alternative);
		}
		
		if (selObj instanceof Design)
		{
			Design design = (Design) selObj;
			Alternative alternative = (Alternative) contentProvider.getParent(design);
			return (PlanningMaster) contentProvider.getParent(alternative);
		}

		return null;
	}


	public void setEventBroker(IEventBroker eventBroker)
	{
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setEventBroker(eventBroker);				
		}	

		this.eventBroker = eventBroker;
		eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", draWEventHandler);
		eventBroker.subscribe(PlanningEvent.PLANNING_EVENT+"*", planningEventHandler);
		
		if(detailPage != null)
			detailPage.setEventBroker(eventBroker);
	}
	
	public void setLayerContentRegistry(ILayerContentRepository layerContentRegistry)
	{
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setLayerContentRepository(layerContentRegistry);	
		}		
	}
	
	public void setAlternativeFactoryRepository(IAlternativeFactoryRegistry alternativeFactoryRepository)
	{
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setAlternativeFactoryRepository(alternativeFactoryRepository);	
		}				
	}

	public void setItemStyleRepository(IItemStyleRepository itemStyleRepository)
	{
		this.itemStyleRepository = itemStyleRepository;
	}
	
	public List<String> getVisibleLayers()
	{
		return visibleLayers;
	}

	public void setVisibleLayers(List<String> visibleLayers)
	{
		this.visibleLayers = visibleLayers;
	}

	public void	initDrawDocument()
	{
		
		//drawDocument.checkImportSVG();
		
		
		// die Seite 'pageName' einstellen
		//drawDocument.selectDrawPage(drawDocument.getPageName());
		
		//drawDocument.setMeasureUnit((short) 3);
		
		// Massstabdaten einlesen
		drawDocument.pullScaleData();
		
		// vorhandene Layer ermitteln
		drawDocument.pullLayer();

		// Styledaten einlesen
		/*
		Style style = new Style();
		style.pullStyle(drawDocument);	
		
		style.setLineColor(new Integer( 0xff0000 ));
		
		style.pushStyle(drawDocument);
		
		//graphicStyle.setLineColor(new Integer( 0xff0000 ));
		
		Layer [] layers = drawDocument.getLayers();
		for(Layer layer : layers)
		{
			if(layer.isActive(drawDocument))
				System.out.println(layer.getName()+" is active: ");
		}
		*/
		
	}
	
	public void initDesign()
	{
		if((curDesign != null) && (drawDocument != null))
		{
			//
			// sicherstellen, dass alle erforderlichen (im Design definierten) 
			// Layer im Dokument vorhanden sind
			//
			
			// die Layer des Dokuments auflisten
			List<String>layerNames = new ArrayList<String>();
			Layer [] layers = drawDocument.getLayers();						
			for(Layer layer : layers)
				layerNames.add(layer.getName());
			
			// die Itemnames auflisten
			PlanningItem [] items = curDesign.getItems();
			if(ArrayUtils.isNotEmpty(items))
			{
				for(PlanningItem item : items)
				{
					String itemLayerName = item.getLayer();
					if(!layerNames.contains(itemLayerName))
						drawDocument.addLayer(itemLayerName);
				}							
			}
		}
	}
	
	
	public void initPage()
	{		
		Integer pageDenominator = null;
				
		if((curDesign != null) && (drawDocument != null))
		{			
			String pageName = drawDocument.getPageName();						
			if(StringUtils.isNotEmpty(pageName))
			{
				// Page selektieren
				drawDocument.selectDrawPage(pageName);
				
				// Pagemassstab an Dokument uebergeben
				Page [] pages = curDesign.getPages();
				for(Page page : pages)
				{
					if(StringUtils.equals(page.getName(), pageName))
					{
						// Dokument uebernimmt den Masstab der Page						
						pageDenominator = page.getScaleDenominator();						
						if(pageDenominator == null)
						{
							// in Page ist noch kein Massstab definiert
							MessageDialog.openWarning(getShell(),"Zeichnung","noch kein Maßstab definiert");
							
							// Massstab aus dem Dokument holen ...
							pageDenominator = drawDocument.pullScaleDenominator();
							
							// ... und in Page speichern
							page.setScaleDenominator(pageDenominator);
						}
						break;
					}
				}
				
				
				Integer documentDenominator = drawDocument.pullScaleDenominator();				
				if(!pageDenominator.equals(documentDenominator))
				{
					// den im Dokument verwendeten Massstab aktualisieren 
					Scale scale = drawDocument.getScale();
					scale.setScaleDenominator(pageDenominator);
					scale.setMeasureUnit(Scale.MEASUREUNIT_M);
					scale.pushScaleProperties();
				}
				
			}
		}
	}
	
	public void setDirtyable(MDirtyable dirtyable)
	{
		this.dirtyable = dirtyable;
	}

	public void setPlanningData(List<PlanningMaster>planningData )
	{
		treeViewer.setInput(planningData);
	}

	public void setDetailPage(PlanungDetailsComposite detailPage)
	{
		this.detailPage = detailPage;
	}
	
	public PlanungDetailsComposite getDetailPage()
	{
		return detailPage;
	}

	public Design getCurDesign()
	{
		return curDesign;
	}
	
	public void setCurDesign(Design curDesign)
	{
		this.curDesign = curDesign;
	}

	public Action getRegisteredAction(PlanungActionID actionID)
	{
		return actionRegistry.get(actionID);
	}
	
	public PlanningModel getModel()
	{
		return planningModel;
	}
	
	public DrawDocument getDrawDocument()
	{
		return drawDocument;
	}
	
	public PlanningItem getCurItem()
	{
		return curItem;
	}

	public void setDrawDocument(DrawDocument drawDocument)
	{
		this.drawDocument = drawDocument;
		
		// die Actionen aktualisieren
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for (PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setDrawDocument(drawDocument);
		}
	}
	
	public boolean isViewerSet()
	{
		return viewerSet;
	}

	public void setViewerSet(boolean viewerSet)
	{
		this.viewerSet = viewerSet;
	}

	public void setModel(PlanningModel planningModel)
	{
		this.planningModel = planningModel;
		this.planningModel.loadModel();
		treeViewer.setInput(planningModel);		
				
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setPlanningModel(planningModel);				
		}	
	}

	public TreeViewer getTreeViewer()
	{
		return treeViewer;
	}
	
	public Design seekDesignByDocumentPath(String documentPath)
	{
		PlanungContentProvider cp = (PlanungContentProvider) treeViewer.getContentProvider();
		PlanningMaster [] plannings = (PlanningMaster[]) cp.getElements(treeViewer.getInput());
		for(PlanningMaster planning : plannings)
		{
			Alternative [] alternatives = planning.getAlternatives();
			if(ArrayUtils.isNotEmpty(alternatives))
			{
				for(Alternative alternative : alternatives)
				{
					Design [] designs = alternative.getDesigns();
					if(ArrayUtils.isNotEmpty(designs))
					{
						for(Design design : designs)
						{
							if(StringUtils.equals(design.getDesignURL(), documentPath))
								return design;
						}
					}
				}
			}
		}
		
		return null;
	}

	private void updateWidgets()
	{
		StructuredSelection selection = (StructuredSelection)treeViewer.getSelection();
		Object selObj = selection.getFirstElement();
		
		if(dirtyable != null)
			dirtyable.setDirty(planningModel.isModified());

		Design selectedDesign = null;		
		if(drawDocument == null)
		{
			if(selObj instanceof Design)
				selectedDesign = (Design)selObj;
			else
			{
				if (selObj instanceof PlanningItem)
				{
					PlanungContentProvider contentProvider = (PlanungContentProvider) treeViewer
							.getContentProvider();
					selectedDesign = (Design) contentProvider.getParent(selObj);
				}
			}			
		}

		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			Action action = actionRegistry.get(id);			
			switch (id)
				{
					case ADD_PLANUNG:
						action.setEnabled((selObj instanceof PlanningItem)
								|| (selObj instanceof PlanningMaster)
								|| (selObj instanceof Design)
								|| (selObj instanceof Alternative));
						menuRegistry.get(PlanungActionID.ADD_PLANUNG).setEnabled(action.isEnabled());
						break;

					case DELETE_PLANUNG:
						action.setEnabled((selObj instanceof PlanningItem)
								|| (selObj instanceof PlanningMaster)
								|| (selObj instanceof Design)
								|| (selObj instanceof Alternative));
						menuRegistry.get(PlanungActionID.DELETE_PLANUNG).setEnabled(action.isEnabled());
						break;
						
					case EDIT_PLANUNG:
						action.setEnabled((selObj instanceof PlanningItem)
								|| (selObj instanceof PlanningMaster)
								|| (selObj instanceof Design)
								|| (selObj instanceof Alternative));
						break;
						
					case SAVE_ACTION:
						action.setEnabled(planningModel.isModified());
						break;
						
					case CLOSE_DESIGN:
						action.setEnabled(drawDocument != null);
						menuRegistry.get(PlanungActionID.CLOSE_DESIGN).setEnabled(action.isEnabled());
						break;

					case OPEN_DESIGN:
						//action.setEnabled((curDesign != null) && (drawDocument == null));
						//action.setEnabled((curDesign == null) && (drawDocument == null));
						action.setEnabled(selectedDesign != null);
						menuRegistry.get(PlanungActionID.OPEN_DESIGN).setEnabled(action.isEnabled());
						break;

					case LAYER_VIEWSET:
						action.setEnabled((curItem != null)
								&& (curDesign != null)
								&& (drawDocument != null));
						menuRegistry.get(PlanungActionID.LAYER_VIEWSET).setEnabled(action.isEnabled());
						break;

					case CONFIGURE_LAYERVIEWSET:
						action.setEnabled((curItem != null)
								&& (curDesign != null)
								&& (drawDocument != null));
						menuRegistry.get(PlanungActionID.CONFIGURE_LAYERVIEWSET).setEnabled(action.isEnabled());
						break;


					default:
						break;
				}
		}
	}
}
