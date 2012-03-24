/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.property.Resource;
import i18ntool.util.ViewAssistant;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ShowExplorerAction extends Action implements IWorkbenchAction {

	public final String ID = ShowExplorerAction.class.getName();

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;

	public ShowExplorerAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + Resource.SHOW_EXPLORER_VIEW);
		setToolTipText(Resource.SHOW_EXPLORER_VIEW);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void run() {
		doAction();
	}

	private void doAction() {
		ViewAssistant.getInstance().showView(i18ntool.view.ExplorerView.ID);
	}

}
