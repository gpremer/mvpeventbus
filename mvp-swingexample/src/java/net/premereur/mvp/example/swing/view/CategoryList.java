package net.premereur.mvp.example.swing.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.premereur.mvp.core.View;

public class CategoryList implements View {
	public CategoryList() {
		init();
	}

	private void init() {
		JScrollPane pane = new JScrollPane();
		JTable table = new JTable();
		pane.add(table);
		table.setFillsViewportHeight(true);
		
	}
}
