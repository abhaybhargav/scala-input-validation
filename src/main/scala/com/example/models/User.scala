package com.example.models

case class User(
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  age: Int,
  phoneNumber: Option[String] = None,
  address: Option[String] = None
)