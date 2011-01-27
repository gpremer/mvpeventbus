package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.vaadin.categorymgt.CategoryMgtBus;

import com.google.inject.Inject;
import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * Handles the application-level events. A.o. it sets up the main window and handles application shutdown.
 * 
 * @author gpremer
 * 
 */
public final class ApplicationPresenter extends BasePresenter<ApplicationWindow, ApplicationBus> {

    /**
     * Creates an {@link ApplicationPresenter}.
     * 
     * @param eventBus the event bus the presenter should send to.
     * @param view the view the presenter should manage.
     */
    @Inject
    public ApplicationPresenter(final EventBus eventBus, final ApplicationWindow view) {
        super((ApplicationBus) eventBus, view);
    }

    /**
     * The application is initialising.
     * 
     * @param application the application instance we should add the view to.
     */
    public void onInit(final Application application) {
        ApplicationWindow main = getView();
        application.setMainWindow(main);
        addAppListener(application, main);
    }

    /**
     * The application (user session) is closing.
     * 
     * @param application the application instance for the user.
     */
    public void onClose(final Application application) {
        application.close();
    }

    /**
     * The user want to perform category management.
     */
    public void onSelectCategoryMgt() {
        getEventBus(CategoryMgtBus.class).activate(getView());
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

    /**
     * See {@link ApplicationBus#setWorkPane(ComponentContainer)}.
     * 
     * @param container the view to make central
     */
    public void onSetWorkPane(final ComponentContainer container) {
        getView().setWorkPane(container);
    }

    /**
     * See {@link ApplicationBus#showMessage(String)}.
     * 
     * @param message the message to display
     */
    public void onShowMessage(final String message) {
        getView().showNotification(message);
    }

    /**
     * See {@link ApplicationBus#setURIFragment(String)}.
     * @param fragment the new URI fragment
     */
    public void onSetURIFragment(final String fragment) {
       getView().setURIFragment(fragment); 
    }
}
