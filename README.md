# live-score-api

[![Build Status](https://travis-ci.org/Real-Time-Football/live-score-api.svg?branch=master)](https://travis-ci.org/Real-Time-Football/live-score-api)

This project is a Java implementation of the example took from the book "Architecting Applications for the Enterprise". Its first goal is to represent a match by event, thus making usage of CRQS and Event Sourcing architecture patterns.

## Technical details

- Java 8
- Spring MVC 2
- JUnit 5
- MongoDB 3.6

## Instructions

Before running you shloud setup a Docker container with a MongoDB instance. Run the command bellow to setup the container:

```bash
docker-compose up -d
```

## API documentation

### Swagger

<http://localhost:8080/swagger-ui.html>

### Postman

Import file Match.postman_collection.json
