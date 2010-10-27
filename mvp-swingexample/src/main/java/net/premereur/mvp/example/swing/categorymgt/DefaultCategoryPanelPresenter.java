package net.premereur.mvp.example.swing.categorymgt;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.swing.application.ApplicationBus;

@Singleton
public class DefaultCategoryPanelPresenter extends BasePresenter<DefaultCategoryPanel, CategoryMgtBus> {

    @Inject
    public DefaultCategoryPanelPresenter(final EventBus eventBus, final DefaultCategoryPanel view) {
        super((CategoryMgtBus) eventBus, view);
    }

    public final void onDefaultCategoryPanelActivated() {
        getView().setCreateButtonListener(this);
        getEventBus(ApplicationBus.class).setCenterComponent(getView());
    }

    public final void createNewCategory() {
        getEventBus().activateCategoryCreation();
    }

}
