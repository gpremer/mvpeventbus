package net.premereur.mvp.example.domain.model;


public class Product extends IdentifiableEntity {
	private String name;

	public Product(String name) {
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
