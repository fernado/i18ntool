package i18ntool.entity;

import iceworld.fernado.entity.IEntity;

public class ValueEntity implements IEntity {

	private static final long serialVersionUID = 1L;

	private String original;
	
	private String current;

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}
	
}
