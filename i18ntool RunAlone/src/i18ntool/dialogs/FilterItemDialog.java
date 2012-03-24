/**
 * 
 * @author fernado  
 * @date 01/09/2010
 */
package i18ntool.dialogs;

import i18ntool.component.FilterModel;
import i18ntool.component.IComponent;
import i18ntool.consts.Filter;
import i18ntool.property.Resource;
import iceworld.fernado.consts.Constants;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class FilterItemDialog extends Dialog {
	
	public static final String ID = FilterItemDialog.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);
	
	private IComponent fcbm = null;
	
	private Filter value;
	
	public FilterItemDialog(final Shell shell) {
		super(shell);
	}

	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(450, 300);
		newShell.setText(Resource.FILTER_ITEM);
	}
	
	protected Control createDialogArea(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayout(layout);
		composite.setLayoutData(data);
		init(composite);
		return parent;
	}
	
	private void init(final Composite composite) {
		Label lblInstruction = new Label(composite, SWT.NONE);
		lblInstruction.setText(Resource.FILTER_ITEM_DESCRIPTION);
		
		Label lblEmpty = new Label(composite, SWT.NONE);
		lblEmpty.setText(Constants.EMPTY_STRING);
		
		fcbm = new FilterModel(composite);
	}
	
	protected void okPressed() {
		log.log(Level.INFO, "i18ntool.dialogs.FilterItemDialog okPressed");
		setValue(((FilterModel) fcbm).getValue());
		super.okPressed();
	}
	
	protected void cancelPressed() {
		log.log(Level.INFO, "i18ntool.dialogs.FilterItemDialog cancelPressed");
		super.cancelPressed();
	}
	
	private void setValue(final Filter value) {
		this.value = value;
	}
	
	public Filter getValue() {
		return value;
	}
	
}
