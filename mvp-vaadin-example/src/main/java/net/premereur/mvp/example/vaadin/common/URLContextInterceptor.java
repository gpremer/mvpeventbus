package net.premereur.mvp.example.vaadin.common;

import java.lang.reflect.Method;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.EventInterceptor;
import net.premereur.mvp.example.vaadin.app.ApplicationBus;

/**
 * Intercepts events that are marked as changing the URL. Set the URL fragment as specified in the annotation set on these events.
 * 
 * @author gpremer
 * 
 */
public class URLContextInterceptor implements EventInterceptor {

    @Override
    public boolean beforeEvent(final EventBus bus, final Method eventMethod, final Object[] args) {
        final URLContext urlAnnot = eventMethod.getAnnotation(URLContext.class);
        if (urlAnnot != null) {
            final String fragment = urlAnnot.value();
            System.out.println(fragment);
            ((ApplicationBus) bus).setURIFragment(fragment);
        }
        return PROCEED;
    }

}
