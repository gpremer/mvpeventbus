package net.premereur.mvp.core.basic;

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
