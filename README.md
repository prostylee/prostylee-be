# prostylee-be
For backend

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Docker](https://www.docker.com/get-started)
- Gradle
- Intellij

## Running the application locally

### Run MySQL

Open terminal at folder `src/main/docker`
Run the following command:
```shell
docker-compose up
```

### Run Spring Boot Application

There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the `main` method in the `vn.prostylee.ProStyleeApplication` class from your IDE.

Or run the following command in a terminal:

```shell
./gradlew bootRun
```

### Test app

```shell
http://localhost:8090/api/ping
```

### API documentation

```shell
http://localhost:8090/api/swagger-ui/index.html
```

## Deploying the application to AWS

TODO