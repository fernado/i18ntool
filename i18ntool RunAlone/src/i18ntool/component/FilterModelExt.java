/**
 * 
 * @author fernado
 * @date Jan 11, 2011
 */
package i18ntool.component;

import i18ntool.consts.Filter;
import i18ntool.property.Resource;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class FilterModelExt extends FilterModel {
	private Button rdChangedOrNormal;
	private Button rdEmptyOrNormal;
	private Button rdChangedOrEmptyOrNormal;
	private boolean flagExport;
	
	/**
	 * the constructor will init the Controls, and add listener to them
	 * @param composite
	 * @param value
	 */
	public FilterModelExt(final Composite parent, final boolean flagExport) {
		super(parent);
		this.flagExport = flagExport;
		init();
		initEnabled();
	}
	
	private void init() {
		Composite composite = getComposite();
		rdChangedOrNormal = new Button(composite, SWT.RADIO);
		rdChangedOrNormal.setLayoutData(composite.getLayoutData());
		rdChangedOrNormal.setText(Resource.SHOW_CHANGED_OR_NORMAL);
		
		rdEmptyOrNormal = new Button(composite, SWT.RADIO);
		rdEmptyOrNormal.setLayoutData(composite.getLayoutData());
		rdEmptyOrNormal.setText(Resource.SHOW_EMPTY_OR_NORMAL);
		
		rdChangedOrEmptyOrNormal = new Button(composite, SWT.RADIO);
		rdChangedOrEmptyOrNormal.setLayoutData(composite.getLayoutData());
		rdChangedOrEmptyOrNormal.setText(Resource.SHOW_CHANGED_OR_EMPTY_NORMAL);
		
		setChangedOrEmptyText(true);
	}
	
	/**
	 * enabled or disabled for the Button(SWT.RADIO)
	 */
	private void initEnabled() {
		if (flagExport) {
			rdEmptyOrNormal.setEnabled(true);
			rdChangedOrNormal.setEnabled(true);
			rdChangedOrEmptyOrNormal.setEnabled(true);
		} else {
			rdEmptyOrNormal.setEnabled(false);
			rdChangedOrNormal.setEnabled(false);
			rdChangedOrEmptyOrNormal.setEnabled(false);
		}
	}

	public void setChangedOrNormalEnabled(final boolean enabled) {
		rdChangedOrNormal.setEnabled(enabled);
	}
	
	public void setEmptyOrNormalEnabled(final boolean enabled) {
		rdEmptyOrNormal.setEnabled(enabled);
	}
	
	public void setChangedOrEmptyOrNormalEnabled(final boolean enabled) {
		rdChangedOrEmptyOrNormal.setEnabled(enabled);
	}
	
	/**
	 * Note: DO NOT INVOKE the getValue() method,
	 * getValue() method is it's parent method.
	 * 
	 * get which one you selected
	 * Normal
	 * Changed
	 * Empty
	 * Changed/Empty
	 * Changed/Normal
	 * Empty/Normal
	 * Changed/Empty/Normal
	 */
	public Filter getValue() {
		Filter filter = super.getValue();
		if (Filter.UNKNOWN != filter) {
			return filter;
		} else if (rdChangedOrNormal.getSelection()) {
			return Filter.CHANGED_OR_NORMAL;
		} else if (rdEmptyOrNormal.getSelection()) {
			return Filter.EMPTY_OR_NORMAL;
		} else if (rdChangedOrEmptyOrNormal.getSelection()) {
			return Filter.CHANGED_OR_EMPTY_OR_NORMAL;
		}
		return Filter.UNKNOWN;
	}
}
