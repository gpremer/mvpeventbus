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

    /**
     * Verifies an event bus segment.
     * 
     * @param eventBusClass the segment interface.
     * @return a collection of validation failure messages. Can be empty.
     */
    Collection<String> verifyInterface(Class<? extends EventBus> eventBusClass);
}
