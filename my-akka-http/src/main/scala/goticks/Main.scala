/**
 * akka in action
 * chapter 2. up and running
 * - rest API 로 ticket 서버 구축
 * https://github.com/gilbutITbook/006877/tree/master/chapter-up-and-running
 */
package goticks

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Main extends App
  with RequestTimeout {

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val api = new RestApi(system, requestTimeout(config)).routes

  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api, host, port)

  val log = Logging(system.eventStream, "go-ticks")
  bindingFuture.map{ serverBinding =>
    log.info(s"RestApi bound to ${serverBinding.localAddress}")
  }.onComplete{
    case Success(value) =>
    case Failure(exception) =>
      log.error(exception, "Failed to bind to {}:{}!", host, port)
      system.terminate()
  }
}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}