# MPLS-FOOD
https://mpls-food.herokuapp.com/

Created with https://bootify.io

Minneapolis flag image from https://upload.wikimedia.org/wikipedia/commons/9/9d/Flag_of_Minneapolis.svg

## Required
* A relational database. Out of the box this will work with a local PostgreSQL instance
* [chromedriver](https://chromedriver.chromium.org/downloads) on your path that matches your current Chrome version

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
### add users
Fixtures for all models can be found in src/main/test/resources/seeds. The default password for the user and the admin is `retek01!`

To generate your own credentials you'll need to bcrypt passwords, then assign your user to a role in `authorities`. You can bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator.

### linting
#### Qodana
This requires a running Docker container locally
```bash
qodana scan --show-report
```

#### Checkstyle
```bash
gradle checkstyleMain
```

### CI
#### gradle build scan
If using GitHub Actions you may need to initialize the project's gradle scan by running locally and accepting the terms
```bash
gradle build --scan
```

#### heroku
Test locally using `heroku local web`

### PostgreSQL
Get size of database
```bash
select pg_size_pretty(pg_database_size('dbname'));

select pg_database_size('dbname')
```

## data
https://www.mplsdowntown.com/wp-content/uploads/2022/07/220706-restaurant-list.pdf (07/06/2022)
