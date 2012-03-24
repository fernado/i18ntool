/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.dialogs.SearchItemByKeyDialog;
import i18ntool.property.Resource;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.search.PatternConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class SearchItemByKeyAction extends Action implements IWorkbenchAction {
	
	public static final String ID = SearchItemByKeyAction.class.getName();

	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;

	public SearchItemByKeyAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + Resource.SEARCH_ITEMS_BY_KEY);
		setToolTipText(Resource.SEARCH_ITEMS_BY_KEY);
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
		SearchItemByKeyDialog sibkd = new SearchItemByKeyDialog(window.getShell());
		int code = sibkd.open();
		if (Window.OK == code) {
			NodeAssistant.getInstance().setTreeNode(findNodes(sibkd.getResults()));
			
			IViewPart searchView = ViewAssistant.getInstance().findView(i18ntool.view.SearchView.ID);
			if (null == searchView) {
				ViewAssistant.getInstance().showView(i18ntool.view.SearchView.ID);
			} else {
				((i18ntool.view.SearchView) searchView).refresh();
			}
		}
		sibkd.close();
		log.log(Level.INFO, ID + ".doAction() end");
	}

	private INode findNodes(final String[] keys) {
		INode result = NodeAssistant.getInstance().getSearchTreeNode();
		NodeAssistant.getInstance().clear(result);
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				Pattern p = PatternConstructor.createPattern(keys[0], Boolean.getBoolean(keys[1]),
						Boolean.getBoolean(keys[2]));
				Matcher m = p.matcher(node.getName());
				if (m.find()) {
					result.addChild(node);
				}
			}
		}
		return result;
	}

}
