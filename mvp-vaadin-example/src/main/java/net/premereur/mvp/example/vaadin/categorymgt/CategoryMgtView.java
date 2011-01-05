package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Displays a left pane with a list of categories and a right pane that is dependent on the selection in the list.
 * 
 * @author gpremer
 * 
 */
public class CategoryMgtView extends SplitPanel implements View {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNS = {"name"};
    private final Table table = new Table("Categories");
    private final CategoryDetailForm categoryForm = new CategoryDetailForm();

    public CategoryMgtView() {
        super(SplitPanel.ORIENTATION_HORIZONTAL);
        initLeft();
        initRight();
    }

    void setCategories(final Container categoryItems) {
        this.table.setContainerDataSource(categoryItems);
        this.table.setVisibleColumns(COLUMNS);
    }

    private void initLeft() {
        final VerticalLayout left = new VerticalLayout();
        setFirstComponent(left);
        left.addComponent(this.table);
        this.table.setWidth("100%");
    }

    private void initRight() {
    }

    @SuppressWarnings("serial")
    void forwardCategorySelection(final CategoryMgtPresenter presenter) {
        this.table.setImmediate(true);
        this.table.setSelectable(true);
        this.table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final Category item = (Category) CategoryMgtView.this.table.getValue();
                if (item == null) {
                    presenter.deselectCategory();
                } else {
                    presenter.selectCategory(item);
                }
            }
        });
    }

    void edit(final CategoryItem category) {
        setSecondComponent(this.categoryForm);
        this.categoryForm.setCategory(category);
    }

    @SuppressWarnings("serial")
    void forwardCategorySave(final CategoryMgtPresenter categoryMgtPresenter) {
        this.categoryForm.setSaveListener(new ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                commit(categoryMgtPresenter);
            }
        });
    }

    private void commit(final CategoryMgtPresenter presenter) {
        this.categoryForm.commit(); // write fields to bean
        presenter.saveCategory((CategoryItem) (this.categoryForm.getItemDataSource()));
    }

    @SuppressWarnings("unchecked")
    void refreshCategories(final Category category) {
        final BeanItemContainer<Category> ds = (BeanItemContainer<Category>) this.table.getContainerDataSource();
        final BeanItem<Category> item = ds.getItem(category);
        for (final String column : COLUMNS) {
            final Property itemProperty = item.getItemProperty(column);
            itemProperty.setValue(itemProperty.getValue());
        }
    }

}
