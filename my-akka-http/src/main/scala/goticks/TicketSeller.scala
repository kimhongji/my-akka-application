/**
 * akka in action
 * chapter 2. up and running
 * - rest API 로 ticket 서버 구축
 * https://github.com/gilbutITbook/006877/tree/master/chapter-up-and-running
 */
package goticks

import akka.actor.{ Actor, Props, PoisonPill }

object TicketSeller {
  def props(event: String) = Props(new TicketSeller(event))

  case class Add(tickets: Vector[Ticket])
  case class Buy(tickets: Int)
  case class Ticket(id: Int)
  case class Tickets(event: String, entries: Vector[Ticket] = Vector.empty[Ticket])
  case object GetEvent
  case object Cancel
}

class TicketSeller(event: String) extends Actor{
  import TicketSeller._

  var tickets = Vector.empty[Ticket]

  def receive = {
    case Add(newTickets) => tickets = tickets ++ newTickets
    case Buy(nrOfTickets) =>
      val entries = tickets.take(nrOfTickets)
      if(entries.size >= nrOfTickets) {
        sender() ! Tickets(event, entries)
        tickets = tickets.drop(nrOfTickets)
      } else sender() ! Tickets(event) // 생성자에서 entries는 empty 가 default임
    case GetEvent => sender() ! Some(BoxOffice.Event(event, tickets.size))
    case Cancel =>
      sender() ! Some(BoxOffice.Event(event, tickets.size))
      self ! PoisonPill
  }
}
