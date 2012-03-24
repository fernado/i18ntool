package i18ntool;

import i18ntool.action.AboutAction;
import i18ntool.action.GeneralAction;
import i18ntool.action.LoadResourcesFromFileAction;
import i18ntool.action.SaveResourcesToFileAction;
import i18ntool.action.ShowExplorerAction;
import i18ntool.property.Resource;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exitAction;

	private ShowExplorerAction showExplorerAction;
	
	private LoadResourcesFromFileAction lrffAction;
	private SaveResourcesToFileAction srtfEncodingAction;
	
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAllAction;
	private IWorkbenchAction closeAction;
	private IWorkbenchAction closeAllAction;
	private IWorkbenchAction closeAllSavedAction;
	private IWorkbenchAction aboutAction;
	
	private GeneralAction generalAction;
	
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		saveAction = ActionFactory.SAVE.create(window);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAction);
		register(saveAllAction);
		
		closeAction = ActionFactory.CLOSE.create(window);
		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		register(closeAction);
		register(closeAllAction);
		
		closeAllSavedAction = ActionFactory.CLOSE_ALL_SAVED.create(window);
		register(closeAllSavedAction);
		
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		showExplorerAction = new ShowExplorerAction(window);
		register(showExplorerAction);
		
		lrffAction = new LoadResourcesFromFileAction(window);
		register(lrffAction);
		
		srtfEncodingAction = new SaveResourcesToFileAction(window);
		register(srtfEncodingAction);
		
		generalAction = new GeneralAction(window);
		register(generalAction);
		
		aboutAction = new AboutAction(window);
		register(aboutAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&" + Resource.FILE, Resource.FILE);
		MenuManager preferenceMenu = new MenuManager("&" + Resource.PREFERENCE, Resource.PREFERENCE);
		MenuManager helpMenu = new MenuManager("&" + Resource.HELP, Resource.HELP);
		menuBar.add(fileMenu);
		menuBar.add(preferenceMenu);
		menuBar.add(helpMenu);
		
		fileMenu.add(showExplorerAction);
		fileMenu.add(new Separator());
		
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new Separator());

		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(closeAllSavedAction);
		fileMenu.add(new Separator());

		fileMenu.add(exitAction);

		preferenceMenu.add(lrffAction);
		preferenceMenu.add(srtfEncodingAction);
		preferenceMenu.add(new Separator());
		preferenceMenu.add(generalAction);
		preferenceMenu.add(new Separator());
		
		helpMenu.add(aboutAction);
		
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
		coolBar.add(toolbar);
	}

}
