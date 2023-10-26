# school-admin
This is an backend API application for school administrator system 

## Installation Options
### IDE
Intellij preferred

### Java
Download and install jdk17 
(Corretto-17 downloaded via Intellij IDE)

### Postgresql
Download and install Postgresql database
https://www.postgresql.org/download/

## How To Use
### Option 1 : Run Local Instance 
Update password in `src/main/resources/application.yml` for local Postgresql database 

Build, package and run application in IDE, such as Intellij 
`src/main/java/edu/school/admin/AdminApplication.java`

Or

Package and run application via command line if maven installed

`mvn package`

`java -jar admin-0.0.1-SNAPSHOT.jar`

Or 

Package and run admin-0.0.1-SNAPSHOT.jar in docker compose

Package via command line `mvn package` if maven installed or via IDE 

`docker-compose down`

`docker rmi docker-spring-boot-postgres:latest`

`docker-compose up`

Test API with Postman collection: `SchoolAdminLocal.postman_collection.json`

### Option 2 : Access Online API
API link: `http://ec2-13-229-91-43.ap-southeast-1.compute.amazonaws.com:8080/api/`

Test API with Postman collection: `SchoolAdminAWS.postman_collection.json`
