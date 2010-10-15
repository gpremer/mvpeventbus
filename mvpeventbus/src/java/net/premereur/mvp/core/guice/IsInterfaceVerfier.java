package net.premereur.mvp.core.guice;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.Modifier;
import java.util.Collection;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.basic.EventBusInterfaceVerifier;


public class IsInterfaceVerfier implements EventBusInterfaceVerifier {

	@Override
	public Collection<String> verifyInterface(Class<? extends EventBus> eventBusClass) {
		int modifiers = eventBusClass.getModifiers();
		if (!Modifier.isInterface(modifiers)) {
			return asList("The handler " + eventBusClass + " has to be an interface");
		}
		return emptyList();
	}

}
