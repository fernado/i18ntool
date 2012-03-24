/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.property.Resource;
import i18ntool.util.Refresh;
import i18ntool.util.ResourceHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class LoadResourcesFromFileAction extends Action implements IWorkbenchAction {

	public static final String ID = LoadResourcesFromFileAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;
	
	public LoadResourcesFromFileAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		initText();
	}
	
	private void initText() {
		setText(Resource.LOAD_RESOURCE);
		setToolTipText(Resource.LOAD_RESOURCE);
	}

	@Override
	public void dispose() {
		log.log(Level.INFO, ID + ".dispose()");
	}

	@Override
	public void run() {
		doAction();
	}

	private void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		ResourceHandler.getInstance().refresh();
		Refresh.getInstance().doRefresh();
		log.log(Level.INFO, ID + ".doAction() end");
	}

}
