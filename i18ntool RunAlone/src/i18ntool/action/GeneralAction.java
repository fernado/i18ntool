/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.editor.GeneralEditor;
import i18ntool.util.ViewAssistant;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class GeneralAction extends Action implements IWorkbenchAction {

	public static final String ID = GeneralAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;

	public GeneralAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("General");
		setToolTipText("General");
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
		log.log(Level.INFO, ID + ".doAction()start");
		ViewAssistant.getInstance().openEditor(window.getActivePage(), GeneralEditor.ID);
		log.log(Level.INFO, ID + ".doAction() end");
	}

}
