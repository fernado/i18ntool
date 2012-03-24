/**
 * This class is do some ext for node.
 * @author fernado
 * @date Jan 13, 2011
 */
package i18ntool.util;

import i18ntool.datas.Data;
import i18ntool.entity.ITreeNode;
import i18ntool.entity.TreeNode;
import i18ntool.listener.TreeNodeChangeListener;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.entity.INode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NodeAssistant {
	
	private static final NodeAssistant instance = new NodeAssistant();
	
	private NodeAssistant() {
	}
	
	public static final NodeAssistant getInstance() {
		return instance;
	}
	
	private INode treeNode;
	/**
	 * recode removed nodes
	 */
	private List<INode> removedNodes = Collections.synchronizedList(new ArrayList<INode>());
	
	private TreeNodeChangeListener tncl = new TreeNodeChangeListener();
	
	
	public void addRemovedNodes(final INode node) {
		removedNodes.add(node);
	}
	
	public List<INode> getRemovedNodes() {
		return removedNodes;
	}
	
	public void clearRemovedNodes() {
		removedNodes.clear();
	}
	
	/**
	 * clear the tree node's children
	 * @param treeNode
	 */
	public void clear(final INode treeNode) {
		while (treeNode.getChildren().size() > 0) {
			treeNode.removeChild(treeNode.getChildren().get(0));
		}
	}
	
	public INode getData() {
		return Data.createData();
	}

	public INode getSearchTreeNode() {
		if (null == treeNode) {
			treeNode = Data.createSearchData();
		}
		return treeNode;
	}

	public void setTreeNode(final INode treeNode) {
		this.treeNode = treeNode;
	}
	
	/**
	 * search the tree node by tree node's name
	 * @param newName
	 * @return
	 */
	public boolean isKeyExist(final String newName) {
		for (INode node: getData().getChildren()) {
			if (node.getName().equals(newName)) {
				return true;
			}
		}
		return false;
	}
	
	public String getKeyComment(final String keyName) {
		for (INode node: getData().getChildren()) {
			if (node.getName().equals(keyName)) {
				return ((TreeNode) node).getComment();
			}
		}
		return Constants.EMPTY_STRING;
	}
	
	/**
	 * copy a tree node, the new tree node which contains the name, type, status, values.
	 * @param treeNode
	 * @return
	 */
	public INode copyATreeNode(INode treeNode) {
		INode result = new TreeNode(treeNode.getName(), treeNode.getType());
		((TreeNode) result).setValueMap(((TreeNode) treeNode).getValueMap());
		List<INode> nodes = treeNode.getChildren();
		if (null != nodes) {
			for (INode node: nodes) {
				INode child = new TreeNode(node.getName(), node.getType());
				((TreeNode) treeNode).setValueMap(((TreeNode) treeNode).getValueMap());
				result.addChild(child);
			}
		}
		return result;
	}
	
    public void addTreeNodeChangeListener(INode treeNode) {
    	((ITreeNode) treeNode).addTreeNodeChangeListener(tncl);
    	for (INode node : treeNode.getChildren()) {
    		((ITreeNode) node).addTreeNodeChangeListener(tncl);
    	}
    }
    
    public void removeTreeNodeChangeListener(INode treeNode) {
    	for (INode node : treeNode.getChildren()) {
    		((ITreeNode) node).removeTreeNodeChangeListener(tncl);
    	}
    	((ITreeNode) treeNode).removeTreeNodeChangeListener(tncl);
    }
}
