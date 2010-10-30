package net.premereur.mvp.example.swing.categorymgt;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.swing.application.ApplicationBus;

/**
 * The presenter for the {@link DefaultCategoryPanel} view.
 * 
 * @author gpremer
 * 
 */
@Singleton
public class DefaultCategoryPanelPresenter extends BasePresenter<DefaultCategoryPanel, CategoryMgtBus> {

    @Inject
    public DefaultCategoryPanelPresenter(final EventBus eventBus, final DefaultCategoryPanel view) {
        super((CategoryMgtBus) eventBus, view);
    }

    /**
     * See {@link CategoryMgtBus#noCategorySelected()}.
     */
    public final void onNoCategorySelected() {
        getView().setCreateButtonListener(this);
        getEventBus(ApplicationBus.class).setCenterComponent(getView());
    }

    /**
     * Converts the category create UI event to the corresponding bus event.
     */
    public final void createNewCategory() {
        getEventBus().activateCategoryCreation();
    }

}
