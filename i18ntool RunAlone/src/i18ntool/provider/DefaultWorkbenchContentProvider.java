/**
 * 
 * @author fernado  
 * @date 03/09/2010
 */
package i18ntool.provider;

import i18ntool.util.NodeAssistant;
import iceworld.fernado.entity.INode;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

public class DefaultWorkbenchContentProvider extends BaseWorkbenchContentProvider {
	
    public final void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		if (null != oldInput) {
			NodeAssistant.getInstance().removeTreeNodeChangeListener((INode) oldInput);
		}
		if (null != newInput) {
			NodeAssistant.getInstance().addTreeNodeChangeListener((INode) newInput);
		}
    }

}
