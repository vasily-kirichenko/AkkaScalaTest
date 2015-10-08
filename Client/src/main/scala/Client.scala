package testCluster

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.pattern._
import akka.util.Timeout

object MyInt {
  implicit def intToMyInt(x: Int): MyInt = new MyInt(x)
  def apply(x: Int): MyInt = new MyInt(x)
}

class MyInt(value: Int) {
  def ===>(that: MyInt): Boolean = this == that
  def eq(that: MyInt): Boolean = this ===> that
}

object Another {
  import MyInt._

  val _1 = 1 ===> MyInt(2)
  val _2 = 1 ===> 2
  val _3 = 1 eq 2
}












class Client() extends Actor with ActorLogging {
  log.info("Client started!")
  var count = 0
  var started = System.currentTimeMillis
  val server = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/server")
  log.info(s"Server = $server")
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def sendRequest(): Unit = {
    count = count + 1
    server ? CalculateFib(20)
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