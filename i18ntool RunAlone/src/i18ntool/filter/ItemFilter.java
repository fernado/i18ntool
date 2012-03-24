/**
 * 
 * @author fernado  
 * @date 08/08/2010
 */
package i18ntool.filter;

import i18ntool.consts.Filter;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ItemFilter extends ViewerFilter {

	private Filter filter;
	
	public ItemFilter(Filter filter) {
		this.filter = filter;
	}
	
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		switch (filter) {
			case NORMAL:
				if (new NodeFilter().isNormalLeaf(element)) {
					return selected(viewer, parentElement, element);
				}
				break;
			case CHANGED_OR_EMPTY:
				if (new NodeFilter().isChangedOrEmptyLeaf(element)) {
					return selected(viewer, parentElement, element);
				}
				break;
			case CHANGED:
				if (new NodeFilter().isChangedLeaf(element)) {
					return selected(viewer, parentElement, element);
				}
				break;
			case EMPTY:
				if (new NodeFilter().isEmptyLeaf(element)) {
					return selected(viewer, parentElement, element);
				}
				break;
			default: break;
		}

		return false;
	}
	
	private boolean selected(Viewer viewer, Object parentElement, Object element) {
		StructuredViewer sviewer = (StructuredViewer) viewer;
		ITreeContentProvider provider = (ITreeContentProvider) sviewer.getContentProvider();
		for (Object child : provider.getChildren(element)) {
			if (select(viewer, element, child)) {
				return true;
			}
		}
		return true;
	}

}
