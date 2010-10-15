package net.premereur.mvp.core.verifiers;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Verifies handler methods.
 * 
 * @author gpremer
 * 
 */
public interface MethodVerifier {

	/**
	 * Returns a collection of messages describing any verification failures.
	 * @param m the method to examine.
	 * @return a collection of failure messages. Can be empty.
	 */
	Collection<String> verifyMethod(Method m);
}
