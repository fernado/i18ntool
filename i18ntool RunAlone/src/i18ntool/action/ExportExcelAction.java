/**
 * 
 * @author fernado
 * @date Jan 10, 2011
 */
package i18ntool.action;

import i18ntool.consts.Constant;
import i18ntool.consts.Filter;
import i18ntool.file.impl.SaveExcel;
import i18ntool.util.MessageAssistant;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ExportExcelAction extends Action implements IWorkbenchAction {

	public static final String ID = ExportExcelAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;
	
	public ExportExcelAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&" + "Export Excel Test");
		setToolTipText("Export Excel Test");
	}

	@Override
	public void dispose() {
		log.log(Level.INFO, ID + ".dispose()");
	}
	
	@Override
	public void run() {
		doAction();
	}

	private void doAction() {
		log.log(Level.INFO, ID + ".doAction() start");
		String exportFolder = MessageAssistant.getInstance().getFolder() + Constant.EXPORT_PATH;
		try {
			new SaveExcel(exportFolder, Filter.CHANGED_OR_EMPTY).saveResources();
		} catch (IOException e) {
			log.log(Level.WARNING, ID + ".doAction() -- IOException");
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, ID + ".doAction() -- URISyntaxException");
		}
		log.log(Level.INFO, ID + ".doAction() end");
	}
}
