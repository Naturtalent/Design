package it.naturtalent.libreoffice;


public class DrawDocumentEvent 
{

	public static final String DRAWDOCUMENT_EVENT = "drawEvent/"; //$NON-NLS-N$
	
	// eine Zeichnung wurde geoffnet
	public static final String DRAWDOCUMENT_EVENT_DOCUMENT_OPEN = DRAWDOCUMENT_EVENT+"drawDocumentOpen"; //$NON-NLS-N$
	
	public static final String DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE = DRAWDOCUMENT_EVENT+"drawDocumentClose"; //$NON-NLS-N$
	
	//public static final String DRAWDOCUMENT_EVENT_DOCUMENT_ADDED = DRAWDOCUMENT_EVENT+"drawDocumentAdded"; //$NON-NLS-N$
	
	//public static final String DRAWDOCUMENT_EVENT_DOCUMENT_MODIFIED = DRAWDOCUMENT_EVENT+"drawDocumentModified"; //$NON-NLS-N$
	
	//public static final String DRAWDOCUMENT_EVENT_SCALEDENOMINATOR_CHANGED = DRAWDOCUMENT_EVENT+"drawDocumentScaleDenominator"; //$NON-NLS-N$
	
	public static final String DRAWDOCUMENT_EVENT_SHAPE_PULLED = DRAWDOCUMENT_EVENT+"drawDocumentshapepulled"; //$NON-NLS-N$
	
	public static final String DRAWDOCUMENT_EVENT_SHAPE_SELECTED = DRAWDOCUMENT_EVENT+"drawDocumentshapeselected"; //$NON-NLS-N$
	
	public static final String DRAWDOCUMENT_EVENT_DOCUMENT_CHANGEREQUEST = DRAWDOCUMENT_EVENT+"drawDocumentchangerequest"; //$NON-NLS-N$
}
