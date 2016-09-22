package com.akkademy
import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable.HashMap
import com.akkademy.messages.{MapHolder, SetRequest}
class AkkademyDb extends Actor {
  val map = MapHolder.map
  val log = Logging(context.system, this)
  override def receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
    }
    case o => log.info("received unknown message: {}", o);
  }
}