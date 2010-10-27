package net.premereur.mvp.example.domain.model;

import java.util.UUID;

/**
 * Base class for entities that have a UUID as their identifier.
 * 
 * @author gpremer
 * 
 */
public abstract class IdentifiableEntity {

    private UUID id = UUID.randomUUID();

    /**
     * Returns the identifier.
     */
    public final UUID getId() {
        return id;
    }

    // CHECKSTYLE IGNORE EXTENSION
    @Override
    public boolean equals(final Object obj) {
        // This assumes that id are truly unique, also over class boundaries
        return obj instanceof IdentifiableEntity && ((IdentifiableEntity) obj).getId().equals(getId());
    }

    // CHECKSTYLE IGNORE EXTENSION
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
