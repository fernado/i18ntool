/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.util;


import iceworld.fernado.search.SearchMessages;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Widget;

public class SearchTypeEditor extends SelectionAdapter implements DisposeListener {

	private Combo fTextField;

	private final static String TYPE_DELIMITER = SearchMessages.SearchTypeEditor_typeDelimiter;
	public final static String FILE_PATTERN_NEGATOR = "!";

	@Override
	public void widgetDisposed(DisposeEvent event) {
		Widget widget = event.widget;
		if (widget == fTextField)
			fTextField = null;
	}

	private static final Comparator<String> SEARCH_TYPES_COMPARATOR = new Comparator<String>() {
		public int compare(String fp1, String fp2) {
			boolean isNegative1 = fp1.startsWith(FILE_PATTERN_NEGATOR);
			boolean isNegative2 = fp2.startsWith(FILE_PATTERN_NEGATOR);
			if (isNegative1 != isNegative2) {
				return isNegative1 ? 1 : -1;
			}
			return fp1.compareTo(fp2);
		}
	};

	public SearchTypeEditor(Combo textField) {
		fTextField = textField;
		fTextField.addDisposeListener(this);
	}

	public String[] getSearchTypes() {
		Set<String> result = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer(fTextField.getText(), TYPE_DELIMITER);

		while (tokenizer.hasMoreTokens()) {
			String currentExtension = tokenizer.nextToken().trim();
			result.add(currentExtension);
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	public void setSearchTypes(String[] types) {
		fTextField.setText(typesToString(types));
	}

	public static String typesToString(String[] types) {
		Arrays.sort(types, SEARCH_TYPES_COMPARATOR);
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				result.append(TYPE_DELIMITER);
				result.append(" ");
			}
			result.append(types[i]);
		}
		return result.toString();
	}
}
