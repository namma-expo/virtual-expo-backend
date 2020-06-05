## Welcome to vexpo
### Requirements

 - [ ] Java 8 or above
 - [ ] mysql db
 - [ ] maven


### execute commands

    mvn spring-boot:run -Dspring.config.location=application.properties
or

    java -jar virtual-expo-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:mysql://localhost:3306/virtualexpodb --spring.datasource.username=root --spring.datasource.password=password
    
    
Docker Image:
=============
created the first version "Docker-image" of our project

### docker commands:

1. docker pull basavesh1308/virtual-expo-app

2. docker run -d -p 8080:8080 <image-ID>
 
3. to see running containers
docker ps -a
    

