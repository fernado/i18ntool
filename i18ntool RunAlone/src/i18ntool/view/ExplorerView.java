/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.view;

import i18ntool.action.AddItemAction;
import i18ntool.action.DeleteItemAction;
import i18ntool.action.FilterItemAction;
import i18ntool.action.ReNameItemAction;
import i18ntool.action.SearchItemByKeyAction;
import i18ntool.action.SearchItemByValueAction;
import i18ntool.adapter.TreeNodeAdapterFactory;
import i18ntool.editor.GeneralEditor;
import i18ntool.editor.TreeNodeEditor;
import i18ntool.editor.TreeNodeEditorInput;
import i18ntool.entity.ITreeNode;
import i18ntool.entity.TreeNode;
import i18ntool.property.Resource;
import i18ntool.provider.DefaultWorkbenchContentProvider;
import i18ntool.sorter.TreeNodeSorter;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ResourceHandler;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.entity.INode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class ExplorerView extends ViewPart {

	private class ContextMenuListener implements IMenuListener {
		@Override
		public void menuAboutToShow(IMenuManager manager) {
			fillContextMenu(mgr);
		}
	}
	
	public static final String ID = ExplorerView.class.getName();

	private static final MenuManager mgr = new MenuManager();

	private ContextMenuListener cmListener = new ContextMenuListener();

	private TreeViewer viewer;

	private IAdapterFactory adapterFactory = new TreeNodeAdapterFactory();

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		init();
		addListener();
	}

	@Override
	public void dispose() {
		Platform.getAdapterManager().unregisterAdapters(adapterFactory);
		removeListener();
		clearContextMenu();
		ResourceHandler.getInstance().savePreservationInConfigFiles();
		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void init() {
		this.setPartName(Resource.EXPLORER);
		getSite().setSelectionProvider(viewer);
		viewer.setContentProvider(new DefaultWorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		Platform.getAdapterManager().registerAdapters(adapterFactory, TreeNode.class);
		INode treeNode = NodeAssistant.getInstance().getData();
		viewer.setUseHashlookup(true);
		viewer.setInput(treeNode);
		viewer.setComparator(new TreeNodeSorter());

		addContextMenu();
		initializeToolBar();
	}

	private void initializeToolBar() {
		IMenuManager iMenuManager = getViewSite().getActionBars().getMenuManager();
		iMenuManager.add(new FilterItemAction(getViewSite().getWorkbenchWindow()));
	}

	private void removeListener() {
		getSite().getPage().removePartListener(partListener);
	}

	private void addListener() {
		getSite().getPage().addPartListener(partListener);

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				TreeNode node = (TreeNode) selection.getFirstElement();
				ViewAssistant.getInstance().openEditor(node, getViewSite().getPage(), selection);
			}
		});
	}

	private IPartListener2 partListener = new IPartListener2() {

		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				if (part instanceof TreeNodeEditor) {
					TreeNodeEditor editor = (TreeNodeEditor) part;
					TreeNodeEditorInput input = (TreeNodeEditorInput) editor.getEditorInput();
					String name = input.getName();
					setSelection(name);
				} else if (part instanceof GeneralEditor) {
					
				}
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				if (part instanceof TreeNodeEditor) {
					TreeNodeEditor editor = (TreeNodeEditor) part;
					TreeNodeEditorInput input = (TreeNodeEditorInput) editor.getEditorInput();
					String name = input.getName();
					setSelection(name);
				} else if (part instanceof GeneralEditor) {
					
				}
			}
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof IEditorPart) {
				if (part instanceof TreeNodeEditor) {
					TreeNodeEditor editor = (TreeNodeEditor) part;
					TreeNodeEditorInput input = (TreeNodeEditorInput) editor.getEditorInput();
					String name = input.getName();
					ViewAssistant.getInstance().removeCache(name);
				} else if (part instanceof GeneralEditor) {
					
				}
			}
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partOpened(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
		}

	};

	public ISelection getSelection() {
		return viewer.getSelection();
	}

	private void clearContextMenu() {
		mgr.removeMenuListener(cmListener);
		mgr.removeAll();
	}

	private void addContextMenu() {
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(cmListener);
		Menu menu = mgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getViewSite().registerContextMenu(mgr, viewer);

		addItemAction = new AddItemAction(getViewSite().getWorkbenchWindow());
		deleteItemAction = new DeleteItemAction(getViewSite().getWorkbenchWindow());
		reNameItemAction = new ReNameItemAction(getViewSite().getWorkbenchWindow());
		searchItemByKeyAction = new SearchItemByKeyAction(getViewSite().getWorkbenchWindow());
		searchItemByValueAction = new SearchItemByValueAction(getViewSite().getWorkbenchWindow());
	}

	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(addItemAction);
		mgr.add(deleteItemAction);
		mgr.add(reNameItemAction);
		mgr.add(searchItemByKeyAction);
		mgr.add(searchItemByValueAction);
	}


	private static AddItemAction addItemAction;
	private static DeleteItemAction deleteItemAction;
	private static ReNameItemAction reNameItemAction;
	private static SearchItemByKeyAction searchItemByKeyAction;
	private static SearchItemByValueAction searchItemByValueAction;

	/**
	 * viewer can choose the node depends on name
	 * 
	 * @param name
	 */
	public void setSelection(String name) {
		ISelection selection = ViewAssistant.getInstance().getSelection(name);
		if (null == selection) {
			selection = new StructuredSelection(name);
			viewer.setSelection(selection, true);
		} else {
			viewer.setSelection(selection, true);
		}
	}

	/**
	 * viewer can choose the node depends on TreeNode
	 * 
	 * @param node
	 */
	public void setSelection(ITreeNode node) {
		ISelection selection = ViewAssistant.getInstance().getSelection(node.getName());
		if (null == selection) {
			List<ITreeNode> list = new ArrayList<ITreeNode>();
			list.add(node);
			selection = new StructuredSelection(list);
			viewer.setSelection(selection);
		} else {
			viewer.setSelection(selection, true);
		}
	}
	
	public TreeViewer getViewer() {
		return viewer;
	}
	
}
