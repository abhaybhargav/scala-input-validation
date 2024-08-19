# Scala Validation Demo

This project demonstrates input validation concepts in a Scala web application using Akka HTTP. It showcases two types of input validation: request filter validation and JSONSchema validation.

## Table of Contents
1. [Setup](#setup)
2. [Running the Application](#running-the-application)
3. [API Endpoints](#api-endpoints)
4. [Usage Examples](#usage-examples)
5. [Implementation Details](#implementation-details)

## Setup

1. Ensure you have Docker installed on your system.
2. Clone this repository:
   ```
   git clone https://github.com/yourusername/scala-validation-demo.git
   cd scala-validation-demo
   ```

## Running the Application

1. Build the Docker image:
   ```
   docker build -t scala-validation-demo .
   ```

2. Run the container:
   ```
   docker run -p 8880:8880 scala-validation-demo
   ```

The application will be accessible at http://localhost:8880.

## API Endpoints

The application provides three signup routes:

1. `/signup`: Basic signup without validation
2. `/signup-filter`: Signup with request filter validation
3. `/signup-schema`: Signup with JSONSchema validation

## Usage Examples

Here are examples of how to use each endpoint, including both successful and failed validation cases.

### 1. Basic Signup (No Validation)

**Successful Request:**

```bash
curl -X POST http://localhost:8880/signup \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "age": 30
  }'
```

**Response:**
```
User created: John Doe
```

### 2. Signup with Request Filter Validation

**Successful Request:**

```bash
curl -X POST http://localhost:8880/signup-filter \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "password": "securepass123",
    "age": 25,
    "phoneNumber": "+1234567890"
  }'
```

**Response:**
```
User created: Jane Smith
```

**Failed Validation:**

```bash
curl -X POST http://localhost:8880/signup-filter \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "",
    "lastName": "Doe",
    "email": "invalid-email",
    "password": "short",
    "age": 15,
    "phoneNumber": "invalid-phone"
  }'
```

**Response:**
```
First name cannot be empty, Invalid email format, Password must be at least 8 characters long, Age must be between 18 and 120, Invalid phone number format
```

### 3. Signup with JSONSchema Validation

**Successful Request:**

```bash
curl -X POST http://localhost:8880/signup-schema \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.johnson@example.com",
    "password": "strongpass456",
    "age": 35,
    "address": "123 Main St, Anytown, USA"
  }'
```

**Response:**
```
User created: Alice Johnson
```

**Failed Validation:**

```bash
curl -X POST http://localhost:8880/signup-schema \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": 123,
    "lastName": true,
    "email": "alice.johnson@example.com",
    "password": "pass",
    "age": "thirty"
  }'
```

**Response:**
```
Validation failed: firstName: expected string, got number; lastName: expected string, got boolean; password: expected minLength: 8, got: 4; age: expected integer, got string
```

## Implementation Details

### User Model

The User model is defined as follows:

```scala
case class User(
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  age: Int,
  phoneNumber: Option[String] = None,
  address: Option[String] = None
)
```

### Request Filter Validation

The RequestFilterValidator uses custom logic to validate each field:

```scala
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

  // Email and phone number validation methods...
}
```

### JSONSchema Validation

The JsonSchemaValidator uses the json-schema library to generate a schema from the User case class:

```scala
object JsonSchemaValidator {
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

  // Other methods...
}
```

This implementation showcases two different approaches to input validation in a Scala web application, providing a foundation for building robust and secure APIs.