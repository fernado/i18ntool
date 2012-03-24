/**
 * 
 * @author fernado  
 * @date 09/09/2010
 */
package i18ntool.editor;

import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class TextTreeNodeEditor extends EditorPart {

	public static final String ID = TextTreeNodeEditor.class.getName();
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ID);
	
	@Override
	public void doSave(final IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		if (null != input) {
			this.setPartName(input.getName());
		}
		this.setInput(input);
		this.setSite(site);
	}
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void createPartControl(final Composite parent) {
		
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}
