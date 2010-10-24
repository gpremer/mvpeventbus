package net.premereur.mvp.core.basic;

import net.premereur.mvp.core.guice.IsInterfaceVerfier;
import net.premereur.mvp.core.verifiers.ConcreteClassVerifier;
import net.premereur.mvp.core.verifiers.HandlerVerifier;
import net.premereur.mvp.core.verifiers.HasDefaultConstructorVerifier;
import net.premereur.mvp.core.verifiers.HasNoPrimitiveAgrumentsVerifier;
import net.premereur.mvp.core.verifiers.HasUseViewAnnotationVerifier;
import net.premereur.mvp.core.verifiers.IsVoidMethodVerifier;
import net.premereur.mvp.core.verifiers.MethodVerifier;

/**
 * A verifier for the basic {@link net.premereur.mvp.core.EventBusFactory}. Verifies as much as possible at event bus construction time that the set up is
 * correct.
 * 
 * @author gpremer
 * 
 */
public class EventBusVerifier extends net.premereur.mvp.core.verifiers.EventBusVerifier {

    private static final HandlerVerifier[] HANDLER_VERIFIERS = {new HasDefaultConstructorVerifier(), new ConcreteClassVerifier(),
            new HasUseViewAnnotationVerifier()};
    private static final MethodVerifier[] METHOD_VERIFIERS = {new HasNoPrimitiveAgrumentsVerifier(), new IsVoidMethodVerifier()};
    private static final EventBusInterfaceVerifier[] EVENT_BUS_VERIFIERS = {new IsInterfaceVerfier()};

    /**
     * {@inheritDoc}
     */
    @Override
    protected HandlerVerifier[] getHandlerVerifiers() {
        return HANDLER_VERIFIERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MethodVerifier[] getMethodVerifiers() {
        return METHOD_VERIFIERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EventBusInterfaceVerifier[] getEventBusInterfaceVerifiers() {
        return EVENT_BUS_VERIFIERS;
    }
}
