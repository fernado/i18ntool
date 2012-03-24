/**
 * 
 * @author fernado  
 * @date 08/09/2010
 */
package i18ntool.action;

import i18ntool.consts.Filter;
import i18ntool.file.impl.SaveProperties;
import i18ntool.property.Resource;
import i18ntool.util.MessageAssistant;
import iceworld.fernado.consts.Constants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class SaveResourcesToFileAction extends Action implements IWorkbenchAction {

	public static final String ID = SaveResourcesToFileAction.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;
	
	private boolean encoding;

	public SaveResourcesToFileAction(final IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		initText();
	}
	
	private void initText() {
		encoding = true;
		setText(Resource.SAVE_RESOURCE);
		setToolTipText(Resource.SAVE_RESOURCE);
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
		MessageAssistant.getInstance().setEncoding(encoding);
		Filter filter = Filter.UNKNOWN;
		switch (MessageAssistant.getInstance().getPreservation()) {
			case NORMAL:
				filter = Filter.NORMAL;
				break;
			case SPECIAL1:
				filter = Filter.NOT_EMPTY_VALUE;
				break;
			case SPECIAL2:
				filter = Filter.NOT_EMPTY_VALUE_BUT_ALLOW_ORIGINAL_VALUE_EMPTY;
				break;
			default:
				break;
		}
		
		try {
			new SaveProperties(MessageAssistant.getInstance().getFolder(), filter, Constants.EMPTY_STRING).saveResources();
		} catch (IOException e) {
			log.log(Level.WARNING, ID + ".doAction() -- IOException");
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, ID + ".doAction() -- URISyntaxException");
		}
		MessageAssistant.getInstance().setEncoding(false);
		log.log(Level.INFO, ID + ".doAction() end");
	}
	

}
