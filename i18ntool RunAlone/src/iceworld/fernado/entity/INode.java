/**
 * 
 * A node interface, all the concrete node will implements this one
 * 
 * @author fernado
 * @date Jan 14, 2011
 */
package iceworld.fernado.entity;


import iceworld.fernado.consts.Type;

import java.util.List;

public interface INode extends IEntity {

	String getName();
	
	Type getType();
	
	boolean hasChild(final String childName);
	
	boolean hasChild(final INode child);
	
	List<INode> getChildren();
	
	INode getChild(final String childName);
	
	void addChild(final INode node);
	
	INode getParent();
	
	void removeChild(final INode node);
}
