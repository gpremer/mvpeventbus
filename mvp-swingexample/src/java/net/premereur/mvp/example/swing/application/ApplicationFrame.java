package net.premereur.mvp.example.swing.application;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.premereur.mvp.core.View;

public class ApplicationFrame extends JFrame implements View {

	private static final long serialVersionUID = 1L;

	private JLabel feedbackLbl = new JLabel();

	private JComponent leftComponent;

	private JComponent centralComponent;

	public ApplicationFrame() {
		super("MVP Swing demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(feedbackLbl, BorderLayout.PAGE_END);
		setMinimumSize(new Dimension(200, 400));
	}

	public void setFeedback(String text) {
		feedbackLbl.setText(text);
	}

	public synchronized void setLeftComponent(JComponent component) {
		if (leftComponent != null) {
			getContentPane().remove(leftComponent);
		}
		leftComponent = component;
		getContentPane().add(component, BorderLayout.LINE_START);
		pack();
	}

	public synchronized void setCentralComponent(JComponent component) {
		if (centralComponent != null) {
			getContentPane().remove(centralComponent);
		}
		centralComponent = component;
		getContentPane().add(component, BorderLayout.CENTER);
		pack();
	}

}
