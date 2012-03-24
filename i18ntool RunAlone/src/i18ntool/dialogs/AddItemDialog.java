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

public class AddItemDialog extends Dialog {

	public static final String ID = AddItemDialog.class.getName();

	private static final Logger log = Logger.getLogger(ID);

	private Label lblInstruction;
	private Label lblKey;
	private Text txtKey;

	private String key;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;

	public AddItemDialog(final Shell shell) {
		super(shell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(WIDTH, HEIGHT);
		newShell.setText(Resource.ADD_KEY);
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
		lblInstruction.setText(Resource.ADD_KEY_DESCRIPTION);
		lblInstruction.setBackground(composite.getBackground());

		lblKey = new Label(composite, SWT.NONE);
		lblKey.setText("&" + Resource.KEY);

		txtKey = new Text(composite, SWT.BORDER);
		txtKey.setLayoutData(gdForText);

		return parent;
	}

	protected void okPressed() {
		key = txtKey.getText();
		if (Utils.isEmpty(key)) {
			MessageDialog.openError(getShell(), Resource.INVALID_KEY, Resource.KEY_MUST_NOT_BLANK);
			return;
		}
		if (NodeAssistant.getInstance().isKeyExist(getKey())) {
			MessageDialog.openError(getShell(), Resource.INVALID_KEY, Resource.EXIST_KEY);
			return;
		}
		log.log(Level.INFO, "i18ntool.dialogs.AddItemDialog okPressed");
		super.okPressed();
	}

	protected void cancelPressed() {
		log.log(Level.INFO, "i18ntool.dialogs.AddItemDialog cancelPressed");
		super.cancelPressed();
	}

	public String getKey() {
		return key;
	}
}
