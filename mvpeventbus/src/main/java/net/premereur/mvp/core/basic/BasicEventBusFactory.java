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
public final class BasicEventBusFactory extends AbstractEventBusFactory {

    /**
     * {@inheritDoc}
     */
    protected Object createProxy(final Class<? extends EventBus>[] eventBusIntfs, final InvocationHandler handler) {
        return Proxy.newProxyInstance(BasicEventBusFactory.class.getClassLoader(), eventBusIntfs, handler);
    }

    /**
     * {@inheritDoc}
     */
    protected EventBusInvocationHandler createHandler(final Class<? extends EventBus>[] eventBusIntfs) {
        return new EventBusInvocationHandler(eventBusIntfs);
    }

}
