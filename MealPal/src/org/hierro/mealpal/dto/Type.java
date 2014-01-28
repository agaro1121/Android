package org.hierro.mealpal.dto;

public class Type {
	private long id;
	private String type;
	
	public Type(long id, String type) {
		super();
		this.id = id;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
	
}
