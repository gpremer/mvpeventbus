package net.premereur.mvp.example.domain.model;

/**
 * A category groups products. It is characterised by a name.
 * 
 * @author gpremer
 * 
 */
public class Category extends IdentifiableEntity {
    private String name;

    /**
     * Creates a new category with its name.
     * 
     * @param name the name of the category
     */
    public Category(final String name) {
        super();
        this.name = name;
    }

    /**
     * Returns the name of the category.
     * 
     */
    public final String getName() {
        return name;
    }

    /**
     * Changes the name of a category.
     * 
     * @param name the new name
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
