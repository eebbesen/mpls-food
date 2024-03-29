# MPLS-FOOD
![Qodana](https://github.com/eebbesen/mpls-food/actions/workflows/code_quality.yml/badge.svg)

![reviewdog](https://github.com/eebbesen/mpls-food/actions/workflows/reviewdog.yml/badge.svg)

![tests](https://github.com/eebbesen/mpls-food/actions/workflows/test.yml/badge.svg)


Created with https://bootify.io.

Minneapolis flag image from https://upload.wikimedia.org/wikipedia/commons/9/9d/Flag_of_Minneapolis.svg.

Try it out at https://mpls-food.onrender.com/days -- note this is _extremely_ slow, especially as the app has to restart
after less than one hour of activity.

In addition to the browser-based application there is a GraphQL api (currently only for all places or places by id).

## Required

* A relational database
    * Out of the box this will start dependent upon a local PostgreSQL instance
* [Chrome for testing](https://googlechromelabs.github.io/chrome-for-testing/#stable) on your path that matches your
  current Chrome version
    * this is only needed if you are running end-to-end tests
* Environment variables
    * JDBC_DATABASE_URL
    * JDBC_DATABASE_USERNAME
    * JDBC_DATABASE_PASSWORD

## Start

* modify application.yml to point to a running database instance
* run MplsFoodApplication.java
    * DDL will be executed on startup based on application.yml settings
* navigate to http://localhost:8080

### Start via gradle

```bash
gradle bootRun --args='--spring.profiles.active=dev'
```

## Tools

### Find dependencies

```bash
gradle -q dependencies
```

### data

Note that Spring has built-in data population that is not in use by default but that you can employ. Fixtures for all
models can be found in src/main/resources/db/data.sql.

The default password for the user and the admin is `retek01!`. To generate your own credentials you'll need to bcrypt
passwords, then assign users to a roles in `authorities`. You can bcrypt a password
using https://www.devglan.com/online-tools/bcrypt-hash-generator.

#### add all data

In the following order -- note that uploads is a large file and not required 
to be populated for the application operate properly 

* users
* authorities
* places
* place_hours
* deals
* days
* rewards
* deal_log
* uploads

### linting

#### Qodana

This documented approach requires Docker running locally and installation of the Qodana
CLI: https://github.com/JetBrains/qodana-cli.

```bash
qodana scan --show-report
```

#### Sonar

https://docs.sonarqube.org/latest/try-out-sonarqube/ for setup and run instructions

```
./gradlew sonar \
-Dsonar.projectKey=mpls-food \
-Dsonar.host.url=http://localhost:9000 \
-Dsonar.login=sqp_xxxx
```

#### Checkstyle

```bash
gradle checkstyleMain
```

#### Schemalint

https://github.com/kristiandupont/schemalint
Linter for PostgreSQL based on rules defined in `src/test/resources/schemalintrc.js`.

Install once

```bash
npm i -g schemalint
```

Run

```bash
cd src/test/resources/
npx schemalint
```

### CI

#### gradle build scan

This will run the tests and build a jarfile.
If using GitHub Actions you may need to initialize the project's gradle scan by running locally and accepting the terms.
You can also run with `--no-scan` and skip the scan (skipping is done for the Docker images)

```bash
gradle build --scan
```

The test suite includes Chromedriver-based end-to-end tests that require the app running at http://localhost:8080. You
can exclude end-to-end tests by running

```bash
gradle build --scan -PexcludeTests='**/endtoend*'
```

### PostgreSQL

Get size of database

```bash
select pg_size_pretty(pg_database_size('mpls-food'));
select pg_database_size('mpls-food')
```

## Chromedriver tests

https://bonigarcia.dev/webdrivermanager/#advanced-configuration

These tests can be brittle due to dependencies on webdrivermanager.
If you get errors pointing out a mismatch between your Chrome version and the driver version you can try one or more of
the following steps:

* Add `WebDriverManager.chromedriver().clearDriverCache();` before the setup call to refresh
* Explicitly pass the Chrome version
  number `:test --tests "com.humegatech.mpls_food.endtoend.*" -Dwdm.chromeDriverVersion="119.0.6045.105"`
* Log the version being used and where it is located

```java
System.out.printf("USING chromedriver %s version %s%n",
                  WebDriverManager.chromedriver().getDownloadedDriverPath(),
                  WebDriverManager.chromedriver().getDownloadedDriverVersion());
```

## data

Data manually collected using

* Google Maps
* Walking around
* https://www.mplsdowntown.com/wp-content/uploads/2022/07/220706-restaurant-list.pdf (07/06/2022)

# Deployment

## Docker

The Dockerfile is set to run the application using the production profile.

```bash
docker build -f Dockerfile -t mpls-food .
docker run --rm --name mpls-food -p 8080:8080 mpls-food
```

If connecting to database with connection properties different from the defaults in application.yml you need to specify
those properties

```bash
docker run --rm --name mpls-food \
-e JDBC_DATABASE_URL=jdbc:postgresql://<host>:5432/<database> \
-e JDBC_DATABASE_USERNAME=<user> -e JDBC_DATABASE_PASSWORD='<password>' \
-p 8080:8080 mpls-food
```

### Run the application with Docker Compose

Once the jar has been built you can run a container that has both the database and application running in it

```bash
mkdir -p volumes/db/
docker compose up
```
 