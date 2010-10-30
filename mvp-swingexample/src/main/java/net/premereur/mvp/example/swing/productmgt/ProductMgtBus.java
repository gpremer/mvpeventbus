package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

/**
 * Event bus segment for presenters and views related to Product management.
 * 
 * @author gpremer
 * 
 */
public interface ProductMgtBus extends EventBus {

    /**
     * Signals that the product management module should be activated.
     */
    @Event(ProductSearchPresenter.class)
    void productMgtActivated();

}
