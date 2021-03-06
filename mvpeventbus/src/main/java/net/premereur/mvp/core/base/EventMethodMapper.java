package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.Event.Instantiation;

/**
 * Maintains the connection between an event and the presenter methods that the event should be forwarded to.
 * 
 * @author gpremer
 * 
 */
public class EventMethodMapper {

    private static final int AFTER_PREFIX_POS = 2;

    /**
     * Combines a handler class with an event handler method within that handler. The class cannot be derived from the method because of possible inheritance.
     * 
     * @author gpremer
     * 
     */
    public static final class HandlerMethodPair {
        private final Class<?> handlerClass;
        private final Method method;

        HandlerMethodPair(final Class<?> presenterClass, final Method method) {
            this.handlerClass = presenterClass;
            this.method = method;
        }

        /**
         * Returns the presenter class.
         * 
         * @return a class of of presenters.
         */
        public Class<?> getHandlerClass() {
            return handlerClass;
        }

        /**
         * The event handling method.
         * 
         * @return a method.
         */
        public Method getMethod() {
            return method;
        }
    }

    private final Map<Method, List<HandlerMethodPair>>[] eventMethodsByEventMethodByPolicy;
    private final Set<Method> delayedEventmethods = new HashSet<Method>();
    private final Map<String, Method> firstMethodOfSignature;

    /**
     * Creates a new EventMethodMapper.
     */
    @SuppressWarnings("unchecked")
    public EventMethodMapper() {
        firstMethodOfSignature = new HashMap<String, Method>(); // Too bad the memory is retained after the last addHandlerMethods
        eventMethodsByEventMethodByPolicy = new HashMap[Event.Instantiation.values().length];
        for (Event.Instantiation policy : Event.Instantiation.values()) {
            eventMethodsByEventMethodByPolicy[policy.ordinal()] = new HashMap<Method, List<HandlerMethodPair>>();
        }
    }

    /**
     * Adds and verifies methods annotated with {@link Event} on an event bus.
     * 
     * @Event on an event bus interface.
     * @param eventBusEventMethods a number of event bus event methods. It is assumed that these methods are valid. I.e. this has to be checked before calling
     *            this method.
     * @param verificationErrors a collection to add possible verification errors to
     */
    public final void addHandlerMethods(final Iterable<Method> eventBusEventMethods, final Collection<String> verificationErrors) {
        for (final Method eventMethod : eventBusEventMethods) {
            final Event eventAnnot = eventMethod.getAnnotation(Event.class);
            final String eventMethodSignature = eventMethodSignature(eventMethod);
            final List<HandlerMethodPair> handlingMethods = createHandlerMethodPairsForEvent(eventMethod, eventAnnot, verificationErrors);
            registerHandlingMethods(eventMethod, eventMethodSignature, handlingMethods, eventMethodsByEventMethodByPolicy[eventAnnot.instantiation().ordinal()]);
            if (eventAnnot.invocation() == Event.Invocation.DELAYED) {
                registerDelayedEvent(eventMethod); // the same event a different bus may have a different invocation policy
            }
        }
    }

    private void registerHandlingMethods(final Method eventMethod, final String eventMethodSignature, final List<HandlerMethodPair> handlingMethods,
            final Map<Method, List<HandlerMethodPair>> handlingMethodsByEventMethod) {
        // The reason for this extra temporary list is that the same logical event on different event bus segments have different Method instances.
        // Therefore, we let all those events point to the same list (instance) that contains the handlers. This makes lookup fast and minimises memory usage.
        final List<HandlerMethodPair> allHandlingMethods;
        if (!firstMethodOfSignature.containsKey(eventMethodSignature)) { // the first time a method of this signature is seen?
            firstMethodOfSignature.put(eventMethodSignature, eventMethod);
            allHandlingMethods = handlingMethods;
        } else {
            final Method firstMethodWithSignature = firstMethodOfSignature.get(eventMethodSignature);
            allHandlingMethods = handlingMethodsByEventMethod.get(firstMethodWithSignature);
            allHandlingMethods.addAll(handlingMethods);
        }
        handlingMethodsByEventMethod.put(eventMethod, allHandlingMethods);
    }

    private void registerDelayedEvent(final Method eventMethod) {
        delayedEventmethods.add(eventMethod);
    }

    private static List<HandlerMethodPair> createHandlerMethodPairsForEvent(final Method eventMethod, final Event eventAnt,
            final Collection<String> verificationErrors) {
        final List<HandlerMethodPair> handlingMethods = new ArrayList<HandlerMethodPair>();
        for (final Class<? extends EventHandler> presenter : eventAnt.value()) {
            handlingMethods.add(new HandlerMethodPair(presenter, correspondingPresenterMethod(presenter, eventMethod, verificationErrors)));
        }
        return handlingMethods;
    }

    private static Method correspondingPresenterMethod(final Class<? extends EventHandler> handlerClass, final Method ebm,
            final Collection<String> verificationErrors) {
        for (final Method pm : handlerClass.getMethods()) {
            if (presenterMethodCorrespondsWithEventBusMethod(pm, ebm)) {
                return pm;
            }
        }
        verificationErrors.add("Did not find corresponding public event handler on " + handlerClass.getName() + " for event bus method " + ebm.getName());
        return null; // TODO change this so that a Method object is returned always
    }

    private static boolean presenterMethodCorrespondsWithEventBusMethod(final Method pm, final Method ebm) {
        return eventMethodSignature(ebm).equals(eventMethodSignature(pm));
    }

    private static String eventMethodSignature(final Method m) {
        final StringBuilder sb = new StringBuilder();
        addNameWithoutPrefix(sb, m.getName());
        sb.append('+');
        addArgumentTypes(m, sb);
        return sb.toString();
    }

    private static void addArgumentTypes(final Method m, final StringBuilder sb) {
        for (Type t : m.getGenericParameterTypes()) {
            sb.append(t.toString());
        }
    }

    private static void addNameWithoutPrefix(final StringBuilder sb, final String name) {
        if (name.startsWith("on") && name.length() > AFTER_PREFIX_POS) {
            sb.append(name.substring(AFTER_PREFIX_POS, AFTER_PREFIX_POS + 1).toLowerCase()).append(name.substring(AFTER_PREFIX_POS + 1, name.length()));
        } else {
            sb.append(name);
        }
    }

    private static final List<HandlerMethodPair> NO_METHODS = Collections.emptyList();

    /**
     * Returns all event handler methods (and the owing presenter classes) for the given event.
     * 
     * @param eventMethod an event bus event method
     * @param policy the instantiation policy
     * @return all matching event handler methods (and classes)
     */
    public final Iterable<HandlerMethodPair> getHandlerEvents(final Method eventMethod, final Instantiation policy) {
        final Iterable<HandlerMethodPair> methods = this.eventMethodsByEventMethodByPolicy[policy.ordinal()].get(eventMethod);
        return methods == null ? NO_METHODS : methods;
    }

    /**
     * Verifies whether an event bus method was declared as DELAYED.
     * 
     * @param eventMethod the event method to check
     */
    public final boolean isDelayedMethod(final Method eventMethod) {
        return delayedEventmethods.contains(eventMethod);
    }

}
