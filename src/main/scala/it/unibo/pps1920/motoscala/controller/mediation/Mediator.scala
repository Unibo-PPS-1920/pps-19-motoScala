package it.unibo.pps1920.motoscala.controller.mediation

trait MediatorLike {

}

trait EventObserver[T] {
  def notify(event: T): Unit
}

trait EventSubject {
  import scala.reflect.ClassTag
  def subscribe[T](observer: EventObserver[T])(implicit eventType: ClassTag[T]): Unit
  def unsubscribe[T](observer: EventObserver[T])
  def publishEvent[T](ev: T)(implicit eventType: ClassTag[T]): Unit
}

class Mediator private() extends EventSubject {
  import scala.reflect.ClassTag
  private var observers: Map[EventObserver[_], Class[_]] = Map()
  override def publishEvent[T](ev: T)(implicit eventType: ClassTag[T]): Unit = {
    observers.filter(o => o._2.isAssignableFrom(eventType.runtimeClass))
      .foreach(t => t._1.asInstanceOf[EventObserver[T]].notify(ev))
  }
  override def subscribe[T](observer: EventObserver[T])(implicit eventType: ClassTag[T]): Unit = {
    observers = observers + (observer -> eventType.runtimeClass)
  }
  override def unsubscribe[T](observer: EventObserver[T]): Unit = observers = observers - observer
}

class Observer1 extends EventObserver[Event] {
  override def notify(event: Event): Unit = event match {
    case UpdateEvent(k) => println(k)
    case DrawEvent(k) => println(k)
    case _ => println("not my message")
  }
}
class Observer2 extends EventObserver[EventProva] {
  override def notify(event: EventProva): Unit = event match {
    case CommandEvent(k) => println("Observer2:" + k)
  }
}
object prova extends App {
  val mediator = Mediator()
  val observer = new Observer1()
  val observer2 = new Observer2()
  mediator.subscribe[Event](observer)
  mediator.subscribe[EventProva](observer2)
  mediator.unsubscribe(observer)
  mediator.publishEvent[DrawEvent](new DrawEvent(234))
  mediator.publishEvent[UpdateEvent](new UpdateEvent(234))
  mediator.publishEvent[EventProva](new CommandEvent(302))
}

object Mediator {
  def apply(): Mediator = new Mediator()
}
