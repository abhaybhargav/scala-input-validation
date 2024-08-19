package com.example.validators

import com.example.models.User

object RequestFilterValidator {
  def validate(user: User): List[String] = {
    var errors = List.empty[String]

    if (user.firstName.isEmpty) errors = errors :+ "First name cannot be empty"
    if (user.lastName.isEmpty) errors = errors :+ "Last name cannot be empty"
    if (!isValidEmail(user.email)) errors = errors :+ "Invalid email format"
    if (user.password.length < 8) errors = errors :+ "Password must be at least 8 characters long"
    if (user.age < 18 || user.age > 120) errors = errors :+ "Age must be between 18 and 120"
    
    user.phoneNumber.foreach { phone =>
      if (!isValidPhoneNumber(phone)) errors = errors :+ "Invalid phone number format"
    }

    errors
  }

  private def isValidEmail(email: String): Boolean = {
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r.findFirstIn(email).isDefined
  }

  private def isValidPhoneNumber(phone: String): Boolean = {
    """^\+?[0-9]{10,14}$""".r.findFirstIn(phone).isDefined
  }
}