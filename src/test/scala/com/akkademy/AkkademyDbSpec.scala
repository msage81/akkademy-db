package com.akkademy

import akka.util.Timeout
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}
import akka.actor.{ActorSystem, Props}
import com.akkademy.messages.{MapHolder, SetRequest}
import akka.testkit.TestActorRef

import scala.concurrent.duration._
class AkkademyDbSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {
  implicit val system = ActorSystem()
  describe("akkademyDb") {
    describe("given SetRequest"){
      it("should place key/value into map that can be accessed with underlying"){
        val actorRef = TestActorRef(new AkkademyDb) // TestActorRef synchronous
        actorRef ! SetRequest("key", "value")
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.get("key") should equal(Some("value"))
      }
      it("should place key/value into map"){
        val actorRef = system.actorOf(Props(classOf[AkkademyDb])) // Actual ActorRef asynchronous
        actorRef ! SetRequest("key1", "value1")
        Thread.sleep(1000) // need to sleep because actor call is asynchronous
        MapHolder.map.get("key1") should equal(Some("value1")) // tac
      }
    }
  }
}
