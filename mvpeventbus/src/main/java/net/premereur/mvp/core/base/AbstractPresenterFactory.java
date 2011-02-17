package net.premereur.mvp.core.base;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

/**
 * Base class for {@link PresenterFactory} implementations. It implements the machinery for managing presenters in the face of multiple concurrently used event
 * busses.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractPresenterFactory implements PresenterFactory {

    // A WeakHashMap is chosen so that the event bus and all its associated presenters can be released when the event bus is no longer referenced elsewhere
    private final WeakHashMap<EventBus, Map<Class<?>, Presenter<View, ? extends EventBus>>> cache = new WeakHashMap<EventBus, Map<Class<?>, Presenter<View, ? extends EventBus>>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final Presenter<View, ? extends EventBus> getPresenter(final Class<?> presenterClass, final EventBus eventBus) {
        // There should be no 2 threads requiring the same event bus, just to be
        // on the safe side
        final Map<Class<?>, Presenter<View, ? extends EventBus>> eventBusPresenters = presentersForBus(eventBus);
        Presenter<View, ? extends EventBus> presenter = eventBusPresenters.get(presenterClass);
        if (presenter == null) {
            presenter = createPresenter(presenterClass, eventBus);
            eventBusPresenters.put(presenterClass, presenter);
        }
        return presenter;
    }

    /**
     * Creates an actual instance of the presenter class.
     * 
     * @param presenterClass the class for which an instance should be created.
     * @param eventBus the event bus for which the presenter should operate.
     */
    protected abstract Presenter<View, ? extends EventBus> createPresenter(Class<?> presenterClass, EventBus eventBus);

    @Override
    public final void detachPresenter(final Presenter<?, ?> presenter, final EventBus eventBus) {
        final Map<Class<?>, Presenter<View, ? extends EventBus>> eventBusPresenters = presentersForBus(eventBus);
        eventBusPresenters.remove(presenter.getClass());
    }

    private Map<Class<?>, Presenter<View, ? extends EventBus>> presentersForBus(final EventBus eventBus) {
        synchronized (eventBus) {
            Map<Class<?>, Presenter<View, ? extends EventBus>> eventBusPresenters = cache.get(eventBus);
            if (eventBusPresenters == null) {
                eventBusPresenters = new HashMap<Class<?>, Presenter<View, ? extends EventBus>>();
                cache.put(eventBus, eventBusPresenters);
            }
            return eventBusPresenters;
        }
    }

}
