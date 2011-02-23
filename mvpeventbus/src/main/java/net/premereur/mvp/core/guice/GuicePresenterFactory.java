package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.base.AbstractPresenterFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * A {@link net.premereur.mvp.core.base.EventHandlerManager} that uses Guice to inject dependencies in the {@link Presenter}s it creates.
 * 
 * @author gpremer
 * 
 */
public final class GuicePresenterFactory extends AbstractPresenterFactory {

    private final Injector injector;

    /**
     * Creates a factory for Presenters that initialised by a Guice injector.
     * 
     * @param injector the Guice injector to resolve dependencies with
     */
    @Inject
    public GuicePresenterFactory(final Injector injector) {
        this.injector = injector;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Presenter<View, ? extends EventBus> createPresenter(final Class<?> presenterClass, final EventBus eventBus) {
        return (Presenter<View, ? extends EventBus>) injector.getInstance(presenterClass);
    }

}
