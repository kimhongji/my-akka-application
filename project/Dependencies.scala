import sbt._

object Dependencies extends DependenciesImprovements {

  def compile   (deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "container")

  val akkaVersion = "2.5.26"

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion
  )
}

trait DependenciesImprovements {
  implicit class ModuleIDImprovement(m: ModuleID) {
    def excludeLoggersAndGuava =
      excludeLoggers excludeLoggers

    def excludeLoggers = m.excludeAll(
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "ch.qos.logback"),
      ExclusionRule(organization = "log4j"),
      ExclusionRule(organization = "org.apache.logging.log4j"))
  }
}
