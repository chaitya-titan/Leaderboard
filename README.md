# Coding LeaderBoard backend

## Description

* This is a leaderboard backend application built with Spring Boot and Spring Data JPA using Postgres. It provides RESTful APIs for creating, reading, updating, and deleting leaderboard contest.

## Running the Application

#### Just use run on BlogApiApplication if on IntelliJ IDEA or use the following command if using Gradle

```bash
./gradlew bootRun
```

## Database

### MongoDB

#### If you don't have mongoDB installed, run the following command
```bash
brew install mongodb-community@7.0
mongod --version
```

#### To Login to MongoDB

```bash
brew services start mongodb-community@7.0
mongosh
```

## API Endpoints

### `Get /users`
Retrieve a list of all registered users

### `Get /users/{userId}`
Retrieve the details of a specific user

### `POST /users`
Register a new user to the contest

### `PUT /users/{userID}`
```
Pass the score in the body 
ex:- 
{
    "score": 32
}
```
Update the score of a specific user

### `DELETE /users/{userId}`
Deregister a specific user from the contest


## Postman Collection

#### A Postman collection for testing the API is available below

```
https://api.postman.com/collections/20879467-1902a829-ff58-46aa-8482-5f5e95734cc8?access_key=PMAT-01J0EP8Y5JYJTPT0VES3M2CNFN
```