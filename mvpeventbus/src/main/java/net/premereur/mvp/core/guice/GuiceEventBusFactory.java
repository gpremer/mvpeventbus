package net.premereur.mvp.core.guice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventBusFactory;
import net.premereur.mvp.core.basic.BasicEventBusFactory;
import net.premereur.mvp.core.basic.EventBusInvocationHandler;
import net.premereur.mvp.core.basic.PresenterFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceEventBusFactory extends BasicEventBusFactory implements EventBusFactory {

	final private Injector guiceInjector;
	final private EventBusModule eventBusModule;

	public GuiceEventBusFactory(Module... modules) {
		eventBusModule = new EventBusModule();
		List<Module> moduleList = new ArrayList<Module>(Arrays.asList(modules));
		moduleList.add(eventBusModule);
		guiceInjector = Guice.createInjector(moduleList);
	}
	
	@Override
	protected EventBusInvocationHandler createHandler(final Class<? extends EventBus>[] eventBusIntfs) {
		GuiceEventBusInvocationHandler handler = new GuiceEventBusInvocationHandler(eventBusIntfs, guiceInjector.getInstance(PresenterFactory.class));
		return handler;
	}
	
	@Override
	protected <E> Object createProxy(Class<E> mainEventBusIntf, Class<? extends EventBus>[] eventBusIntfs, EventBusInvocationHandler handler) {
		Object proxy = super.createProxy(mainEventBusIntf, eventBusIntfs, handler);
		eventBusModule.setEventBus((EventBus) proxy);
		return proxy;
	}
}
