package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent

import scala.collection.mutable

case class CommandQueue() {
  private val eventQueue: mutable.Queue[CommandEvent] = mutable.Queue()
  def enqueue(event: CommandEvent): Unit = this.synchronized(eventQueue.enqueue(event))
  def dequeue(): CommandEvent = this.synchronized(eventQueue.dequeue())
  def dequeueAll(): Seq[CommandEvent] = this.synchronized(eventQueue.dequeueAll(_ => true))
}
