# CachingService

A simple Spring Boot application demonstrating a custom caching service with REST endpoints.

## Features

- Add, retrieve, and remove cache entities via REST API
- In-memory cache with a configurable maximum size
- Simulated database fallback
- Endpoints for cache and database size

## Requirements

- Java 
- Maven

## Setup

1. *Clone the repository*
2. *Build the project:*
   
   mvn clean install
   
3. *Run the application:*
   
   mvn spring-boot:run
   
   or run the Application class from your IDE.

## REST API Endpoints

Base URL: http://localhost:8080/cache

| Method | Endpoint                | Description                | Body Example                |
|--------|-------------------------|----------------------------|-----------------------------|
| POST   | /cache                | Add entity to cache        | { "id": "1", "data": "A" }|
| GET    | /cache/{id}           | Get entity by id           |                             |
| GET    | /cache/{id}?loadFromDB=true | Get entity from DB if not in cache |         |
| DELETE | /cache/{id}           | Remove entity by id        |                             |
| DELETE | /cache/all            | Remove all entities        |                             |
| GET    | /cache/cacheSize      | Get cache size             |                             |
| GET    | /cache/dbSize         | Get DB size                |                             |

## Example Usage with Postman

- *Add entity:*  
  POST http://localhost:8080/cache  
  Body (JSON):  
  json
  {
    "id": "1",
    "data": "value"
  }
  

- *Get entity:*  
  GET http://localhost:8080/cache/1

- *Remove entity:*  
  DELETE http://localhost:8080/cache/1

## Notes

- Ensure the Application class is used to start the application.
