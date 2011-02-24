package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collection;

import net.premereur.mvp.core.EventHandler;

/**
 * Verifies that the class a constructor without arguments.
 * 
 * @author gpremer
 * 
 */
public final class HasDefaultConstructorVerifier implements HandlerVerifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> verify(final Class<? extends EventHandler> handlerClass) {
        try {
            handlerClass.getConstructor();
            return emptyList();
        } catch (final NoSuchMethodException e) {
            return asList("The handler " + handlerClass + " has to have a default constructor");
        }
    }

}
