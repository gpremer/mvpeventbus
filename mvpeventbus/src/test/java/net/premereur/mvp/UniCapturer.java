package net.premereur.mvp;

public class UniCapturer<T> {
    private T captured;

    public void capture(final T target) {
        this.captured = target;
    }

    public T getCaptured() {
        return captured;
    }

    public void reset() {
        this.captured = null;
    }

}
