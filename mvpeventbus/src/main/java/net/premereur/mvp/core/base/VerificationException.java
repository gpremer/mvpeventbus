package net.premereur.mvp.core.base;

import java.util.Collection;

/**
 * A simple holder for static event bus validation errors. One exception can hold any number of textual error descriptions.
 * 
 * @author gpremer
 * 
 */
public final class VerificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Collection<String> errors;

    /**
     * Creates a new {@link VerificationException}.
     * 
     * @param errors textual error descriptions.
     */
    public VerificationException(final Collection<String> errors) {
        super("Eventbus contains configuration errors");
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        for (String errorMsg : errors) {
            sb.append("\n").append(errorMsg);
        }
        return sb.toString();
    }

}
