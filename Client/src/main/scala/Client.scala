package testCluster

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.pattern._
import akka.util.Timeout

object Test {
  case class Thunk[X](xT: () => X) {
    def >>=[Y](x2yT: X => Thunk[Y]): Thunk[Y] = {
      print(">>=\n")
      x2yT(xT()).xT()
    }
    def run() = xT()
  }

  implicit def result[X](x: X): Thunk[X] = Thunk (() => x)

  val nested: Thunk[Int] =
    2 >>= {
      x => x + 1 >>= {
        x => x * 2 >>= { x =>
          val y = 10
          val z = y + 1
          x - z >>= { x => x }
        }
      }
    }

  val nonNested =
    1 >>= { x =>
      x + 1 } >>= { x =>
      x * 2 } >>= { x =>
      val y = 10
      val z = y + 1
      x - z
    } //>>= result
}

class Client() extends Actor with ActorLogging {
  log.info("Client started!")
  var count = 0
  var started = System.currentTimeMillis
  val server = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/server")
  log.info(s"Server = $server")

  //implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def sendRequest(): Unit = {
    count = count + 1
    server ! CalculateFib(20)
    if (count % 1000 == 0) {
      log.info(s"$count total, ${1000.0 * count / (System.currentTimeMillis - started)} /s")
    }
  }

  override def preStart() = sendRequest()

  override def receive = {
    case FibResult(_, _) => sendRequest()
  }
}