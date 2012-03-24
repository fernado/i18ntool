/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.tray;

import i18ntool.Activator;
import i18ntool.consts.Constant;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.PlatformUI;

public class SystemTrayMaster implements SelectionListener, Listener {
	private Menu menu;
	private MenuItem[] menuItems = new MenuItem[0];

	private final class RestoreWindowListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			restoreWindow();
		}
	}

	public void restoreWindow() {
		Shell shell = getShell();
		shell.open();
		shell.setMinimized(false);
		shell.forceActive();
		shell.forceFocus();
	}

	public void clearItems() {
		for (MenuItem menuItem : menuItems) {
			menuItem.removeSelectionListener(restoreWindowListener);
			menuItem.dispose();
		}
	}

	public void showMenu() {
		clearItems();
		MenuItem openItem = new MenuItem(menu, SWT.PUSH);
		MenuItem closeItem = new MenuItem(menu, SWT.NONE);
		closeItem.setText("Close");
		closeItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				closeApplication();
			}
		});

		menuItems = new MenuItem[] { openItem, closeItem };
		openItem.setText("Active");
		openItem.addSelectionListener(restoreWindowListener);
		menu.setVisible(true);
	}

	private Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public void minimizeWindow() {
		getShell().setMinimized(true);
		getShell().setVisible(false);
	}

	public void createSystemTray() {
		Tray tray = Display.getDefault().getSystemTray();
		TrayItem item = new TrayItem(tray, SWT.NONE);
		item.setText("i18nTool");
		item.setToolTipText("i18nTool");
		Image image = Activator.getImageDescriptor(Constant.TRAY_IMG).createImage();
		item.setImage(image);
		image.dispose();
		item.addSelectionListener(this);
		item.addListener(SWT.MenuDetect, this);
		menu = new Menu(getShell(), SWT.POP_UP);
	}

	private RestoreWindowListener restoreWindowListener;

	public SystemTrayMaster() {
		restoreWindowListener = new RestoreWindowListener();
	}

	protected void closeApplication() {
		PlatformUI.getWorkbench().close();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		restoreWindow();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
	}

	@Override
	public void handleEvent(Event event) {
		showMenu();
	}

}
