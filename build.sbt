import Dependencies._

name := "my-akka-application"

version := "0.1"

scalaVersion := "2.11.8"

lazy val root: Project = (project in file("."))
  .aggregate(myAkkaHttp, myAkkaActor)

lazy val myAkkaHttp = Project("my-akka-http", file("my-akka-http"))
  .settings(
    libraryDependencies ++= akka
  )

lazy val myAkkaActor = Project("my-akka-actor", file("my-akka-actor"))
  .settings(
    libraryDependencies ++= akka
  )