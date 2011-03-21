package net.premereur.mvp.core.guice;

import java.util.LinkedList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.EventHandlerManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

/**
 * A Guice module that registers the {@link EventHandlerManager}.
 * 
 * @author gpremer
 * 
 */
public final class EventBusModule extends AbstractModule {

    private final Set<Class<? extends EventBus>> eventBusIntfs;

    private static final ThreadLocal<LinkedList<EventBus>> EVENT_BUS_STORE = new ThreadLocal<LinkedList<EventBus>>() {
        protected LinkedList<EventBus> initialValue() {
            return new LinkedList<EventBus>();
        };
    };

    @SuppressWarnings("unchecked")
    private final Provider eventBusProvider = new Provider<EventBus>() {
        @Override
        public EventBus get() {
            final LinkedList<EventBus> threadEventBus = EVENT_BUS_STORE.get();
            if (threadEventBus.isEmpty()) {
                throw new IllegalStateException("There is no event bus associated with the thread");
            }
            return threadEventBus.getLast(); // can't throw because we checked for emptiness
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
     * @param threadEventBus the event bus that should be in scope.
     */
    static void pushThreadEventBus(final EventBus threadEventBus) {
        if (threadEventBus == null) {
            throw new IllegalArgumentException("The event bus cannot be set to null");
        }
        EVENT_BUS_STORE.get().addLast(threadEventBus);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        bind(EventHandlerManager.class).to(GuiceEventHandlerFactory.class);
        bind(EventBus.class).toProvider(eventBusProvider);
        for (final Class<? extends EventBus> busClass : eventBusIntfs) {
            bind(busClass).toProvider(eventBusProvider);
        }
    }

    /**
     * Breaks the association of the current thread with an event bus instance.
     */
    static void popThreadEventBus() {
        EVENT_BUS_STORE.get().removeLast(); // will throw if there are no elements
    }

    /**
     * For unit testing: checks if there's an event bus associated with the current thread.
     */
    static boolean booleanHasThreadEventBus() {
        return !EVENT_BUS_STORE.get().isEmpty();
    }

}
