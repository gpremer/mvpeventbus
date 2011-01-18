package net.premereur.mvp.core.guice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.PresenterFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

/**
 * A Guice module that registers the {@link PresenterFactory}.
 * 
 * @author gpremer
 * 
 */
public final class EventBusModule extends AbstractModule {

    private final Set<Class<? extends EventBus>> eventBusIntfs;
    // private EventBus eventBus;

    private static final ThreadLocal<EventBus> EVENT_BUS_STORE = new ThreadLocal<EventBus>();

    @SuppressWarnings("unchecked")
    private final Provider eventBusProvider = new Provider<EventBus>() {
        @Override
        public EventBus get() {
            return EVENT_BUS_STORE.get();
        }
    };

    /**
     * Instantiates a Guice module that binds all known event bus types to the single event bus instance (for this module). We're clearly working around Guice
     * here. Better solutions are welcome.
     * 
     * @param eventBusIntfs all interfaces that need to be provided
     */
    public EventBusModule(final Collection<Class<? extends EventBus>> eventBusIntfs) {
        this.eventBusIntfs = new HashSet<Class<? extends EventBus>>(eventBusIntfs);
    }

    /**
     * Sets the event bus provider to return the given event bus instance for the current thread.
     * 
     * @param eventBus the event bus that should be in scope.
     */
    static void setThreadEventBus(final EventBus eventBus) {
        EVENT_BUS_STORE.set(eventBus);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        bind(PresenterFactory.class).to(GuicePresenterFactory.class);
        bind(EventBus.class).toProvider(eventBusProvider);
        for (final Class<? extends EventBus> busClass : eventBusIntfs) {
            bind(busClass).toProvider(eventBusProvider);
        }
    }

}
