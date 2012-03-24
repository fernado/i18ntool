/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.entity;

import i18ntool.consts.Status;
import i18ntool.listener.ITreeNodeChangeListener;
import i18ntool.util.LogManager;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.PlatformObject;

public class TreeNode extends PlatformObject implements ITreeNode {

	private static final long serialVersionUID = 1L;

	public static final String ID = TreeNode.class.getName();
	
	private static final Logger log = LogManager.getLogger(ID);

	private List<ITreeNodeChangeListener> listeners = Collections.synchronizedList(new ArrayList<ITreeNodeChangeListener>());

	private INode parent;
	private List<INode> children = new ArrayList<INode>();
	private String name;
	private String fullname;
	private Status status;
	private Type type;
	/**
	 * valueMap <property file's name, property's value>
	 */
	private Map<String, ValueEntity> valueMap = new HashMap<String, ValueEntity>();
	private String comment;
	private Map<String, Status> statusMap = new HashMap<String, Status>();

	public TreeNode(final String name, final Type type) {
		this(name, type, Status.NORMAL);
	}

	public TreeNode(final String name, final Type type, final Status status) {
		this.name = name;
		this.type = type;
		this.status = status;
	}

	public void addChild(final INode node) {
		getChildren().add(node);
		((TreeNode) node).setParent(this);
		fireTreeNodeAdded(node);
		log.log(Level.INFO, ID + ".addChild(" + node.getName() + ")");
	}

	public void removeChild(final INode node) {
		getChildren().remove(node);
		((TreeNode) node).setParent(null);
		fireTreeNodeDeleted();
		log.log(Level.INFO, ID + ".removeChild(" + node.getName() + ")");
	}
	
	public void removeAllChildren() {
		Iterator<? extends INode> itr = getChildren().iterator();
		while (itr.hasNext()) {
			if (null != itr.next()) {
				itr.remove();
			}
		}
	}

	public boolean hasChildren() {
		return getChildren().size() > 0;
	}
	
	public boolean hasChild(final INode child) {
		if (hasChildren()) {
			for (INode node: getChildren()) {
				if (node.getName().equals(child.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasChild(final String childName) {
		if (hasChildren()) {
			for (INode node: getChildren()) {
				if (node.getName().equals(childName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public INode getChild(final String childName) {
		for (INode node: getChildren()) {
			if (node.getName().equals(childName)) {
				return node;
			}
		}
		return null;
	}

	public String getFullName() {
		if (null == fullname) {
			return getWholeName();
		}
		return fullname = getWholeName();
	}

	private String getWholeName() {
		List<String> list = new ArrayList<String>();
		INode parent = getParent();
		StringBuilder result = new StringBuilder();
		list.add(getName());
		while (null != parent) {
			list.add(parent.getName());
			parent = parent.getParent();
		}
		for (int i = list.size() - 3; i > -1; i--) {
			result.append(list.get(i)).append(Constants.DOT);
		}
		return result.length() > 0 ? result.toString().substring(0, result.length() - 1) : Constants.EMPTY_STRING;
	}

	public void setName(final String newName) {
		this.name = newName;
	}

	public Type getType() {
		return type;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public INode getParent() {
		return parent;
	}

	public void setParent(final INode parent) {
		this.parent = parent;
	}

	public List<INode> getChildren() {
		return children;
	}

	public void setChildren(final List<INode> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public Map<String, ValueEntity> getValueMap() {
		return valueMap;
	}

	public void setValueMap(final Map<String, ValueEntity> valueMap) {
		this.valueMap = valueMap;
	}
	
	public Map<String, Status> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(final Map<String, Status> statusMap) {
		this.statusMap = statusMap;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		setStatus(status, false);
	}
	
	public void setStatus(final Status status, final boolean isParent) {
		this.status = status;
		fireTreeNodeUpdate(isParent);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void addTreeNodeChangeListener(final ITreeNodeChangeListener listener) {
		if (null == listeners) {
			listeners = new ArrayList<ITreeNodeChangeListener>();
		}
		if (listeners.contains(listener)) {
			return;
		}
		listeners.add(listener);
	}

	@Override
	public void removeTreeNodeChangeListener(final ITreeNodeChangeListener listener) {
		if (null == listeners) {
			return;
		}
		listeners.remove(listener);
	}

	protected void fireTreeNodeUpdate(final boolean isParent) {
		Iterator<ITreeNodeChangeListener> itr = listeners.iterator();
		while (itr.hasNext()) {
			((ITreeNodeChangeListener) itr.next()).onUpdate(this.parent, this, isParent);
		}
	}

	protected void fireTreeNodeAdded(final INode child) {
		Iterator<ITreeNodeChangeListener> itr = listeners.iterator();
		while (itr.hasNext()) {
			((ITreeNodeChangeListener) itr.next()).onAddChild(this);
		}
	}

	protected void fireTreeNodeDeleted() {
		Iterator<ITreeNodeChangeListener> itr = listeners.iterator();
		while (itr.hasNext()) {
			((ITreeNodeChangeListener) itr.next()).onDelete(this);
		}
	}
}
