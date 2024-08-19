package com.example.validators

import com.example.models.User
import com.github.andyglow.json.JsonFormatter
import com.github.andyglow.jsonschema.AsValue
import json4s._
import json4s.jackson.JsonMethods._
import json4s.jackson.Serialization
import json4s.jackson.Serialization.{read, write}

object JsonSchemaValidator {
  implicit val formats: Formats = DefaultFormats

  private val schema = AsValue.schema[User]
  private val jsonSchema = JsonFormatter.format(schema)

  def validate(json: String): Either[String, User] = {
    try {
      val parsedJson = parse(json)
      val validationResult = schema.validate(parsedJson)
      
      if (validationResult.isValid) {
        Right(read[User](json))
      } else {
        val errors = validationResult.errors.map(_.message).mkString(", ")
        Left(s"Validation failed: $errors")
      }
    } catch {
      case e: Exception => Left(s"Error parsing JSON: ${e.getMessage}")
    }
  }

  def getJsonSchema: String = jsonSchema
}