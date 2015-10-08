package testCluster

sealed trait Request
case class CalculateFib(n: Int) extends Request

sealed trait Response
case class FibResult(n: Int, result: Int) extends Response