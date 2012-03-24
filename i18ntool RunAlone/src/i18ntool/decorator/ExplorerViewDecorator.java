/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.decorator;

import i18ntool.entity.TreeNode;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontDecorator;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class ExplorerViewDecorator implements ILabelDecorator, IFontDecorator {

	@Override
	public Image decorateImage(Image image, Object element) {
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Font decorateFont(Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode) element;
			if (node.getName().equals("CLIENT_LIST_EREQ.QC01.0")) {
				return JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
			} else {
				return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
			}
		}
		return null;
	}

}
