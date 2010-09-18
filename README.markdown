MVP Event Bus
=============

Goal
----

The MVP Event Bus framework is used to let the Views and Presenters communicate with each other in a loosely coupled fashion.
For an overview of why you could use the MVP pattern have a look at : [WikiPedia](http://en.wikipedia.org/wiki/Model-view-presenter).
The rationale of using MVP in conjunction with an event bus see this [Google IO 2009 session](http://code.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html).

Overview
--------

### The EventBus

An eventbus is a simple interface extending `EventBus` and containing methods marked with an `@Event` annotation:

    interface DemoEventBus extends EventBus {
      @Event(ApplicationPresenter.class)
      void applicationStarted();

      @Event({MyPresenter.class, ApplicationPresenter.class})
      void prepareShutdown(User user);

      // Many more events ....
    }

Somewhere in the initialisation part of your application you then request an instance of this interface:

    public void init() {
      EventBusFactory.createEventBus(DemoEventBus.class).applicationStarted();
    }

The `EventBusFactory` will create all `Presenter`s mentioned in the `Event` annotations when (and only when) the event handled by the Presenter is sent. Once created, a presenter is kept alive, so it is safe to keep state within the presenter.

### Presenters

`Presenter`s implement the application logic and are simple classes that implement the `Presenter` interface or inherit from `BasePresenter`.

    @UsesView(ApplicationFrame.class)
    public class ApplicationPresenter extends BasePresenter<ApplicationFrame, DemoEventBus> {

      public void onApplicationStarted() {
        ApplicationFrame view = getView();

        getEventBus().categoryListActivated();
	view.pack();
	view.setVisible(true);
      }
   
      // Many more event handlers
    }

As can be observed, the Presenter has a handler method named after the event but starting with 'on'. The `EventBusFactory` injects the `View` specified in the `@UsesView` annotation. It also provides a reference to the event bus object used for delivering the event so that the Presenter can also send new events to the bus.

# Views

Views are classes that interact with the UI framework you use in your application and implement the `View` marker interface.

    public class ApplicationFrame extends JFrame implements View {
      // view logic here
    }

It is suggested that UI-framework-specific methods call back to methods in the presenter that the View belongs to.
