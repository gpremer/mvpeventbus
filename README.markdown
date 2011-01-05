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

The `EventBusFactory` will create all `Presenter`s mentioned in the `Event` annotations the first time en event handled by the Presenter is sent. Once created, a presenter is kept alive, so it is safe to keep state within the presenter. Future versions of the framework will allow explicit removal of the presenters and the associated view.

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

For a complete example, look at the included mvp-swingexample or mvp-vaadin-example projects.

## Extensions

On top of the base functionality, there are a number of interesting additions

### Event bus segments

For larger applications it does not make sense to have a single event bus interface that reference all presenters in the whole application. Therefore it is possible to split up the event bus in distinct segments. Each of the segments can then group related presenters. At run time, there is still a single "fysical" bus that has access to all events, but at design and compile time, the dependency graph is much simplified.

To work with event bus fragments, all you have to do is make separate event bus interfaces. These interfaces have no relationshop on each other. Only when requesting an event bus instance from the factory, are the fragments combined.

E.g.

   EVENT_BUS_FACTORY.createEventBus(ApplicationBus.class, CategoryMgtBus.class).init(this);

In this example `ApplicationBus` and `CategoryMgtBus` both are event bus interface each with their own set of presenters (the sets may overlap, although this will typically not be the case). The resulting event bus instance implements both interfaces. The declared type is the type of the first argument, in this case `CategoryMgtBus`, but the instance ccan be cast to any of the other interfaces. 

Since frequent casting in client code is not so nice, the `BasePresenter` has some syntactic sugar for this. The standard type of the event bus instance is declared through the generics type argument and the other segment types can accessed through a helper method `getEventBus(<segment interface>)` as in:

   /**
    * See {@link CategoryMgtBus#changedCategory(Category)}.
    */
   public void onChangedCategory(final Category category) {
     getView().refreshCategories(category);
     getEventBus(ApplicationBus.class).showMessage("Saved category");
   }

### Guice integration

To be elaborated.

## Acknowledgement

The interface of the framework was heavily inspired by the [mvp4g](http://code.google.com/p/mvp4g/) project.

Building
--------

The sources can be built using gradle 0.9. Assuming gradle 0.9 is in the path, invoking `gradle build` should build everything quite nicely.

The only run-time dependency on the core is guice 2.0. The vaadin example obviously requires [vaadin](http://www.vaadin.com/).