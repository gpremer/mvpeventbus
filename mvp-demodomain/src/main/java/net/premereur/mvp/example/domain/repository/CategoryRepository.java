package net.premereur.mvp.example.domain.repository;

import java.util.List;

import net.premereur.mvp.example.domain.model.Category;

/**
 * Manages persistence of {@link Category} instances.
 * 
 * @author gpremer
 * 
 */
public interface CategoryRepository {

    /**
     * Lists all categories.
     * 
     * @return a List of categories
     */
    List<Category> allCategories();

    /**
     * Persists a category.
     * 
     * @param category the category to persist
     */
    void save(Category category);

}
