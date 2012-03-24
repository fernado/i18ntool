/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.action;

import i18ntool.entity.TreeNode;
import i18ntool.property.Resource;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class DeleteItemAction extends Action implements ISelectionListener, IWorkbenchAction {

	public static final String ID = DeleteItemAction.class.getName();

	private static final Logger log = Logger.getLogger(ID);
	
	private final IWorkbenchWindow window;

	private IStructuredSelection selection;

	public DeleteItemAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + Resource.DELETE_ITEM);
		setToolTipText(Resource.DELETE_ITEM_FROM_LIST);
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection incoming) {
		// Selection containing elements
		if (incoming instanceof IStructuredSelection) {
			selection = (IStructuredSelection) incoming;
			setEnabled(selection.size() == 1 && selection.getFirstElement() instanceof TreeNode);
		} else {
			// Other selections, for example containing text or of other kinds.
			setEnabled(false);
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
		log.log(Level.INFO, ID + ".dispose()");
	}

	@Override
	public void run() {
		doAction();
	}

	private void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		INode node = (INode) selection.getFirstElement();
		if (null != node) {
			if (node.getType() == Type.LEAF) {
				INode parent = node.getParent();
				boolean result = MessageDialog.openConfirm(window.getShell(), Resource.CONFIRM,
						Resource.SURE_TO_DELETE_KEY + node.getName());
				if (result) {
					log.log(Level.INFO, ID + ".doAction() OK");
				} else {
					log.log(Level.INFO, ID + ".doAction() CANCEL");
				}
				((TreeNode) node).setParent(null);
				parent.removeChild(node);
				NodeAssistant.getInstance().addRemovedNodes(node);
				NodeAssistant.getInstance().removeTreeNodeChangeListener(node);
				ViewAssistant.getInstance().closeEditor(node, window.getActivePage());
			}
		}
		log.log(Level.INFO, ID + ".doAction() end");
	}

}
