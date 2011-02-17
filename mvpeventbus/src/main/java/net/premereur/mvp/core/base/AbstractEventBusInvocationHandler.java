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
import net.premereur.mvp.core.Presenter;
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

    private final Collection<EventInterceptor> interceptors;

    private static final Logger LOG = Logger.getLogger("net.premereur.mvp.core");

    private static final Method DETACH_METHOD;

    static {
        try {
            DETACH_METHOD = EventBus.class.getMethod("detach", Presenter.class);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Bug in " + AbstractEventBusInvocationHandler.class, e); // This is a bug
        }
    }

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param presenterFactory the factory to create presenter instances with
     * @param verifier the verifier the concrete InvocationHandler needs
     * @param interceptors The interceptors to call before the event dispatch
     */
    public AbstractEventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final PresenterFactory presenterFactory,
            final EventBusVerifier verifier, final Collection<EventInterceptor> interceptors) {
        this.presenterFactory = presenterFactory;
        this.interceptors = interceptors;
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
            LOG.fine("Receiving event " + methodName(eventMethod) + LogHelper.formatArguments(" with ", args));
        }
        prepareEventBusForCalling((EventBus) proxy);
        if (executeInterceptorChain((EventBus) proxy, eventMethod, args)) {
            dispatchEventToHandlers(proxy, eventMethod, args);
        }
        return null;
    }

    /**
     * Can be overridden to do necessary set up with the event bus before it can receive events.
     * 
     * @param eventBus the event bus that is to be prepared for calling.
     */
    protected void prepareEventBusForCalling(final EventBus eventBus) {
    }

    private boolean executeInterceptorChain(final EventBus bus, final Method eventMethod, final Object[] args) {
        for (final EventInterceptor interceptor : interceptors) {
            if (!interceptor.beforeEvent(bus, eventMethod, args)) {
                return false;
            }
        }
        return true;
    }

    private void dispatchEventToHandlers(final Object proxy, final Method eventMethod, final Object[] args) throws IllegalAccessException,
            InvocationTargetException {
        for (final EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
            final Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
            final Method method = handlerMethodPair.getMethod();
            try {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Dispatching to " + handler.getClass() + " -> " + methodName(method));
                }
                method.invoke(handler, args);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetException(e, "While invoking " + methodName(method) + " on " + handler.getClass());
            }
        }
    }

    private boolean isSpecialMethod(final Method method) {
        final String methodName = methodName(method);
        return methodName.equals("hashCode") || (method.equals(DETACH_METHOD));
    }

    private Object handleSpecialMethods(final Object proxy, final Method method, final Object[] args) {
        final String methodName = methodName(method);
        if (methodName.equals("hashCode")) {
            return hashCode(); // The hash code of the handler
        }
        if (method.equals(DETACH_METHOD)) {
            final Presenter<?, ?> presenter = (Presenter<?, ?>) args[0];
            presenterFactory.detachPresenter(presenter, (EventBus) proxy);
            return null; // void
        }
        return null;
    }

    /**
     * @param method
     * @return
     */
    private String methodName(final Method method) {
        return method.getName();
    }

}
