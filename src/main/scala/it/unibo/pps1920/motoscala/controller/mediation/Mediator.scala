package it.unibo.pps1920.motoscala.controller.mediation

import scala.reflect.ClassTag

trait MediatorLike

trait EventObserver[T] {
  def notify(event: T): Unit
}

trait EventSubject {
  def subscribe[T: ClassTag](observer: EventObserver[T]): Unit
  def unsubscribe[T](observer: EventObserver[T]): Unit
  def publishEvent[T: ClassTag](ev: T): Unit
}

class Mediator private() extends EventSubject {
  type EventType = Class[_]
  private var observers: Map[EventObserver[_], EventType] = Map()

  override def publishEvent[T: ClassTag](ev: T): Unit = {
    observers.filter(o => o._2.isAssignableFrom(implicitly[ClassTag[T]].runtimeClass))
      .foreach(t => t._1.asInstanceOf[EventObserver[T]].notify(ev))
  }
  override def subscribe[T: ClassTag](observer: EventObserver[T]): Unit =
    observers = observers + (observer -> implicitly[ClassTag[T]].runtimeClass)
  override def unsubscribe[T](observer: EventObserver[T]): Unit = observers = observers - observer
}

class Observer1 extends EventObserver[Event] {
  override def notify(event: Event): Unit = event match {
    case UpdateEvent(k) => println(k)
    case DrawEvent(k) => println(k)
    case CommandEvent(k) => println("Command event extends from Event")
  }
}
class Observer2 extends EventObserver[EventProva] {
  override def notify(event: EventProva): Unit = event match {
    case CommandEvent(k) => println("Observer2:" + k)
  }
}

object Mediator {
  def apply(): Mediator = new Mediator()
}

object prova extends App {
  val mediator = Mediator()
  val observer = new Observer1()
  val observer2 = new Observer2()
  mediator.subscribe(observer)
  mediator.subscribe(observer2)
  //mediator.unsubscribe(observer)
  mediator.publishEvent[DrawEvent](new DrawEvent(234))
  mediator.publishEvent[UpdateEvent](new UpdateEvent(234))
  mediator.publishEvent[EventProva](new CommandEvent(302))
}

