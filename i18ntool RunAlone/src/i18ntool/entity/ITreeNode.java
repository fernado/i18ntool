/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.entity;


import i18ntool.listener.ITreeNodeChangeListener;
import iceworld.fernado.entity.INode;

public interface ITreeNode extends INode {

	void addTreeNodeChangeListener(final ITreeNodeChangeListener lister);

	void removeTreeNodeChangeListener(final ITreeNodeChangeListener lister);
	
}
