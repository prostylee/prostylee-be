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

#### Start docker

Open terminal at folder `src/main/docker`

Run the following command:
```shell
docker-compose up
```
Or
```shell
./gradlew startDocker
```

#### Stop docker

Run the following command:
```shell
docker-compose down
```
Or
```shell
./gradlew stopDocker
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

## Flyway Database migration
Flyway adheres to the following naming convention for migration scripts:

<Prefix><Version>__<Description>.sql

Where:

<Prefix> – Default prefix is V, which may be configured in the above configuration file using the flyway.sqlMigrationPrefix property.
<Version> – Migration version number. Major and minor versions may be separated by an underscore. The migration version should always start with 1.
<Description> – Textual description of the migration. The description needs to be separated from the version numbers with a double underscore.
Example: V1_1_0__my_first_migration.sql

So, let's create a directory db/migration in $PROJECT_ROOT with a migration script.

## Run Testing

### Run Unit test

```shell
./gradlew test
```

### Run Component test

```shell
./gradlew componentTest
```

### Run End to End test

```shell
./gradlew e2eTest
```

### Run Unit and Component test

```shell
./gradlew test componentTest
```

### Run all tests

```shell
./gradlew cleanRunAllTests
```
