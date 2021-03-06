package testCluster

import java.io.File
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val conf =
      ConfigFactory
        .parseFile(new File(".\\application.conf"))
        .withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", conf)
    system.actorOf(Props(classOf[Client]), "client")
  }
}