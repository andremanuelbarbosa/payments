# Payments Service

## Design

### API
![API Design](
https://github.com/andremanuelbarbosa/payments-service/raw/master/src/test/resources/images/api-design.png)

### Frameworks
* DropWizard - multi purpose framework for building web applications (in particular web services) which includes the following useful components:  
    * Jetty - HTTP Container
    * Jersey - JAX-RS Implementation
    * Jackson - JSON Mapping
    * JDBI - DB Interaction (layer over JDBC)
    * Liquibase - DB Structure
    * Logback - Logging
* Google Guice - DI
* Guava - Utilities Library
* Swagger - API Documentation and REST Client in JS
* Cucumber - BDD Testing

### Classes
In addition to the classes required to initialize and configure DropWizard, the following approach was used to implement the application flow: 
* __PaymentsApi__ - Holds the JAX-RS interface for the API endpoints and some logic to handle things like resources not found or bad requests.
* __PaymentsManager__ - Acts as a Facade between the API layer and the underlying DAO. This is where most of the complex business logic lives (or should live).
* __PaymentsDao__ - Interface that defines the methods which can be used to interact with the DB.
* __PaymentsDaoJdbi__ - JDBI implementation of the PaymentsDAO interface which contains the SQL used to interact with the DB.
* __PaymentSetMapper__ - Implementation of the ResultSetMapper that converts the ResultSet object into a Payment object.
* __Payment__ - The actual domain/resource object, which holds the actual data structure for the Payment Resource. This class is composed of several nested classes that represent things like the several Parties involved in the Payment, Charges Information, etc. The class structure reflects the JSON format and simplifies the generation of the JSON responses.
* __Resource__ - Abstract class for all the resources of the API which holds the Type attribute.
* __Resources__ - Wrapper for the API responses which are related to listings, which include a "data" and "links" attributes.

## Testing
A Cucumber BDD feature file has been created for each of the 5 features (API endpoints) with the most common scenarios. The approach taken was to write up the scenario first, implement the underlying steps to make the tests run, get the tests to fail, then implement the actual service logic until the scenario was successful (i.e. BDD). Below is an example of how one of these scenarios looks like:

```cucumber
  Scenario: Response is 200 and contains the Payment Resource when invoking the GET /payments/{id} endpoint and the Payment exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    When the client makes a "GET" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "200" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
```

These tests are executed as part of the build or can also be triggered by running the PaymentServicesAcceptanceTest class. Once the tests are executed, there should be a Cucumber report on "/target/cucumber/index.html" on the project folder which contains the detailed outcome for each feature/scenario and also the associated steps. I have also included an improved version of this report which is generated under the "targer/cucumber-html-reports" folder, which contains an overview of the features/scenarios/steps and looks like this:

![Cucumber HTML Reports](
https://github.com/andremanuelbarbosa/payments-service/raw/master/src/test/resources/images/cucumber-html-reports.png)

## Usage
As the project is using Maven, just do "mvn clean install package" to build the application and then run the generated jar as "java -jar payment-service-1.0.0-SNAPSHOT.jar server [path_to_conf_file.yml]". An example YAML config file would like:

```yaml
server:
  rootPath: /api/
  applicationConnectors:
  - type: http
    port: 8080
  adminConnectors:
  - type: http
    port: 8081
logging:
  level: INFO
  loggers:
    com.andremanuelbarbosa.payments: DEBUG
database:
  driverClass: org.postgresql.Driver
  user: postgres
  password: postgres
  url: jdbc:postgresql://localhost:5432/payments-service
```

I have been using Postgres as the underlying DB but the service should work with different vendors too like Oracle or MySQL. Just change the configuration to the appropriate JDBC URL.

The default URL for the application is http://localhost:8080/api and you should see the Swagger UI when accessing this URL.