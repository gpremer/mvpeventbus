package net.premereur.mvp.core;

public interface EventBusFactory {

    <E extends EventBus> E createEventBus(Class<E> mainEventBusIntf, Class<?>... segmentIntfs);

}
