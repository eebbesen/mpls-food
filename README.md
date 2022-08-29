## Start
* make sure postgresql is running
* run MplsFoodApplication.java
* navigate to http://localhost:8080

### Start using gradle
```bash
gradle bootRun --args='--spring.profiles.active=default'
```

## tools
### find dependencies
```bash
gradle -q dependencies
```
### add users
Fixtures for all models can be found in test/test_data. The default password for the user and the admin is `retek01!`

To generate your own credentials you'll need to bcrypt passwords, then assign your user to a role in `authorities`. You can bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator.
