MVP Event Bus
=============

Goal
----

The MVP Event Bus framework is used to let the Presenters in the Model-View-Presenter pattern communicate with each other in a loosely coupled fashion.

For an overview of why you could use the MVP pattern have a look at : [Wikipedia](http://en.wikipedia.org/wiki/Model-view-presenter).

For the rationale of using MVP in conjunction with an event bus see this Google IO 2009 [session](http://code.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html).

Overview
--------

### The EventBus

An event bus is a simple interface extending `EventBus` and containing methods marked with an `@Event` annotation. These methods represent the events that are sent over the event bus.

    interface DemoEventBus extends EventBus {
      @Event(ApplicationPresenter.class)
      void applicationStarted();

      @Event({MyPresenter.class, ApplicationPresenter.class})
      void prepareShutdown(User user);

      // Many more events ....
    }

Event methods are void methods that can take any number of parameters (of reference types), so there is no need to create special-purpose event classes.

### Creating event busses

Somewhere in the initialisation part of your application you request an instance of this interface:

    public void init() {
      EventBusFactory.createEventBus(DemoEventBus.class).applicationStarted();
    }

The `EventBusFactory` will create all `Presenter`s mentioned in the `Event` annotations the first time en event handled by the Presenter is sent. Once created, a presenter is kept alive, so it is safe to keep state within the presenter.

### Presenters

`Presenter`s implement the application logic and are simple classes that implement the `Presenter` interface. There's a convenience class `BasePresenter` that implements this interface and that you can extend from.

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

As can be observed, the Presenter has a handler method named after the event but starting with 'on'. The `EventBusFactory` injects the `View` specified in the `@UsesView` annotation. It also provides a reference to the event bus object used for delivering the event so that the Presenter can send new events to the bus.

### Views

Views are classes that interact with the UI framework you use in your application and implement the `View` marker interface.

    public class ApplicationFrame extends JFrame implements View {
      // view logic here
    }

It is suggested that UI-framework-specific methods call back to methods in the presenter that the View belongs to.

For a complete example, look at the included mvp-swingexample project.

## Acknowledgement

The interface of the framework was heavily inspired by the [mvp4g](http://code.google.com/p/mvp4g/) project.

Building
--------

The sources can be built using gradle 0.9-rc2. Assuming gradle 0.9 is in the path, invoking `gradle build` should build everything quite nicely.

All dependencies are packaged in the source tree. For now, the only run-time dependency is guice 2.0.