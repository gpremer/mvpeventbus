package net.premereur.mvp.core.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;

/**
 * Base class for {@link EventHandlerManager} implementations. It implements the machinery for managing presenters in the face of multiple concurrently used event
 * busses.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractPresenterFactory implements EventHandlerManager {

    // A WeakHashMap is chosen so that the event bus and all its associated presenters can be released when the event bus is no longer referenced elsewhere
    private final WeakHashMap<EventBus, Map<Class<?>, List<EventHandler>>> busHandlers = new WeakHashMap<EventBus, Map<Class<?>, List<EventHandler>>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<EventHandler> getEventHandlers(final Class<?> presenterClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusPresenters = presentersForBus(eventBus);
        List<EventHandler> presenters = eventBusPresenters.get(presenterClass);
        if (presenters == null) {
            presenters = new ArrayList<EventHandler>();
            presenters.add(createPresenter(presenterClass, eventBus));
            eventBusPresenters.put(presenterClass, presenters);
        }
        return presenters;
    }

    @Override
    public final EventHandler getNewEventHandler(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusPresenters = presentersForBus(eventBus);
        List<EventHandler> presenters = eventBusPresenters.get(handlerClass);
        if (presenters == null) {
            presenters = new ArrayList<EventHandler>();
            eventBusPresenters.put(handlerClass, presenters);
        }
        final EventHandler newPresenter = createPresenter(handlerClass, eventBus);
        presenters.add(newPresenter);
        return newPresenter;
    }

    @Override
    public final List<EventHandler> getExistingEventHandlers(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusPresenters = presentersForBus(eventBus);
        List<EventHandler> presenters = eventBusPresenters.get(handlerClass);
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
    protected abstract EventHandler createPresenter(Class<?> presenterClass, EventBus eventBus);

    @Override
    public final void detachPresenter(final EventHandler presenter, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusPresenters = presentersForBus(eventBus);
        final List<EventHandler> presenters = eventBusPresenters.get(presenter.getClass());
        presenters.remove(presenter); // may leave an empty list
        if (presenters.isEmpty()) {
            // There should be no 2 threads requiring the same event bus, just to be
            // on the safe side
            synchronized (eventBus) {
                eventBusPresenters.put(presenter.getClass(), null);
            }
        }
    }

    private Map<Class<?>, List<EventHandler>> presentersForBus(final EventBus eventBus) {
        synchronized (eventBus) {
            Map<Class<?>, List<EventHandler>> eventBusPresenters = busHandlers.get(eventBus);
            if (eventBusPresenters == null) {
                eventBusPresenters = new HashMap<Class<?>, List<EventHandler>>();
                busHandlers.put(eventBus, eventBusPresenters);
            }
            return eventBusPresenters;
        }
    }

}
