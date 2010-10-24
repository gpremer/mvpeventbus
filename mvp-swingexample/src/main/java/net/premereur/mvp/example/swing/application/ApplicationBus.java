package net.premereur.mvp.example.swing.application;

import javax.swing.JComponent;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

/**
 * Event bus segment for application-wide events and event related to the main canvas.
 * 
 * @author gpremer
 * 
 */
public interface ApplicationBus extends EventBus {

    /**
     * Sent when the application starts.
     */
    @Event(ApplicationPresenter.class)
    void applicationStarted();

    /**
     * Clears the main application screen.
     */
    @Event(ApplicationPresenter.class)
    void clearScreen();

    /**
     * Assigns the main application screen's left component.
     * 
     * @param component the component to place
     */
    @Event(ApplicationPresenter.class)
    void setLeftComponent(JComponent component);

    /**
     * Assigns the main application screen's center component.
     * 
     * @param component the component to place
     */
    @Event(ApplicationPresenter.class)
    void setCenterComponent(JComponent component);

    /**
     * Sets the text in the feedback area.
     * 
     * @param text the text to set
     */
    @Event(ApplicationPresenter.class)
    void setFeedback(String text);

}
