package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.View;

import com.vaadin.data.Container;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class CategoryMgtView extends SplitPanel implements View {

	/**
	 * class version.
	 */
	private static final long serialVersionUID = 1L;
	private Table table;

	public CategoryMgtView() {
		super(SplitPanel.ORIENTATION_HORIZONTAL);
		initLeft();
		initRight();
	}

	void setCategories(final Container categoryItems) {		
		table.setContainerDataSource(categoryItems);
		table.setVisibleColumns(new String[] {"name"});
	}

	private void initLeft() {
		final VerticalLayout left = new VerticalLayout();
		setFirstComponent(left);
		table = new Table("Categories");
		left.addComponent(table);
		table.setWidth("100%");
	}

	private void initRight() {
		setSecondComponent(new Label("Second"));
	}
}
