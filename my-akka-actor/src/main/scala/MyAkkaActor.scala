import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.event.Logging

import scala.concurrent.duration.DurationInt

case object Ping
case object Pong

class Pinger extends Actor {
  var countDown = 10

  def receive = {
    case Pong =>
      println(s"${self.path} received pong, count down $countDown")

      if(countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor{
  def receive = {
    case Ping =>
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}


object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("pingpong")
    val pinger = system.actorOf(Props(new Pinger), "pinger")
    //val pinger2 = system.actorOf(Props[Pinger], "pinger")
    val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")
    val ponger2 = system.actorOf(Props(classOf[Ponger], pinger), "ponger2")

    import system.dispatcher
    system.scheduler.scheduleOnce(500 millis) {
      ponger ! Ping
    }
    system.scheduler.scheduleOnce(500 millis) {
      ponger2 ! Ping
    }

  }
}
