package testCluster

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load()
    val system = ActorSystem("ClusterSystem", conf)
    system.actorOf(Props(classOf[Client]), "client")
  }
}