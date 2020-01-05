
## Simple WEB CRAWLER API

Author: Melchor Tatlonghari

## Instructions
1. Extract zip file in a local directory
2. run `mvn clean install` in same directory to build the sources before running
3. Run `mvn spring-boot:run` in the commandline to start the application

OR
1. you can run the application via running `MainApplication.java` in your IDE  

### Documentation
#### GET Request
- http://localhost:8080/ - health check
- http://localhost:8080/webcrawl?url=https://www.google.com/search?sxsrf=ACYBGNS - crawl with url parameter


## Technologies/Libraries/Frameworks used
- Java 8
- Spring/SpringBoot - Framework for lightweight API service
- Maven - to handle all dependencies and builds involved
- Jackson - for validating incoming request and response. Serialization/Deserialization of json requests

### Known Issues/Technical Notes    
- I am limiting the number of sites/depth of the crawler to avoid infinite loops
- I am using Breadth-Depth Search via a Queue to crawl through the sites
- I am using Sets to keep a unique list of urls already visited
- Only site visited will have Title
