package net.premereur.mvp.core.verifiers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.Method;
import java.util.Collection;

public class IsVoidMethodVerifier implements MethodVerifier {

    @Override
    public Collection<String> verifyMethod(final Method m) {
        if (m.getReturnType().getName() != "void") {
            return asList("Method " + m.getName() + "of " + m.getDeclaringClass() + " has non-void return type");
        }
        return emptyList();
    }

}
