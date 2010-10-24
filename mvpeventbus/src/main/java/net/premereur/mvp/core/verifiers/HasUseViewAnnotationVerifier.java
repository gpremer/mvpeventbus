package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collection;

import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.UsesView;

public class HasUseViewAnnotationVerifier implements HandlerVerifier {

    @Override
    public Collection<String> verify(@SuppressWarnings("unchecked")
    Class<? extends Presenter> handlerClass) {
        UsesView viewAnnot = handlerClass.getAnnotation(UsesView.class);
        if (viewAnnot == null || viewAnnot.value() == null) {
            return asList("Should use " + UsesView.class.getName() + " annotation to declare view class on " + handlerClass);
        }
        return emptyList();
    }

}
