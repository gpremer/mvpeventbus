package net.premereur.mvp.core.basic;

import static net.premereur.mvp.util.reflection.ReflectionUtil.uncheckedNewInstance;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.base.AbstractPresenterFactory;

/**
 * A {@link net.premereur.mvp.core.base.EventHandlerManager} that uses only standard J2SE libraries and has limited injection possibilities (only the view).
 * 
 * FIXME: This class does not handle multiple concurrent event busses.
 * 
 * @author gpremer
 * 
 */
public final class PresenterFactory extends AbstractPresenterFactory {

    private static final ViewFactory VIEW_FACTORY = new ViewFactory();

    @SuppressWarnings("unchecked")
    @Override
    protected net.premereur.mvp.core.Presenter<View, ? extends EventBus> createPresenter(final Class<?> presenterClass, final EventBus eventBus) {
        final Presenter handler = (Presenter) uncheckedNewInstance(presenterClass);
        handler.setEventBus(eventBus);
        handler.setView(VIEW_FACTORY.newView(presenterClass, handler));
        return handler;
    }
}
