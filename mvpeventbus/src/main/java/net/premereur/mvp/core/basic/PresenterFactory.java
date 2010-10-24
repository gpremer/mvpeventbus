package net.premereur.mvp.core.basic;

import static net.premereur.mvp.util.reflection.ReflectionUtil.uncheckedNewInstance;

import java.util.HashMap;
import java.util.Map;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

public class PresenterFactory {

    private final Map<Class<?>, Presenter<View, ? extends EventBus>> handlerInstancesByClass = new HashMap<Class<?>, Presenter<View, ? extends EventBus>>();
    private static final ViewFactory viewFactory = new ViewFactory();

    public Presenter<View, ? extends EventBus> getPresenter(Class<?> presenterClass, EventBus eventBus) {
        return getHandlerInstance(presenterClass, eventBus);
    }

    private Presenter<View, ? extends EventBus> getHandlerInstance(Class<?> handlerClazz, EventBus eventBus) {
        synchronized (handlerInstancesByClass) {
            Presenter<View, ? extends EventBus> handlerInstance = (Presenter<View, ? extends EventBus>) handlerInstancesByClass.get(handlerClazz);
            if (handlerInstance == null) {
                handlerInstance = newHandler(handlerClazz, eventBus);
                handlerInstancesByClass.put(handlerClazz, handlerInstance);
            }
            return handlerInstance;
        }
    }

    @SuppressWarnings("unchecked")
    private static Presenter<View, ? extends EventBus> newHandler(Class<?> handlerClazz, EventBus eventBus) {
        Presenter handler = (Presenter) uncheckedNewInstance(handlerClazz);
        handler.setEventBus(eventBus);
        handler.setView(viewFactory.newView(handlerClazz, eventBus, handler));
        return handler;
    }

}
