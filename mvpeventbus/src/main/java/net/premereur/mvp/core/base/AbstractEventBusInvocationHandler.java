package net.premereur.mvp.core.base;

import static net.premereur.mvp.core.base.MethodInvocationResult.noInvocation;
import static net.premereur.mvp.core.base.MethodInvocationResult.voidInvocation;
import static net.premereur.mvp.core.base.MethodInvocationResult.withResult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.util.reflection.ReflectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class with common functionality for event bus invocation handlers.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractEventBusInvocationHandler implements InvocationHandler {
    private final EventMethodMapper methodMapper;

    private final EventHandlerManager handlerManager;

    private final Collection<EventInterceptor> interceptors;

    private final ThreadLocal<Queue<MethodInvocation>> delayedMethodQueue = new ThreadLocal<Queue<MethodInvocation>>() {
        protected Queue<MethodInvocation> initialValue() {
            return new ConcurrentLinkedQueue<MethodInvocation>();
        }
    };
    private final ThreadLocal<Boolean> isInvoking = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        };
    };

    private static final HandlerFetchStrategy DISPATCH_HANDLER_FETCH_STRATEGY;
    private static final HandlerFetchStrategy CREATE_HANDLER_FETCH_STRATEGY;
    private static final HandlerFetchStrategy EXISTING_HANDLER_FETCH_STRATEGY;

    private static final Logger LOG = LoggerFactory.getLogger("net.premereur.mvp.core");

    private static final Method DETACH_METHOD;
    private static final Method ATTACH_METHOD;

    static {
        DISPATCH_HANDLER_FETCH_STRATEGY = new DispatchHandlerFetchStrategy();
        CREATE_HANDLER_FETCH_STRATEGY = new CreateHandlerFetchStrategy();
        EXISTING_HANDLER_FETCH_STRATEGY = new ExistingHandlerFetchStrategy();
        try {
            DETACH_METHOD = EventBus.class.getMethod("detach", EventHandler.class);
            ATTACH_METHOD = EventBus.class.getMethod("attach", EventHandler.class);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Bug in " + AbstractEventBusInvocationHandler.class, e); // This is a bug
        }
    }

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param handlerMgr the factory to create handler instances with
     * @param verifier the verifier the concrete InvocationHandler needs
     * @param interceptors The interceptors to call before the event dispatch
     */
    public AbstractEventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final EventHandlerManager handlerMgr,
            final EventBusVerifier verifier, final Collection<EventInterceptor> interceptors) {
        this.handlerManager = handlerMgr;
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

    @Override
    public final Object invoke(final Object proxy, final Method eventMethod, final Object[] args) throws Throwable {
        final EventBus eventBus = (EventBus) proxy;
        final MethodInvocationResult specialMethodResult = handleSpecialMethods(eventBus, eventMethod, args);
        if (specialMethodResult.isHandled()) {
            return specialMethodResult.getResult();
        }
        if (needsToBeDelayed(eventMethod)) {
            delayMethod(eventBus, eventMethod, args);
        } else {
            handleEventMethod(eventMethod, args, eventBus);
            handleDelayedMethods();
        }
        return null; // event handler methods are void methods
    }

    private void delayMethod(final EventBus eventBus, final Method eventMethod, final Object[] args) {
        LOG.debug("Delaying event {}", methodName(eventMethod));
        delayedMethodQueue.get().add(new MethodInvocation(eventBus, eventMethod, args));
    }

    private boolean needsToBeDelayed(final Method eventMethod) {
        return methodMapper.isDelayedMethod(eventMethod) && isInvokingEvent();
    }

    private void handleEventMethod(final Method eventMethod, final Object[] args, final EventBus eventBus) throws IllegalAccessException,
            InvocationTargetException {
        prepareEventBusForCalling(eventBus); // To give the Guice implementation a chance of setting a "current" event bus
        setInvokingEvent(true);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing event {} {}", methodName(eventMethod), LogHelper.formatArguments(" with ", args));
            }
            if (executeInterceptorChain(eventBus, eventMethod, args)) {
                dispatchEventToHandlers(eventBus, eventMethod, args, DISPATCH_HANDLER_FETCH_STRATEGY);
                dispatchEventToHandlers(eventBus, eventMethod, args, CREATE_HANDLER_FETCH_STRATEGY);
                dispatchEventToHandlers(eventBus, eventMethod, args, EXISTING_HANDLER_FETCH_STRATEGY);
            }
        } finally {
            setInvokingEvent(false);
            unprepareEventBusForCalling(eventBus);
        }
    }

    private void setInvokingEvent(final boolean invoking) {
        isInvoking.set(invoking);
    }

    private boolean isInvokingEvent() {
        return isInvoking.get();
    }

    private void handleDelayedMethods() throws Throwable {
        while (!delayedMethodQueue.get().isEmpty()) {
            final MethodInvocation invocation = delayedMethodQueue.get().poll();
            invoke(invocation.getEventBus(), invocation.getEventMethod(), invocation.getArgs());
        }
    }

    /**
     * Can be overridden to do necessary tear down with the event bus after it has finished receiving events.
     * 
     * @param eventBus the event bus that is to be closed for calling.
     */
    protected void unprepareEventBusForCalling(final EventBus eventBus) {
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

    private void dispatchEventToHandlers(final EventBus eventBus, final Method eventMethod, final Object[] args, final HandlerFetchStrategy handlerFetchStrategy)
            throws IllegalAccessException, InvocationTargetException {
        for (final EventMethodMapper.HandlerMethodPair handlerMethodPair : handlerFetchStrategy.getHandlerMethodPairs(methodMapper, eventMethod)) {
            final Iterable<EventHandler> handlers = handlerFetchStrategy.getHandlers(handlerManager, handlerMethodPair.getHandlerClass(), eventBus);
            final Method method = handlerMethodPair.getMethod();
            for (EventHandler handler : handlers) {
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Dispatching to {} -> {}", handler.getClass(), methodName(method));
                    }
                    method.invoke(handler, args);
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e, "While invoking " + methodName(method) + " on " + handler.getClass());
                }
            }
        }
    }

    private MethodInvocationResult handleSpecialMethods(final EventBus eventBus, final Method method, final Object[] args) {
        final MethodInvocationResult stdJavaResult = handleStandardJavaMethods(eventBus, method, args);
        if (stdJavaResult.isHandled()) {
            return stdJavaResult;
        }
        return handleBuiltInMethods(eventBus, method, args);
    }

    private MethodInvocationResult handleStandardJavaMethods(final EventBus eventBus, final Method method, final Object[] args) {
        final String methodName = methodName(method);
        if (args == null && methodName.equals("hashCode")) {
            return withResult(hashCode()); // The hash code of the handler
        }
        if (args != null && args.length == 1 && methodName.equals("equals")) {
            return withResult(eventBus == args[0]);
        }
        if (args == null && methodName.equals("toString")) {
            return withResult("EventBus[0x" + Integer.toHexString(hashCode()) + "]");
        }
        return noInvocation();
    }

    private MethodInvocationResult handleBuiltInMethods(final EventBus eventBus, final Method method, final Object[] args) {
        if (method.equals(DETACH_METHOD)) {
            final EventHandler handler = (EventHandler) args[0];
            handlerManager.detachPresenter(handler, eventBus);
            return voidInvocation();
        }
        if (method.equals(ATTACH_METHOD)) {
            final EventHandler handler = (EventHandler) args[0];
            handlerManager.attachPresenter(handler, eventBus);
            return voidInvocation();
        }
        return noInvocation();
    }

    private String methodName(final Method method) {
        return method.getName();
    }

}
