/**
 * 
 * @author fernado
 * @date Jan 11, 2011
 */
package i18ntool.consts;

public enum Filter {
	
	NORMAL,
	
	/**
	 * key's value is NOT empty
	 */
	NOT_EMPTY_VALUE,
	
	/**
	 * key's value is NOT empty but old key's value is empty is OK
	 */
	NOT_EMPTY_VALUE_BUT_ALLOW_ORIGINAL_VALUE_EMPTY,
	
	CHANGED,
	
	EMPTY,
	
	CHANGED_OR_EMPTY,
	
	CHANGED_OR_NORMAL,
	
	EMPTY_OR_NORMAL,
	
	CHANGED_OR_EMPTY_OR_NORMAL,
	
	UNKNOWN
}
