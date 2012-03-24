package i18ntool;

import i18ntool.tray.SystemTrayMaster;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private SystemTrayMaster trayMaster;

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setTitle("i18nTool");
		configurer.setShowProgressIndicator(true);
	}

	public void postWindowOpen() {
		super.postWindowOpen();
		// IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		// configurer.getWindow().getShell().setMaximized(true);
		createSystemTray();

	}

	private void createSystemTray() {
		trayMaster = new SystemTrayMaster();
		trayMaster.createSystemTray();
	}

	public boolean divWindowShellClose() {
		trayMaster.minimizeWindow();
		return false;
	}
	
	

}
