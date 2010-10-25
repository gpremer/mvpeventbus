package net.premereur.mvp.core.guice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusFactory;
import net.premereur.mvp.core.basic.PresenterFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * A factory for event busses that uses guice for dependency injection of the instances it creates.
 * 
 * @author gpremer
 * 
 */
public final class GuiceEventBusFactory extends AbstractEventBusFactory {

    final private Injector guiceInjector;
    final private EventBusModule eventBusModule;

    /**
     * Creates a GuiceEventBusFactory.
     * 
     * @param modules the modules containing configuration information for objects that need to be injected.
     */
    public GuiceEventBusFactory(Module... modules) {
        eventBusModule = new EventBusModule();
        List<Module> moduleList = new ArrayList<Module>(Arrays.asList(modules));
        moduleList.add(eventBusModule);
        guiceInjector = Guice.createInjector(moduleList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InvocationHandler createHandler(final Class<? extends EventBus>[] eventBusIntfs) {
        return new GuiceEventBusInvocationHandler(eventBusIntfs, guiceInjector.getInstance(PresenterFactory.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object createProxy(Class<? extends EventBus>[] eventBusIntfs, InvocationHandler handler) {
        Object proxy = Proxy.newProxyInstance(GuiceEventBusFactory.class.getClassLoader(), eventBusIntfs, handler);
        eventBusModule.setEventBus((EventBus) proxy);
        return proxy;
    }
}
