package pong

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.PongActor
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ScalaAskExamplesTest extends FunSpecLike with Matchers {
  val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[PongActor]))

  describe("Pong actor") {
    it("should respond with Pong") {
      val future = askPong("Ping") //uses the implicit timeout
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "Pong")
    }
    it("should fail on unknown message") {
      val future = askPong("unknown")
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }
  describe("FutureExamples"){
    var result = ""
    import scala.concurrent.ExecutionContext.Implicits.global
    it("respond appropriately to a valid request"){
      val future: Future[Any] = pongActor ? "Ping"
      future.onSuccess(
        {
        case x: String => result = "replied with: " + x
        })
      Thread.sleep(100)
      assert(result == "replied with: Pong")
    }
    it("respond appropriately to a invalid request"){
      val future: Future[Any] = pongActor ? "NotPing"
      future.onFailure(
        {
          case x: Exception => result = x.getMessage
        })
      Thread.sleep(100)
      assert(result == "unknown message")
    }
  }
  def askPong(message: String): Future[String] = (pongActor ? message).mapTo[String]

}
