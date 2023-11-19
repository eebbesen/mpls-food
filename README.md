# MPLS-FOOD
https://mpls-food.herokuapp.com/

Created with https://bootify.io

Minneapolis flag image from https://upload.wikimedia.org/wikipedia/commons/9/9d/Flag_of_Minneapolis.svg

## Required
* A relational database
  * Out of the box this will work with a local PostgreSQL instance (default) and h2 (when starting with `h2` profile)
* [chrome for testing](https://googlechromelabs.github.io/chrome-for-testing/#stable) on your path that matches your current Chrome version
  * this is only needed if you are running end-to-end tests

## Start
* modify application.yml to point to a running database instance
* run MplsFoodApplication.java
  * DDL will be executed on startup based on application.yml `spring:jpa:hibernate:ddl-auto:` value
* navigate to http://localhost:8080
* data seed scripts are in `src/test/test_data/`

### Start via gradle
```bash
gradle bootRun --args='--spring.profiles.active=default'
```

## tools
### find dependencies
```bash
gradle -q dependencies
```
### data
Note that Spring has built-in data population that is not in use by default but that you can employ.
#### add users
Fixtures for all models can be found in src/main/test/resources/test_data. The default password for the user and the admin is `retek01!`

To generate your own credentials you'll need to bcrypt passwords, then assign users to a roles in `authorities`. You can bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator.

#### add all data
In the following order -- note that uploads is a large file
* users
* authorities
* places
* deals
* days
* rewards
* deal_log
* uploads

### linting
#### Qodana
This requires a running Docker container locally
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

### CI
#### gradle build scan
This will run the tests and build a jarfile.
If using GitHub Actions you may need to initialize the project's gradle scan by running locally and accepting the terms.
You can also run without `--scan`.
```bash
gradle build --scan
```

The test suite includes end-to-end tests that require the app running at http://localhost:8080. You can exclude end-to-end tests by running
```bash
gradle build --scan -PexcludeTests=**/endtoend*

```

#### heroku
Test Heroku locally using `heroku local web`

### PostgreSQL
Get size of database
```bash
select pg_size_pretty(pg_database_size('dbname'));

select pg_database_size('dbname')
```

## data
https://www.mplsdowntown.com/wp-content/uploads/2022/07/220706-restaurant-list.pdf (07/06/2022)
