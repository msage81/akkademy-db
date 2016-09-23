package com.akkademy

import akka.actor.Actor.Receive
import akka.actor.Status.Failure
import akka.actor.{Actor, ActorSystem}
import akka.testkit.TestActorRef
import com.akkademy.messages.SetRequest
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}

import scala.collection.mutable.ListBuffer

class PongActorSpec extends FunSpecLike with Matchers /*with BeforeAndAfterEach */{
  implicit val system = ActorSystem()
  describe("PongActor") {
    describe("when sent a Ping message") {
      it("should return a Pong message") {
        val actorRef = TestActorRef(new PongActor) // TestActorRef synchronous
        val anonActor = TestActorRef(new Actor{
          var list = ListBuffer[String]()
          override def receive: Receive = {
            case "start" => actorRef ! "Ping"
            case message:String => list +=  message
          }
        })
        anonActor ! "start"
        anonActor.underlyingActor.list(0) should equal("Pong")
      }
    }
    describe("when sent a non-Ping message") {
      it("should result in an exception") {
        val actorRef = TestActorRef(new PongActor) // TestActorRef synchronous
        val anonActor = TestActorRef(new Actor{
          var list = ListBuffer[Any]()
          override def receive: Receive = {
            case "start" => actorRef ! "Ouch"
            case message:String => list +=  message
            case failure => list += failure
          }
        })
        anonActor ! "start"
        anonActor.underlyingActor.list(0).asInstanceOf[Failure].cause.getMessage should equal("unknown message")

      }
    }
  }
}

