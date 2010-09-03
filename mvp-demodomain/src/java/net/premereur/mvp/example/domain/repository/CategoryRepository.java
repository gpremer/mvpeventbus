package net.premereur.mvp.example.domain.repository;

import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.example.domain.model.Category;


public class CategoryRepository {

	public List<Category> allCategories() {
		return Arrays.asList(new Category[] { new Category("Category 1"), new Category("Category 2"), new Category("Category 3"), new Category("Category 4") });
	}
}
