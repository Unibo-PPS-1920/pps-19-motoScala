package it.unibo.pps1920.motoscala.controller.mediation

trait MediatorLike {

}

trait EventObserver[T] {
  def notify(event: T): Unit
}

trait EventSubject {
  def subscribe[T](observer: EventObserver[T])
  def unsubscribe[T](observer: EventObserver[T])
  def publishEvent[T](ev: T): Unit
}

class Mediator private() extends EventSubject {
  private var observers: Set[EventObserver[_]] = Set()
  override def publishEvent[T](ev: T): Unit = {
    observers.filter()
  }
  override def subscribe[T](observer: EventObserver[T]): Unit = observers = observers + observer
  override def unsubscribe[T](observer: EventObserver[T]): Unit = observers = observers - observer
}

object prova extends App {
  val mediator = Mediator()
  val observer = new EventObserver {
    override def notify(event: Event): Unit = event match {
      case UpdateEvent(k) => println(k)
      case _ => println("other")
    }
  }
  mediator.subscribe(observer)
  mediator.publishEvent(new DrawEvent(234))
  mediator.publishEvent(new UpdateEvent(23))
}

object Mediator {
  def apply(): Mediator = new Mediator()
}
