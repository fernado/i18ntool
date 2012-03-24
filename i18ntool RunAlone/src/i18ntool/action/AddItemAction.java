/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.action;

import i18ntool.consts.Status;
import i18ntool.dialogs.AddItemDialog;
import i18ntool.entity.ITreeNode;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.property.Resource;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import i18ntool.view.ExplorerView;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class AddItemAction extends Action implements IWorkbenchAction {

	public static final String ID = AddItemAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;

	public AddItemAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + Resource.ADD_KEY);
		setToolTipText(Resource.ADD_KEY_TO_LIST);
	}

	@Override
	public void dispose() {
		log.log(Level.INFO, ID + ".dispose()");
	}

	@Override
	public void run() {
		doAction();
	}

	public void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		AddItemDialog aid = new AddItemDialog(window.getShell());
		INode node = null;
		ITreeNode newNode = null;
		int code = aid.open();
		if (Window.OK == code) {
			node = NodeAssistant.getInstance().getData();
			if (node.getType() == Type.LEAF) {
				node = node.getParent();
			}
			newNode = new TreeNode(aid.getKey(), Type.LEAF, Status.SAVE_NEED_COMPLETE);
			String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
			Map<String, ValueEntity> valueMap = new HashMap<String, ValueEntity>();
			for (String messageFile: messageFiles) {
				valueMap.put(messageFile, new ValueEntity());
			}
			((TreeNode) newNode).setValueMap(valueMap);
			node.addChild(newNode);
			
			ExplorerView ev = (ExplorerView) window.getActivePage().findView(
					i18ntool.view.ExplorerView.ID);
			log.log(Level.INFO, ID + ".run() add a new node ("
					+ newNode.getName() + ")");
			ev.setSelection(newNode);
			NodeAssistant.getInstance().addTreeNodeChangeListener(newNode);
		}
		aid.close();
		log.log(Level.INFO, ID + ".doAction() end");
	}
}
