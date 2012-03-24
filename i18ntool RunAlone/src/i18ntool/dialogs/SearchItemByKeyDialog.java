/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.dialogs;

import i18ntool.property.Resource;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SearchItemByKeyDialog extends Dialog {

	public static final String ID = SearchItemByKeyDialog.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);

	private Label lblInstruction;
	private Label lblKey;
	private Text txtKey;

	private String[] results = { Constants.EMPTY_STRING, Constants.FALSE, Constants.FALSE };

	private Button chkCaseSensitive;
	private Button chkRegularExpression;

	public SearchItemByKeyDialog(final Shell shell) {
		super(shell);
	}

	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(450, 300);
		newShell.setText(Resource.SEARCH_ITEMS_BY_KEY);
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
		lblInstruction.setText(Resource.SEARCH_ITEMS_BY_KEY_DESCRIPTION);

		lblKey = new Label(composite, SWT.NONE);
		lblKey.setText("&" + Resource.KEY);

		txtKey = new Text(composite, SWT.BORDER);
		txtKey.setLayoutData(gdForText);

		addCheck(composite);
		return parent;
	}

	private void addCheck(final Composite parent) {
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false);
		chkCaseSensitive = new Button(parent, SWT.CHECK);
		gd.horizontalSpan = 2;
		chkCaseSensitive.setLayoutData(gd);
		chkCaseSensitive.setText(Resource.CASE_SENSITIVE);
		
		chkRegularExpression = new Button(parent, SWT.CHECK);
		chkCaseSensitive.setLayoutData(gd);
		chkRegularExpression.setText(Resource.REGULAR_EXPRESSION);
		addLisenter();
	}

	private void addLisenter() {
		chkCaseSensitive.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (chkCaseSensitive.getSelection()) {
					results[1] = Constants.TRUE;
				} else {
					results[1] = Constants.FALSE;
				}
			}
		});

		chkRegularExpression.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (chkRegularExpression.getSelection()) {
					results[2] = Constants.TRUE;
				} else {
					results[2] = Constants.FALSE;
				}
			}
		});
	}

	protected void okPressed() {
		results[0] = txtKey.getText();
		if (Utils.isEmpty(results[0])) {
			MessageDialog.openError(getShell(), Resource.INVALID_KEY, Resource.KEY_MUST_NOT_BLANK);
			return;
		}
		log.log(Level.INFO, "i18ntool.dialogs.SearchItemByKeyDialog okPressed");
		super.okPressed();
	}

	protected void cancelPressed() {
		log.log(Level.INFO, "i18ntool.dialogs.SearchItemByKeyDialog cancelPressed");
		super.cancelPressed();
	}

	public String[] getResults() {
		return results;
	}
}
