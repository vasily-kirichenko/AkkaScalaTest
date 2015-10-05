package testCluster

import akka.actor._

class Client() extends Actor with ActorLogging {
  log.info("Client started!")
  var count = 0
  var started = System.currentTimeMillis
  def server() = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/server")

  def sendRequest(): Unit = {
    count = count + 1
    server() ! s"Request #$count}"
    if (count % 1000 == 0) {
      log.info (s"$count total, ${1000.0 * count / (System.currentTimeMillis - started)} /s")
    }
  }

  override def preStart(): Unit = {
    sendRequest()
  }

  override def receive = {
    case msg: String => sendRequest()
  }
}