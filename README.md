# TDS-Java-Task-Repo

A repo to hold the Java task that TDS asked me to complete for their interview process



I built this running Java 25 with Spring Boot 4.0.3 but as long as you're running Java 17 and above this will run fine, and even if you aren't running it on a lower version of Spring Boot shouldn't affect the functionality.



I choose to incorporate Spring Boot as it's especially good for making RESTful Applications and that what I'm used to as well as the other benefits.



As it has Apache built in, to run the service/app you just need to hover over the TdsJavaTaskApplication file in your IDE (mine was Eclipse) and then right click run as Java application. Also I just kept the default host localhost:8080.



A couple of assumptions or choices I made were:



* The Bill Id is to be unique so I used a value where based on the algorithm used it wouldn't be generated again in real scenarios. Of course if this was stored in databases it would've been handled differently but as it's an in memory solution I handled it being unique via an algorithm.
* I made the number of total car spaces be capped at less than 100.
* I made the default number of car spaces be 30 but I created an endpoint to have the option to set it to a specific number for demoing.
* I have the registration length capped at 8 characters.







Questions I would have asked if this was possible:



* Do you want to have the bill and parking response returned even if it's under a minute?
* Is there a specific Date Time format required?
* Is there a specific cap on the number of parking spaces to be available?
* How do you want errors due to input validation or due to scenarios such as no available parking space to be displayed to the end user?



I did do most of the implementation via TDD with this case being unit tests so may have written more tests than expected but was helpful in catching any weird behaviour/logic issues along the way and if there's any feedback on improving these tests or my way of going around them I would definitely welcome that.



I didn't write any internal comments but named the methods and variables appropriately so it should be relatively to understand what part is doing what.



Also If you want to follow the process from where the end user send the API requests to for this request you can navigate to the ParkingController file.

