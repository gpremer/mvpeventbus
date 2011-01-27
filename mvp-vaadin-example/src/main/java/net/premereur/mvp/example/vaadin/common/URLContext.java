package net.premereur.mvp.example.vaadin.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an event method as changing the URL of the application. Useful for supporting the back button and book marking.
 * 
 * @author gpremer
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface URLContext {

    String value();

}
