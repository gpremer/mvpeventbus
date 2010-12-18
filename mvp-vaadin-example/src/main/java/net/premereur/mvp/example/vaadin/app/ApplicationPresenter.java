package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.vaadin.categorymgt.CategoryMgtBus;

import com.google.inject.Inject;
import com.vaadin.Application;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * Handles the application-level events. A.o. it sets up the main window and handles application shutdown.
 * 
 * @author gpremer
 * 
 */
public final class ApplicationPresenter extends BasePresenter<ApplicationWindow, ApplicationBus> {

    @Inject
    public ApplicationPresenter(final EventBus eventBus, final ApplicationWindow view) {
        super((ApplicationBus) eventBus, view);
    }

    public void onInit(final Application application) {
        ApplicationWindow main = getView();
        application.setMainWindow(main);
        addAppListener(application, main);
    }

    public void onClose(final Application application) {
        application.close();
    }

    public void onSelectCategoryMgt() {
        ((CategoryMgtBus) getEventBus()).activate(getView());
    }

    @SuppressWarnings("serial")
    private void addAppListener(final Application application, final ApplicationWindow main) {
        main.addListener(new Window.CloseListener() {

            @Override
            public void windowClose(final CloseEvent e) {
                getEventBus().close(application);
            }
        });
    }

}
