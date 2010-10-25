package net.premereur.mvp.core.verifiers;

import java.util.Collection;

import net.premereur.mvp.core.EventBus;

/**
 * Statically verifies event bus interfaces.
 * 
 * @author gpremer
 * 
 */
public interface EventBusInterfaceVerifier {

    Collection<String> verifyInterface(Class<? extends EventBus> eventBusClass);
}
