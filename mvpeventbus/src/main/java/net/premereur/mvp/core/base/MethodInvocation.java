package net.premereur.mvp.core.base;

import java.lang.reflect.Method;

import net.premereur.mvp.core.EventBus;

final class MethodInvocation {

    private final EventBus eventBus;
    private final Method eventMethod;
    private final Object[] args;

    public MethodInvocation(final EventBus eventBus, final Method eventMethod, final Object[] args) {
        this.eventBus = eventBus;
        this.eventMethod = eventMethod;
        this.args = args;
    }

    /**
     * @return the eventBus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * @return the eventMethod
     */
    public Method getEventMethod() {
        return eventMethod;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

}
