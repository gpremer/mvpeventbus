package net.premereur.mvp.example.swing.presenter;

import java.awt.BorderLayout;

import javax.swing.JComponent;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.ApplicationFrame;

@UsesView(ApplicationFrame.class)
public class ApplicationPresenter extends BasePresenter<ApplicationFrame, DemoEventBus> {	
	
	public void onApplicationStarted() {
		ApplicationFrame view = getView();

		getEventBus().categoryListActivated();
		view.pack();
		view.setVisible(true);
	}
	
	public void onSetLeftComponent(JComponent component) {
		getView().getContentPane().add(component, BorderLayout.LINE_START);
		getView().pack();
	}

	public void onSetCenterComponent(JComponent component) {
		getView().getContentPane().add(component, BorderLayout.CENTER);
		getView().pack();
	}

	public void onSetFeedback(String text) {
		getView().setFeedback(text);
		(new Thread() {
			@Override
			public void run() {
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					
				} finally {
					getView().setFeedback("");
				}
			}
		}).start();
	}

}
