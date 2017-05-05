package it.naturtalent.planning.ui.parts;

import it.naturtalent.e4.office.odf.shapes.IOdfElementFactoryRegistry;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.IItemStyleRepository;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.IPlanningModelFactory;
import it.naturtalent.planung.PlanningModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class PlanungView
{
	public static final String PLANNINGVIEW_ID = "it.naturtalent.telekom.planning";

	@Inject @Optional private IEventBroker eventBroker;
	
	@Optional @Inject ILayerContentRepository layerContentRepository;
	
	@Optional @Inject IPlanningModelFactory planningModelFactory;
	
	@Optional @Inject IItemStyleRepository itemStyleRepository;
	
	@Optional @Inject IAlternativeFactoryRegistry alternativeFactoryRepository;
	
	@Optional @Inject IOdfElementFactoryRegistry odfElementFactoryRegistry;
	
	private PlanungMasterComposite planungMasterComposite;
	
	private PlanungDetailsComposite planungDetailsComposite;
	
	private @Inject MDirtyable dirtyable;
	
	private PlanningModel planningModel;
	
	public PlanungView()
	{
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent)
	{
		parent.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		planungMasterComposite = new PlanungMasterComposite(sashForm, SWT.NONE);		
		
		planungDetailsComposite = new PlanungDetailsComposite(sashForm, SWT.NONE);
		planungDetailsComposite.setLayerContentRepository(layerContentRepository);
		planungDetailsComposite.setPlanungMasterComposite(planungMasterComposite);
				
		//planungMasterComposite.setPlanningData(getTestData());
		planungMasterComposite.setDirtyable(dirtyable);
		planungMasterComposite.setDetailPage(planungDetailsComposite);
		planungMasterComposite.setItemStyleRepository(itemStyleRepository);
						
		planungMasterComposite.setEventBroker(eventBroker);
		planungMasterComposite.setLayerContentRegistry(layerContentRepository);
		planungMasterComposite.setAlternativeFactoryRepository(alternativeFactoryRepository);
		planungDetailsComposite.setAlternativeFactoryRepository(alternativeFactoryRepository);
		planungDetailsComposite.setOdfElementFacorRegistry(odfElementFactoryRegistry);
		
		planningModel = planningModelFactory.createModel();
		planningModel.setEventBroker(eventBroker);
		planningModelFactory.loadModel(planningModel);		
		planungMasterComposite.setModel(planningModel);
		
		sashForm.setWeights(new int[] {1, 1});
	}

	@PreDestroy
	public void dispose()
	{		
		dirtyable.setDirty(false);
	}
	
	@Persist
	public void persist()
	{		
		if(planningModel.isModified())
			planningModel.saveModel();
	}

	@Focus
	public void setFocus()
	{
		// TODO	Set the focus to control
	}
	
	public void setDirty(boolean dirtyFlag)
	{
		if(dirtyable != null)
			dirtyable.setDirty(dirtyFlag);
	}
	
	/*
	private static final String LINUX_TESTURL_STICK="/media/dieter/TOSHIBA/planung.odg";
	private static final String WINDOWS_TESTURL_STICK="E:\\planung.odg";	
	private static String WIN_TESTURL = "C:\\Users\\A682055\\Daten\\Naturtalent4\\workspaces\\telekomCopy\\1410350687331-1\\Strukturpanung\\planung.odg";

	
	private List<PlanningMaster> getTestData()
	{
		Alternative alternative = new Alternative();
		alternative.setKey("1234");
		alternative.setName("FTTH");
		//alternative.setDesignURL(WIN_TESTURL);

		PlanningItem item = new PlanningItem();
		item.setAlternativekey(alternative.getKey());
		item.setLabel("Linien messen");
		item.setLayerName(DefaultLayerNames.Masslinien.getType());
		item.setLayerContentName(LayerContent.DefaultContentNames.LineLength.name());
		PlanningItem [] items = {item};	

		item = new PlanningItem();
		item.setAlternativekey(alternative.getKey());
		item.setLabel("Flächen messen");
		item.setLayerName(DefaultLayerNames.Masslinien.getType());
		item.setLayerContentName(LayerContent.DefaultContentNames.RectangleArea.name());
		items = ArrayUtils.add(items, item);	

		item = new PlanningItem();
		item.setAlternativekey(alternative.getKey());
		item.setLabel("HE");
		item.setLayerName("HE");
		item.setLayerContentName(LayerContent.DefaultContentNames.ShapeTypes.name());
		items = ArrayUtils.add(items, item);
		
		item = new PlanningItem();
		item.setAlternativekey(alternative.getKey());
		item.setLabel("WE");
		item.setLayerName("WE");
		item.setLayerContentName(LayerContent.DefaultContentNames.ShapeTypes.name());
		items = ArrayUtils.add(items, item);
		
		item = new PlanningItem();
		item.setAlternativekey(alternative.getKey());
		item.setLabel("SNRV");
		item.setLayerName("SNRV");
		item.setLayerContentName(LayerContent.DefaultContentNames.LineLength.name());
		items = ArrayUtils.add(items, item);	


		
		alternative.setItems(items);
		
		Alternative alternative2 = new Alternative();
		alternative2.setKey("12345");
		alternative2.setName("UDV");
		item = new PlanningItem();
		item.setAlternativekey(alternative2.getKey());
		item.setLabel("Linien messen");
		item.setLayerName(DefaultLayerNames.Masslinien.getType());
		item.setLayerContentName(LayerContent.DefaultContentNames.LineLength.name());
		PlanningItem [] items2 = {item};	
		alternative2.setItems(items2);

		
		Alternative [] alternatives = {alternative, alternative2}; 
		PlanningMaster master = new PlanningMaster();
		master.setName("Strukturplanung Büchensaal");
		master.setProjektKey("key");
		master.setAlternatives(alternatives);
		
		
		List<PlanningMaster>listMaster = new ArrayList<PlanningMaster>();
		listMaster.add(master);
		return listMaster;
	}
	*/

}
