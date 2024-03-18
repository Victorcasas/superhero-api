# Superhero-api

>|
>| ____                        _   _                     _    ____ ___ \
>|/ ___| _   _ _ __   ___ _ __| | | | ___ _ __ ___      / \  |  _ \_ _|\
>|\___ \| | | | '_ \ / _ \ '__| |_| |/ _ \ '__/ _ \    / _ \ | |_) | | \
>| ___) | |_| | |_) |  __/ |  |  _  |  __/ | | (_) |  / ___ \|  __/| | \
>||____/ \__,_| .__/ \___|_|  |_| |_|\___|_|  \___/  /_/   \_\_|  |___|\
>|             |_|                                                     \
>|

## Description of superhero-api 

Spring Boot 3 and Java 21 application, built trying to follow the s.o.l.i.d. and clean design principles that implements a REST web service for CRUD operations.

The maintenance operations allo w:

• Query all superheroes.
• Query a single superhero by id.
• Query all superheroes that contain, in their name, the value of a parameter sent in the request. For example, if we send "man" it will return "Spiderman", "Superman", "Manolito el fuerte", etc.
• Create a superhero.
• Modify a superhero.
• Delete a superhero.

## Starting the application

After cloning the repository run the following command in Linux:

```
cd superhero-api
./mvnw spring-boot:run
```

In Windows run this command:

```
cd superhero-api
./mvn.cmd spring-boot:run
```
Alternatively, to use your own local maven installation run the following command in any operating system:

```
cd superhero-api
mvn spring-boot:run
```

## Operating

When the application is started, it populates the database with the appropriate script, starts the server and expose the rest service on http port 8080 of localhost, allowing to make requests to it using these url templates:

* GET superheroes/
* POST superheroes/
* GET superheroes/{id}
* PUT superheroes/{id}
* DELETE superheroes/{id}
* superheroes/search?word={word}

To use swagger-ui navigate to http://localhost:8080/swagger-ui/index.html and execute the call filling the properties fields.

## Testing
The service and contoller unit tests are implemented to prove the correct isolated working of them using the needed mocked data. And the integration test use all the infraestructure with the real database, populated with the provided sample data. Tests coverage is available through the Jacoco maven plugin.

Run the tests with the command:

```
cd superhero-api
mvn test
```
## Docker
A Dockerfile is included at the root of the project, allowing to dockerize the application. 

## Improvements

• SuperHeroes are stored in a H2 in-memory database.

• Unit tests.

• Integration test.

• The FlyWay library is used to facilitate the maintenance of database DDL scripts.

• Pagination of long responses has been implemented.

• A custom annotation has been implemented in order to measure execution times logging it to a file.

• Centralized exception handling.

• Dockerfile to dockerize he application.

• The Caffeine in-memory cache library is used to cache requests.

• Spring Security has been used to implement a basic authentication.

• API documentation with Swagger.