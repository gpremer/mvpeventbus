package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.Modifier;
import java.util.Collection;

import net.premereur.mvp.core.Presenter;

/**
 * Checks that a class is concrete, i.e. not an interface or abstract class.
 * 
 * @author gpremer
 * 
 */
public final class ConcreteClassVerifier implements HandlerVerifier {

    @Override
    public Collection<String> verify(@SuppressWarnings("unchecked")
    final Class<? extends Presenter> handlerClass) {
        int modifiers = handlerClass.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            return asList("The handler " + handlerClass + " has to be a concrete class");
        }
        return emptyList();
    }

}
