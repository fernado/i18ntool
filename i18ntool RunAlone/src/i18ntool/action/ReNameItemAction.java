/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.dialogs.RenameItemDialog;
import i18ntool.entity.TreeNode;
import i18ntool.property.Resource;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.consts.Type;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ReNameItemAction extends Action implements ISelectionListener, IWorkbenchAction {
	
	public static final String ID = ReNameItemAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private final IWorkbenchWindow window;

	private IStructuredSelection selection;

	public ReNameItemAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + Resource.RENAME_KEY);
		setToolTipText(Resource.RENAME_KEY_FROM_LIST);
		window.getSelectionService().addSelectionListener(this);
	}

	@SuppressWarnings("unused")
	private void doIt() throws InvocationTargetException, InterruptedException {
		ProgressMonitorDialog pm = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		try {
			pm.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						int totalPages = 100;
						monitor.beginTask("task", totalPages); //$NON-NLS-1$
					} catch (Throwable e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			if (pm.getReturnCode() != ProgressMonitorDialog.CANCEL) {
				throw e;
			}
		} catch (InterruptedException e) {
			if (pm.getReturnCode() != ProgressMonitorDialog.CANCEL) {
				throw e;
			}
		} finally {
			if (pm.getReturnCode() == ProgressMonitorDialog.CANCEL) {

			}
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection incoming) {
		// Selection containing elements
		if (incoming instanceof IStructuredSelection) {
			selection = (IStructuredSelection) incoming;
			setEnabled(selection.size() == 1 && selection.getFirstElement() instanceof TreeNode);
		} else {
			// Other selections, for example containing text or of other kinds.
			setEnabled(false);
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
		log.log(Level.INFO, ID + ".dispose()");
	}

	@Override
	public void run() {
		doAction();
	}

	public void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		TreeNode node = (TreeNode) selection.getFirstElement();
		if (null != node) {
			RenameItemDialog rid = new RenameItemDialog(window.getShell(), node.getName());
			int code = rid.open();
			if (Window.OK == code) {
				if (node.getType() == Type.LEAF) {
					ViewAssistant.getInstance().closeEditor(node, window.getActivePage());
					node.setName(rid.getNewKey());
					node.setStatus(i18ntool.consts.Status.CHANGED, true);
				}
				log.log(Level.INFO, ID + ".run() add a new node ("
						+ node.getName() + ")");
			}
			rid.close();
		}
		log.log(Level.INFO, ID + ".doAction() end");
	}

}
