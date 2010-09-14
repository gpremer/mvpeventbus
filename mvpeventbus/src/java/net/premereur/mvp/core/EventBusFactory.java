package net.premereur.mvp.core;

import static net.premereur.reflection.util.ReflectionUtil.uncheckedNewInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.premereur.mvp.core.impl.EventBusVerifier;
import net.premereur.reflection.util.ReflectionUtil;

public class EventBusFactory {

	@SuppressWarnings("unchecked")
	static public <E extends EventBus> E createEventBus(Class<E> clazz) {
		InvocationHandler handler = new EventBusInvocationHandler(clazz);
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
	}

	private static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final Map<Class<?>, Object> handlerInstancesByClass = new HashMap<Class<?>, Object>();

		private final Map<Method, List<Method>> handlingMethodsByEventMethod = new HashMap<Method, List<Method>>();

		private static final EventBusVerifier VERIFIER = new EventBusVerifier();

		public EventBusInvocationHandler(Class<? extends EventBus> eventBusClass) {
			VERIFIER.verify(eventBusClass);
			for (Method eventMethod : ReflectionUtil.annotatedMethods(eventBusClass, Event.class)) {
				Event eventAnt = eventMethod.getAnnotation(Event.class);
				List<Method> handlingMethods = findHandlerMethodsForEvent(eventMethod, eventAnt);
				handlingMethodsByEventMethod.put(eventMethod, handlingMethods);
			}
		}

		private static List<Method> findHandlerMethodsForEvent(Method eventMethod, Event eventAnt) {
			List<Method> handlingMethods = new ArrayList<Method>();
			for (Class<? extends Presenter<? extends View, ? extends EventBus>> presenter : eventAnt.handlers()) {
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
			return getEventMethodId(ebm).equals(getEventMethodId(pm));
		}

		private static String getEventMethodId(final Method m) {
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

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
			for (Method handlerMethod : handlingMethodsByEventMethod.get(eventMethod)) {
				Object handler = getHandlerInstance(handlerMethod.getDeclaringClass(), (EventBus) proxy);
				handlerMethod.invoke(handler, args);
			}
			return null;
		}

		private Object getHandlerInstance(Class<?> handlerClazz, EventBus bus) {
			synchronized (handlerInstancesByClass) {
				Object handlerInstance = handlerInstancesByClass.get(handlerClazz);
				if (handlerInstance == null) {
					handlerInstance = newHandler(handlerClazz, bus);
					handlerInstancesByClass.put(handlerClazz, handlerInstance);
				}
				return handlerInstance;
			}
		}

		@SuppressWarnings("unchecked")
		private static Presenter newHandler(Class<?> handlerClazz, EventBus bus) {
			Presenter handler = (Presenter) uncheckedNewInstance(handlerClazz);
			handler.setEventBus(bus);
			handler.setView(newView(handlerClazz, bus, handler));
			return handler;
		}

		@SuppressWarnings("unchecked")
		private static View newView(Class<?> handlerClazz, EventBus bus, Presenter presenter) {
			View view = uncheckedNewInstance(getViewClass(handlerClazz));
			setPresenterIfRequested(view, presenter);
			return view;
		}

		@SuppressWarnings("unchecked")
		private static void setPresenterIfRequested(View view, Presenter presenter) {
			if (view instanceof NeedsPresenter<?>) {
				((NeedsPresenter) view).setPresenter(presenter);
			}
		}

		private static Class<? extends View> getViewClass(Class<?> handlerClazz) {
			return handlerClazz.getAnnotation(UsesView.class).value();
		}

	}
}
