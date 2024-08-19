package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.example.routes.SignupRoutes
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ValidationDemo {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "ValidationDemo")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val routes = cors() {
      SignupRoutes.routes
    }

    val bindingFuture = Http().newServerAt("0.0.0.0", 8880).bind(routes)
    println(s"Server online at http://localhost:8880/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}