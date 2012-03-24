/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.datas;

import i18ntool.util.ResourceHandler;
import iceworld.fernado.entity.INode;

public class Data {

	public static INode createData() {
		return ResourceHandler.getInstance().getNode();
	}
	
	public static INode createSearchData() {
		return ResourceHandler.getInstance().getSearchResultRoot();
	}
}
