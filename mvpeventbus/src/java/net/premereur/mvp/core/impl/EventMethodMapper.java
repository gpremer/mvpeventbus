package net.premereur.mvp.core.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

public class EventMethodMapper {

	private final Map<Method, List<Method>> handlingMethodsByEventMethod = new HashMap<Method, List<Method>>();

	public EventMethodMapper(Iterable<Method> eventBusEventMethods) {
		for (Method eventMethod : eventBusEventMethods) {
			Event eventAnt = eventMethod.getAnnotation(Event.class);
			List<Method> handlingMethods = findHandlerMethodsForEvent(eventMethod, eventAnt);
			handlingMethodsByEventMethod.put(eventMethod, handlingMethods);
		}
	}

	private static List<Method> findHandlerMethodsForEvent(Method eventMethod, Event eventAnt) {
		List<Method> handlingMethods = new ArrayList<Method>();
		for (Class<? extends Presenter<? extends View, ? extends EventBus>> presenter : eventAnt.value()) {
			handlingMethods.add(correspondingPresenterMethod(presenter, eventMethod));
		}
		return handlingMethods;
	}

	private static Method correspondingPresenterMethod(Class<? extends Presenter<? extends View, ? extends EventBus>> presenter, Method ebm) {
		for (Method pm : presenter.getMethods()) {
			if (presenterMethodCorrespondsWithEventBusMethod(pm, ebm)) {
				return pm;
			}
		}
		throw new IllegalArgumentException("Did not find corresponding public event handler on " + presenter.getName() + " for event bus method "
				+ ebm.getName());
	}

	private static boolean presenterMethodCorrespondsWithEventBusMethod(Method pm, Method ebm) {
		return getEventMethodSignature(ebm).equals(getEventMethodSignature(pm));
	}

	private static String getEventMethodSignature(final Method m) {
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

	public Iterable<Method> getHandlerEvents(Method eventMethod) {
		return this.handlingMethodsByEventMethod.get(eventMethod);
	}

}
