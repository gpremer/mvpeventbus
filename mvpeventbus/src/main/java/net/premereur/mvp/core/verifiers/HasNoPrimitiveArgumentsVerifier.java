package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Verifies that a method has no primitive arguments.
 * 
 * @author gpremer
 * 
 */
public final class HasNoPrimitiveArgumentsVerifier implements MethodVerifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> verifyMethod(final Method m) {
        for (final Type t : m.getGenericParameterTypes()) {
            if (t instanceof Class<?> && ((Class<?>) t).isPrimitive()) {
                return asList("Method " + m.getName() + "of " + m.getDeclaringClass() + " has primitive arguments");
            }
        }
        return emptyList();
    }

}
