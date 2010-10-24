/**
 * 
 */
package net.premereur.mvp.core.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.util.reflection.ReflectionUtil;

public class EventBusInvocationHandler implements InvocationHandler {
    private final EventMethodMapper methodMapper;

    private final PresenterFactory presenterFactory;

    private static final EventBusVerifier VERIFIER = new EventBusVerifier();

    protected EventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final PresenterFactory presenterFactory,
            final EventBusVerifier verifier) {
        this.presenterFactory = presenterFactory;
        this.methodMapper = new EventMethodMapper();
        final Collection<String> verificationErrors = new ArrayList<String>();
        registerAllEventMethods(eventBusClasses, verifier, verificationErrors);
        throwVerificationIfNeeded(verificationErrors);
    }

    private void registerAllEventMethods(final Class<? extends EventBus>[] eventBusClasses, final EventBusVerifier verifier,
            final Collection<String> verificationErrors) {
        for (final Class<? extends EventBus> eventBusIntf : eventBusClasses) {
            verifier.verify(eventBusIntf, verificationErrors);
            methodMapper.addHandlerMethods(ReflectionUtil.annotatedMethods(eventBusIntf, Event.class), verificationErrors);
        }
    }

    protected void throwVerificationIfNeeded(final Collection<String> verificationErrors) {
        if (!verificationErrors.isEmpty()) {
            throw new VerificationException(verificationErrors);
        }
    }

    public EventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses) {
        this(eventBusClasses, new PresenterFactory(), VERIFIER);
    }

    @Override
    public Object invoke(final Object proxy, final Method eventMethod, final Object[] args) throws Throwable {
        for (final EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
            final Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
            final Method method = handlerMethodPair.getMethod();
            try {
                method.invoke(handler, args);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetException(e, "While invoking " + method.getName() + " on " + handler.getClass());
            }
        }
        return handleSpecialMethods(proxy, eventMethod, args);
    }

    protected Object handleSpecialMethods(Object proxy, Method method, Object[] args) {
        if (method.getName().equals("hashCode")) {
            return hashCode(); // The hash code of the handler
        }
        return null;
    }

}