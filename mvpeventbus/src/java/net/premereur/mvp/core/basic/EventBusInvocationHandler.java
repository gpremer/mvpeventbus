/**
 * 
 */
package net.premereur.mvp.core.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.impl.EventBusVerifier;
import net.premereur.mvp.core.impl.EventMethodMapper;
import net.premereur.mvp.util.reflection.ReflectionUtil;

public class EventBusInvocationHandler implements InvocationHandler {
	private final EventMethodMapper methodMapper;

	private final PresenterFactory presenterFactory;

	private final EventBusVerifier verifier;

	private static final EventBusVerifier VERIFIER = new EventBusVerifier();

	protected EventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses, PresenterFactory presenterFactory, EventBusVerifier verifier) {
		this.presenterFactory = presenterFactory;
		this.verifier = verifier;
		methodMapper = new EventMethodMapper();
		for (Class<? extends EventBus> eventBusIntf : eventBusClasses) {
			verifier.verify(eventBusIntf);
			methodMapper.addHandlerMethods(ReflectionUtil.annotatedMethods(eventBusIntf, Event.class));
		}
	}

	public EventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses) {
		this(eventBusClasses, new PresenterFactory(), VERIFIER);
	}

	@Override
	public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
		for (EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
			Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
			handlerMethodPair.getMethod().invoke(handler, args);
		}
		return null;
	}

}