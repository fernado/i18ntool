/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.consts;

public enum Status {

	/**
	 * load first, after saving, the value is empty 
	 */
	SAVE_NEED_COMPLETE,
	/**
	 * normal
	 */
	NORMAL, 
	/**
	 * load first, after saving, the value is not the same as loading
	 */
	CHANGED,
	/**
	 * After saving, changed and empty together
	 */
	SAVE_CHANGED_OR_EMPTY,
	
	FIND_OUT,
	/**
	 * when loading, the value is empty
	 */
	LOAD_NEED_COMPLETE
}
