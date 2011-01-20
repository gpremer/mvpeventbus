package net.premereur.mvp.core.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.util.reflection.ReflectionUtil;

/**
 * Base class with common functionality for event bus invocation handlers.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractEventBusInvocationHandler implements InvocationHandler {
    private final EventMethodMapper methodMapper;

    private final PresenterFactory presenterFactory;

    private static final Logger LOG = Logger.getLogger("net.premereur.mvp.core");

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param presenterFactory the factory to create presenter instances with
     * @param verifier the verifier the concrete InvocationHandler needs
     */
    public AbstractEventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final PresenterFactory presenterFactory,
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

    private void throwVerificationIfNeeded(final Collection<String> verificationErrors) {
        if (!verificationErrors.isEmpty()) {
            throw new VerificationException(verificationErrors);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object invoke(final Object proxy, final Method eventMethod, final Object[] args) throws Throwable {
        if (isSpecialMethod(eventMethod)) {
            return handleSpecialMethods(proxy, eventMethod, args);
        }
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Receiving event " + eventMethod.getName() + LogHelper.formatArguments(" with ", args));
        }
        for (final EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
            final Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
            final Method method = handlerMethodPair.getMethod();
            try {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Dispatching to " + handler.getClass() + " -> " + method.getName());
                }
                method.invoke(handler, args);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetException(e, "While invoking " + method.getName() + " on " + handler.getClass());
            }
        }
        return null;
    }

    private boolean isSpecialMethod(final Method method) {
        return method.getName().equals("hashCode");
    }

    private Object handleSpecialMethods(final Object proxy, final Method method, final Object[] args) {
        if (method.getName().equals("hashCode")) {
            return hashCode(); // The hash code of the handler
        }
        return null;
    }

}
