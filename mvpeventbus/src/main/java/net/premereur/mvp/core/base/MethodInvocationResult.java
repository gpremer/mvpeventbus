package net.premereur.mvp.core.base;

/**
 * Wrapper around an invocation result that also knows whether a invocation happened or not.
 * 
 * @author gpremer
 * 
 */
final class MethodInvocationResult {
    private final Object result;
    private final boolean handled;

    private static final MethodInvocationResult NO_INVOCATION = new MethodInvocationResult(null, false);

    public static MethodInvocationResult withResult(final Object result) {
        return new MethodInvocationResult(result, true);
    }

    public static MethodInvocationResult voidInvocation() {
        return new MethodInvocationResult(null, true);
    }

    public static MethodInvocationResult noInvocation() {
        return NO_INVOCATION;
    }

    private MethodInvocationResult(final Object result, final boolean handled) {
        this.result = result;
        this.handled = handled;
    }

    public Object getResult() {
        return result;
    }

    public boolean isHandled() {
        return handled;
    }
}
