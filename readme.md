# Rewards calculation service

The Rewards Calculation service is responsible for creating and managing user transaction, and calculation rewards points for the customer.

# Quick start

### Prerequisites
* [Install Maven](https://maven.apache.org/install.html)
* [Install Java 17](https://docs.oracle.com/en/java/javase/18/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)
* [Install docker](https://docs.docker.com/engine/install/)

### General information
Application is backed by H2 in-memory database so all user data are retained as long as application is running.
You can easily replace the database with any other relational database by changing the appropriate entries in application.properties file

#### Application run
To run the application please fallow below steps:
______
* clone git repository
```shell script
git clone {gitProjetcURL}
```

* change directory
```shell script
cd rewards
```

* Build the application:
```shell script
mvn clean install
```

* Run application locally
```shell script
java -jar targert/rewards-0.0.1-SNAPSHOT.jar
```

Once you run the above commands you can access the API on the port 8080. Application could be also run on Docker, see [Docker section](###Docker)

### Test run guide

`mvn test ` run both unit & integration tests

### Authorization
Each API endpoint is secured by Basic Authentication mechanism.
To get access to API endpoints you have to use following configured credentials:

`user name`: **demo**

`user password:` **demo!@#$**

You can easily change default credentials by changing application.properties entries:
```
spring.security.user.name
spring.security.user.password
```

### Postman collections
To use the Rewards Calculation service you can use Postman collections located in **postman** directory

### API documentation
API documentation is accessible under http://localhost:8080/swagger-ui/index.html

### Docker

#### Docker image build
* clone git repository
```shell script
git clone {gitProjetcURL}
```

* change directory
```shell script
cd rewards
```

* Build the application:
```shell script
mvn clean install
```

* Build docker image
```shell script
docker build -t rewards/rewards-service .
```

#### Docker image local run
```shell script
docker run -d -p 8080:8080 --name reward-service rewards/rewards-service
```
