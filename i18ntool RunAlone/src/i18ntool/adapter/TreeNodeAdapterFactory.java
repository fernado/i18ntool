/**
 * 
 * <pre>
 * use IAdapterFactory and BaseWorkbenchContentProvider and WorkbenchLabelProvider to 
 * replace class1 implements ITreeContentProvider, class2 extends LabelProvider implements ILabelProvider
 * is better, becaz it is very easier to refresh the viewer
 * 
 * 		viewer.setContentProvider(new BaseWorkbenchContentProvider());
 *		viewer.setLabelProvider(new WorkbenchLabelProvider());
 *		Platform.getAdapterManager().registerAdapters(adapterFactory, TreeNode.class); 
 * </pre>
 *		
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.adapter;

import i18ntool.Activator;
import i18ntool.consts.Constant;
import i18ntool.entity.TreeNode;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.entity.INode;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class TreeNodeAdapterFactory implements IAdapterFactory {

	private IWorkbenchAdapter defaultAdapter = new IWorkbenchAdapter() {

		@Override
		public Object[] getChildren(final Object o) {
			if (o instanceof INode) {
				return ((INode) o).getChildren().toArray(new Object[0]);
			}
			return new Object[0];
		}

		@Override
		public ImageDescriptor getImageDescriptor(final Object object) {
			ImageDescriptor result = null;
			if (object instanceof TreeNode) {
				TreeNode node = (TreeNode) object;
				result =  Activator.getImageDescriptor(Constant.CIRCLE_BLUE_NODE_IMG);
				if (null != node) {
					switch (node.getStatus()) {
					case NORMAL:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_BLUE_NODE_IMG);
						break;
					case LOAD_NEED_COMPLETE:
						result =  Activator.getImageDescriptor(Constant.RHOMBUS_DARK_PINK_NODE_IMG);
						break;
					case SAVE_NEED_COMPLETE:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_RED_NODE_IMG);
						break;
					case CHANGED:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_ORANGE_NODE_IMG);
						break;
					case SAVE_CHANGED_OR_EMPTY:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_PINK_NODE_IMG);
						break;
					case FIND_OUT:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_GREEN_NODE_IMG);
						break;
					default:
						result =  Activator.getImageDescriptor(Constant.CIRCLE_BLUE_NODE_IMG);
						break;
					}
				}
			}

			return result;
		}

		@Override
		public String getLabel(final Object o) {
			if (o instanceof INode) {
				INode node = (INode) o;
				return node.getName();
			}
			return Constants.EMPTY_STRING;
		}

		@Override
		public Object getParent(final Object o) {
			if (o instanceof INode) {
				return ((INode) o).getParent();
			}
			return null;
		}

	};

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {
		if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof TreeNode) {
			return defaultAdapter;
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IWorkbenchAdapter.class };
	}

}
