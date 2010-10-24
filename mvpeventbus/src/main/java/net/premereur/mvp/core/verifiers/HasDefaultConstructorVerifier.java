package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collection;

import net.premereur.mvp.core.Presenter;

public class HasDefaultConstructorVerifier implements HandlerVerifier {

    @Override
    public Collection<String> verify(@SuppressWarnings("unchecked")
    Class<? extends Presenter> handlerClass) {
        try {
            handlerClass.getConstructor();
            return emptyList();
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return asList("The handler " + handlerClass + " has to have a default constructor");
    }

}
