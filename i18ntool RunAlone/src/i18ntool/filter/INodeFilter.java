/**
 * 
 * @author fernado
 * @date Nov 12, 2010
 */
package i18ntool.filter;

public interface INodeFilter {
	
	/**
	 * Status.NORMAL
	 * @param element
	 * @return
	 */
	boolean isNormalLeaf(final Object element);
	/**
	 * Status.LOAD_NEED_COMPLETE || Status.SAVE_NEED_COMPLETE || Status.CHANGED
	 * @param element
	 * @return
	 */
	boolean isChangedOrEmptyLeaf(final Object element);
	/**
	 * Status.CHANGED
	 * @param element
	 * @return
	 */
	boolean isChangedLeaf(final Object element);
	/**
	 * Status.LOAD_NEED_COMPLETE || Status.SAVE_NEED_COMPLETE
	 * @param element
	 * @return
	 */
	boolean isEmptyLeaf(final Object element);
	
}
