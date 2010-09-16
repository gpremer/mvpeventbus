package net.premereur.mvp.core.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

public class EventMethodMapper {

	private final Map<String, List<Method>> handlingMethodsByEventMethod = new HashMap<String, List<Method>>();

	public void addHandlerMethods(final Iterable<Method> eventBusEventMethods) {
		for (final Method eventMethod : eventBusEventMethods) {
			final Event eventAnt = eventMethod.getAnnotation(Event.class);
			final String eventMethodSignature = eventMethodSignature(eventMethod);
			final List<Method> handlingMethods = findHandlerMethodsForEvent(eventMethod, eventAnt);
			if (handlingMethodsByEventMethod.containsKey(eventMethodSignature)) {
				handlingMethodsByEventMethod.get(eventMethodSignature).addAll(handlingMethods);
			} else {
				handlingMethodsByEventMethod.put(eventMethodSignature, handlingMethods);
			}
		}
	}

	private static List<Method> findHandlerMethodsForEvent(final Method eventMethod, final Event eventAnt) {
		final List<Method> handlingMethods = new ArrayList<Method>();
		for (final Class<? extends Presenter<? extends View, ? extends EventBus>> presenter : eventAnt.value()) {
			handlingMethods.add(correspondingPresenterMethod(presenter, eventMethod));
		}
		return handlingMethods;
	}

	private static Method correspondingPresenterMethod(final Class<? extends Presenter<? extends View, ? extends EventBus>> presenter, final Method ebm) {
		for (final Method pm : presenter.getMethods()) {
			if (presenterMethodCorrespondsWithEventBusMethod(pm, ebm)) {
				return pm;
			}
		}
		throw new IllegalArgumentException("Did not find corresponding public event handler on " + presenter.getName() + " for event bus method "
				+ ebm.getName());
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
		if (name.startsWith("on") && name.length() > 2) {
			sb.append(name.substring(2, 3).toLowerCase()).append(name.substring(3, name.length()));
		} else {
			sb.append(name);
		}
	}

	private static final List<Method> NO_METHODS = Collections.emptyList();
	
	public Iterable<Method> getHandlerEvents(final Method eventMethod) {
		final Iterable<Method> methods = this.handlingMethodsByEventMethod.get(eventMethodSignature(eventMethod));
		return methods == null ? NO_METHODS : methods;
	}

}
