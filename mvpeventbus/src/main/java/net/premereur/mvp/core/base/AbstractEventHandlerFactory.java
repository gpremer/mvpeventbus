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
 * Base class for {@link EventHandlerManager} implementations. It implements the machinery for managing handlers in the face of multiple concurrently used event
 * busses.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractEventHandlerFactory implements EventHandlerManager {

    // A WeakHashMap is chosen so that the event bus and all its associated handlers can be released when the event bus is no longer referenced elsewhere
    private final WeakHashMap<EventBus, Map<Class<?>, List<EventHandler>>> busHandlers = new WeakHashMap<EventBus, Map<Class<?>, List<EventHandler>>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<EventHandler> getEventHandlers(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusHandlers = handlersForBus(eventBus);
        List<EventHandler> handlers = eventBusHandlers.get(handlerClass);
        if (handlers == null) {
            handlers = new ArrayList<EventHandler>();
            handlers.add(createEventHandler(handlerClass, eventBus));
            eventBusHandlers.put(handlerClass, handlers);
        }
        return handlers;
    }

    @Override
    public final EventHandler getNewEventHandler(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusHandlers = handlersForBus(eventBus);
        List<EventHandler> handlers = eventBusHandlers.get(handlerClass);
        if (handlers == null) {
            handlers = new ArrayList<EventHandler>();
            eventBusHandlers.put(handlerClass, handlers);
        }
        final EventHandler newHandler = createEventHandler(handlerClass, eventBus);
        handlers.add(newHandler);
        return newHandler;
    }

    @Override
    public final List<EventHandler> getExistingEventHandlers(final Class<?> handlerClass, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusHandlers = handlersForBus(eventBus);
        List<EventHandler> handlers = eventBusHandlers.get(handlerClass);
        if (handlers == null) {
            handlers = Collections.emptyList();
        }
        return handlers;
    }

    /**
     * Creates an actual instance of the handler class.
     * 
     * @param handlerClass the class for which an instance should be created.
     * @param eventBus the event bus for which the handler should operate.
     */
    protected abstract EventHandler createEventHandler(Class<?> handlerClass, EventBus eventBus);

    @Override
    public final void detachPresenter(final EventHandler handler, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusHandlers = handlersForBus(eventBus);
        final List<EventHandler> handlers = eventBusHandlers.get(handler.getClass());
        handlers.remove(handler); // may leave an empty list
        if (handlers.isEmpty()) {
            eventBusHandlers.put(handler.getClass(), null);
        }
    }

    @Override
    public final void attachPresenter(final EventHandler eventHandler, final EventBus eventBus) {
        final Map<Class<?>, List<EventHandler>> eventBusHandlers = handlersForBus(eventBus);
        final Class<? extends EventHandler> handlerClass = eventHandler.getClass();
        List<EventHandler> handlers = eventBusHandlers.get(handlerClass);
        if (handlers == null) {
            handlers = new ArrayList<EventHandler>();
            eventBusHandlers.put(handlerClass, handlers);
        }
        handlers.add(eventHandler);
    }

    private Map<Class<?>, List<EventHandler>> handlersForBus(final EventBus eventBus) {
        synchronized (eventBus) {
            Map<Class<?>, List<EventHandler>> eventBusHandlers = busHandlers.get(eventBus);
            if (eventBusHandlers == null) {
                eventBusHandlers = new HashMap<Class<?>, List<EventHandler>>();
                busHandlers.put(eventBus, eventBusHandlers);
            }
            return eventBusHandlers;
        }
    }

}
