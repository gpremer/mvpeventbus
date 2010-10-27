package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

public interface ProductMgtBus extends EventBus {

    @Event(ProductSearchPresenter.class)
    void productMgtActivated();

}
