/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.view;

import i18ntool.entity.TreeNode;
import i18ntool.property.Resource;
import i18ntool.provider.DefaultWorkbenchContentProvider;
import i18ntool.sorter.TreeNodeSorter;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.entity.INode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class SearchView extends ViewPart {

	private class ContextMenuListener implements IMenuListener {
		@Override
		public void menuAboutToShow(IMenuManager manager) {
			fillContextMenu(mgr);
		}
	}
	
	public static final String ID = SearchView.class.getName();

	private TreeViewer viewer;
	private static final MenuManager mgr = new MenuManager();
	private ContextMenuListener cmListener = new ContextMenuListener();

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		init();
		addListener();
	}

	private void addListener() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				INode node = (TreeNode) selection.getFirstElement();
				ViewAssistant.getInstance().openEditor(node, getViewSite().getPage(), selection);
			}
		});
	}

	private void init() {
		this.setPartName(Resource.SEARCH);
		getSite().setSelectionProvider(viewer);
		viewer.setContentProvider(new DefaultWorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		INode treeNode = NodeAssistant.getInstance().getSearchTreeNode();
		viewer.setUseHashlookup(true);
		viewer.setInput(treeNode);
		viewer.setComparator(new TreeNodeSorter());

		//clearContextMenu();
		//addContextMenu();
	}
	
	public void refresh() {
		INode treeNode = NodeAssistant.getInstance().getSearchTreeNode();
		viewer.setInput(treeNode);
		viewer.setComparator(new TreeNodeSorter());
	}

	@SuppressWarnings("unused")
	private void clearContextMenu() {
		mgr.removeMenuListener(cmListener);
		mgr.removeAll();
	}

	@SuppressWarnings("unused")
	private void addContextMenu() {
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(cmListener);
		Menu menu = mgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getViewSite().registerContextMenu(mgr, viewer);

		clearAction = new ClearAction(getViewSite().getWorkbenchWindow());
	}

	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(clearAction);
	}

	@Override
	public void dispose() {
		//clearContextMenu();
		//removeListener();
		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@SuppressWarnings("unused")
	private static void createContextMenuGroups(IMenuManager menu) {

	}

	@SuppressWarnings("unused")
	private static void createViewMenuGroups(IMenuManager menu) {

	}

	@SuppressWarnings("unused")
	private static void createToolBarGroups(IToolBarManager toolbar) {

	}

	private static ClearAction clearAction;

	private class ClearAction extends Action implements IWorkbenchAction {

		public final String ID = ClearAction.class.getName();

		@SuppressWarnings("unused")
		private final IWorkbenchWindow window;

		public ClearAction(IWorkbenchWindow window) {
			this.window = window;
			setId(ID);
			setText("&" + Resource.CLEAR_ITEM);
			setToolTipText(Resource.CLEAR_ITEM_FROM_LIST);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void run() {
			doAction();
		}

		private void doAction() {
			NodeAssistant.getInstance().clear(NodeAssistant.getInstance().getSearchTreeNode());
		}

	}

}
