MVP Event Bus
=============

Goal
----

The MVP Event Bus framework lets Presenters in the Model-View-Presenter pattern communicate with each other in a loosely coupled fashion.

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

Event methods are void methods that can take any number of parameters (of reference types), so there is no need to create special-purpose event classes. Sending an event is the same as calling a method annotated with `@Event`. The advantage of this approach is that it is not necessary to write dedicated, repetitive, `Event` classes for the events you want to send over the bus.

### Creating event busses

Somewhere in the initialisation part of your application request an object implementing the interface you have defined:

    public void init() {
      final EventBusFactory<DemoEventBus> factory = BasicEventBusFactory.withMainType(DemoEventBus.class).build()
      final DemoEventBus eventBus = factory.create();
    }

First build a factory object based on the specification of what types should be implemented and then ask this factory to build a bus instance. This instance is automagically generated using dynamic proxies, so you don't have to do anything besides defining the interface. In a stand-alone application, e.g. using Swing, you  likely have one factory instance and one bus instance. In a web application you'll have one factory and one bus for every user session.

### Presenters

The `EventBusFactory` will create all `Presenter`s mentioned in the `Event` annotations the first time an event handled by the presenter is sent. After the first request, a presenter is stored for subsequent requests. Obviously, each event bus instances has its own instance of all presenters so that different user sessions can have different state.

The role of the presenter is to provide the application logic. Presenters have to implement the `Presenter` tagging interface. There's a convenience class `BasePresenter` that implements this interface you can extend from.

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

As mentioned before, once created a presenter is kept alive, so it is safe to store state within the presenter. However, when the event bus instance that forwards events to the presenter becomes unreferenced (e.g. the user's session terminates), all associated presenters are removed as well. Future versions of the framework may allow explicit removal of the presenters and the associated view.

### Views

Views are classes that interact with the UI framework you use in your application and implement the `View` marker interface.

    public class ApplicationFrame extends JFrame implements View {
      // view logic here
    }

It is suggested that UI-framework-specific methods call back to methods in the presenter that the View belongs to. The goal is to have as little logic as possible in the view and as much as possible in the presenter. The view should only contain the code that requires a run-time environment to execute tests. The presenter should contain everything that can be tested a as unit test. In practice, it depends on the amount of test coverage you aim for. If you're not unit testing every last bit of your application, it may be convenient to shift a bit more reponsability to the view.

For a complete example, look at the included mvp-swingexample or mvp-vaadin-example projects.

### Validation at wiring time

Because the event dispatching methods in the event bus interface and the event handlers in the presenters are only linked through a naming convention, the framework will check the validity of your set up as soon as possible. From the moment the `build` method is invoked on the factory builder, all `Presenter`s defined in `@Event` annotation are checked to make sure that they contain the requisite event handler methods with the correct argument types. If not, an exception is issued. Thus, if you can create an event bus factory, you can be assured that there will be no run-time surprises due to method lookup misses. Also, all exceptions that do occur try to be as descriptive as possible so you can correct the issue as efficiently as possible.

These look ups at wiring time are also used to avoid having to perform reflection at event dispatching time.

Extensions
----------

On top of the base functionality, there are a number of interesting additions.

### Event bus segment types

For larger applications it doesn't make sense to have a single event bus interface containing all events and referring to all presenters for the whole application. Therefore, it is possible to get different views of the whole event bus that only show a number of, likely related, events. These parts are called "segments", but keep in mind that there is only one event bus that handles all events: events are not local to a certain segment and event bus segments are not chained. Still, thinking of segments allows modularising applications at design time.

To work with event bus segments, all you have to do is make separate event bus interfaces. These interfaces have no programmatic relationship to each other at design time. Only when configuring an event bus factory, are the fragments combined.

E.g.

    BasicEventBusFactory.withSegments(MainEventBus.class, ChainedEventBus.class, OtherEventBus.class)
                        .build()

In this example `MainEventBus`, `ChainedEventBus` and `OtherEventBus` are event bus interfaces each with their own set of presenters (the sets may overlap, although this will typically not be the case). The resulting event bus instance implements both interfaces. The declared type of any event bus instance created from this factory is the type of the first argument, in this case `MainEventBus`, but the instance can be cast to any of the other interfaces. 

Since frequent casting in client code doesn't look very nice, the `BasePresenter` has some syntactic sugar for this. The standard type of the event bus instance is declared through the generics type argument. The other segment types can be accessed through a helper method `getEventBus(<segment interface>)` as in:

    /**
     * See {@link CategoryMgtBus#changedCategory(Category)}.
     */
    public void onChangedCategory(final Category category) {
      getView().refreshCategories(category);
      getEventBus(ApplicationBus.class).showMessage("Saved category");
    }

This example also demostrates a suggested way of documenting event methods. The `@link` Javadoc tag in the event handler, and the `@Event` annotation in the event dispatcher allow navigating between event definition and implementation despite the absence of a compiler-enforced link.   

### Guice integration

The basic event bus factory can only create event busses that inject the event bus interfaces and the view into the presenter. `GuiceEventBusFactory` creates event busses that use Google Guice to inject anything that can be configured in a Guice module. Obviously, the presenter needs to be annotated with Guice's `@Inject`.

After you have defined modules, you can use them with a `GuiceEventBusFactory` as in

    factory = GuiceEventBusFactory.withMainSegment(MyEventBus.class)
                                  .withAdditionalSegment(MyOtherEventBus.class)
                                  .using(testModule).build()

This is just like specifying a `BasicEventBusFactory`, with the addition of a `using` method that takes one or more Guice modules. Note that this factory has been specified with explicit main and event bus segments, this is equivalent to using the single `withSegments` methods, but a tad more expressive.

Anyway, suppose you define a binding for a `CategoryRepository`, you can then expect all parameters to the following constructor being supplied by Guice:

    @Inject
    public CategoryMgtPresenter(final CategoryMgtBus eventBus, final CategoryMgtView view, 
                                final CategoryRepository categoryRepository) {
        super(eventBus, view);
        this.categoryRepository = categoryRepository;
    }

There's no need to bind implementations for the `eventBus` and `view` parameters in your modules as those are supplied by the framework itself.

You can also inject more than one interface on the same event bus if you prefer to avoid casting to the correct segment type. E.g. you can both inject the segment that corresponds with the application-level events and the segment that corresponds to the module-level events. You'll end up with references to the same object, but typed differently.

### Interception

For defining cross-cutting concerns, mvpeventbus allows defining event interceptors at the bus level. I.e., you can define interceptor classes that receive every event sent to the bus. These interceptors are given a reference to the originally called event with its parameters.

An interceptor is defined when setting up the factory

    BasicEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(new MyInterceptor())

The interceptor has to implement the `EventInterceptor` interface

    static public class MyInterceptor implements EventInterceptor {

        @Override
        public boolean beforeEvent(final EventBus bus, final Method eventMethod, final Object[] args) {
            // do something here
            return PROCEED; // or HALT
        }

    }

If the interceptor returns true (the value of the constant PROCEED) event handling proceeds. If it returns false (the value of the constant HALT), event execution is halted. It is possible to define multiple interceptors by simple using `interceptedBy` more than once. In that case, the first interceptor that yields false will also prevent the next interceptor being executed.

Using interceptors can be very powerfull in combination with custom annotations. An interceptor can query the event method for any annotations defined on it and then use the values of the annotation's properties. In effect this allows you to pass extra parameters to the interceptor to guide the cross-cutting behaviour. Using this technique it should be possible to avoid having to know which events exist on the bus in order to limit the applicability of the interceptor.

Given the signature of the `beforeEvent` method, it is obviously possible to send additional events to the event bus if required.

If you use the `GuiceEventBusFactory`, you can also use Guice AOP, but you have be aware of two concerns:
   1. it works only for objects created by Guice, which the event bus itself is not
   2. they get applied for every call to an injected object
So if one event dispatches to two handlers, the Guice interceptors will be invoked for all injected objects for both handlers. That may be just what you need with respect to those objects, but it does no good if you want to define cross-cutting behaviour at the event level.

Therefore, `GuiceEventBusFactory` also allows adding interceptors by class, as in

    GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(MyGuiceInterceptor.class)

The interceptor will be instantiated by Guice at the moment the factory is created. Depending on what you want to inject, it may be necessary to inject a Guice `Provider` instead of a dependency instance directly since this enables scoped objects at run time. The interceptor itself will *not* be instantiated for every event interception for performance reasons.

### Event handler life cycle

As explained before, the normal way of creating event handlers is when an event is first invoked on an event bus instance. From that moment on, the handler stays in memory as long as the event bus instance itself does. In a web application environment, typically until the web session holding the event bus expires. For large applications this may be a bit wasteful since the presenter has state associated with it; not in the least the view object!

#### Cleaning out presenters

A presenter can release itself by calling the event bus method `detach`:

    public void onRemove() {
      eventbus.detach(this);
    }

Note that in the default case, since a presenter maintains it view, all information on a screen remains unchanged even if a view is temporarily replaced by a view from another presenter. If this is not what you want you can either detach the presenter when it ceases to be visible or you can reset it's content when it becomes hidden or visible again. 

#### Creating additional presenters

Sometimes you may want to have more than one instance of a given presenter on the same event bus. For example, you may have a few different panes that each show a different entity of the same kind. This can be achieved by giving an additional argument to the `@Event` annotation.

    static interface MyEventBus extends EventBus {
        @Event(value = {MyPresenter.class}, instantiation = Policy.TO_NEW_INSTANCE)
        void createEvent(final MultiCapturer capturer);
    }

In this case, a new instance of `MyPresenter` will be instantiated every time the `createEvent` event is sent to the bus. The accompanying handler method `onCreateEvent` will only be invoked for the new instance. Event methods not tagged with `TO_NEW_INSTANCE` will be dispatched to all exising presenters of the handler type.

#### Deferring presenter creation

If you only want to send an event to a presenter in case an instance of it already exists, you can use the `TO_EXISTING_INSTANCES` instantation policy as in:

    static interface MyEventBus extends EventBus {
        @Event(value = {MyPresenter.class}, instantiation = Policy.TO_EXISTING_INSTANCES)
        void existingEvent(final MultiCapturer capturer);
    }

Here, all instances of `MyPresenter` attached to the even bus instance are called with their `onExistingEvent` method. No new instance of `MyPresenter` will be created.

#### Dispatching to all presenters, creating one if necessary

This is the default case, but if you want, you can use the instantiation policy `TO_INSTANCES` explicitly.

Note that if you want that events to several instances of the same presenter class are handled differently, you should only use information that can be gathered from the event itself. So, add additional, explicit parameters to the event method. Don't use objects that are shared between sender and receiver or use magical context maps that could contain everything.

Acknowledgement
---------------

The interface of the mvpeventbus framework was inspired by the [mvp4g](http://code.google.com/p/mvp4g/) project. The difference is that mvpeventbus is more generic and can be used in stand-alone, e.g. Swing, and server-side, e.g. Vaadin, applications.

Building
--------

The sources can be built using gradle 0.9 and Java 6.0. Assuming gradle 0.9 and a JDK 6 are on your path, invoking `gradle build` should build everything quite nicely. The binary jars are completely compatible with Java 5.

The only run-time dependency on the core is guice 2.0. The vaadin example obviously requires [vaadin](http://www.vaadin.com/).
