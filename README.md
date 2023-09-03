# Clever-Bank Console Application

A web banking application built in Java 17 using Gradle, PostgreSQL, and JDBC.
This application manages banks, accounts, users, and transactions for Clever-Bank. 
It implements various banking operations such as accruing interest, withdraws, transfers and refills.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Requirements](#requirements)
3. [Usage](#usage)
4. [Features](#features)
5. [File Structure](#file-structure)
6. [Configuration](#configuration)
7. [Running the Application](#running-the-application)
8. [Running the tests](#running-the-tests)

## Project Overview

The Clever-Bank Console Application is designed to manage multiple banks, users, accounts, and transactions.
It provides core banking functionalities, adheres to SOLID principles, and maintains a clean and organized codebase.

## Requirements

1. **Java 17**: Ensure you have Java 17 installed.

2. **Gradle**: The project uses Gradle for build automation. You can install Gradle from [here](https://gradle.org/install/).

3. **Docker**: You'll need Docker to run PostgreSQL database and Liquibase.

## Usage

The application supports various banking operations:

- Refill
- Withdraw
- Transfer
- Interest Accrual
- Transaction History
- Account Statements
- Money Statements

## Features

### Core Features

1. **CRUD Operations**: Basic CRUD operations for all entities (Bank, User, Account, Transaction) via Servlets.

2. **Configuration**: Configuration settings, including interest rates, are stored in a YAML configuration file.

3. **Refill and Withdraw**: Users can refill and withdraw funds from their accounts in CleverBank.

4. **Transfer**: Transfer funds between accounts, one or both of which are CleverBank,
using a single transaction to achieve thread safety.

5. **Interest Accrual**: Automatically accrues interest on account balances at the end of each month.

6. **Unit Testing**: Unit tests covering more than 70% of the codebase, with a focus on service layer testing.

### Additional Features (Optional)

1. **Transaction Statements**: Generate account or money statements for users in PDF format between two dates.
   (Statements are saved into $TOMCAT_HOME$/bin/statements directory)

2. **Check Generation**: Automatically generates check files for every transaction.
   (Checks are saved into $TOMCAT_HOME$/bin/checks directory)

## Endpoints description

## User Endpoint

The endpoint URL is constructed as follows:
- `GET /users/{id}`
- `GET /users`
- `POST /users`
- `PUT /users/{id}`
- `DELETE /users/{id}`

### Input and Output Data

### GET

### Input

#### 1. Retrieve User by ID
- Endpoint: `GET /users/{id}`
- Example URL: `GET /users/12`
- Description: Retrieves user information by providing the user's ID as a path parameter.

#### 2. Retrieve List of Users with Pagination
- Endpoint: `GET /users`
- Example URL: `GET /users?page=1&size=10`
- Description: Retrieves a list of users with optional pagination. You can specify the page number (`page`) and the number of results per page (`size`) as query parameters.

### Output

#### 1. UserResponseDto (User Information)
- JSON Response Example:
  ```json
  {
    "id": 123,
    "name": "John",
    "surname": "Doe",
    "birthdate": "1990-05-15"
  }
  
### POST

### Input

#### Create User
- Endpoint: `POST /users`
- Example Request:
  ```http
  POST /users
  Content-Type: application/json

  {
    "name": "Alice",
    "surname": "Smith",
    "birthdate": "1990-05-15"
  }
  
### Output

#### 1. UserResponseDto (User Information)
- JSON Response Example:
  ```json
    {
    "id": 12,
    "name": "Alice",
    "surname": "Smith",
    "birthdate": "1990-05-15"
    }

### PUT

### Input

#### Update User
- Endpoint: `PUT /users/{id}`
- Example Request:
  ```http
  PUT /users/123
  Content-Type: application/json

  {
    "name": "Updated Name",
    "surname": "Updated Surname",
    "birthdate": "1990-06-20"
  }
### Output

#### 1. UserResponseDto (User Information)
- JSON Response Example:
  ```json
    {
    "id": 123,
    "name": "Updated Name",
    "surname": "Updated Surname",
    "birthdate": "1990-06-20"
    }

### DELETE

### Input

### Input

#### Delete User
- Endpoint: `DELETE /users/{id}`
- Example Request:
  ```http
  DELETE /users/123
  
### Output

Successful Deletion (Status 204 No Content)
Description: If the user is successfully deleted, the API responds with a status code of 204
(No Content), indicating a successful deletion. There is no JSON response body in this case.

 ## Bank Endpoint

 ### Endpoint URL
The endpoint URL is constructed as follows:

- `GET /banks/{id}`
- `GET /banks`
- `POST /banks`
- `PUT /banks/{id}`
- `DELETE /banks/{id}`

### Input and Output Data

### GET

### Input

#### 1. Retrieve Bank by ID
- Endpoint: `GET /banks/{id}`
- Example URL: `GET /banks/12`
- Description: Retrieves bank information by providing the bank's ID as a path parameter.

#### 2. Retrieve List of Banks with Pagination
- Endpoint: `GET /banks`
- Example URL: `GET /banks?page=1&size=10`
- Description: Retrieves a list of banks with optional pagination. You can specify the page number (`page`) and the number of results per page (`size`) as query parameters.

### Output

#### 1. BankResponseDto (Bank Information)
- JSON Response Example:
  ```json
    [
    {
    "id": 1,
    "name": "XYZ Bank"
    },
    {
    "id": 2,
    "name": "DEF Bank"
    },
    ...
    ]

### POST

### Input

#### Create Bank
- Endpoint: `POST /banks`
- Example Request:
  ```http
    POST /banks
    Content-Type: application/json
    
    {
    "name": "New Bank"
    }

### Output

#### 1. BankResponseDto (Bank Information)
- JSON Response Example:
  ```json
    {
    "id": 12,
    "name": "New Bank"
    }

### PUT

### Input

#### Update Bank
- Endpoint: `PUT /banks/{id}`
- Example Request:
  ```http
    PUT /banks/123
    Content-Type: application/json
    
    {
    "name": "Updated Bank Name"
    }
  
### Output

#### 1. BankResponseDto (Bank Information)
- JSON Response Example:
  ```json
    {
    "id": 123,
    "name": "Updated Bank Name"
    }

### DELETE

### Input

### Input

#### Delete Bank
- Endpoint: `DELETE /users/{id}`
- Example Request:
  ```http
  DELETE /banks/123

### Output

Successful Deletion (Status 204 No Content): 
If the bank is successfully deleted, the API responds with a status code of 204 (No Content),
indicating a successful deletion. 
There is no JSON response body in this case.

# Account Endpoint

## Endpoint URL

The endpoint URL is constructed as follows:

- `GET /accounts/{id}`
- `GET /accounts`
- `POST /accounts`
- `PUT /accounts/{number}/{transactionType}`
- `DELETE /accounts/{id}`

## Input and Output Data

### GET

#### Retrieve Account by ID

**Input:**
- Endpoint: `GET /accounts/{id}`
- Example URL: `GET /accounts/12`
- Description: Retrieves account information by providing the account's ID as a path parameter.

**Output:**
- JSON Response Example:
  ```json
  {
    "id": 123,
    "number": "1234567890",
    "balance": 1000.0,
    "currency": "USD",
    "userId": 456,
    "bankId": 789,
    "createdDate": "2023-09-01"
  }

### POST

### Input

#### Create Account
- Endpoint: `POST /accounts`
- Example Request:
  ```http
  POST /accounts
  Content-Type: application/json
  
  {
  "number": "1234567890",
  "balance": 1000.0,
  "currency": "USD",
  "userId": 456,
  "bankId": 789
  }

### Output

#### 1. AccountResponseDto (Account Information)
- JSON Response Example:
  ```json
  {
  "id": 12,
  "number": "1234567890123456",
  "balance": 1000.0,
  "currency": "USD",
  "userId": 1,
  "bankId": 1,
  "createdDate": "2023-09-03"
  }

### PUT
Perform Account Transactions

### Input

- Endpoint: `PUT /accounts/{number}/{transactionType}`
- Example Request:
  ```http
  PUT /accounts/1234567890123456/withdraw/500

### Output

Transaction success or failure, depending on the transaction type 
(withdraw, refill, or transfer). No JSON response body for this operation.

### DELETE

### Input

#### Delete Account
- Endpoint: `DELETE /accounts/{id}`
- Example Request:
  ```http
  DELETE /accounts/123

### Output

Successful Deletion (Status 204 No Content): If the account is successfully deleted, 
the API responds with a status code of 204 (No Content), indicating a successful deletion.
There is no JSON response body in this case.

# Transaction Endpoint

## Endpoint URL
  The endpoint URL is constructed as follows:

- `GET /transactions/{id}`
- `GET /transactions`
- `POST /transactions`

## Input and Output Data

### GET

**Input:**
- Endpoint: `GET /transactions/{id}`
- Example URL: `GET /transactions/12`
- Description: Retrieves transaction information by providing the transaction's ID as a path parameter.

**Output:**
- JSON Response Example:
  ```json
  {
    "id": 123,
    "amount": 100.0,
    "type": "WITHDRAW",
    "currency": "USD",
    "senderAccountId": 1,
    "receiverAccountId": null,
    "createdDate": "2023-09-03T10:30:00"
  }

# Statement Endpoint

The Statement Endpoint provides operations for generating account and money statements within
specified date ranges.

## Endpoint URL

The endpoint URL is constructed as follows:

- `GET /statements/account/{accountNumber}?from={fromDate}&to={toDate}`: Generate an account statement within a specified date range.
- `GET /statements/money/{userId}?from={fromDate}&to={toDate}`: Generate a money transaction statement for a user within a specified date range.

## Input

### Generate Account Statement

**Input:**
- Endpoint: `GET /statements/account/{accountNumber}?from={fromDate}&to={toDate}`
- Example URL: `GET /statements/account/123456?from=01-09-2023&to=30-09-2023`
- Description: Generates an account statement for the specified account number within 
the provided date range.

### Generate Money Statement

**Input:**
- Endpoint: `GET /statements/money/{userId}?from={fromDate}&to={toDate}`
- Example URL: `GET /statements/money/456?from=01-09-2023&to=30-09-2023`
- Description: Generates a money transaction statement for the specified user ID within
the provided date range.

## Output

The generated statements is returned in PDF format. The output typically includes details of
transactions, balances, and other relevant information.

## File Structure

The project follows a well-organized file structure:

- **src/main/java**: Contains Java source code.
- **src/test/java**: Houses unit tests.
- **src/main/resources**: Stores YAML configuration files.
- **databases**: Stores Liquibase DB migrations.
- **$TOMCAT_HOME$/bin/checks**: Stores generated transaction check files.
- **$TOMCAT_HOME$/bin/statements/money**: Stores generated money statements (PDF).
- **$TOMCAT_HOME$/bin/statements/account**: Stores generated account statements (PDF).

## Configuration

- Application configuration is managed in YAML file located in `src/main/resources`.

## Running the Application

To run the application:

1. Clone the repository: `git clone <repository_url>`

2. Configure PostgreSQL settings in `application.yml`.

3. Build the project: `./gradlew build`

4. Run the application: `./gradlew run`

## Running the tests
```bash
./gradlew test
