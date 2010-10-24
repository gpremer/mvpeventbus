package net.premereur.mvp.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional annotation for {@link View}s that need their presenter injected.
 * 
 * @author gpremer
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UsesPresenter {

	Class<? extends Presenter<?, ?>> value();
}