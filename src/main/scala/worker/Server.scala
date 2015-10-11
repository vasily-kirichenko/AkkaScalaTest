package testCluster

import akka.actor._

object Fib {
  def fib(n: Int): Int = {
    n match {
      case 0 | 1 => n
      case _ => fib(n - 1) + fib(n - 2)
    }
  }
}

class Server() extends Actor with ActorLogging {
  log.info("Server started!")
  val started = System.currentTimeMillis
  var count = 0

  override def receive = {
    case CalculateFib(n) => {
      count = count + 1
      sender() ! FibResult(n, Fib.fib(n))
      if (count % 1000 == 0) {
        log.info (s"$count total, ${1000.0 * count / (System.currentTimeMillis - started)} /s")
      }
    }
  }
}
