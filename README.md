## start
* make sure postgresql is running
* run MplsFoodApplication.java
* navigate to http://localhost:8080

## add users
* bcrypt a password using https://www.devglan.com/online-tools/bcrypt-hash-generator
* insert users records
```bash
INSERT INTO users (username,password,enabled)
VALUES ('user',
'<BCRYPTED_PASSWORD>', true);

INSERT INTO users (username,password,enabled)
VALUES ('admin',
'<BCRYPTED_PASSWORD>', true); 
```
