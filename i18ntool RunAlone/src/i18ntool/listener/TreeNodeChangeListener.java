/**
 * 
 * @author fernado  
 * @date 28/08/2010
 */
package i18ntool.listener;

import i18ntool.util.LogManager;
import i18ntool.util.Refresh;
import iceworld.fernado.entity.INode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TreeNodeChangeListener implements ITreeNodeChangeListener {
	
	public static final String ID = TreeNodeChangeListener.class.getName();

	private static final Logger log = LogManager.getLogger(ID);

	public TreeNodeChangeListener() {
	}

	@Override
	public void onAddChild(final INode node) {
		log.log(Level.INFO, "viewer.onAddChild()");
		refresh(node);
	}

	@Override
	public void onDelete(final INode node) {
		log.log(Level.INFO, "viewer.onDelete");
		refresh(node);
	}

	@Override
	public void onUpdate(final INode parent, final INode node, final boolean isParent) {
		log.log(Level.INFO, "viewer.onUpdate()");
		if (isParent) {
			refresh(parent);
		} else {
			refresh(node);
		}
	}

	private void refresh(final INode node) {
		Refresh.getInstance().doRefreshAfterTreeNodeChanged(node);
	}
}
