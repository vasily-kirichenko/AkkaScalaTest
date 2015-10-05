package testCluster

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load()
    val system = ActorSystem("ClusterSystem", conf)
    system.actorOf(Props(classOf[Server]), "server")
  }
}