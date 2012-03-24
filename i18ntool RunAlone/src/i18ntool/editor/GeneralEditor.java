/**
 * This is the general editor part
 * 
 * 
 * @author fernado  
 * @date 09/09/2010
 */
package i18ntool.editor;

import i18ntool.component.AddCommentModel;
import i18ntool.component.FilterModelExt;
import i18ntool.component.IComponent;
import i18ntool.consts.Constant;
import i18ntool.consts.Filter;
import i18ntool.consts.Preservation;
import i18ntool.file.impl.LoadExcel;
import i18ntool.file.impl.LoadProperties;
import i18ntool.file.impl.SaveExcel;
import i18ntool.file.impl.SaveProperties;
import i18ntool.filter.INodeFilter;
import i18ntool.filter.NodeFilter;
import i18ntool.property.Resource;
import i18ntool.util.LogManager;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ResourceHandler;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.entity.INode;
import iceworld.fernado.file.ISaveResource;
import iceworld.fernado.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class GeneralEditor extends EditorPart {
	
	public static final String ID = GeneralEditor.class.getName();
	private static final Logger log = LogManager.getLogger(ID);
	
	private Text txtBrowser;
	
	private Text txtImportPath;
	private Text txtExportPath;
	
	private Button btnBrowser;
	private Button btnReset;
	
	private Button btnImport;
	private Button btnExport;
	
	private Label lblExportMsg;
	private Label lblImportMsg;
	
	private IComponent fme;
	private AddCommentModel acm;
	
	private Color errorColor = null;
	@SuppressWarnings("unused")
	private Color correctColor = null;
	
	private Button rdImportExcel;
	private Button rdImportProperties;
	private Button rdExportExcel;
	private Button rdExportProperties;
	
	private Button rdNormalSave;
	private Button rdSpecialSave1;
	private Button rdSpecialSave2;
	
	private GeneralEditorInput gei;
	
	
	@Override
	public void doSave(final IProgressMonitor monitor) {
	}
	
	@Override
	public void doSaveAs() {
	}
	
	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		if (null != input) {
			this.setPartName(input.getName());
		}
		this.setInput(input);
		this.setSite(site);
		if (input instanceof GeneralEditorInput) {
			this.gei = (GeneralEditorInput) input;
		}
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void createPartControl(final Composite parent) {
		Composite composite = addScrolledComposite(parent);
		
		initColor();
		
		initResourceLocator(composite);
		initSaveOption(composite);
		initImport(composite);
		initExport(composite);
		
		addListener();
	}
	
	private Composite addScrolledComposite(final Composite parent) {
		parent.setLayout(new FillLayout());
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
	    scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setExpandVertical(true);
	    scrolledComposite.setMinWidth(800);
	    scrolledComposite.setMinHeight(800);
	    
	    GridLayout layout = new GridLayout();
	    Composite composite = new Composite(scrolledComposite, SWT.NONE);
	    composite.setLayout(layout);
		scrolledComposite.setContent(composite);
		
		return composite;
	}
	
	private void initColor() {
		errorColor = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		correctColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	}
	
	private void initResourceLocator(final Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Resource.RESOURCE_LOCATOR);
		GridLayout layout = new GridLayout(4, false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false);
		group.setLayout(layout);
		group.setLayoutData(data);
		
		initResourceContent(group);
	}
	
	private void initResourceContent(final Composite composite) {
		GridData gdForText = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		Label lblResourceFolder = new Label(composite, SWT.NONE);
		lblResourceFolder.setText(Resource.FOLDER);
		
		txtBrowser = new Text(composite, SWT.BORDER | SWT.SINGLE);
		txtBrowser.setLayoutData(gdForText);
		txtBrowser.setText(gei.getBrowserFolder());
		txtBrowser.setEditable(false);
		
		btnBrowser = new Button(composite, SWT.NONE);
		btnBrowser.setText(Resource.BROWSER);
		
		btnReset = new Button(composite, SWT.NONE);
		btnReset.setText(Resource.RESET);
	}
	
	private void initSaveOption(final Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Resource.SAVE_CONFIGURATION);
		GridLayout layout = new GridLayout(4, false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false);
		group.setLayout(layout);
		group.setLayoutData(data);
		
		initSaveContent(group);
	}
	
	private void initSaveContent(final Composite composite) {
		GridData gdRadio = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdRadio.horizontalSpan = 3;
		rdNormalSave = new Button(composite, SWT.RADIO);
		rdSpecialSave1 = new Button(composite, SWT.RADIO);
		rdSpecialSave2 = new Button(composite, SWT.RADIO);
		rdNormalSave.setLayoutData(gdRadio);
		rdSpecialSave1.setLayoutData(gdRadio);
		rdSpecialSave2.setLayoutData(gdRadio);
		rdNormalSave.setText("Save normal");
		rdSpecialSave1.setText("Only save key which is NOT empty value");
		rdSpecialSave2.setText("Only save key which value is NOT empty but keep the old empty key");
		saveConfigurationSelected();
	}
	
	private void initImport(final Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Resource.IMPORT);
		GridLayout layout = new GridLayout(4, false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false);
		group.setLayout(layout);
		group.setLayoutData(data);
		initImportOption(group);
		initImportText(group);
		initImportSubmit(group);
		
		initImportExt();
	}
	
	private void initImportExt() {
		rdImportProperties.setSelection(true);
		txtImportPath.setText(gei.getBrowserFolder() + Constant.IMPORT_PATH);
	}
	
	private void initImportOption(final Composite composite) {
		GridData gdRadio = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdRadio.horizontalSpan = 2;
		rdImportProperties = new Button(composite, SWT.RADIO);
		rdImportExcel = new Button(composite, SWT.RADIO);
		rdImportProperties.setLayoutData(gdRadio);
		rdImportExcel.setLayoutData(gdRadio);
		rdImportProperties.setText(Resource.IMPORT_PROPERTIES);
		rdImportExcel.setText(Resource.IMPORT_EXCEL);
	}
	
	private void initImportText(final Composite composite) {
		GridData gdText = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdText.horizontalSpan = 3;
		Label lblImportPropertiesToMergePath = new Label(composite, SWT.NONE);
		lblImportPropertiesToMergePath.setText(Resource.IMPORT_PATH);
		
		txtImportPath = new Text(composite, SWT.BORDER | SWT.SINGLE);
		txtImportPath.setLayoutData(gdText);
		txtImportPath.setEditable(false);
	}
	
	private void initImportSubmit(final Composite composite) {
		initImportMsg(composite);
		initImportBtn(composite);
	}
	
	private void initImportBtn(final Composite composite) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		btnImport = new Button(composite, SWT.NONE);
		btnImport.setLayoutData(data);
		btnImport.setText(Resource.IMPORT);
	}
	
	private void initImportMsg(final Composite composite) {
		GridData gdText = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdText.horizontalSpan = 3;
		lblImportMsg = new Label(composite, SWT.NONE);
		lblImportMsg.setLayoutData(gdText);
	}
	
	private void initExport(final Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Resource.EXPORT);
		GridLayout layout = new GridLayout(4, false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false);
		group.setLayout(layout);
		group.setLayoutData(data);
		
		initExportOption(group);
		initExportAddComment(group);
		initExportFilter(group);
		initExportPath(group);
		initExportSubmit(group);
		
		initExportExt();
	}
	
	private void initExportExt() {
		rdExportProperties.setSelection(true);
		acm.setAddCommentModelEnabled(true);
		((FilterModelExt) fme).setChangedOrNormalEnabled(false);
		((FilterModelExt) fme).setEmptyOrNormalEnabled(false);
	}
	
	private void initExportOption(final Composite composite) {
		GridData gdRadio = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdRadio.horizontalSpan = 2;
		rdExportProperties = new Button(composite, SWT.RADIO);
		rdExportExcel = new Button(composite, SWT.RADIO);
		rdExportProperties.setLayoutData(gdRadio);
		rdExportExcel.setLayoutData(gdRadio);
		rdExportProperties.setText(Resource.EXPORT_PROPERTIES);
		rdExportExcel.setText(Resource.EXPORT_EXCEL);
	}
	
	private void initExportMsg(final Composite composite) {
		lblExportMsg = new Label(composite, SWT.NONE);
		GridData gdText = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		gdText.horizontalSpan = 3;
		lblExportMsg.setLayoutData(gdText);
		lblExportMsg.setForeground(errorColor);
	}
	
	private void initExportAddComment(final Composite composite) {
		Group groupCommentExport = new Group(composite, SWT.NONE);
		GridLayout groupFilterExportLayout = new GridLayout();
		GridData groupFilterExportData = new GridData(GridData.FILL, GridData.FILL, true, false);
		groupFilterExportData.horizontalSpan = 4;
		groupCommentExport.setLayout(groupFilterExportLayout);
		groupCommentExport.setLayoutData(groupFilterExportData);
		groupCommentExport.setText(Resource.ADD_A_LANGUAGE_AS_TEMPLATE);
		acm = new AddCommentModel(MessageAssistant.getInstance().getMessageFiles(), groupCommentExport);
	}
	
	private void initExportFilter(final Composite composite) {
		Group groupFilterExport = new Group(composite, SWT.NONE);
		GridLayout groupFilterExportLayout = new GridLayout();
		GridData groupFilterExportData = new GridData(GridData.FILL, GridData.FILL, true, false);
		groupFilterExportData.horizontalSpan = 4;
		groupFilterExport.setLayout(groupFilterExportLayout);
		groupFilterExport.setLayoutData(groupFilterExportData);
		groupFilterExport.setText(Resource.FILTER_ITEM);
		
		initCheckBoxes(groupFilterExport);
	}
	
	private void initExportPath(final Composite composite) {
		Label lblExportToMergePath = new Label(composite, SWT.NONE);
		lblExportToMergePath.setText(Resource.EXPORT_PATH);
		
		txtExportPath = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gdText = new GridData(GridData.FILL, GridData.FILL, true, false);
		gdText.horizontalSpan = 3;
		txtExportPath.setLayoutData(gdText);
		txtExportPath.setText(gei.getBrowserFolder() + Constant.EXPORT_PATH);
		txtExportPath.setEditable(false);
	}
	
	private void initExportSubmit(final Composite composite) {
		initExportMsg(composite);
		initExportBtn(composite);
	}
	
	private void initExportBtn(final Composite composite) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.horizontalSpan = 1;
		btnExport = new Button(composite, SWT.NONE);
		btnExport.setLayoutData(data);
		btnExport.setText(Resource.EXPORT);
	}
	
	private void initCheckBoxes(final Composite composite) {
		fme = new FilterModelExt(composite, false);
	}
	
	@Override
	public void setFocus() {
	}
	
	private void browserPressed() {
		DirectoryDialog dirDialog = new DirectoryDialog(Display.getCurrent().getActiveShell()); 
		String filterPath = new File(MessageAssistant.getInstance().getFolder()).getAbsolutePath();
		dirDialog.setFilterPath(filterPath);
		String folder = Utils.replaceBacklashAsSlash(dirDialog.open()) + Constants.SLASH;
		// set folder to others places
		gei.setResFolder(folder);
		txtBrowser.setText(gei.getBrowserFolder());
		txtImportPath.setText(afterBrowseringImport());
		txtExportPath.setText(afterBrowseringExport());
		
		MessageAssistant.getInstance().setMessageFolder(gei.getBrowserFolder());
		ResourceHandler.getInstance().saveFolderInConfigFiles();
	}
	
	private void resetPressed() {
		gei.setResFolder(Constants.EMPTY_STRING);
		
		txtBrowser.setText(Constant.RESOURCE_PATH);
		txtImportPath.setText(afterResetingImport());
		txtExportPath.setText(afterResetingExport());
		
		MessageAssistant.getInstance().setMessageFolder(Constant.RESOURCE_PATH);
		ResourceHandler.getInstance().saveFolderInConfigFiles();
	}
	
	private String afterResetingExport() {
		return isExportPropertiesSelected()
			? Constant.RESOURCE_PATH + Constant.EXPORT_PATH
					: Constant.RESOURCE_PATH + Constant.EXPORT_PATH	+ Constant.EXPORT_CUSTOMER_XLS;
	}
	
	private String afterResetingImport() {
		return isImportPropertiesSelected() 
			? Constant.RESOURCE_PATH + Constant.IMPORT_PATH
				: Constant.RESOURCE_PATH + Constant.IMPORT_CUSTOMER_XLS;
	}
	
	private String afterBrowseringExport() {
		return isExportPropertiesSelected()
			? gei.getBrowserFolder() + Constant.EXPORT_PATH
					: gei.getBrowserFolder() + Constant.EXPORT_PATH	+ Constant.EXPORT_CUSTOMER_XLS;
	}
	
	private String afterBrowseringImport() {
		return isImportPropertiesSelected() 
			? gei.getBrowserFolder() + Constant.IMPORT_PATH
					: gei.getBrowserFolder() + Constant.IMPORT_PATH + Constant.IMPORT_CUSTOMER_XLS;
	}
	
	private void importPressed() {
		if (isImportPropertiesSelected()) {
			checkImportPropertiesError(new LoadProperties(Constant.IMPORT_PATH).readResources());
		} else {
			checkImportExcelError(new LoadExcel(Constant.IMPORT_PATH + Constant.IMPORT_CUSTOMER_XLS).readResources());
		}
	}
	
	private void checkImportPropertiesError(final boolean isError) {
		if (isError) {
			lblImportMsg.setForeground(errorColor);
			lblImportMsg.setText(Resource.ERROR_NO_PROPERTY_FILE_EXIST);
			log.log(Level.WARNING, Resource.ERROR_NO_PROPERTY_FILE_EXIST);
		} else {
			log.log(Level.WARNING, "Warning! import property file(s) successfully!");
			ViewAssistant.getInstance().closeAllEditor(this);
		}
	}
	
	private void checkImportExcelError(final boolean isError) {
		if (isError) {
			lblImportMsg.setForeground(errorColor);
			lblImportMsg.setText(Resource.ERROR_NO_EXCEL_FILE_EXIST);
			log.log(Level.WARNING, Resource.ERROR_NO_EXCEL_FILE_EXIST);
		} else {
			log.log(Level.WARNING, "Warning! import excel file successfully!");
			ViewAssistant.getInstance().closeAllEditor(this);
		}
	}
	
	/**
	 * Before export done, there are some checking
	 */
	private void exportPressed() {
		log.log(Level.INFO, "EXPORT --");
		lblExportMsg.setText(Constants.EMPTY_STRING);

		if (!checkExportTypeChecked()) {
			return;
		}
		if (!checkTypeMapChecked()) {
			return;
		}
		if (isExportPropertiesSelected()) {
			checkError(doExportProperties(), false);
		} else {
			checkError(doExportExcel(), true);
		}
	}
	
	private boolean isImportPropertiesSelected() {
		return (rdImportProperties.getSelection() && !rdImportExcel.getSelection());
	}
	
	private boolean isExportPropertiesSelected() {
		return (rdExportProperties.getSelection() && !rdExportExcel.getSelection());
	}
	
	private boolean doExportProperties() {
		boolean exportError = false;
		String exportFolder = MessageAssistant.getInstance().getFolder() + Constant.EXPORT_PATH;
		log.log(Level.WARNING, "EXPORT TYPE IS " + ((FilterModelExt) fme).getValue());
		SaveProperties exportFile = new SaveProperties(exportFolder, ((FilterModelExt) fme).getValue(), acm.getValue());
		try {
			exportFile.removeResources();
			exportFile.saveResources();
		} catch (IOException e) {
			exportError = true;
			log.log(Level.WARNING, "CANT OPEN OR EXPORT THE FILE IN " + Constant.EXPORT_PATH);
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, "There is path error under the " + Constant.EXPORT_PATH);
			exportError = true;
		}
		return exportError;
	}
	
	private boolean doExportExcel() {
		boolean exportError = false;
		String file = MessageAssistant.getInstance().getFolder() + Constant.EXPORT_PATH + Constant.EXPORT_CUSTOMER_XLS;
		Filter filter = ((FilterModelExt) fme).getValue();
		log.log(Level.INFO, ID + "doExportExcel() filepath is: " + file);
		log.log(Level.WARNING, "EXPORT TYPE IS " + filter);
		ISaveResource we = new SaveExcel(file, filter);
		log.log(Level.INFO, ID + "doExportExcel() SaveExcel init");
		try {
			we.removeResources();
			we.saveResources();
		} catch (IOException e) {
			log.log(Level.WARNING, "CANT OPEN OR EXPORT THE EXCEL IN " + file);
			exportError = true;
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, "There is path error under the " + file);
			exportError = true;
		}
		return exportError;
	}
	
	private void checkError(final boolean isError, final boolean isProperties) {
		if (isError) {
			lblExportMsg.setText(Resource.ERROR_ACTION_HAS_BEEN_DUMPED);
		} else {
			if (isProperties) {
				log.log(Level.WARNING, "WARNING! EXPORT PROPERTY FILES(S) SUCCESSFULLY!");
			} else {
				log.log(Level.WARNING, "WARNING! EXPORT EXCEL SUCCESSFULLY!");
			}
		}
	}
	
	private boolean checkExportTypeChecked() {
		if (Filter.UNKNOWN == ((FilterModelExt) fme).getValue()) {
			lblExportMsg.setText(Resource.ERROR_AT_LEAST_ONE_ITEM_SELECTED);
			log.log(Level.WARNING, Resource.ERROR_AT_LEAST_ONE_ITEM_SELECTED);
			return false;
		}
		return true;
	}
	
	private boolean checkTypeMapChecked() {
		boolean result = false;
		switch (((FilterModelExt) fme).getValue()) {
		case CHANGED_OR_EMPTY: 
			result = checkChangedOrEmpty();
			break;
		case CHANGED:
			result = checkChanged();
			break;
		case EMPTY:
		case EMPTY_OR_NORMAL:
			result = checkEmpty();
			break;
		case NORMAL:
		case CHANGED_OR_NORMAL:
		case CHANGED_OR_EMPTY_OR_NORMAL:
			result = true;
		default:
			break;
		}
		if (!result) {
			lblExportMsg.setText(Resource.ERROR_NO_EXIST_ITEM_FOR_EXPORT);
			log.log(Level.WARNING, "THERE IS NO EXISTS ITEMS FOR YOUR EXPORT.");
		}
		return result;
	}
	
	private boolean checkChangedOrEmpty() {
		INodeFilter filter = new NodeFilter();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (filter.isChangedOrEmptyLeaf(node)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkEmpty() {
		INodeFilter filter = new NodeFilter();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (filter.isEmptyLeaf(node)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkChanged() {
		INodeFilter filter = new NodeFilter();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (filter.isChangedLeaf(node)) {
				return true;
			}
		}
		return false;
	}
	
	private void addListener() {
		btnBrowser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserPressed();
			}
		});
		
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetPressed();
			}
		});
		
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importPressed();
			}
		});
		
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportPressed();
			}
		});
		
		rdImportProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importProertiesSelected();
			}
		});
		
		rdImportExcel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importExcelSelected();
			}
		});
		
		rdExportProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportProertiesSelected();
			}
		});
		
		rdExportExcel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportExcelSelected();
			}
		});
		
		rdNormalSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				persistanceSaveConfiguration(Preservation.NORMAL);
			}
		});
		
		rdSpecialSave1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				persistanceSaveConfiguration(Preservation.SPECIAL1);
			}
		});
		
		rdSpecialSave2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				persistanceSaveConfiguration(Preservation.SPECIAL2);
			}
		});
	}
	
	private void persistanceSaveConfiguration(Preservation preservation) {
		MessageAssistant.getInstance().setPreservation(preservation);
		ResourceHandler.getInstance().savePreservationInConfigFiles();
	}
	
	private void saveConfigurationSelected() {
		switch (MessageAssistant.getInstance().getPreservation()) {
			case NORMAL:
				rdNormalSave.setSelection(true);
				break;
			case SPECIAL1:
				rdSpecialSave1.setSelection(true);
				break;
			case SPECIAL2:
				rdSpecialSave2.setSelection(true);
				break;
			default:
				rdNormalSave.setSelection(true);
		}
	}

	private void exportProertiesSelected() {
		((FilterModelExt) fme).setChangedOrNormalEnabled(false);
		((FilterModelExt) fme).setEmptyOrNormalEnabled(false);
		((FilterModelExt) fme).setChangedOrEmptyOrNormalEnabled(false);
		((FilterModelExt) fme).setChangedOrEmptyText(false);
		((FilterModelExt) fme).setDefaultSelection();
		acm.setAddCommentModelEnabled(true);
		String text = txtExportPath.getText();
		txtExportPath.setText(text.substring(0, text.lastIndexOf(Constants.SLASH) + 1));
	}
	
	private void exportExcelSelected() {
		((FilterModelExt) fme).setChangedOrNormalEnabled(true);
		((FilterModelExt) fme).setEmptyOrNormalEnabled(true);
		((FilterModelExt) fme).setChangedOrEmptyOrNormalEnabled(true);
		((FilterModelExt) fme).setChangedOrEmptyText(true);
		((FilterModelExt) fme).setDefaultSelection();
		acm.setAddCommentModelEnabled(false);
		acm.setEnabled(false);
		acm.clearSelection();
		String text = txtExportPath.getText();
		if (text.indexOf(Constant.EXPORT_CUSTOMER_XLS) < 0) {
			txtExportPath.setText(text + Constant.EXPORT_CUSTOMER_XLS);
		}
	}
	
	private void importProertiesSelected() {
		String text = txtImportPath.getText();
		txtImportPath.setText(text.substring(0, text.lastIndexOf(Constants.SLASH) + 1));
	}
	
	private void importExcelSelected() {
		String text = txtImportPath.getText();
		if (text.indexOf(Constant.IMPORT_CUSTOMER_XLS) < 0) {
			txtImportPath.setText(text + Constant.IMPORT_CUSTOMER_XLS);
		}
	}
}
