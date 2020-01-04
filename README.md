
## Simple WEB CRAWLER API

Author: Melchor Tatlonghari

## Instructions
1. run `mvn clean install` in same directory to build the sources before running
2. you can run the application via running `MainApplication.java` in your IDE 
or `mvn spring-boot:run` in the commandline if you are in the same directory of the pom

### Documentation

#### GET Request

- http://localhost:8080/ - health check


## Technologies/Libraries/Frameworks used
- Java 8
- Spring/SpringBoot - Framework for lightweight API service
- Maven - to handle all dependencies and builds involved
- Jackson - for validating incoming request and response. Serialization/Deserialization of json requests

### Known Issues/Technical Notes
- We are limiting the number of sites/depth of the crawler to avoid infinite loops
- We are using Breadth-Depth Search via a Queue to crawl through the sites
- We are using Sets to keep a unique list of urls already visited
- Only site visited will have Title
