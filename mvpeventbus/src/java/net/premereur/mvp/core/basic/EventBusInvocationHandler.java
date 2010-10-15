/**
 * 
 */
package net.premereur.mvp.core.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.util.reflection.ReflectionUtil;

public class EventBusInvocationHandler implements InvocationHandler {
	private final EventMethodMapper methodMapper;

	private final PresenterFactory presenterFactory;

	private static final EventBusVerifier VERIFIER = new EventBusVerifier();

	protected EventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses, PresenterFactory presenterFactory, EventBusVerifier verifier) {
		this.presenterFactory = presenterFactory;
		this.methodMapper = new EventMethodMapper();
		final Collection<String> verificationErrors = new ArrayList<String>();
		for (Class<? extends EventBus> eventBusIntf : eventBusClasses) {
			verifier.verify(eventBusIntf, verificationErrors);
			methodMapper.addHandlerMethods(ReflectionUtil.annotatedMethods(eventBusIntf, Event.class), verificationErrors);
		}
		if (!verificationErrors.isEmpty()) {
			throw new VerificationException(verificationErrors);
		}
	}

	public EventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses) {
		this(eventBusClasses, new PresenterFactory(), VERIFIER);
	}

	@Override
	public Object invoke(final Object proxy, final Method eventMethod, final Object[] args) throws Throwable {
		for (EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
			final Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
			final Method method = handlerMethodPair.getMethod();
			try {
				System.out.println("Invoking " + method.getName() + " on " + handler.getClass());
				method.invoke(handler, args);
			} catch (InvocationTargetException e) {
				throw new InvocationTargetException(e, "While invoking " + method.getName() + " on " + handler.getClass());
			}
		}
		return null;
	}

}