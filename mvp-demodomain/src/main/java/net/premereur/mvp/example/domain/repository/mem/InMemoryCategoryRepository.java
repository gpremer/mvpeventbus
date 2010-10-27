package net.premereur.mvp.example.domain.repository.mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;

/**
 * In-memory implementation of the category repository.
 * 
 * @author gpremer
 * 
 */
public final class InMemoryCategoryRepository implements CategoryRepository {

    private final List<Category> categories = new ArrayList<Category>(Arrays.asList(new Category[] {new Category("Category 1"), new Category("Category 2"),
            new Category("Category 3"), new Category("Category 4")}));

    public InMemoryCategoryRepository() {

    }

    /**
     * {@inheritDoc}
     */
    public List<Category> allCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void save(final Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

}
