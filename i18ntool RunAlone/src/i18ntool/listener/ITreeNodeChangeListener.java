/**
 * This is a tree node change listener,
 * when the tree node changed, one of the method will be invoke.
 * 
 * @author fernado  
 * @date 28/08/2010
 */
package i18ntool.listener;

import iceworld.fernado.entity.INode;

public interface ITreeNodeChangeListener {


	/**
	 * update the node
	 * @param node
	 */
	void onUpdate(final INode parent, final INode node, final boolean isParent);

	/**
	 * add a node
	 * @param node
	 */
	void onAddChild(final INode node);

	/**
	 * delete the node
	 * @param node
	 */
	void onDelete(final INode node);
}
