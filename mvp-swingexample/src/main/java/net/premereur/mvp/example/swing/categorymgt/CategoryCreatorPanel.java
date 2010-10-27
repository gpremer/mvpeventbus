package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.View;

/**
 * A panel that allows creating new categories.
 * 
 * @author gpremer
 * 
 */
public class CategoryCreatorPanel extends CategoryPanelBase implements View {

    private static final long serialVersionUID = 1L;

    public CategoryCreatorPanel() {
        super("Make new category");
    }

    protected void init() {
    }

}
