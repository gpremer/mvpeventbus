package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.basic.EventBusVerifier;
import net.premereur.mvp.core.verifiers.ConcreteClassVerifier;
import net.premereur.mvp.core.verifiers.HandlerVerifier;

public class GuiceEventBusVerifier extends EventBusVerifier {

    private static final HandlerVerifier[] HANDLER_VERIFIERS = { new ConcreteClassVerifier(), };

    protected HandlerVerifier[] getHandlerVerifiers() {
        return HANDLER_VERIFIERS;
    }

}
