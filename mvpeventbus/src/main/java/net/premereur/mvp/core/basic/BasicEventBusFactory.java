package net.premereur.mvp.core.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusFactory;

/**
 * A factory for event busses that uses only standard Java SE mechanisms.
 * 
 * @author gpremer
 * 
 */
public class BasicEventBusFactory extends AbstractEventBusFactory {

    /**
     * {@inheritDoc}
     */
    protected final Object createProxy(Class<? extends EventBus>[] eventBusIntfs, InvocationHandler handler) {
        return Proxy.newProxyInstance(BasicEventBusFactory.class.getClassLoader(), eventBusIntfs, handler);
    }

    /**
     * {@inheritDoc}
     */
    protected EventBusInvocationHandler createHandler(Class<? extends EventBus>[] eventBusIntfs) {
        return new EventBusInvocationHandler(eventBusIntfs);
    }

}
