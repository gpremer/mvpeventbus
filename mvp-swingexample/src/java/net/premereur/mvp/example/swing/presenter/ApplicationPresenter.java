package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.ApplicationFrame;

@UsesView(ApplicationFrame.class)
public class ApplicationPresenter extends BasePresenter<ApplicationFrame, DemoEventBus> {	
	
	public void onApplicationStarted() {
		ApplicationFrame view = getView();

		getEventBus().categoryListActivated(view);
		view.pack();
		view.setVisible(true);
	}

}
