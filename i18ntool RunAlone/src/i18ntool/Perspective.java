package i18ntool;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addStandaloneView(i18ntool.view.ExplorerView.ID, true, IPageLayout.LEFT, .3f,
				editorArea);
		layout.addStandaloneViewPlaceholder(i18ntool.view.SearchView.ID, IPageLayout.RIGHT, .65f,
				editorArea, true);
		layout.addFastView("org.eclipse.ui.views.ProgressView", .5f);
	}

}
