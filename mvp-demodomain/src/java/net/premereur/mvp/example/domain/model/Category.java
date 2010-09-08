package net.premereur.mvp.example.domain.model;

import java.util.UUID;

public class Category {
	private UUID id = UUID.randomUUID();
	private String name;

	public Category(String name) {
		super();
		this.name = name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {		
		return obj instanceof Category && ((Category)obj).getId().equals(getId());
	}

	@Override
	public int hashCode() {		
		return getId().hashCode();
	}
}
