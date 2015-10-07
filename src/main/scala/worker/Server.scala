package testCluster

import akka.actor._

object Fib {
  def fib(n: Int): Int = {
    n match {
      case 0 | 1 => n
      case n => fib(n - 1) + fib(n - 2)
    }
  }
}

class Server() extends Actor with ActorLogging {
  log.info("Server started!")
  val started = System.currentTimeMillis
  var count = 0

  override def receive = {
    case msg: String => {
      count = count + 1
      sender() ! s"Response for $msg, fib(30) = ${Fib.fib(30)}"
      if (count % 1000 == 0) {
        log.info (s"$count total, ${1000.0 * count / (System.currentTimeMillis - started)} /s")
      }
    }
  }
}
