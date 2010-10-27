package net.premereur.mvp.example.domain.repository;

import java.util.List;

import net.premereur.mvp.example.domain.model.Product;

/**
 * Manages persisting {@link Product}s.
 * 
 * @author gpremer
 * 
 */
public interface ProductRepository {

    /**
     * Lists all products.
     * 
     * @return a List of products.
     */
    List<Product> allProducts();

    /**
     * Looks up a product based on a prefix.
     * 
     * @param prefix the first characters of the product name
     * @return the matching products
     */
    List<Product> searchProducts(String prefix);

    /**
     * Persists a product.
     * 
     * @param product the product to save
     */
    void save(Product product);

}
