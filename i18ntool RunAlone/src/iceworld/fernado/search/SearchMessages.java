package iceworld.fernado.search;

import org.eclipse.osgi.util.NLS;

public final class SearchMessages extends NLS {

	private static final String BUNDLE_NAME = SearchMessages.class.getName();

	// Do not instantiate
	private SearchMessages() {
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, SearchMessages.class);
	}

	public static String SearchPage_search_btn_text;
	public static String SearchPage_cancel_btn_text;

	public static String SearchTypeEditor_typeDelimiter;

	public static String SearchPage_containingText_text;
	public static String SearchPage_caseSensitive;
	public static String SearchPage_containingText_hint;
	public static String SearchPage_regularExpression;

	public static String PatternConstructor_error_line_delim_position;
	public static String PatternConstructor_error_escape_sequence;
	public static String PatternConstructor_error_hex_escape_sequence;
	public static String PatternConstructor_error_unicode_escape_sequence;
	public static String PreviousSearchesDialog_title;
	public static String PreviousSearchesDialog_message;
}
