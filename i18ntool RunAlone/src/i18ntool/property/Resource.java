/**
 * 
 * @author fernado
 * @date Jan 6, 2011
 */
package i18ntool.property;

import org.eclipse.osgi.util.NLS;

public final class Resource extends NLS {
	
	private static final String BUNDLE_NAME = Resource.class.getName();
		
	// Do not instantiate
	private Resource() {
	}
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Resource.class);
	}

	public static String EXPLORER;
	public static String SEARCH;
	public static String SHOW_EXPLORER_VIEW;
	public static String CLEAR_THE_CONTENT;
	public static String ENCODING;
	public static String NO_ENCODING;
	public static String LOAD_RESOURCE;
	public static String SAVE_RESOURCE;
	public static String PREFERENCE;
	public static String HELP;
	public static String FILE;
	public static String FOLDER;
	public static String BROWSER;
	public static String RESET;
	public static String IMPORT;
	public static String EXPORT;
	public static String IMPORT_PROPERTIES;
	public static String IMPORT_EXCEL;
	public static String EXPORT_PROPERTIES;
	public static String EXPORT_EXCEL;
	public static String SAVE_CONFIGURATION;
	public static String RESOURCE_LOCATOR;
	public static String IMPORT_PATH;
	public static String IMPORT_EXCEL_TO_PATH;
	public static String EXPORT_PATH;
	public static String EXPORT_EXCEL_TO_PATH;
	public static String CLEAR_ITEM;
	public static String CLEAR_ITEM_FROM_LIST;
	public static String ABOUT;
	public static String ADD_KEY;
	public static String ADD_KEY_TO_LIST;
	public static String DELETE_ITEM;
	public static String DELETE_ITEM_FROM_LIST;
	public static String CONFIRM;
	public static String SURE_TO_DELETE_KEY;
	public static String INVALID_KEY;
	public static String EXIST_KEY;
	public static String ADD_KEY_DESCRIPTION;
	public static String KEY_MUST_NOT_BLANK;
	public static String SEARCH_ITEMS_BY_VALUE;
	public static String SEARCH_ITEMS_BY_VALUE_DESCRIPTION;
	public static String VALUE;
	public static String CASE_SENSITIVE;
	public static String REGULAR_EXPRESSION;
	public static String SEARCH_ITEMS_BY_KEY;
	public static String SEARCH_ITEMS_BY_KEY_DESCRIPTION;
	public static String KEY;
	public static String OLD_KEY;
	public static String NEW_KEY;
	public static String RENAME_KEY;
	public static String RENAME_KEY_FROM_LIST;
	public static String RENAME_KEY_DESCRIPTION;
	public static String FILTER_ITEM;
	public static String FILTER_ITEM_DESCRIPTION;
	public static String ADD_A_LANGUAGE_AS_TEMPLATE;
	public static String USEFUL_FOR_CHANGED_OR_EMPTY_NODE;
	public static String THE_TEMPLATE_IS;
	public static String SHOW_NORMAL;
	public static String SHOW_CHANGED;
	public static String SHOW_EMPTY;
	public static String SHOW_CHANGED_OR_EMPTY;
	public static String SHOW_CHANGED_OR_EMPTY_IN_EXCEL;
	public static String SHOW_CHANGED_OR_NORMAL;
	public static String SHOW_CHANGED_OR_EMPTY_NORMAL;
	public static String SHOW_EMPTY_OR_NORMAL;
	public static String ERROR_ACTION_HAS_BEEN_DUMPED;
	public static String ERROR_AT_LEAST_ONE_ITEM_SELECTED;
	public static String ERROR_NO_EXIST_ITEM_FOR_EXPORT;
	public static String ERROR_NO_PROPERTY_FILE_EXIST;
	public static String ERROR_NO_EXCEL_FILE_EXIST;
	
	public static String SHOW_DO_NOT_TYPE_ENTER_KEY_AND_USE_R_N;
}