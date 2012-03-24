/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.action;

import i18ntool.consts.Constant;
import i18ntool.dialogs.FilterItemDialog;
import i18ntool.filter.ItemFilter;
import i18ntool.view.ExplorerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class FilterItemAction extends Action implements IWorkbenchAction {

	private ViewerFilter[] filters = new ViewerFilter[0];
	
	public static final String ID = FilterItemAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;

	public FilterItemAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		this.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), Constant.CIRCLE_FILTER_NODE_IMG));
		setText("Filter Item");
		setToolTipText("Fitler Item");
	}

	@Override
	public void dispose() {
		log.log(Level.INFO, ID + ".dispose()");
	}

	@Override
	public void run() {
		doAction();
	}

	public void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		FilterItemDialog fid = new FilterItemDialog(window.getShell());
		int code = fid.open();
		if (Window.OK == code) {
			ExplorerView ev = (ExplorerView) window.getActivePage().findView(
					i18ntool.view.ExplorerView.ID);
			List<ViewerFilter> list = new ArrayList<ViewerFilter>();
			switch (fid.getValue()) {
			case NORMAL:
				list.add(new ItemFilter(fid.getValue()));
				filters = list.toArray(new ViewerFilter[0]);
				log.log(Level.INFO, ID + ".doAction() STATUS_ONE");
				break;
			case CHANGED_OR_EMPTY:
				list.add(new ItemFilter(fid.getValue()));
				filters = list.toArray(new ViewerFilter[0]);
				log.log(Level.INFO, "i18ntool.action.FitlerItemAction.doAction() STATUS_TWO");
				break;
			case CHANGED:
				list.add(new ItemFilter(fid.getValue()));
				filters = list.toArray(new ViewerFilter[0]);
				log.log(Level.INFO, ID + ".doAction() STATUS_THREE");
				break;
			case EMPTY:
				list.add(new ItemFilter(fid.getValue()));
				filters = list.toArray(new ViewerFilter[0]);
				log.log(Level.INFO, ID + ".doAction() STATUS_FOUR");
				break;
			default:
				break;
			}
			ev.getViewer().setFilters(filters);
		}
		fid.close();
		log.log(Level.INFO, "i18ntool.action.FitlerItemAction.doAction() end");
	}
}
