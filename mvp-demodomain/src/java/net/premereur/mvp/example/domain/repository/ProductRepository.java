package net.premereur.mvp.example.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.premereur.mvp.example.domain.model.Product;

public class ProductRepository {
	private static final ProductRepository instance = new ProductRepository();

	private final List<Product> products = new ArrayList<Product>(Arrays.asList(new Product[] { new Product("Product 1"), new Product("Product 2"),
			new Product("Product 3"), new Product("Product 4") }));

	public static ProductRepository instance() {
		return instance;
	}

	private ProductRepository() {

	}

	public List<Product> allProducts() {
		return Collections.unmodifiableList(products);
	}

	public void save(Product Product) {
		if (!products.contains(Product)) {
			products.add(Product);
		}
	}
}
