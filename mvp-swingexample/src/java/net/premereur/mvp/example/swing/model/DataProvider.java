package net.premereur.mvp.example.swing.model;

import java.util.Arrays;
import java.util.List;

public class DataProvider {

	static public List<Category> allCategories() {
		return Arrays.asList(new Category[] { new Category("Category 1"), new Category("Category 2"), new Category("Category 3"), new Category("Category 4") });
	}
}
