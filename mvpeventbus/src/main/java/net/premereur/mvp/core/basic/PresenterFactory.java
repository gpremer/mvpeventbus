package net.premereur.mvp.core.basic;

import static net.premereur.mvp.util.reflection.ReflectionUtil.uncheckedNewInstance;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.base.AbstractEventHandlerFactory;

/**
 * A {@link net.premereur.mvp.core.base.EventHandlerManager} that uses only standard J2SE libraries and has limited injection possibilities (only the view).
 * 
 * FIXME: This class does not handle multiple concurrent event busses.
 * 
 * @author gpremer
 * 
 */
public final class PresenterFactory extends AbstractEventHandlerFactory {

    private static final ViewFactory VIEW_FACTORY = new ViewFactory();

    @SuppressWarnings("unchecked")
    @Override
    protected EventHandler createEventHandler(final Class<?> presenterClass, final EventBus eventBus) {
        final EventHandler handler = (EventHandler) uncheckedNewInstance(presenterClass);
        if (handler instanceof Presenter) {
            Presenter presenter = (Presenter) handler;
            presenter.setEventBus(eventBus);
            presenter.setView(VIEW_FACTORY.newView(presenterClass, presenter));
        }
        return handler;
    }
}
