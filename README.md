
## Simple TAXI CAB API

Author: Melchor Tatlonghari

## Instructions
1. Replace application.properties with correct jdbc url before starting spring-boot
(if no database is installed, follow instructions on the bottom on Docker setup.)
2. run `mvn clean install` in same directory to build the sources before running
3. you can run the application via running `MainApplication.java` in your IDE or `mvn spring-boot:run` in the 
commandline if you are in the same directory of the pom

### Documentation
|  Parameter 	|   Value	|  Optional 	|   Format	|   	
|---	|---	|---	|---		|
| medallions  	|   list of id of the taxi cab	| false  	| `List<String>`  	   	|
| date  	|  travel dates based on pickup date for specified cab 	|   false	|   YYYY-MM-DD	   	|
| fetchNewData  	|   ignore cache and fetch from database  	|   	true|  boolean 	   	|

### POST Request
URL: http://localhost:8080/fetch

**Request example:**

```
   {
     "medallions": [
       "0081EFFCBB2AD30F9D85B3A07F5EAB45",
       "0310297769C8B049C0EA8E87C697F755"
     ],
     "date": "2013-12-31"
   }
 ```

**Response example**

```$xslt
{  
   "dateRequested":"2013-12-31",
   "cabTrips":{  
      "0081EFFCBB2AD30F9D85B3A07F5EAB45":[  
         {  
            "medallion":"0081EFFCBB2AD30F9D85B3A07F5EAB45",
            "hackLicense":"45DB971CC11B3AECAAA6C739DE79A041",
            "vendorId":"VTS",
            "rateCode":1,
            "storeAndForwardFlag":"",
            "pickupDateTime":[  
               2013,
               12,
               31
            ],
            "dropoffDateTime":[  
               2013,
               12,
               31
            ],
            "passengerCount":5,
            "tripTimeSeconds":1.51,
            "tripDistance":0.0
         }
      ],
      "0310297769C8B049C0EA8E87C697F755":[  
         {  
            "medallion":"0310297769C8B049C0EA8E87C697F755",
            "hackLicense":"B11C57D8A5C3FA200FA94112F46A2CB3",
            "vendorId":"VTS",
            "rateCode":1,
            "storeAndForwardFlag":"",
            "pickupDateTime":[  
               2013,
               12,
               31
            ],
            "dropoffDateTime":[  
               2013,
               12,
               31
            ],
            "passengerCount":1,
            "tripTimeSeconds":1.43,
            "tripDistance":0.0
         }
      ]
   }
}
```

***NOTE:***
remember to set Accept and Content-Type header as
`application/json`

### GET Request

- http://localhost:8080/clearcache - clears local cache
- http://localhost:8080/ - health check



## Technologies/Libraries/Frameworks used
- Java 8
-  Lombok - removes boilerplate code from java like setters and getters, as well as loggers through annotations
(Builder, AllArgsConstructor, Data, NoArgConstructor, Slf4j)
- JDBI - lightweight mysql connector for fetching data 
- Spring/SpringBoot - Framework for lightweight API service
- Maven - to handle all dependencies and builds involved
- Jackson - for validating incoming request and response. Serialization/Deserialization of json requests
- Mockito - mocking objects (database/cache) for testing purposes
- (Optional) Docker - to set up database should you not have database installed 

### Known Issues/Technical Notes
- For simplicity's sake, I have included  api request/response, as well as the mapper in data package.
- We are only using HashMap as a pseudo cache for local queries
- We are using Medallion request as the key for the local cache, it could be improved as a nested map, where medallion is the key, to a hashmap of dates.
- We have a catch all exception handler using ApiExceptionHandler
- We are using interfaces for database for easy swaps of underlying infrastructure should there be a need to
- Validations are done via @valid annotation and individual jackson annotation in pojo
- We are using plain text for user name password of db. this should be encrypted 


#### Logging

Logs will show if the request are from Database or from cache:

```
2019-01-15 20:45:47.115  INFO 14981 --- [nio-8080-exec-1] DatabaseDao               : Fetching from DATABASE::[0081EFFCBB2AD30F9D85B3A07F5EAB45, 0310297769C8B049C0EA8E87C697F755]2013-12-31
2019-01-15 20:47:40.187  INFO 14981 --- [nio-8080-exec-3] LocalCache              : Checking key from CACHE : 0081EFFCBB2AD30F9D85B3A07F5EAB45
```

#### Setup Database on Docker (Optional)
below are the instructions if you do not have MYSQL installed, and are using docker.

Pull docker image:

`docker pull mysql`
 
 Start Docker:
 
`docker run --rm --name mysql_database -p 3306:3306 -e MYSQL_ROOT_PASSWORD=test -d mysql`

Data dump:

`docker exec -i mysql_database mysql -uroot -ptest local <file_name>.sql`