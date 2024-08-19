package com.example.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import com.example.models.User
import com.example.validators.{RequestFilterValidator, JsonSchemaValidator}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

object SignupRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val routes =
    path("signup") {
      post {
        entity(as[User]) { user =>
          complete {
            StatusCodes.Created -> s"User created: ${user.firstName} ${user.lastName}"
          }
        }
      }
    } ~
    path("signup-filter") {
      post {
        entity(as[User]) { user =>
          RequestFilterValidator.validate(user) match {
            case Nil => complete(StatusCodes.Created -> s"User created: ${user.firstName} ${user.lastName}")
            case errors => complete(StatusCodes.BadRequest -> errors.mkString(", "))
          }
        }
      }
    } ~
    path("signup-schema") {
      post {
        entity(as[String]) { json =>
          JsonSchemaValidator.validate(json) match {
            case Right(validatedUser) => complete(StatusCodes.Created -> s"User created: ${validatedUser.firstName} ${validatedUser.lastName}")
            case Left(errors) => complete(StatusCodes.BadRequest -> errors)
          }
        }
      }
    }
}