package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.base.AbstractEventHandlerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * A {@link net.premereur.mvp.core.base.EventHandlerManager} that uses Guice to inject dependencies in the {@link EventHandler}s it creates.
 * 
 * @author gpremer
 * 
 */
public final class GuiceEventHandlerFactory extends AbstractEventHandlerFactory {

    private final Injector injector;

    /**
     * Creates a factory for Presenters that initialised by a Guice injector.
     * 
     * @param injector the Guice injector to resolve dependencies with
     */
    @Inject
    public GuiceEventHandlerFactory(final Injector injector) {
        this.injector = injector;
    }

    @Override
    protected EventHandler createEventHandler(final Class<?> presenterClass, final EventBus eventBus) {
        return (EventHandler) injector.getInstance(presenterClass);
    }

}
