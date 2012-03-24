/**
 * 
 * @author fernado
 * @date Nov 10, 2010
 */
package i18ntool.component;

import i18ntool.consts.Constant;
import i18ntool.property.Resource;
import iceworld.fernado.consts.Constants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public final class AddCommentModel implements IComponent {

	private Button chkLanguage;
	private Button[] chkLanguages;
	private String[] languages;
	private Label lblTemplate;
	private Composite parent;

	public AddCommentModel(final String[] languages, final Composite parent) {
		this.languages = languages;
		this.parent = parent;
		init();
	}

	private void init() {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gdOne = new GridData(GridData.FILL, GridData.FILL, true, false);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setLayoutData(gdOne);
		
		chkLanguage = new Button(composite, SWT.CHECK);
		chkLanguage.setText(Resource.USEFUL_FOR_CHANGED_OR_EMPTY_NODE);
		lblTemplate = new Label(composite, SWT.NONE);
		lblTemplate.setText(Resource.THE_TEMPLATE_IS + Constant.ASSIST_FILE);
		chkLanguage.setLayoutData(gdOne);
		chkLanguages = new Button[languages.length];
		for (int i = 0; i < languages.length; i++) {
			chkLanguages[i] = new Button(composite, SWT.RADIO);
			chkLanguages[i].setText(languages[i]);
			chkLanguages[i].setEnabled(false);
			chkLanguages[i].setLayoutData(gdOne);
		}
		
		addListener();
	}
	
	private void addListener() {
		chkLanguage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Button) {
					Button check = (Button) e.getSource();
					if (check.getSelection()) {
						setEnabled(true);
						setDefaultSelection();
					} else {
						setEnabled(false);
						clearSelection();
					}
				}
			}
		});
		
		for (int i = 0; i < chkLanguages.length; i++) {
			chkLanguages[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (e.getSource() instanceof Button) {
						for (int i = 0; i < chkLanguages.length; i++) {
							chkLanguages[i].setSelection(false);
						}
						Button check = (Button) e.getSource();
						check.setSelection(true);
					}
				}
			});
		}
	}
	
	public void setAddCommentModelEnabled(final boolean enabled) {
		chkLanguage.setEnabled(enabled);
		lblTemplate.setEnabled(enabled);
	}
	
	public void setEnabled(final boolean enabled) {
		for (int i = 0; i < chkLanguages.length; i++) {
			chkLanguages[i].setEnabled(enabled);
		}
	}
	
	public void clearSelection() {
		for (int i = 0; i < chkLanguages.length; i++) {
			chkLanguages[i].setSelection(false);
		}
		chkLanguage.setSelection(false);
	}
	
	private void setDefaultSelection() {
		chkLanguages[0].setSelection(true);
	}
	
	public String getValue() {
		for (int i = 0; i < chkLanguages.length; i++) {
			if (chkLanguages[i].getSelection()) {
				return chkLanguages[i].getText();
			}
		}
		return Constants.EMPTY_STRING;
	}
}
