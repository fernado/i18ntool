/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.editor;

import i18ntool.entity.InputEntity;
import i18ntool.entity.TreeNode;
import iceworld.fernado.entity.INode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class TreeNodeEditorInput implements IEditorInput {

	private INode node;

	private InputEntity entity;

	public InputEntity getEntity() {
		return entity;
	}

	public void setEntity(final InputEntity entity) {
		this.entity = entity;
	}

	public TreeNodeEditorInput(final INode node, final InputEntity entity) {
		this.node = node;
		this.entity = entity;
	}

	public INode getNode() {
		return node;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return ((TreeNode) node).getFullName();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		if (adapter.equals(i18ntool.view.ExplorerView.class)) {
		}
		return null;
	}
}
