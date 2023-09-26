## Drones Intructions

## Previous requirements

- Java JDK 11 or higher installed
- Maven installed

## Compilation

1. Clone this repository to your local machine.
2. Open a terminal and navigate to the root directory of the project.
3. Run the following command to build the project:
mvn clean compile
## Execution

1. Once the build is complete, run the following command to run the application:
mvn spring-boot:run
2. The application will run on the default port 8080. You can access it in your web browser using the following URL:
http://localhost:8080 but in this url you will not currently find any positive results, we recommend using the urls given in steps 3 and 4, these allow you to have a better interaction with the API for Drones
3. Swagger: http://localhost:8080/swagger-ui/index.html
4. H2-DB (user drone and password drone): http://localhost:8080/h2-console
## Proof

1. To run the project's unit tests, use the following command:
mvn test
2. The tests will be run and the result will be displayed in the terminal.

Ready! You should now be able to compile, run, and test this Spring Boot project.
 
