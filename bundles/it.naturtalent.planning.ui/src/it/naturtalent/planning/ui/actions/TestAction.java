package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.calc.CalcDocument;
import it.naturtalent.planning.ui.Activator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.ResourcesPlugin;

public class TestAction extends AbstractPlanungAction
{

	private final static String SCHEDULEFILE = "schedule.ods";
	private String scheduleTemplatePath = Activator.TEMPLATES + File.separator + SCHEDULEFILE;
	
	public TestAction()
	{
		super();
		setImageDescriptor(Icon.ICON_MESSEN.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("testen");
	}

	@Override
	public void run()
	{
		
		File schedulePath = getScheduleFile();		
		CalcDocument calcDocument = new CalcDocument();
		calcDocument.loadPage(schedulePath.getPath());
		
		
		System.out.println("testen");
	}
	
	private File getScheduleFile()
	{
		File workspaceDir = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toFile();
		File scheduleFile = new File(workspaceDir,SCHEDULEFILE);
		if(!scheduleFile.exists())
		{
			File templateFile = FileUtils.toFile(Activator.getPluginPath(scheduleTemplatePath));
			try
			{
				FileUtils.copyFileToDirectory(templateFile, workspaceDir);				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return scheduleFile;
	}
	
}
