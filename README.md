# prostylee-be
For prostylee backend

## Requirements

For building and running the application you need:

- JDK 11
- Docker
- Gradle
- Intellij

## Running the application locally

### Run Postgres

#### Start docker
Go to place stay the gradlew file.

Run the following command:
```shell
./gradlew startDocker
```

#### Stop docker

Run the following command:
```shell
./gradlew stopDocker
```

### Run Spring Boot Application

There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the `main` method in the `ProStyleeApplication` class from your IDE.

Or run the following command in a terminal:

```shell
./gradlew bootRun
```

### Check app running

```shell
http://localhost:8090/api/ping
```

### API documentation

```shell
http://localhost:8090/api/swagger-ui/index.html
```

## Flyway Database migration
Flyway adheres to the following naming convention for migration scripts:

<Prefix><Version>__<Description>.sql

Where:

- `<Prefix>` – Default prefix is V, which may be configured in the above configuration file using the flyway.sqlMigrationPrefix property.
- `<Version>` – Migration version number. Major and minor versions may be separated by an underscore. The migration version should always start with 1.
- `<Description>` – Textual description of the migration. The description needs to be separated from the version numbers with a double underscore.
Example: `V1_1_0__my_first_migration.sql`

So, let's create a directory db/migration in $PROJECT_ROOT with a migration script.

## Run Testing

### Run Unit test

```shell
./gradlew test
```

### Run Integration  test

```shell
./gradlew integrationTest
```

### Run End to End test

Start Prostylee application first.

```shell
./gradlew bootRun
```

Run end to end test

```shell
./gradlew e2eTest
```

### Run Unit and Integration  test

```shell
./gradlew test integrationTest
```

### Run all tests

```shell
./gradlew cleanRunAllTests
```

## Package & deploy

### Package

Run the following command:

```shell
./gradlew build
```

After build succesfully, we will have a jar file at folder `build/libs`.
To start spring appliation from this jar file, run the following command:

```shell
java -Dspring.profiles.active=staging -jar ./build/libs/prostylee-be-1.0.0-SNAPSHOT.jar
```

### Deploy the application to AWS

TODO