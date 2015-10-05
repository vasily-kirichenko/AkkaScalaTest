package testCluster

import akka.actor._

class Server() extends Actor with ActorLogging {
  log.info("Server started!")
  val started = System.currentTimeMillis
  var count = 0

  override def receive = {
    case msg: String => {
      count = count + 1
      sender() ! s"Response for $msg"
      if (count % 1000 == 0) {
        log.info (s"$count total, ${1000.0 * count / (System.currentTimeMillis - started)} /s")
      }
    }
  }
}
