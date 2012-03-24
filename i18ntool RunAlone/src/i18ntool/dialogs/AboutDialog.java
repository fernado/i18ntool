/**
 * 
 * @author fernado
 * @date Nov 12, 2010
 */
package i18ntool.dialogs;

import i18ntool.Activator;
import i18ntool.consts.Constant;
import i18ntool.property.Resource;
import iceworld.fernado.util.Utils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AboutDialog extends TrayDialog {

	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	
	private Text text;
	
	public AboutDialog(final Shell shell) {
		super(shell);
	}
	
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(WIDTH, HEIGHT);
		newShell.setText(Resource.ABOUT);
	}
	
	protected Control createDialogArea(final Composite parent) {
		
        // brand the about box if there is product info
        Image aboutImage = Activator.getImageDescriptor(Constant.ABOUT_IMG).createImage();
		
        // the image & text	
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayout(layout);
		composite.setLayoutData(data);

        //image on left side of dialog
        if (aboutImage != null) {
            Label imageLabel = new Label(composite, SWT.NONE);

            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.BEGINNING;
            data.grabExcessHorizontalSpace = false;
            imageLabel.setLayoutData(data);
            imageLabel.setImage(aboutImage);
        }
        
        text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI); 
        text.setEditable(false);
        text.setLayoutData(data);
        text.setText(getText());
        
        // horizontal bar
        Label bar = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        bar.setLayoutData(data);
        return parent;
	}
	
	protected void createButtonsForButtonBar(final Composite parent) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
        Label l = new Label(parent, SWT.NONE);
        l.setLayoutData(data);
		Button b = createButton(parent, IDialogConstants.OK_ID, "OK", true);
        b.setFocus();
	}
	
	private static String getText() {
		StringBuilder result = new StringBuilder();
		result.append("i18nTool for Developer\n\n\n")
			.append("Author: fernado\n\n")
			.append("Build id: ")
			.append(Utils.getCurrentVersionId());
		return result.toString();
	}

	
}
