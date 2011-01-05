package net.premereur.mvp.example.vaadin.categorymgt;

import java.util.Arrays;

import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Allows creating new Categories and updating existing ones.
 * 
 * @author gpremer
 * 
 */
public final class CategoryDetailForm extends Form {

    private static final long serialVersionUID = 1L;
    private final Button commitBtn = new Button("Save");
    private final Button cancelBtn = new Button("Cancel");

    public CategoryDetailForm() {
        setWriteThrough(false);
        setCaption("Category mgt");
        setUpButtons();
    }

    @SuppressWarnings("serial")
    private void setUpButtons() {
        getFooter().addComponent(commitBtn);
        getFooter().addComponent(cancelBtn);
        final Form form = this;
        cancelBtn.addListener(new ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                form.discard();
            }
        });
    }

    void setSaveListener(final ClickListener listener) {
        commitBtn.addListener(listener);
    }
    
    /**
     * Sets the category that's going be edited.
     * 
     * @param categoryItem
     */
    void setCategory(final CategoryItem categoryItem) {
        setItemDataSource(categoryItem);
        setVisibleItemProperties(Arrays.asList("name"));
    }

}
