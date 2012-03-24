/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.entity;

import iceworld.fernado.entity.IEntity;


public class InputEntity implements IEntity {

	private static final long serialVersionUID = 1L;

	private String currentKey;
	private String originalKey;
	private String[] currentValues;
	private String[] originalValues;

	private String currentComment;
	private String originalComment;
	
	public String getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(final String currentKey) {
		this.currentKey = currentKey;
	}

	public String getOriginalKey() {
		return originalKey;
	}

	public void setOriginalKey(final String originalKey) {
		this.originalKey = originalKey;
	}

	public String[] getCurrentValues() {
		return currentValues;
	}

	public void setCurrentValues(final String[] currentValues) {
		this.currentValues = currentValues;
	}

	public String[] getOriginalValues() {
		return originalValues;
	}

	public void setOriginalValues(final String[] originalValues) {
		this.originalValues = originalValues;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrentComment() {
		return currentComment;
	}

	public void setCurrentComment(String currentComment) {
		this.currentComment = currentComment;
	}

	public String getOriginalComment() {
		return originalComment;
	}

	public void setOriginalComment(String originalComment) {
		this.originalComment = originalComment;
	}

	
}
