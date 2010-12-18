package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Displays a left pane with a list of categories and a right pane that is dependent on the selection in the list.
 * 
 * @author gpremer
 * 
 */
public final class CategoryMgtView extends SplitPanel implements View {

    /**
     * class version.
     */
    private static final long serialVersionUID = 1L;
    private Table table;
    private CategoryDetailForm categoryForm = new CategoryDetailForm();

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
    }

    @SuppressWarnings("serial")
    void forwardCategorySelection(final CategoryMgtPresenter presenter) {
        table.setImmediate(true);
        table.setSelectable(true);
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(final ValueChangeEvent event) {
                presenter.selectCategory((Category) table.getValue());
            }
        });
    }

    void edit(final CategoryItem category) {
        setSecondComponent(categoryForm);
        categoryForm.setCategory(category);
    }

}
