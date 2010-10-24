package net.premereur.mvp.example.domain.model;

public class Category extends IdentifiableEntity {
	private String name;

	public Category(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
