package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.Collection;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.verifiers.EventBusInterfaceVerifier;
import net.premereur.mvp.core.verifiers.HandlerVerifier;
import net.premereur.mvp.core.verifiers.MethodVerifier;
import net.premereur.mvp.util.reflection.ReflectionUtil;

/**
 * A verifier for the {@link net.premereur.mvp.core.EventBusFactory}s. Verifies as much as possible at event bus construction time that the set up is correct.
 * Uses the template method pattern.
 * 
 * @author gpremer
 * 
 */
public abstract class EventBusVerifier {

    /**
     * Verifies the given event bus recording the results in the supplied collection.
     * 
     * @param eventBusIntf the actual event bus class
     * @param verificationErrors to add errors to
     */
    public final void verify(final Class<? extends EventBus> eventBusIntf, final Collection<String> verificationErrors) {
        for (final Method eventMethod : ReflectionUtil.annotatedMethods(eventBusIntf, Event.class)) {
            final Event eventAnt = eventMethod.getAnnotation(Event.class);
            verifyHandlers(eventAnt.value(), verificationErrors);
            verifyEventBusMethods(eventMethod, verificationErrors);
        }
        this.verifyEventBus(eventBusIntf, verificationErrors);
    }

    private void verifyEventBus(final Class<? extends EventBus> eventBusClass, final Collection<String> verificationErrors) {
        for (final EventBusInterfaceVerifier verifier : getEventBusInterfaceVerifiers()) {
            verificationErrors.addAll(verifier.verifyInterface(eventBusClass));
        }
    }

    private void verifyHandlers(final Class<? extends EventHandler>[] handlers, final Collection<String> verificationErrors) {
        for (final Class<? extends EventHandler> handlerClass : handlers) {
            for (final HandlerVerifier verifier : getHandlerVerifiers()) {
                verificationErrors.addAll(verifier.verify(handlerClass));
            }
        }
    }

    private void verifyEventBusMethods(final Method m, final Collection<String> verificationErrors) {
        for (final MethodVerifier verifier : getMethodVerifiers()) {
            verificationErrors.addAll(verifier.verifyMethod(m));
        }
    }

    /**
     * The set of {@link HandlerVerifier}s this event bus verifier uses.
     * 
     * @return an array of {@link HandlerVerifier}s
     */
    protected abstract HandlerVerifier[] getHandlerVerifiers();

    /**
     * The set of {@link MethodVerifier}s this event bus verifier uses.
     * 
     * @return an array of {@link MethodVerifier}s
     */
    protected abstract MethodVerifier[] getMethodVerifiers();

    /**
     * The set of {@link EventBusInterfaceVerifier}s this event bus verifier uses.
     * 
     * @return an array of {@link EventBusInterfaceVerifier}s
     */
    protected abstract EventBusInterfaceVerifier[] getEventBusInterfaceVerifiers();
}
