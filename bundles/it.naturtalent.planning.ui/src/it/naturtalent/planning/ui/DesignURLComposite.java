package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.DrawDocument;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class DesignURLComposite extends Composite
{

	private CCombo comboURL;
	private String drawPath;
	private IEventBroker eventBroker;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DesignURLComposite(Composite parent, int style)
	{
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(1, false));
		
		comboURL = new CCombo(this, SWT.BORDER);
		comboURL.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				String path = comboURL.getText();
				File fileCheck = new File(path);
				if(fileCheck.exists())				
					drawPath = path;
			}
		});
		comboURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// auswaehlen
		Button btnSelect = new Button(composite, SWT.NONE);
		btnSelect.setToolTipText("eine Zeichnung ausw\u00E4hlen");
		btnSelect.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(getShell());
				dialog.setText("Verzeichnis mit der Zeichung");
				
				//dialog.setFileName(FilenameUtils.getBaseName(comboSourceFile.getText()));
				dialog.setFilterExtensions(new String[]{ "*.odg" });
				//dialog.setFilterPath(FilenameUtils.getFullPath(comboSourceFile.getText()));			
				
				drawPath = dialog.open();
				if (StringUtils.isNotEmpty(drawPath))	
					comboURL.setText(drawPath);
			}
		});
		btnSelect.setText("ausw\u00E4hlen");
		
		// oeffnen
		Button btnOpen = new Button(composite, SWT.NONE);
		btnOpen.setToolTipText("die ausgew�hlte Zeichung �ffnen");
		btnOpen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DrawDocument drawDocument = new DrawDocument();				
				drawDocument.loadPage(drawPath);
			}
		});
		btnOpen.setText("\u00F6ffnen");

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}


	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}

	public void setDesignURL(String designURL)
	{
		if((comboURL != null) && (StringUtils.isNotEmpty(designURL)))
		{
			drawPath = designURL;
			comboURL.setText(designURL);			
		}
		
	}
	
}
