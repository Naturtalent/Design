package it.naturtalent.planung;

public interface IPlanningModelFactory
{
	public static final String PLANNINGDATA_COLLECTION_NAME = "plannungdata";

	public PlanningModel createModel();
	
	public void loadModel(PlanningModel planningModel);
	
	public PlanningMaster load(String id);
	
	public void saveModel(PlanningModel planningModel);
	
}
