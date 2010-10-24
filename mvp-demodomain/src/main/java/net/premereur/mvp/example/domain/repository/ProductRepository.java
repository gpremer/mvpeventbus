package net.premereur.mvp.example.domain.repository;

import java.util.List;

import net.premereur.mvp.example.domain.model.Product;

public interface ProductRepository {

	List<Product> allProducts();

	List<Product> searchProducts(String prefix);

	void save(Product Product);

}