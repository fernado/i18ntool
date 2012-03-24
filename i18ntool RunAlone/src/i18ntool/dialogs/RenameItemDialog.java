/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.dialogs;

import i18ntool.property.Resource;
import i18ntool.util.NodeAssistant;
import iceworld.fernado.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RenameItemDialog extends Dialog {

	public static final String ID = RenameItemDialog.class.getName();

	private static final Logger log = Logger.getLogger(ID);

	private Label lblInstruction;
	private Label lblOldKey;
	private Label lblNewKey;
	private Text txtOldKey;
	private Text txtNewKey;

	private String oldKey;
	private String newKey;

	public RenameItemDialog(final Shell shell, final String oldKey) {
		super(shell);
		this.oldKey = oldKey;
	}

	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(400, 300);
		newShell.setText(Resource.RENAME_KEY);
	}

	protected Control createDialogArea(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		GridData data = new GridData(GridData.FILL_BOTH);
		GridData gdForLabel = new GridData(GridData.FILL, GridData.FILL, true, false);
		GridData gdForText = new GridData(GridData.FILL, GridData.FILL, true, false);
		composite.setLayout(layout);
		composite.setLayoutData(data);

		lblInstruction = new Label(composite, SWT.NONE);
		gdForLabel.horizontalSpan = 2;
		lblInstruction.setLayoutData(gdForLabel);
		lblInstruction.setText(Resource.RENAME_KEY_DESCRIPTION);
		lblInstruction.setBackground(composite.getBackground());

		lblOldKey = new Label(composite, SWT.NONE);
		lblOldKey.setText("&" + Resource.OLD_KEY);

		txtOldKey = new Text(composite, SWT.BORDER);
		txtOldKey.setLayoutData(gdForText);
		if (!Utils.isEmpty(oldKey)) {
			txtOldKey.setText(oldKey);
			txtOldKey.setEditable(false);
		}

		lblNewKey = new Label(composite, SWT.NONE);
		lblNewKey.setText("&" + Resource.NEW_KEY);

		txtNewKey = new Text(composite, SWT.BORDER);
		txtNewKey.setLayoutData(gdForText);
		txtNewKey.setFocus();

		return parent;
	}

	protected void okPressed() {
		setNewKey(txtNewKey.getText());
		if (Utils.isEmpty(getNewKey())) {
			MessageDialog.openError(getShell(), Resource.INVALID_KEY, Resource.KEY_MUST_NOT_BLANK);
			return;
		}
		if (NodeAssistant.getInstance().isKeyExist(getNewKey())) {
			MessageDialog.openError(getShell(), Resource.INVALID_KEY, Resource.EXIST_KEY);
			return;
		}
		log.log(Level.INFO, "i18ntool.dialogs.RenameItemDialog okPressed");
		super.okPressed();
	}

	protected void cancelPressed() {
		log.log(Level.INFO, "i18ntool.dialogs.RenameItemDialog cancelPressed");
		super.cancelPressed();
	}

	public String getNewKey() {
		return newKey;
	}

	private void setNewKey(final String newKey) {
		this.newKey = newKey;
	}

}
