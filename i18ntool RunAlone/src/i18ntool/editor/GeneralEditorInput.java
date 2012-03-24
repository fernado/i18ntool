/**
 * 
 * @author fernado  
 * @date 09/09/2010
 */
package i18ntool.editor;

import i18ntool.consts.Constant;
import i18ntool.util.MessageAssistant;
import iceworld.fernado.util.Utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class GeneralEditorInput implements IEditorInput {
	
	private String name;
	
	private String browserFolder;
	
	public GeneralEditorInput(final String name) {
		this.name = name;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		return null;
	}
	
	/**
	 * Get folder when click the browser button, and choose folder result 
	 * @return
	 */
	public String getBrowserFolder() {
		if (!Utils.isEmpty(browserFolder)) {
			return browserFolder;
		}
		if (Utils.isEmpty(MessageAssistant.getInstance().getMessageFolder())) {
			return Constant.RESOURCE_PATH;
		}
		return MessageAssistant.getInstance().getMessageFolder();
	}
	
	public void setResFolder(final String resFolder) {
		this.browserFolder = resFolder;
	}
	
	public void persistanceSaveConfiguration() {
		
	}

}
