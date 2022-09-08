Created with https://bootify.io.

## Start
* modify application.yml to point to a running database instance
* run MplsFoodApplication.java
    * DDL will be executed on startup based on application.yml `spring:jpa:hibernate:ddl-auto:` value
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
### add users
Fixtures for all models can be found in src/main/test/resources/seeds. The default password for the user and the admin is `retek01!`

To generate your own credentials you'll need to bcrypt passwords, then assign your user to a role in `authorities`. You can bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator.

### CI
#### gradle build scan
If using GitHub Actions you may need to initialize the project's gradle scan by running locally and accepting the terms
```bash
gradle build --scan
```