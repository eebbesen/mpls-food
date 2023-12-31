# MPLS-FOOD

Created with https://bootify.io

Minneapolis flag image from https://upload.wikimedia.org/wikipedia/commons/9/9d/Flag_of_Minneapolis.svg

## Required
* A relational database
  * Out of the box this will work with a local PostgreSQL instance (default) and h2 (when starting with `h2` profile)
* [chrome for testing](https://googlechromelabs.github.io/chrome-for-testing/#stable) on your path that matches your current Chrome version
  * this is only needed if you are running end-to-end tests

## Start
* modify application.yml to point to a running database instance (or use H2)
* run MplsFoodApplication.java
  * DDL will be executed on startup based on application.yml setttings
* navigate to http://localhost:8080

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
Note that Spring has built-in data population that is not in use by default but that you can employ. Fixtures for all models can be found in src/main/resources/db/data.sql.

The default password for the user and the admin is `retek01!`. To generate your own credentials you'll need to bcrypt passwords, then assign users to a roles in `authorities`. You can bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator.

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
You can also run without `--scan`.
```bash
gradle build --scan
```

The test suite includes Chromedriver-based end-to-end tests that require the app running at http://localhost:8080. You can exclude end-to-end tests by running
```bash
gradle build --scan -PexcludeTests=**/endtoend*

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
If you get errors pointing out a mismatch between your Chrome version and the driver version you can try one or more of the following steps:
* Add `WebDriverManager.chromedriver().clearDriverCache();` before the setup call to refresh
* Explicitly pass the Chrome version number `:test --tests "com.humegatech.mpls_food.endtoend.*" -Dwdm.chromeDriverVersion="119.0.6045.105"`
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
