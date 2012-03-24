/**
 * 
 * @author fernado
 * @date Nov 12, 2010
 */
package i18ntool.filter;

import i18ntool.consts.Status;
import i18ntool.entity.TreeNode;

public class NodeFilter implements INodeFilter {

	/**
	 * Status.CHANGED
	 * @param element
	 * @return
	 */
	@Override
	public boolean isChangedLeaf(final Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode) element;
			if (Status.CHANGED == node.getStatus()
					||Status.SAVE_CHANGED_OR_EMPTY == node.getStatus()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Status.LOAD_NEED_COMPLETE || Status.SAVE_NEED_COMPLETE
	 * @param element
	 * @return
	 */
	@Override
	public boolean isEmptyLeaf(final Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode) element;
			if (Status.LOAD_NEED_COMPLETE == node.getStatus() 
					|| Status.SAVE_NEED_COMPLETE == node.getStatus()
					|| Status.SAVE_CHANGED_OR_EMPTY == node.getStatus()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Status.NORMAL
	 * @param element
	 * @return
	 */
	@Override
	public boolean isNormalLeaf(final Object element) {
		if (element instanceof TreeNode) {
			return true;
		}
		return false;
	}
	
	/**
	 * Status.LOAD_NEED_COMPLETE || Status.SAVE_NEED_COMPLETE || Status.CHANGED
	 * @param element
	 * @return
	 */
	@Override
	public boolean isChangedOrEmptyLeaf(final Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode) element;
			if (Status.LOAD_NEED_COMPLETE == node.getStatus() 
					|| Status.SAVE_NEED_COMPLETE == node.getStatus() 
					|| Status.CHANGED == node.getStatus()
					|| Status.SAVE_CHANGED_OR_EMPTY == node.getStatus()) {
				return true;
			}
		}
		return false;
	}

}
