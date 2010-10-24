package net.premereur.mvp.example.domain.repository;

import java.util.List;

import net.premereur.mvp.example.domain.model.Category;

public interface CategoryRepository {

	public List<Category> allCategories();

	public void save(Category category);

}
