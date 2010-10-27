package net.premereur.mvp.example.swing.application;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.support.ClickHandler;
import net.premereur.mvp.example.support.swing.ClickHandlerActionListener;

/**
 * The main application window.
 * 
 * @author gpremer
 * 
 */
public class ApplicationFrame extends JFrame implements View {

    private static final long serialVersionUID = 1L;

    private JMenu appMenu;
    private JMenuBar appMenuBar;
    private JSeparator appSep1;
    private JMenuItem catMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem productMenuItem;

    private JLabel feedbackLbl = new JLabel();
    private JComponent leftComponent;
    private JComponent centralComponent;

    public ApplicationFrame() {
        super("MVP Swing demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        initMenu();
        initStatusArea();
        setMinimumSize(new Dimension(600, 400)); // NOCS MAGIC 0
    }

    private void initMenu() {
        appMenuBar = new JMenuBar();
        appMenu = new JMenu();
        catMenuItem = new JMenuItem();
        productMenuItem = new JMenuItem();
        appSep1 = new JSeparator();
        exitMenuItem = new JMenuItem();

        appMenu.setMnemonic('a');
        appMenu.setText("Application");

        catMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK
                | java.awt.event.InputEvent.CTRL_MASK));
        catMenuItem.setMnemonic('c');
        catMenuItem.setText("Categories");
        appMenu.add(catMenuItem);

        productMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK
                | java.awt.event.InputEvent.CTRL_MASK));
        productMenuItem.setMnemonic('p');
        productMenuItem.setText("Products");
        appMenu.add(productMenuItem);

        appMenu.add(appSep1);

        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        appMenu.add(exitMenuItem);

        appMenuBar.add(appMenu);

        setJMenuBar(appMenuBar);
    }

    private void initStatusArea() {
        add(feedbackLbl, BorderLayout.PAGE_END);
    }

    public void setFeedback(final String text) {
        feedbackLbl.setText(text);
    }

    public synchronized void setLeftComponent(final JComponent component) {
        clearLeftComponent();
        leftComponent = component;
        getContentPane().add(component, BorderLayout.LINE_START);
        pack();
    }

    public synchronized void clearLeftComponent() {
        if (leftComponent != null) {
            getContentPane().remove(leftComponent);
            leftComponent = null;
        }
    }

    public synchronized void setCentralComponent(final JComponent component) {
        clearCentralComponent();
        centralComponent = component;
        getContentPane().add(component, BorderLayout.CENTER);
        pack();
    }

    public void clearCentralComponent() {
        if (centralComponent != null) {
            getContentPane().remove(centralComponent);
            centralComponent = null;
        }
    }

    public void setExitListener(final ClickHandler handler) {
        exitMenuItem.addActionListener(new ClickHandlerActionListener(handler));
    }

    public void setCategoryListener(final ClickHandler handler) {
        catMenuItem.addActionListener(new ClickHandlerActionListener(handler));
    }

    public void setProductListener(final ClickHandler handler) {
        productMenuItem.addActionListener(new ClickHandlerActionListener(handler));
    }

}
