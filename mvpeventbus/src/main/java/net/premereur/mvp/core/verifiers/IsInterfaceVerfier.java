package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.Modifier;
import java.util.Collection;

import net.premereur.mvp.core.EventBus;

/**
 * Verifies that the given class type is an interface.
 * 
 * @author gpremer
 * 
 */
public final class IsInterfaceVerfier implements EventBusInterfaceVerifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> verifyInterface(final Class<? extends EventBus> eventBusClass) {
        int modifiers = eventBusClass.getModifiers();
        if (!Modifier.isInterface(modifiers)) {
            return asList("The handler " + eventBusClass + " has to be an interface");
        }
        return emptyList();
    }

}
