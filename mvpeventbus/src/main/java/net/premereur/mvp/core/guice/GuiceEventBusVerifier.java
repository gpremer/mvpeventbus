package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.base.EventBusVerifier;
import net.premereur.mvp.core.verifiers.ConcreteClassVerifier;
import net.premereur.mvp.core.verifiers.EventBusInterfaceVerifier;
import net.premereur.mvp.core.verifiers.HandlerVerifier;
import net.premereur.mvp.core.verifiers.HasNoPrimitiveArgumentsVerifier;
import net.premereur.mvp.core.verifiers.IsInterfaceVerfier;
import net.premereur.mvp.core.verifiers.IsVoidMethodVerifier;
import net.premereur.mvp.core.verifiers.MethodVerifier;

/**
 * An {@link EventBusVerifier} for Guice-backed event busses.
 * 
 * @author gpremer
 * 
 */
public final class GuiceEventBusVerifier extends EventBusVerifier {

    private static final HandlerVerifier[] HANDLER_VERIFIERS = {new ConcreteClassVerifier()};
    private static final MethodVerifier[] METHOD_VERIFIERS = {new HasNoPrimitiveArgumentsVerifier(), new IsVoidMethodVerifier()};
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
