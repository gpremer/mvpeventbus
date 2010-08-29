package net.premereur.mvp.example.swing.view;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.premereur.mvp.core.View;

public class ApplicationFrame extends JFrame implements View {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 1L;
	
	public ApplicationFrame() {
		super("MVP Swing demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add the ubiquitous "Hello World" label.
		JLabel label = new JLabel("Hello World");
		getContentPane().add(label);
	}

}
