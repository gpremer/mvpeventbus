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

Event methods are void methods that can take any number of parameters (of reference types), so there is no need to create special-purpose event classes. Sending an event is the same as calling an method annotated with `@Event`. The advantage of this approach is that it is not necessary to write a dedicated, repetitive, `Event` class for any event you want to send over the bus.

### Creating event busses

Somewhere in the initialisation part of your application you request an object implementing the interface you have defined:

    public void init() {
      final EventBusFactory<DemoEventBus> factory = BasicEventBusFactory.withMainType(DemoEventBus.class).build()
      final DemoEventBus eventBus = factory.create();
    }

First you build a factory object based on the specification of what types should be implemented and then you ask this factory to build an bus instance. This instance is automagically generated using dynamic proxies, so you don't have to do anything besides defining the interface. In a stand-alone application, e.g. using Swing, you  likely have one factory instance and one bus instance. In a web application on the other hand, you'll have one factory and one bus for every user session.


### Presenters

The `EventBusFactory` will create all `Presenter`s mentioned in the `Event` annotations the first time an event handled by the presenter is sent. After the first request, a presenter is stored for subsequent requests. Obviously, all event bus instances have their own instance of all presenters so that different user sessions can have different state.

The role of the presenter is to provide the application logic. Presenters have to implement the `Presenter` tagging interface. There's a convenience class `BasePresenter` that implements this interface and that you can extend from.

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

As can be observed, the Presenter has a handler method named after the event but starting with 'on'. The `EventBusFactory` injects the `View` specified in the `@UsesView` annotation. It also provides a reference to the event bus object used for delivering the event so that the Presenter can send new events to the bus. There's also a Guice-enabled version of the event bus factory that avoids the need for a base class and the `UsesView` annotation. See the section on Guice support for that.

As said, once created a presenter is kept alive, so it is safe to store state within the presenter. However, when the event bus instance that forwards events to the presenter becomes unreferenced (e.g. the user's session terminates), all associated presenters are removed as well.  Future versions of the framework may explicit removal of the presenters and the associated view.

### Views

Views are classes that interact with the UI framework you use in your application and implement the `View` marker interface.

    public class ApplicationFrame extends JFrame implements View {
      // view logic here
    }

It is suggested that UI-framework-specific methods call back to methods in the presenter that the View belongs to. The is to have as little logic as possible in the view and as much as possible in the presenter. The view should only contain the code that requires a run-time environment to test. The presenter should contain everything that can be tested a as unit test. In practice, it depends on the amount of test coverage you aim for. If you're not unit testing every last bit of your application, it may be convenient to shift a bit more reponsability to the view.

For a complete example, look at the included mvp-swingexample or mvp-vaadin-example projects.

## Extensions

On top of the base functionality, there are a number of interesting additions.

### Event bus segment types

For larger applications it doesn't make sense to have a single event bus interface containing all events and all presenters for the whole application. Therefore, it is possible to get different views of the whole event bus that only show a number of, likely related, events. These parts are called "segments", but keep in mind that there is only one event bus that handles all events. Events are not local to a certain segment. Still, this concept allows modularising applications at design time.

To work with event bus segments, all you have to do is make separate event bus interfaces. These interfaces have no relationship to each other when coding. Only when configuring an event bus factory at run time, are the fragments combined.

E.g.

    BasicEventBusFactory.withSegments(MainEventBus.class, ChainedEventBus.class, OtherEventBus.class)
                        .build()

In this example `MainEventBus`, `ChainedEventBus` and `OtherEventBus` are event bus interfaces each with their own set of presenters (the sets may overlap, although this will typically not be the case). The resulting event bus instance implements both interfaces. The declared type of any event bus instance created from this factory is the type of the first argument, in this case `MainEventBus`, but the instance can be cast to any of the other interfaces. 

Since frequent casting in client code doesn't look very nice, the `BasePresenter` has some syntactic sugar for this. The standard type of the event bus instance is declared through the generics type argument. The other segment types can accessed through a helper method `getEventBus(<segment interface>)` as in:

    /**
     * See {@link CategoryMgtBus#changedCategory(Category)}.
     */
    public void onChangedCategory(final Category category) {
      getView().refreshCategories(category);
      getEventBus(ApplicationBus.class).showMessage("Saved category");
    }

This example also demostrates a suggested way of documenting event methods. The `@link` Javadoc tag, and the `@Event` annotation allow navigating both ways between event definition and handler.   

### Guice integration

The basic event bus factory can only create event busses that inject the event bus interfaces and the view into the presenter. `GuiceEventBusFactory` creates event busses that uses Google Guice to inject anything that is configured in a Guice module. Obviously, the presenter needs to be annotated with Guice's `@Inject`.

After you have defined modules, you can use them with a `GuiceEventBusFactory` as in

    factory = GuiceEventBusFactory.withMainSegment(MyEventBus.class)
                                  .withAdditionalSegment(MyOtherEventBus.class)
                                  .using(testModule).build()

This is just like specifying a `BasicEventBusFactory`, with the addition of a `using` method that takes one or more Guice modules. Note that this factory has been specified with explicit main and event bus segments, this is equivalent to using `withSegments`, but a tad more expressive.

Anyway, suppose you define a binding for an `CategoryRepository` in Guice, you can now do:

    @Inject
    public CategoryMgtPresenter(final CategoryMgtBus eventBus, final CategoryMgtView view, final CategoryRepository categoryRepository) {
        super(eventBus, view);
        this.categoryRepository = categoryRepository;
    }

You can also inject more than one interface for the same event bus if you want. E.g. the segment that corresponds with the application-level events and the segment that corresponds to the module-level events.

## Acknowledgement

The interface of the mvpeventbus framework was inspired by the [mvp4g](http://code.google.com/p/mvp4g/) project. The difference is that mvpeventbus is more generic and can be used server-side e.g. for Swing and Vaadin applications.

Building
--------

The sources can be built using gradle 0.9. Assuming gradle 0.9 is in the path, invoking `gradle build` should build everything quite nicely.

The only run-time dependency on the core is guice 2.0. The vaadin example obviously requires [vaadin](http://www.vaadin.com/).
