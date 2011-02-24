package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collection;

import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.UsesView;

/**
 * Verifies that a presenter is annotated with a {@link UsesView} annotation.
 * 
 * @author gpremer
 * 
 */
public final class HasUseViewAnnotationVerifier implements HandlerVerifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> verify(final Class<? extends EventHandler> handlerClass) {
        final UsesView viewAnnot = handlerClass.getAnnotation(UsesView.class);
        if (viewAnnot == null || viewAnnot.value() == null) {
            return asList("Should use " + UsesView.class.getName() + " annotation to declare view class on " + handlerClass);
        }
        return emptyList();
    }

}
