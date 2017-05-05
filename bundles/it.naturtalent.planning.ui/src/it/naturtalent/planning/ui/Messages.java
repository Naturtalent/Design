package it.naturtalent.planning.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.naturtalent.planning.ui.messages"; //$NON-NLS-1$
	public static String AddAction_NewPlanningLabel;
	public static String AreaTableComposite_Label_Flaeche;
	public static String DeleteAction_DeleteMessage;
	public static String DeleteAlternativeAction_DeleteMessage;
	public static String DeletePlanningItemAction_DeleteMessage;
	public static String DeleteDesignAction_DeleteMessage;
	public static String DeleteAlternativeAction_MessageTitle;
	public static String DeletePlanningItemAction_MessageTitle;
	public static String DeleteAction_MessageTitlePlanung;
	public static String DeleteAction_ToolTipDelete;
	public static String DesignDetailsComposite_ToolTipOpenDocument;
	public static String DesignHyperlinkAction_TaskCancelMessage;
	public static String DesignHyperlinkAction_TaskOpenLabel;
	public static String LengthTableComposite_tblclmnIndex_text;
	public static String LengthTableComposite_tblclmnLength_text;
	public static String LengthTableComposite_tblclmnShapeType_text;
	public static String MassstabAction_NoReferenceLine;
	public static String MassstabAction_NoScaleFoundTitle;
	public static String MassstabAction_ToolTipMassstab;
	public static String PlanungDialog_lblNamePlanung_text;
	public static String PlanungDialog_btnNeu_text;
	public static String PlanungDialog_btnNeu_1_text;
	public static String PlanungDialog_this_title;
	public static String PlanungDialog_lblNameAlternative_text;
	public static String PlanungDialog_Title;
	public static String EditPlanungDialog_mghprlnkAssignproject_text;
	public static String EditPlanungDialog_btnNeu_toolTipText;
	public static String EditPlanungDialog_btnNeu_1_toolTipText;
	public static String EditPlanungDialog_DefinePlanningTitle;
	public static String ScaleSettingDialog_this_title;
	public static String ScaleSettingDialog_lblMastab_text;
	public static String ScaleSettingDialog_lblLength_text;
	public static String ScaleSettingDialog_this_message;
	public static String PlanungMasterComposite_sctnPlanning_text;
	public static String TestDialog_Test;
	public static String PlanningWizardPage_this_title;
	public static String PlanningWizardPage_this_description;
	public static String PlanningWizardPage_btnSelectProject_text;
	public static String PlanningWizardPage_btnSelectDesign_text;
	public static String PlanningWizardPage_lblPlanningName_text;
	public static String PlanningWizardPage_lblAssignedProject_text;
	public static String PlanningWizardPage_lblAssignedProject_toolTipText;
	public static String PlanningWizardPage_lblAlternative_text;
	public static String PlanningWizardPage_labelDesign_text;
	public static String PlanningWizardPage_btnNewButton_text;
	public static String PlanningWizardPage_btnResetProject_toolTipText;
	public static String PlanningWizardPage_lblPlanningName_toolTipText;		
	public static String PlanningWizardPage_btnDeleteButton_text;
	public static String PlanningWizardPage_lblNewLabel_text;
	public static String DesignDialog_lblName_text;	
	public static String DesignDialog_lblInhalte_text;
	public static String DesignDialog_this_message;
	public static String DesignDialog_this_title;
	public static String DesignDialog_lblNewLabel_text;
	public static String DesignDialog_btnNewButton_text;
	public static String DesignDialog_btnEdit_text;
	public static String DesignDialog_btnDelete_text;
	public static String PlanningItemDialog_this_message;
	public static String PlanningItemDialog_this_title;
	public static String PlanningItemDialog_lblName_text;
	public static String PlanningItemDialog_lblName_toolTipText;	
	public static String PlanningItemDialog_lblEbene_text;
	public static String PlanningItemDialog_lblEbene_toolTipText;
	public static String PlanningItemDialog_lblClass_text;
	public static String PlanningItemDialog_lblClass_toolTipText;
	public static String PlanningItemDialog_btnNewButton_text;
	public static String PlanningItemDialog_btnStyle_toolTipText;
	public static String SelectAlternativeDialog_this_title;
	public static String SelectAlternativeDialog_this_message;
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Class initialization
	//
	////////////////////////////////////////////////////////////////////////////
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
