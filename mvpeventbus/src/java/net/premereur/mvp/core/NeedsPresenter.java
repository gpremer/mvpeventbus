package net.premereur.mvp.core;

public interface NeedsPresenter<P extends Presenter<? extends View, ?> > {

	void setPresenter(P p);
}
