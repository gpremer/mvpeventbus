package net.premereur.mvp.core.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final WeakHashMap<EventBus, Map<Class<?>, List<Presenter<?, ?>>>> busHandlers = new WeakHashMap<EventBus, Map<Class<?>, List<Presenter<?, ?>>>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Presenter<?, ?>> getPresenters(final Class<?> presenterClass, final EventBus eventBus) {
        final Map<Class<?>, List<Presenter<?, ?>>> eventBusPresenters = presentersForBus(eventBus);
        List<Presenter<?, ?>> presenters = eventBusPresenters.get(presenterClass);
        if (presenters == null) {
            presenters = new ArrayList<Presenter<?, ?>>();
            presenters.add(createPresenter(presenterClass, eventBus));
            eventBusPresenters.put(presenterClass, presenters);
        }
        return presenters;
    }

    @Override
    public final Presenter<?, ?> getNewPresenter(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<Presenter<?, ?>>> eventBusPresenters = presentersForBus(eventBus);
        List<Presenter<?, ?>> presenters = eventBusPresenters.get(handlerClass);
        if (presenters == null) {
            presenters = new ArrayList<Presenter<?, ?>>();
            eventBusPresenters.put(handlerClass, presenters);
        }
        final Presenter<View, ? extends EventBus> newPresenter = createPresenter(handlerClass, eventBus);
        presenters.add(newPresenter);
        return newPresenter;
    }

    @Override
    public final List<Presenter<?, ?>> getExistingPresenters(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<Presenter<?, ?>>> eventBusPresenters = presentersForBus(eventBus);
        List<Presenter<?, ?>> presenters = eventBusPresenters.get(handlerClass);
        if (presenters == null) {
            presenters = Collections.emptyList();
        }
        return presenters;
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
        final Map<Class<?>, List<Presenter<?, ?>>> eventBusPresenters = presentersForBus(eventBus);
        final List<Presenter<?, ?>> presenters = eventBusPresenters.get(presenter.getClass());
        presenters.remove(presenter); // may leave an empty list
        if (presenters.isEmpty()) {
            // There should be no 2 threads requiring the same event bus, just to be
            // on the safe side
            synchronized (eventBus) {
                eventBusPresenters.put(presenter.getClass(), null);
            }
        }
    }

    private Map<Class<?>, List<Presenter<?, ?>>> presentersForBus(final EventBus eventBus) {
        synchronized (eventBus) {
            Map<Class<?>, List<Presenter<?, ?>>> eventBusPresenters = busHandlers.get(eventBus);
            if (eventBusPresenters == null) {
                eventBusPresenters = new HashMap<Class<?>, List<Presenter<?, ?>>>();
                busHandlers.put(eventBus, eventBusPresenters);
            }
            return eventBusPresenters;
        }
    }

}
