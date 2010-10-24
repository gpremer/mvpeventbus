package net.premereur.mvp.example.domain.model;

import java.util.UUID;

public class IdentifiableEntity {

	private UUID id = UUID.randomUUID();

	public UUID getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		// This assumes that id are truly unique, also over class boundaries
		return obj instanceof IdentifiableEntity && ((IdentifiableEntity) obj).getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
