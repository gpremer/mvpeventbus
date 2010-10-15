package net.premereur.mvp.core.basic;

import java.util.Collection;

public class VerificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Collection<String> errors;

	public VerificationException(Collection<String> errors) {
		super("Eventbus contains configuration errors");
		this.errors = errors;
	}

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
