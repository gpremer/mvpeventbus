package net.premereur.mvp.core.verifiers;

import java.util.Collection;

import net.premereur.mvp.core.Presenter;

/**
 * Verifies handler classes.
 * 
 * @author gpremer
 * 
 */
public interface HandlerVerifier {

    /**
     * Returns a collection of messages describing any verification failures.
     * 
     * @param handlerClass the class to examine.
     * @return a collection of failure messages. Can be empty.
     */
    Collection<String> verify(@SuppressWarnings("unchecked")
    Class<? extends Presenter> handlerClass);
}
