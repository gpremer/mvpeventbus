package net.premereur.mvp.core.basic;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.View;

/**
 * A basic implementation of the {@link Presenter} interface that can be used by many presenter implementations.
 * 
 * @author gpremer
 * 
 * @param <V> The type of the view the presenter is managing
 * @param <E> The type of the event bus segment the presenter is mainly sending events to
 */
public abstract class BasePresenter<V extends View, E extends EventBus> implements Presenter<V, E> {

    private E eventBus;

    private V view;

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setEventBus(final E mainEventBusSegment) {
        this.eventBus = mainEventBusSegment;
    }

    /**
     * The event bus this presenter is associated with cast to the segment type specified as a generic type.
     * 
     * @return the event bus
     */
    protected final E getEventBus() {
        return eventBus;
    }

    /**
     * Returns the event bus associated with the presenter cast to the type given as an argument. This is meant to be used for composite busses that actually
     * use the requested bus segment.
     * 
     * @param <Bus> The type of the bus segment
     * @param eventBusClass The class of the type of the bus segment
     * @return the event bus
     */
    @SuppressWarnings("unchecked")
    protected final <Bus extends EventBus> Bus getEventBus(final Class<Bus> eventBusClass) {
        return (Bus) getEventBus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setView(final V managedView) {
        this.view = managedView;
    }

    /**
     * The view the presenter is managing.
     * 
     * @return the managed view
     */
    protected final V getView() {
        return view;
    }
}
