package net.premereur.mvp.example.swing.data;

import java.util.Arrays;
import java.util.List;

public class DataProvider {

	static public List<String> allCategories() {
		return Arrays.asList(new String[] { "Category 1", "Category 2", "Category 3", "Category 4" });
	}
}
