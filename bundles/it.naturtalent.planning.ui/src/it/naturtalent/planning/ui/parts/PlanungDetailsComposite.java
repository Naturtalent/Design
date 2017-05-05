package it.naturtalent.planning.ui.parts;

import it.naturtalent.e4.office.odf.shapes.IOdfElementFactoryRegistry;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planning.ui.DesignDetailsComposite;
import it.naturtalent.planning.ui.actions.AbstractPlanungAction;
import it.naturtalent.planning.ui.actions.MassstabAction;
import it.naturtalent.planning.ui.actions.MessenAction;
import it.naturtalent.planning.ui.actions.PageAction;
import it.naturtalent.planning.ui.actions.PullAction;
import it.naturtalent.planning.ui.actions.PushAction;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.ILayerContentFactory;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.LayerContent;
import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningEvent;
import it.naturtalent.planung.PlanningItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PlanungDetailsComposite extends Composite
{

	private ILayerContentRepository layerContentRepository;
	
	private LayerContent layerContent;
	
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
	private ScrolledForm scrldfrmDetails;
	
	private Section sctnDetails;
	
	private Composite dynamicComposite;
	
	// aktuell in Bearbeitung
	private DrawDocument drawDocument;
	
	private IEventBroker eventBroker;
	
	private PlanungMasterComposite planungMasterComposite;
	
	private DesignDetailsComposite alternativeDetailsComposite;
	
	// Reaktion auf Ereignisse im Planungsmodell
	private EventHandler planningEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			Object eventObj = event.getProperty(IEventBroker.DATA);
			
			/*
			// Page wurde (ueber Hyperlink) angefordert
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_PAGEREQUEST))
			{
				Page page = (Page) eventObj;
				if(drawDocument == null)
				{
					// Dokument oeffnen
					openPage(page);
				}
				else
				{
					System.out.println("Seite im geoffneten Dokument selektieren");
				}
				
				return;
			}
			*/

			
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_ADD_PAGE))
			{
				Page page = (Page) eventObj;
				if(drawDocument != null)
					drawDocument.addDrawPage(page.getName());
				return;
			}

			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_REMOVE_PAGE))
			{
				Page page = (Page) eventObj;
				if(drawDocument != null)
					drawDocument.removeDrawPage(page.getName());
				return;
			}

			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_RENAME_PAGE))
			{				
				if (eventObj instanceof String [])
				{
					String [] names = (String[]) eventObj;					
					if (drawDocument != null)
						drawDocument.renameDrawPage(names[0], names[1]);											
				}
				
				return;
			}
			
			/*
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_MODIFIED))
			{
				Design design = planungMasterComposite.getCurDesign();
				if(design != null)
				{
					Object obj = alternativeDetailsComposite.getData();
					if (obj instanceof Map)
					{
						Map<Design, ImageHyperlink> designLinkMap = (Map<Design, ImageHyperlink>) obj;
						ImageHyperlink designHyperlink = (ImageHyperlink) designLinkMap.get(design);
						designHyperlink.setText(design.getName());
					}
				}				
			}
			*/
		}
	};
	
	// Reaktion auf Ereignisse im DrawDocument 
	private EventHandler draWEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			Object eventObj = event.getProperty(IEventBroker.DATA);
			
			// DrawDokument wurde geoffnet
			if (StringUtils.equals(event.getTopic(),
					DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN))
			{
				// kill Watchdog
				//cancelWatchDog = true;
				
				if (eventObj instanceof DrawDocument)
				{
					// aktuell in Bearbeitung
					drawDocument = (DrawDocument) eventObj;
					
					// die Actionen aktualisieren
					Set<PlanungActionID> keySet = actionRegistry.keySet();
					for (PlanungActionID id : keySet)
					{
						AbstractPlanungAction action = actionRegistry.get(id);
						action.setDrawDocument(drawDocument);
					}
					updateWidgets();
					return;
				}
			}
			
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE))
			{
				// DrawDokument wurde geschlossen
				drawDocument = null;
				Set<PlanungActionID> keySet = actionRegistry.keySet();
				for(PlanungActionID id : keySet)
				{
					AbstractPlanungAction action = actionRegistry.get(id);
					action.setDrawDocument(null);				
				}	
				updateWidgets();
				return;
			}
			
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_SHAPE_PULLED))
			{
				List<Shape>shapes = (List<Shape>) eventObj;
				
				//Scale scale = drawDocument.getScale();
				//layerContent.setScaleDenominator(scale.getScaleDenominator());
				//layerContent.setScaleDenominator();
				
				// Page ueber Namen ermitteln
				Page page = null;
				Design design = planungMasterComposite.getCurDesign();
				String pageName = drawDocument.getPageName();
				Page [] pages = design.getPages();
				for(Page pageSeek : pages)
				{
					if(StringUtils.equals(pageName, pageSeek.getName()))
					{
						page = pageSeek;
						break;
					}
				}
				
				if (page != null)
				{
					layerContent.setScaleDenominator(page.getScaleDenominator());
					layerContent.setShapes(shapes);
				}
			}
		}
	};
	
	public enum PlanungActionID
	{	
		PAGE_ACTION,
		MASSSTAB_ACTION,
		MESSEN_ACTION,
		PULL_ACTION,
		PUSH_ACTION,
	}
	private Map<PlanungActionID, AbstractPlanungAction> actionRegistry = new HashMap<PlanungActionID, AbstractPlanungAction>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PlanungDetailsComposite(Composite parent, int style)
	{
		super(parent, style);
		addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				if(eventBroker != null)
				{
					eventBroker.unsubscribe(draWEventHandler);
					eventBroker.unsubscribe(planningEventHandler);
				}
				
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));
		
		sctnDetails = toolkit.createSection(this, Section.TITLE_BAR);
		GridData gd_sctnDetails = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_sctnDetails.heightHint = 430;
		sctnDetails.setLayoutData(gd_sctnDetails);
		toolkit.paintBordersFor(sctnDetails);
		sctnDetails.setText("Details");
		sctnDetails.setExpanded(true);
		createSectionToolbar(sctnDetails);
		
		scrldfrmDetails = toolkit.createScrolledForm(sctnDetails);
		sctnDetails.setClient(scrldfrmDetails);
		toolkit.paintBordersFor(scrldfrmDetails);
		scrldfrmDetails.setText("Design");
		scrldfrmDetails.getBody().setLayout(new GridLayout(1, false));
		
		//LengthTableComposite lengthTableComposite = new LengthTableComposite(scrldfrmDetails.getBody(), SWT.NONE);
		//toolkit.adapt(lengthTableComposite);
		//toolkit.paintBordersFor(lengthTableComposite);
		
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

		// Page Action
		action = new PageAction();
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.PAGE_ACTION, action);		

		// Massstab Action
		action = new MassstabAction();
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.MASSSTAB_ACTION, action);		

		// Messen Action
		action = new MessenAction(); 
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.MESSEN_ACTION, action);

		// Pull Action
		action = new PullAction(); 
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.PULL_ACTION, action);

		// Push Action
		action = new PushAction(); 
		toolBarManager.add(action);
		actionRegistry.put(PlanungActionID.PUSH_ACTION, action);

		toolBarManager.update(true);
	}
	
	public void setDetailTitle(String title)
	{
		scrldfrmDetails.setText(title);
	}
	
	public void setDetailMessage(String message)
	{
		scrldfrmDetails.setMessage(message, IMessageProvider.INFORMATION);
	}
	

	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
		eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", draWEventHandler);	
		eventBroker.subscribe(PlanningEvent.PLANNING_EVENT+"*", planningEventHandler);
		
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setEventBroker(eventBroker);				
		}	
	}
	
	public void setOdfElementFacorRegistry(IOdfElementFactoryRegistry odfElementFactoryRegistry)
	{
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setOdfElementFactoryRegistry(odfElementFactoryRegistry);		
		}	
	}
	
	/*
	public void dispose()	
	{
		if(eventBroker != null)
			eventBroker.unsubscribe(draWEventHandler);
	}
	*/

	public Composite getDetailsComposite()
	{
		return scrldfrmDetails.getBody();
	}

	// LayercontentRegistry verfuegbar machen
	public void setLayerContentRepository(ILayerContentRepository layerContentRepository)
	{
		this.layerContentRepository = layerContentRepository;
	}
	
	public ILayerContentRepository getLayerContentRepository()
	{
		return layerContentRepository;
	}

	public void setDynamicComposite(Composite dynamicComposite)
	{				
		if(this.dynamicComposite != null)
			this.dynamicComposite.dispose();
			
		this.dynamicComposite = dynamicComposite;
		if (dynamicComposite != null)
		{			
			scrldfrmDetails.setSize(scrldfrmDetails.computeSize(SWT.DEFAULT,SWT.DEFAULT));
			sctnDetails.layout();
		}
		
		alternativeDetailsComposite = null;
		if (dynamicComposite instanceof DesignDetailsComposite)
			alternativeDetailsComposite = (DesignDetailsComposite) dynamicComposite;
		
		updateWidgets();
	}
	
	/*
	public void setLayerContent(String contentName)
	{
		layerContent = layerContentRepository.getLayerContent(contentName);
		Composite composite = layerContent.getContentComposite(scrldfrmDetails.getBody());
		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		setSize(composite);
	}
	
	public void setSize(Composite dynamicComposite)
	{				
		if(this.dynamicComposite != null)
			this.dynamicComposite.dispose();
			
		this.dynamicComposite = dynamicComposite;
		scrldfrmDetails.setSize(scrldfrmDetails.computeSize(SWT.DEFAULT, SWT.DEFAULT));				
		sctnDetails.layout();
	}
	*/

	public void setLayerContent(String contentName)
	{
		ILayerContentFactory contentFactory = layerContentRepository
				.getLayerContentFactory(contentName);
		layerContent = contentFactory.createLayerContent(contentName);
		layerContent.setEventBroker(eventBroker);

		// den layercontenteignenen Composite in Detailpage einbinden
		Composite composite = layerContent.getContentComposite(scrldfrmDetails.getBody());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,1, 1));
		setDynamicComposite(composite);	
	}
	
	public void setPlanungMasterComposite(PlanungMasterComposite planungMasterComposite)
	{
		this.planungMasterComposite = planungMasterComposite;
		
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setPlanungMasterComposite(planungMasterComposite);		
		}	
	}

	public void setAlternativeFactoryRepository(IAlternativeFactoryRegistry alternativeFactoryRepository)
	{
		this.planungMasterComposite = planungMasterComposite;
		
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			AbstractPlanungAction action = actionRegistry.get(id);
			action.setAlternativeFactoryRepository(alternativeFactoryRepository);		
		}	
	}

	

	public void updateWidgets()
	{
		boolean isInNode = false;
		
		Set<PlanungActionID> keySet = actionRegistry.keySet();
		for(PlanungActionID id : keySet)
		{
			Composite contentComposite = dynamicComposite;
			if(contentComposite instanceof  DesignDetailsComposite)
				contentComposite = null;
						
			if((contentComposite != null) && (contentComposite.isDisposed()))
				contentComposite = null;
			
			// passt geoffnetes 'drawDocument' zum selektierten Design		
			/*
			if ((planungMasterComposite != null) && (drawDocument != null) && (planungMasterComposite.getCurDesign() != null))
			{
				// Pfad zum Dokument muss uebereinstimmen
				String docPath = drawDocument.getDocumentPath();
				String designPath = planungMasterComposite.getCurDesign().getDesignURL();
				isDesignCorrect = StringUtils.equals(docPath, designPath);
			}
			*/
			
			if(planungMasterComposite != null)
			{
				StructuredSelection selection = (StructuredSelection) planungMasterComposite
						.getTreeViewer().getSelection();
				Object selObj = selection.getFirstElement();
				Design curDesign = planungMasterComposite.getCurDesign();
				Design selectedDesign = null;
				if (curDesign != null)
				{
					if (selObj instanceof Design)
					{
						selectedDesign = (Design) selObj;
						isInNode = selectedDesign.equals(curDesign);
					}
					else
					{
						if (selObj instanceof PlanningItem)
						{
							PlanungContentProvider contentProvider = (PlanungContentProvider) planungMasterComposite
									.getTreeViewer().getContentProvider();
							selectedDesign = (Design) contentProvider
									.getParent(selObj);
							isInNode = selectedDesign.equals(curDesign);
						}
					}
				}
			}

			Action action = actionRegistry.get(id);		
			switch (id)
			{
				case PAGE_ACTION:
					action.setEnabled((drawDocument != null) && (isInNode));
					break;
				
				case MASSSTAB_ACTION:
					action.setEnabled((drawDocument != null) && (isInNode));
					break;

				case MESSEN_ACTION:
					action.setEnabled((drawDocument != null) &&  (isInNode));
					break;
					
				case PULL_ACTION:
					action.setEnabled((contentComposite != null) && (drawDocument != null) && (isInNode));
					break;

				case PUSH_ACTION:
					action.setEnabled((contentComposite != null) && (drawDocument != null) && (isInNode));
					break;

				default:
					break;
			}
			
		}
		
		
		// Designhyperlinks enablen, wenn Zugriff auf gueltiges Dokument moeglich 		
		if((alternativeDetailsComposite != null) && (!alternativeDetailsComposite.isDisposed()))
		{
			@SuppressWarnings("unchecked")
			Map<Page,ImageHyperlink> designLinkMap = (Map<Page, ImageHyperlink>) alternativeDetailsComposite.getData();
			
			if ((designLinkMap != null) && (!designLinkMap.isEmpty()))
			{
				Collection<ImageHyperlink> values = designLinkMap.values();
				for (Iterator<ImageHyperlink> itLink = values.iterator(); itLink.hasNext();)
				{
					ImageHyperlink link = itLink.next();
					link.setEnabled(isInNode);
				}
			}
		}		
		
	}
		

}
