package net.premereur.mvp.example.swing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;

public class ApplicationFrame extends JFrame implements View<DemoEventBus> {

	private static final long serialVersionUID = 1L;

	private JLabel feedbackLbl = new JLabel();
	
	public ApplicationFrame() {
		super("MVP Swing demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(feedbackLbl, BorderLayout.PAGE_END);
		setMinimumSize(new Dimension(200, 400));		
	}

	@Override
	public void setEventBus(DemoEventBus eventBus) {		
	}
	
	public void setFeedback(String text) {
		feedbackLbl.setText(text);
	}

}
