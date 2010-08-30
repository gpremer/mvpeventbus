package net.premereur.mvp.example.swing.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import net.premereur.mvp.core.View;

public class ApplicationFrame extends JFrame implements View {

	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 1L;
	
	public ApplicationFrame() {
		super("MVP Swing demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setMinimumSize(new Dimension(200, 400));
		
	}

}
