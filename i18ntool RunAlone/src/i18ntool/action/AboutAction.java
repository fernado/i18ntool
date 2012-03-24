/**
 * 
 * @author fernado
 * @date Nov 12, 2010
 */
package i18ntool.action;

import i18ntool.dialogs.AboutDialog;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class AboutAction  extends Action implements IWorkbenchAction {

	public static final String ID = AboutAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;
	
	@Override
	public void dispose() {
		log.log(Level.INFO, "i18ntool.action.AboutAction.dispose()");		
	}
	
	public AboutAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&About");
		setToolTipText("About");
	}
	

	@Override
	public void run() {
		doAction();
	}

	private void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		AboutDialog ad = new AboutDialog(window.getShell());
		int code = ad.open();
		if (Window.OK == code) {
			ad.close();
		}
		log.log(Level.INFO, ID + ".doAction() end");
	}


}
