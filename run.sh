#!/bin/bash
mvn spring-boot:run

# mvn clean tomcat7:redeploy
# call mvn clean package
# call mvn tomcat7:deploy
# mvn clean package -Dspring-boot.excludeDevtools=false -Dspring.devtools.restart.enabled=true
#
# call java -jar ./target/spring-boot-test-0.0.1-SNAPSHOT.jar
# call java -jar ./target/first-application-0.0.2-SNAPSHOT.war
# -Dspring.devtools.restart.enabled=true
