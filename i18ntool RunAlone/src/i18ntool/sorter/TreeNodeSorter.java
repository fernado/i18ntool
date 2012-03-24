/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.sorter;

import iceworld.fernado.entity.INode;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class TreeNodeSorter extends ViewerComparator {

	@Override
	public final int compare(final Viewer viewer, final Object e1, final Object e2) {
		INode node1 = null;
		INode node2 = null;
		if (e1 instanceof INode && e2 instanceof INode) {
			node1 = (INode) e1;
			node2 = (INode) e2;
			return node1.getName().compareTo(node2.getName());
		}
		return -1;
	}

}
