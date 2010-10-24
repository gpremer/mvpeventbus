package net.premereur.mvp.example.domain.model;

import java.util.Comparator;

public class Product extends IdentifiableEntity {
	public static final Comparator<Product> NAME_COMPARATOR = new Comparator<Product>() {

		@Override
		public int compare(Product o1, Product o2) {
			if (o1 == null) {
				return o2 == null ? -1 : 1;
			}
			return o1.getName().compareTo(o2.getName());
		}

	};
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
