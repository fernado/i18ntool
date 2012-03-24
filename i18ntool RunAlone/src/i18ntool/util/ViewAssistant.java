/**
 * This class do some viewer doing, it just make the viewer more loop coupling.
 * and do some editor/viewer binding.
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.util;

import i18ntool.editor.GeneralEditorInput;
import i18ntool.editor.TreeNodeEditorInput;
import i18ntool.entity.InputEntity;
import iceworld.fernado.entity.INode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public final class ViewAssistant {

	public static final String ID = ViewAssistant.class.getName();
	private static final ViewAssistant instance = new ViewAssistant();
	private static final Logger log = LogManager.getLogger(ID);
	
	/**
	 * <INode.name, IEditorInput>
	 */
	private static final Map<String, IEditorInput> treeNodeInputMap = new HashMap<String, IEditorInput>();
	
	/**
	 * <INode.name, IEditorInput>
	 */
	private static final Map<String, ISelection> treeNodeSelectionMap = new HashMap<String, ISelection>();
	
	/**
	 * <ID, IDeditorInput>
	 */
	private static final Map<String, IEditorInput> editorInputMap = new HashMap<String, IEditorInput>();

	public static final ViewAssistant getInstance() {
		return instance;
	}

	private ViewAssistant() {
	}

	public Map<String, IEditorInput> getTreeNodeEditorInputMap() {
		return treeNodeInputMap;
	}

	public Map<String, ISelection> getSelectionMap() {
		return treeNodeSelectionMap;
	}

	public IEditorInput getTreeNodeEditorInput(String key) {
		return getTreeNodeEditorInputMap().get(key);
	}

	public ISelection getSelection(String key) {
		return getSelectionMap().get(key);
	}

	/**
	 * set node's name and selection to the map,
	 * which can easy get the map, next time
	 * @param node
	 * @param selection
	 */
	private void addCache(final INode node, final ISelection selection) {
		IEditorInput ei = null;
		if (null == treeNodeInputMap.get(node.getName())) {
			InputEntity ie = new InputEntity();
			ei = new TreeNodeEditorInput(node, ie);
			treeNodeInputMap.put(node.getName(), ei);
			treeNodeSelectionMap.put(node.getName(), selection);
		} else {
			ei = treeNodeInputMap.get(node.getName());
		}
	}

	/**
	 * open Editor, and set node's name and selection to the map, set the editor as active and bring it to top
	 * @param node
	 * @param workbenchPage
	 * @param selection
	 * @return
	 */
	public IEditorPart openEditor(final INode node, final IWorkbenchPage workbenchPage, final ISelection selection) {
		addCache(node, selection);
		IEditorInput ei = getTreeNodeEditorInput(node.getName());
		return openEditor(workbenchPage, ei, i18ntool.editor.TreeNodeEditor.ID);

	}
	
	/**
	 * open Editor and set the editor as active and bring it to top
	 * @param workbenchPage
	 * @param ei
	 * @param ID
	 * @return
	 */
	public IEditorPart openEditor(final IWorkbenchPage workbenchPage, final IEditorInput ei, final String ID) {
		IEditorPart editor = null;
		try {
			editor = workbenchPage.findEditor(ei);
			if (null != editor) {
				workbenchPage.bringToTop(editor);
			} else {
				editor = workbenchPage.openEditor(ei, ID, true);
			}
		} catch (PartInitException pie) {
			log.log(Level.WARNING, "CANT OPEN EDITOR");
		}
		return editor;
	}
	
	/**
	 * open Editor, set ID to the map,
	 * @param workbenchPage
	 * @return
	 */
	public IEditorPart openEditor(final IWorkbenchPage workbenchPage, final String ID) {
		IEditorInput ei = null;
		if (null != editorInputMap.get(ID)) {
			ei = editorInputMap.get(ID);
		} else {
			ei = new GeneralEditorInput("General");
			editorInputMap.put(ID, ei);
		}
		return openEditor (workbenchPage, ei, ID);
	}

	/**
	 * Close EditorPart if it is open
	 * 
	 * @param node
	 * @param workbenchPage
	 */
	public void closeEditor(final INode node, final IWorkbenchPage workbenchPage) {
		String key = node.getName();
		IEditorInput ei = getTreeNodeEditorInput(key);
		if (null != ei) {
			IEditorPart editor = null;
			editor = workbenchPage.findEditor(ei);
			if (null != editor) {
				workbenchPage.closeEditor(editor, true);
				removeCache(key);
			}
		}
	}

	/**
	 * Closes all of the editors belonging to this workbench page.
	 * <p>
	 * If the page has open editors with unsaved content and <code>save</code>
	 * is <code>true</code>, the user will be given the opportunity to save
	 * them.
	 * </p>
	 * 
	 * @param save
	 * 
	 * @return <code>true</code> if all editors were successfully closed, and
	 *         <code>false</code> if at least one is still open
	 */
	public void closeAllEditor(final IWorkbenchPage workbenchPage) {
		workbenchPage.closeAllEditors(true);
	}
	
	/**
	 * Closes all of the editors belonging to this editorPart.
	 * <p>
	 * If the page has open editors with unsaved content and <code>save</code>
	 * is <code>true</code>, the user will be given the opportunity to save
	 * them.
	 * </p>
	 * 
	 * @param save
	 * 
	 * @return <code>true</code> if all editors were successfully closed, and
	 *         <code>false</code> if at least one is still open
	 * @param editorPart
	 */
	public void closeAllEditor(final IEditorPart editorPart) {
		closeAllEditor(editorPart.getEditorSite().getPage());
	}
	
	/**
	 * Closes all of the editors belonging to this editorSite.
	 * <p>
	 * If the page has open editors with unsaved content and <code>save</code>
	 * is <code>true</code>, the user will be given the opportunity to save
	 * them.
	 * </p>
	 * 
	 * @param save
	 * 
	 * @return <code>true</code> if all editors were successfully closed, and
	 *         <code>false</code> if at least one is still open
	 * @param editorSite
	 */
	public void closeAllEditor(final IEditorSite editorSite) {
		closeAllEditor(editorSite.getPage());
	}

	/**
	 * Remove the cache
	 * 
	 * @param key
	 */
	public void removeCache(final String key) {
		treeNodeSelectionMap.remove(key);
		treeNodeInputMap.remove(key);
	}

	/**
	 * show the view you want depends on the view's ID
	 * @param id
	 */
	public void showView(final String id) {
		try {
			getWorkbenchPage().showView(id);
		} catch (PartInitException e) {
			log.log(Level.WARNING, "view : " + id + " cant open");
		}
	}
	
	private static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}
	
	/**
	 * get current active work bench
	 * @return
	 */
	private static IWorkbenchWindow getWorkbenchWindow() {
		return getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * get current active page
	 * @return
	 */
	public static IWorkbenchPage getWorkbenchPage() {
		return getWorkbenchWindow().getActivePage();
	}
	
	/**
	 * hide the view you want
	 * @param view
	 */
	public void hideView(final IViewPart view) {
		getWorkbenchPage().hideView(view);
	}
	
	/**
	 * find view by view's ID
	 * @param ID
	 * @return
	 */
	public IViewPart findView(final String ID) {
		return getWorkbenchPage().findView(ID);
	}
	
	/**
	 * find editor by IEditorInput object
	 * @param input
	 * @return
	 */
	public IEditorPart findEditor(final IEditorInput input) {
		return getWorkbenchPage().findEditor(input);
	}

}
